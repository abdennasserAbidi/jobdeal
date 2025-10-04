package com.example.myjob.domain.usecase.invitation

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.invitation.InvitationRepository
import com.example.myjob.domain.entities.InvitationFilter
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFilteredInvitationsUseCase @Inject constructor(
    private val repository: InvitationRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<InvitationModel>, InvitationFilter>() {

    override suspend fun buildRequest(params: InvitationFilter?): Flow<Resource<PagingData<InvitationModel>>> {
        return repository.getFilteredInvitations(params?: InvitationFilter()).flowOn(dispatcher)
    }
}