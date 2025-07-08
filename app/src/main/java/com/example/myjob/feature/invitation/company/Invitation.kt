package com.example.myjob.feature.invitation.company

import com.example.myjob.domain.entities.invitation.InvitationModel

data class Invitation(
    var data: List<InvitationModel>,
    var page: Int = 1,
    var totalPages: Int = 0
)