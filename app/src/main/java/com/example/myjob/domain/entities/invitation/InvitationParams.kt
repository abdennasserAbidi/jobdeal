package com.example.myjob.domain.entities.invitation

import android.view.View

data class InvitationParams(
    var idConnected: Int = 0,
    var invitationModel: InvitationModel = InvitationModel()
)

data class InvitationModel(
    var idInvitation: Int = View.generateViewId(),
    var idTo: Int = 0,
    var date: String? = "",
    var status: String? = "Holding",
    var fullName: String? = "",
    var gender: String? = "",
    var idCompany: Int = 0,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var companyName: String = "",
    var message: String = "Dveloppeur Android",
    var typeContract: String = "",
    var disponibility: String = "",
    var tgm: String = "",
    var nbDaysPerWeek: String = "",
    var salary: String = "",
    var accepted: Boolean = false
)