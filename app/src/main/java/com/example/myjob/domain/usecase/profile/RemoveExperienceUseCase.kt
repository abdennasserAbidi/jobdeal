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

class RemoveExperienceUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, Int>>() {

    override suspend fun buildRequest(params: Pair<Int, Int>?): Flow<Resource<UserResponse>> {
        return repository.removeExperience(params?.first ?: -1, params?.second ?: -1)
            .flowOn(dispatcher)
    }
}