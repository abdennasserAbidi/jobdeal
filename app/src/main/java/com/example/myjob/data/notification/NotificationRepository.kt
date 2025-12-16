package com.example.myjob.data.notification

import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun updateToken(id: Int, token: String): Flow<Resource<UserResponse>>
    suspend fun sendNotification(base: NotificationMessage): Flow<Resource<UserResponse>>
    suspend fun verifyAccountCompany(id: Int): Flow<Resource<UserResponse>>
    suspend fun getCompaniesValidated(): Flow<Resource<List<String>>>
    suspend fun getInstitutesValidated(): Flow<Resource<List<String>>>

}