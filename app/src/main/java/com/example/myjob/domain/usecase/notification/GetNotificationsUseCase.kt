package com.example.myjob.domain.usecase.notification

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.Repository
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<NotificationModel>, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<PagingData<NotificationModel>>> {
        return repository.getCompanyNotifications(params ?: 0).flowOn(dispatcher)
    }
}