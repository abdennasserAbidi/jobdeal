package com.example.myjob.remote.source.invitation

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.response.UserResponse

interface InvitationDataSource {
    suspend fun getCompanyInvitations(id: Int, pageNumber: Int): GenericResponse<InvitationModel>
    suspend fun getInvitations(id: Int, pageNumber: Int): GenericResponse<InvitationModel>
    suspend fun sendInvitation(invitationParams: InvitationParams): UserResponse
    suspend fun acceptRejectInvitation(invitationParams: InvitationParams): UserResponse
}