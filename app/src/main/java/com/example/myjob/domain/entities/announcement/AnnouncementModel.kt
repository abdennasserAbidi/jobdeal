package com.example.myjob.domain.entities.announcement

import android.view.View

data class AnnouncementModel(
    var idAnnounce: Int = View.generateViewId(),
    var date: String? = "",
    var idCompany: Int = 0,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var companyName: String = "",
    var title: String = "Dveloppeur Android",
    var accepted: Boolean = false
)