package com.example.myjob.domain.usecase.home

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.data.subscription.SubscriptionRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ValidateAccountUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<String, String>() {

    override suspend fun buildRequest(params: String?): Flow<Resource<String>> {
        return repository.validateProfile(params ?: "").flowOn(dispatcher)
    }
}