package com.example.myjob.remote.source.notification

import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.response.UserResponse

interface NotificationDataSource {
    suspend fun updateToken(id: Int, token: String): UserResponse
    suspend fun sendNotification(base: NotificationMessage): UserResponse
}