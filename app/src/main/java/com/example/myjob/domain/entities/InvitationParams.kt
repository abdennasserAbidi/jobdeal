package com.example.myjob.domain.entities

import android.view.View

data class InvitationParams(
    var idConnected: Int = 0,
    var invitationModel: InvitationModel = InvitationModel()
)

data class InvitationModel(
    var idInvitation: Int = View.generateViewId(),
    var idTo: Int = 0,
    var idCompany: Int = 0,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var companyName: String = "",
    var message: String = "Dveloppeur Android",
    var typeContract: String = "",
    var disponibility: String = "",
    var tgm: String = "",
    var accepted: Boolean = false
)