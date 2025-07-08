package com.example.myjob.remote.source.invitation

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
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

    override suspend fun getInvitationDetail(
        id: Int,
        idInvitation: Int
    ): InvitationModel = apiService.getInvitationDetail(id, idInvitation)

    override suspend fun sendInvitation(invitationParams: InvitationParams): UserResponse =
        apiService.sendInvitation(invitationParams)

    override suspend fun finishProcess(invitationParams: InvitationParams): InvitationParams =
        apiService.finishProcess(invitationParams)

    override suspend fun acceptRejectInvitation(invitationParams: InvitationParams): UserResponse =
        apiService.acceptRejectInvitation(invitationParams)
}