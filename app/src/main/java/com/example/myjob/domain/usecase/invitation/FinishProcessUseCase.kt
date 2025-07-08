package com.example.myjob.domain.usecase.invitation

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.invitation.InvitationRepository
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FinishProcessUseCase @Inject constructor(
    private val repository: InvitationRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<InvitationParams, InvitationParams>() {

    override suspend fun buildRequest(params: InvitationParams?): Flow<Resource<InvitationParams>> {
        return repository.finishProcess(params ?: InvitationParams()).flowOn(dispatcher)
    }
}