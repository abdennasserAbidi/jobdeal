package com.example.myjob.domain.entities.notification

import android.view.View

data class NotificationModel(
    var idNotification: Int = View.generateViewId(),
    var date: String? = "",
    var idCompany: Int = 0,
    var idCandidate: Int = 0,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var companyName: String = "",
    var username: String = "",
    var title: String = "Dveloppeur Android",
    var isRead: Boolean = false
)