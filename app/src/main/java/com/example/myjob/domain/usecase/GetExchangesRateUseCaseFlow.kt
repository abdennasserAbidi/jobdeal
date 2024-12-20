package com.example.myjob.domain.usecase

import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.base.reources.Resource
import com.example.myjob.data.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetExchangesRateUseCaseFlow @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<ExchangeRates, Nothing>() {

    override suspend fun buildRequest(params: Nothing?): Flow<Resource<ExchangeRates>> {
        return repository.getExchangesRate().flowOn(dispatcher)
    }
}