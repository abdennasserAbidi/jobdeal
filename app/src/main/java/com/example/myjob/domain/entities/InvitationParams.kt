package com.example.myjob.domain.entities

import android.view.View

data class InvitationParams(var idConnected: Int = 0, var invitationModel: InvitationModel = InvitationModel())

data class InvitationModel(var idInvitation: Int = View.generateViewId(), var idTo: Int = 0, var message: String = "", var typeContract: String = "")