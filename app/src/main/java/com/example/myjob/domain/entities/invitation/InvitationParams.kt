package com.example.myjob.domain.entities.invitation

import android.view.View

data class InvitationParams(
    var idConnected: Int = 0,
    var invitationModel: InvitationModel = InvitationModel()
)

enum class InvitationStatus {
    ON_HOLD, IN_PROCESS, HIRED, NOT_INTERESTED, REJECTED
}

enum class SituationCandidate {
    IDLE, ACCEPTED, REJECTED
}

data class InvitationModel(
    var idInvitation: Int = View.generateViewId(),
    var idTo: Int = 0,
    var date: String? = "",
    var fullName: String? = "",
    var gender: String? = "",
    var idCompany: Int = 0,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var companyName: String = "",
    var message: String = "Dveloppeur Android",
    var typeContract: String = "",
    var descriptionContract: String = "",
    var disponibility: String = "",
    var tgm: String = "",
    var nbDaysPerWeek: String = "",
    var salary: String = "",
    var accepted: Boolean = false,
    var status: String? = InvitationStatus.ON_HOLD.name,
    var reason: String? = "",
    var dateEnd: String? = "",
) {
    fun changeToBadge(): InvitationStatus {
        val statusBadge = when(status) {
            InvitationStatus.ON_HOLD.name -> InvitationStatus.ON_HOLD
            InvitationStatus.IN_PROCESS.name -> InvitationStatus.IN_PROCESS
            InvitationStatus.HIRED.name -> InvitationStatus.HIRED
            InvitationStatus.REJECTED.name -> InvitationStatus.REJECTED
            InvitationStatus.NOT_INTERESTED.name -> InvitationStatus.NOT_INTERESTED
            else -> InvitationStatus.ON_HOLD
        }
        return statusBadge
    }
}