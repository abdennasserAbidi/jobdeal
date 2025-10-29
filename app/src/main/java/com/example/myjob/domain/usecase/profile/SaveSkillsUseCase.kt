package com.example.myjob.domain.usecase.profile

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.profile.ProfileRepository
import com.example.myjob.domain.entities.CandidateSkills
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveSkillsUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, CandidateSkills>() {

    override suspend fun buildRequest(params: CandidateSkills?): Flow<Resource<UserResponse>> {
        return repository.updateCandidateSkills(params ?: CandidateSkills()).flowOn(dispatcher)
    }
}