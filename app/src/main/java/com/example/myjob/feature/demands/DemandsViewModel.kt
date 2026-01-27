package com.example.myjob.feature.demands

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.usecase.demand.GetAllDemandUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemandsViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllDemandUseCase: GetAllDemandUseCase,
) : ViewModel() {

    init {
        getDemands()
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
}