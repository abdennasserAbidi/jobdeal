package com.example.myjob.domain.usecase.subscription

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.subscription.SubscriptionRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, String>() {

    override suspend fun buildRequest(params: String?): Flow<Resource<UserResponse>> {
        return repository.forgotPassword(params ?: "").flowOn(dispatcher)
    }
}