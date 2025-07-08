package com.example.myjob.domain.response

import com.example.myjob.domain.entities.invitation.InvitationModel

data class PagedInvitation(
    val content: List<InvitationModel>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)