package com.example.myjob.feature.demands

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.usecase.demand.CountDownTrialUseCase
import com.example.myjob.domain.usecase.demand.GetAllDemandUseCase
import com.example.myjob.domain.usecase.demand.SaveDemandUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DemandsViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllDemandUseCase: GetAllDemandUseCase,
    private val saveDemandUseCase: SaveDemandUseCase,
    private val countDownTrialUseCase: CountDownTrialUseCase
) : ViewModel() {

    fun isNotMe(userSender: Int): Boolean =
        userSender != sharedPreference.getInt("idUser", 0)
    val marketDemandModel = MutableStateFlow(MarketDemandModel())
    fun changePostName(name: String) {
        marketDemandModel.update {
            it.title = name
            it
        }
    }

    fun changeDescriptions(name: String) {
        marketDemandModel.update {
            it.description = name
            it
        }
    }

    fun changePostType(name: String) {
        marketDemandModel.update {
            it.activitySector = name
            it
        }
    }

    fun changePostId(id: Int) {
        marketDemandModel.update {
            it.id = id
            it
        }
    }

    fun countDownTrial(param: Int) {
        viewModelScope.launch {
            countDownTrialUseCase.execute(param).collect {

            }
        }
    }

    val demandStatus = MutableStateFlow("")

    fun saveDemand() {
        val id = sharedPreference.getInt("idUser", 0)

        val currentDate = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)

        val marketDemandModels = marketDemandModel.value
        marketDemandModels.date = formattedDate
        marketDemandModels.idSender = id

        marketDemandModel.update { marketDemandModels }

        viewModelScope.launch {
            saveDemandUseCase.execute(marketDemandModel.value)
                .collectLatest { res ->
                    if (res.status == ResourceState.SUCCESS) {
                        demandStatus.update { res.data?.message ?: "" }
                    } else if (res.status == ResourceState.ERROR) {
                        demandStatus.update { res.message ?: "" }
                    }
                }
        }
    }

    private val _demands = MutableStateFlow(PagingData.empty<MarketDemandModel>())
    val demands: StateFlow<PagingData<MarketDemandModel>> get() = _demands.asStateFlow()

    fun getDemands() {
        viewModelScope.launch {
            getAllDemandUseCase.execute().collect { res ->
                if (res.status == ResourceState.SUCCESS)
                    _demands.update { res.data ?: PagingData.empty() }
            }
        }
    }

    fun getType(): JobType {
        val type = sharedPreference.getString("offerDemand", "")
        return if (type == "service") JobType.GET
        else JobType.NORMAL
    }

    fun logout() {
        sharedPreference.putString("token", "")
        sharedPreference.putString("offerDemand", "")
    }

    fun changeToJobDeal() {
        sharedPreference.putString("offerDemand", "")
    }
}