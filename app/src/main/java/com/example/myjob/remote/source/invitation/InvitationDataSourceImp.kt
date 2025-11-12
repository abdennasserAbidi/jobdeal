package com.example.myjob.remote.source.invitation

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.InvitationFilter
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationUser
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class InvitationDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : InvitationDataSource {

    override suspend fun getCompanyInvitations(
        id: Int,
        pageNumber: Int
    ): GenericResponse<InvitationModel> =
        apiService.getCompanyInvitations(id, pageNumber)


    override suspend fun getInvitations(
        id: Int,
        pageNumber: Int
    ): GenericResponse<InvitationModel> = apiService.getInvitations(id, pageNumber)

    override suspend fun getInvitationsByTag(
        id: Int,
        pageNumber: Int
    ): GenericResponse<InvitationModel> = apiService.getInvitationsByTag(id, pageNumber)

    override suspend fun getInvitationDetail(
        id: Int,
        idInvitation: Int
    ): InvitationUser = apiService.getInvitationDetail(id, idInvitation)

    override suspend fun getFilteredInvitations(
        invitationFiltered: InvitationFilter,
        pageNumber: Int
    ): GenericResponse<InvitationModel> =
        apiService.getFilteredInvitations(invitationFiltered, pageNumber)

    override suspend fun sendInvitation(invitationParams: InvitationParams): UserResponse =
        apiService.sendInvitation(invitationParams)

    override suspend fun deleteInvitation(idInvitation: Int): UserResponse =
        apiService.deleteInvitation(idInvitation)

    override suspend fun finishProcess(invitationParams: InvitationParams): InvitationParams =
        apiService.finishProcess(invitationParams)

    override suspend fun acceptRejectInvitation(invitationParams: InvitationParams): UserResponse =
        apiService.acceptRejectInvitation(invitationParams)
}