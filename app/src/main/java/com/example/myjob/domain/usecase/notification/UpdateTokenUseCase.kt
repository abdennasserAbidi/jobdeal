package com.example.myjob.domain.usecase.notification

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.notification.NotificationRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateTokenUseCase @Inject constructor(
    private val repository: NotificationRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, String>>() {

    override suspend fun buildRequest(params: Pair<Int, String>?): Flow<Resource<UserResponse>> {
        return repository.updateToken(params?.first ?: -1, params?.second ?: "").flowOn(dispatcher)
    }
}