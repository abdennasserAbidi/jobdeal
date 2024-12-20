package com.example.myjob.domain.usecase

import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.base.reources.Resource
import com.example.myjob.data.Repository
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChangeFlowBaseExchangesRateUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<ExchangeRates, String>() {

    override suspend fun buildRequest(params: String?): Flow<Resource<ExchangeRates>> {
        return repository.changeBaseExchangesRate(params?: "").flowOn(dispatcher)
    }
}