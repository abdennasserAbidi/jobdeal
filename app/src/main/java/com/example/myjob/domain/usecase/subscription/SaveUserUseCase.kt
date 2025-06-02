package com.example.myjob.domain.usecase.subscription

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.subscription.SubscriptionRepository
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.LoginResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<LoginResponse, User>() {

    override suspend fun buildRequest(params: User?): Flow<Resource<LoginResponse>> {
        return repository.saveUser(params ?: User(0)).flowOn(dispatcher)
    }
}