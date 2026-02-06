package com.example.myjob.feature.demands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.usecase.demand.CountDownTrialUseCase
import com.example.myjob.domain.usecase.demand.GetAllDemandUseCase
import com.example.myjob.domain.usecase.demand.GetDemandUseCase
import com.example.myjob.domain.usecase.demand.GetFilteredDemandUseCase
import com.example.myjob.domain.usecase.demand.SaveDemandUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
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
    private val getUserUseCase: GetUserUseCase,
    private val getAllDemandUseCase: GetAllDemandUseCase,
    private val saveDemandUseCase: SaveDemandUseCase,
    private val getDemandUseCase: GetDemandUseCase,
    private val getFilteredDemandUseCase: GetFilteredDemandUseCase,
    private val countDownTrialUseCase: CountDownTrialUseCase
) : ViewModel() {

    val userSender = MutableStateFlow(User())

    fun getUserById(id: Int = sharedPreference.getInt("idUser", 0)) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    GlobalEntries.user = u
                    userSender.update { u }
                }
            }
        }
    }

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

    fun changeLocation(name: String) {
        marketDemandModel.update {
            it.location = name
            it
        }
    }

    fun changeUrgency(name: String) {
        marketDemandModel.update {
            it.urgency = name
            it
        }
    }

    fun changeDeadline(name: String) {
        marketDemandModel.update {
            it.deadline = name
            it
        }
    }

    fun changeBudget(name: String) {
        marketDemandModel.update {
            it.budget = name
            it
        }
    }

    fun changePostType(name: ServiceCategory) {
        marketDemandModel.update {
            it.category = name
            it
        }
    }

    fun changeOtherCategory(name: String) {
        marketDemandModel.update {
            it.otherCategory = name
            it
        }
    }

    fun changeTool(name: ToolCategory) {
        marketDemandModel.update {
            it.tools = name
            it
        }
    }

    fun changeOtherTool(name: String) {
        marketDemandModel.update {
            it.otherTools = name
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
            Log.i("jrzghrzjgrrlkgnz", "saveDemand: ${marketDemandModel.value}")
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

    ///////////////////////////////////////////////////////////////////////////
    // DEMAND BY ID
    ///////////////////////////////////////////////////////////////////////////
    private val _demand = MutableStateFlow(MarketDemandModel())
    val demand: StateFlow<MarketDemandModel> get() = _demand.asStateFlow()

    fun getDemandById(idDemand: Int) {
        viewModelScope.launch {
            getDemandUseCase.execute(idDemand).collect { res ->
                if (res.status == ResourceState.SUCCESS)
                    _demand.update {
                        res.data ?: MarketDemandModel()
                    }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIST DEMANDS
    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////
    // FILTER
    ///////////////////////////////////////////////////////////////////////////
    fun filterDemands(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                getFilteredDemandUseCase.execute(query).collectLatest { res ->
                    _demands.update {
                        res.data ?: PagingData.empty()
                    }
                }
            }

        } else getDemands()
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

    fun convertDate(date: String): String {
        return if (date.isNotEmpty()) {
            val allDate = date.split(", ")
            val completeMonth = allDate[1]
            val year = allDate[2]
            val day = completeMonth.split(" ")[0]
            val month = completeMonth.split(" ")[1]
            val correctMonth = when (month) {
                "janvier" -> "jan"
                "février" -> "fev"
                "mars" -> "mars"
                "avril" -> "avril"
                "mai" -> "mai"
                "juin" -> "juin"
                "juillet" -> "juillet"
                "août" -> "aout"
                "septembre" -> "sep"
                "octobre" -> "oct"
                "novembre" -> "nov"
                "décembre" -> "dec"
                else -> ""
            }

            "$day $correctMonth $year"
        } else ""
    }
}