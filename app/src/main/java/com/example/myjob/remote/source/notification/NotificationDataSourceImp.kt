package com.example.myjob.remote.source.notification

import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class NotificationDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : NotificationDataSource {
    override suspend fun updateToken(id: Int, token: String): UserResponse =
        apiService.updateToken(id, token)

    override suspend fun sendNotification(base: NotificationMessage): UserResponse =
        apiService.sendNotification(base)

    override suspend fun verifyAccountCompany(id: Int): UserResponse =
        apiService.verifyAccountCompany(id)

    override suspend fun getCompaniesValidated(): List<String> =
        apiService.getCompaniesValidated()
}