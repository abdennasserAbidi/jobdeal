package com.example.myjob.feature.notification

data class NotificationState(
    val isEnteringToken: Boolean = true,
    val remoteToken: String = "",
    val messageText: String = ""
)