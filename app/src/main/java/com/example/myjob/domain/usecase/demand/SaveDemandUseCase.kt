package com.example.myjob.domain.usecase.demand

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.demand.DemandRepository
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveDemandUseCase @Inject constructor(
    private val repository: DemandRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, MarketDemandModel>() {

    override suspend fun buildRequest(params: MarketDemandModel?): Flow<Resource<UserResponse>> {
        return repository.saveDemand(params ?: MarketDemandModel()).flowOn(dispatcher)
    }
}