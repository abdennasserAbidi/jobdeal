package com.example.myjob.domain.usecase.subscription

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.subscription.SubscriptionRepository
import com.example.myjob.domain.entities.ResetPasswordParam
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, ResetPasswordParam>() {

    override suspend fun buildRequest(params: ResetPasswordParam?): Flow<Resource<UserResponse>> {
        return repository.resetPassword(params?.token ?: "", params?.newPassword ?: "")
            .flowOn(dispatcher)
    }
}