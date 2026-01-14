package com.example.myjob.remote.source.notification

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.UserResponse

interface NotificationDataSource {
    suspend fun getCompanyNotifications(
        id: Int,
        pageNumber: Int
    ): GenericResponse<NotificationModel>

    suspend fun seenNotification(id: Int):UserResponse
    suspend fun removeNotification(id: Int):UserResponse
    suspend fun updateToken(id: Int, token: String): UserResponse
    suspend fun sendNotification(base: NotificationMessage): UserResponse
    suspend fun verifyAccountCompany(id: Int, isAccepted: Boolean): UserResponse
    suspend fun verifyAccountCandidate(id: Int, isAccepted: Boolean): UserResponse
    suspend fun getCompaniesValidated(): List<String>
    suspend fun getInstitutesValidated(): List<String>
}