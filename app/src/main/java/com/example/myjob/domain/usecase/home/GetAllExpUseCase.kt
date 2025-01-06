package com.example.myjob.domain.usecase.home

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.Repository
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllExpUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<List<Experience>, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<List<Experience>>> {
        return repository.getAllExp(params ?: 0).flowOn(dispatcher)
    }
}