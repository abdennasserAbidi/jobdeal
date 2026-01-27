package com.example.myjob.domain.usecase.demand

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCaseOut
import com.example.myjob.data.demand.DemandRepository
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllDemandUseCase @Inject constructor(
    private val repository: DemandRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCaseOut<PagingData<MarketDemandModel>>() {
    override suspend fun buildRequest(): Flow<Resource<PagingData<MarketDemandModel>>> {
        return repository.getAllDemands().flowOn(dispatcher)
    }
}