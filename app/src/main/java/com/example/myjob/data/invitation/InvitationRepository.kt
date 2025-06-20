package com.example.myjob.data.invitation

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface InvitationRepository {
    suspend fun getCompanyInvitations(id: Int): Flow<Resource<PagingData<InvitationModel>>>
    suspend fun sendInvitation(invitationParams: InvitationParams): Flow<Resource<UserResponse>>
    suspend fun acceptRejectInvitation(invitationParams: InvitationParams): Flow<Resource<UserResponse>>
    suspend fun getInvitations(id: Int): Flow<Resource<PagingData<InvitationModel>>>

}