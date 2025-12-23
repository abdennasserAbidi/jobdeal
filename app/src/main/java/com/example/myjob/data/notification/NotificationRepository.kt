package com.example.myjob.data.notification

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getCompanyNotifications(id: Int): Flow<Resource<PagingData<NotificationModel>>>
    suspend fun seenNotification(id: Int): Flow<Resource<UserResponse>>
    suspend fun removeNotification(id: Int): Flow<Resource<UserResponse>>
    suspend fun updateToken(id: Int, token: String): Flow<Resource<UserResponse>>
    suspend fun sendNotification(base: NotificationMessage): Flow<Resource<UserResponse>>
    suspend fun verifyAccountCompany(id: Int): Flow<Resource<UserResponse>>
    suspend fun getCompaniesValidated(): Flow<Resource<List<String>>>
    suspend fun getInstitutesValidated(): Flow<Resource<List<String>>>

}