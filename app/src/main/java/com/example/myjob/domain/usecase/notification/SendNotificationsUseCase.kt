package com.example.myjob.domain.usecase.notification

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.notification.NotificationRepository
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SendNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, NotificationMessage>() {

    override suspend fun buildRequest(params: NotificationMessage?): Flow<Resource<UserResponse>> {
        return repository.sendNotification(params ?: NotificationMessage()).flowOn(dispatcher)
    }
}