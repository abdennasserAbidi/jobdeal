package com.example.myjob.remote.source.invitation

import com.example.myjob.base.GenericResponse
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.InvitationFilter
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationResponse
import com.example.myjob.domain.entities.invitation.InvitationUser
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface InvitationDataSource {
    suspend fun getCompanyInvitations(id: Int, pageNumber: Int): GenericResponse<InvitationModel>
    suspend fun getInvitations(
        id: Int,
        pageNumber: Int
    ): GenericResponse<InvitationModel>

    suspend fun getInvitationsByTag(
        id: Int,
        pageNumber: Int
    ): GenericResponse<InvitationModel>

    suspend fun getInvitationDetail(id: Int, idInvitation: Int): InvitationUser
    suspend fun getFilteredInvitations(
        invitationFiltered: InvitationFilter,
        pageNumber: Int
    ): GenericResponse<InvitationModel>

    suspend fun sendInvitation(invitationParams: InvitationParams): UserResponse
    suspend fun deleteInvitation(idInvitation: Int): UserResponse
    suspend fun finishProcess(invitationParams: InvitationParams): InvitationParams
    suspend fun acceptRejectInvitation(invitationParams: InvitationParams): UserResponse
}