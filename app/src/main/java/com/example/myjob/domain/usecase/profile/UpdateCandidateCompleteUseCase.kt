package com.example.myjob.domain.usecase.profile

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.profile.ProfileRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateCandidateCompleteUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<UserResponse>> {
        return repository.updateCandidateCompleted(params ?: 0).flowOn(dispatcher)
    }
}