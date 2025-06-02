package com.example.myjob.domain.usecase.search

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.search.SearchRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoveSearchHistoryUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, Int>>() {

    override suspend fun buildRequest(params: Pair<Int, Int>?): Flow<Resource<UserResponse>> {
        return repository.removeSearchHistory(params?.first ?: -1, params?.second ?: -1)
            .flowOn(dispatcher)
    }
}