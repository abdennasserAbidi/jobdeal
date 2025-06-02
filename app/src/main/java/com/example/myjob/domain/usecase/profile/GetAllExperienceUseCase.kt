package com.example.myjob.domain.usecase.profile

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.profile.ProfileRepository
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllExperienceUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<Experience>, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<PagingData<Experience>>> {
        return repository.getAllExperiences(params ?: 0).flowOn(dispatcher)
    }
}