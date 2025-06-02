package com.example.myjob.domain.usecase.search

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.search.SearchRepository
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveSearchUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, SearchHistory>>() {

    override suspend fun buildRequest(params: Pair<Int, SearchHistory>?): Flow<Resource<UserResponse>> {
        return repository.saveSearchHistory(params?.first ?: -1, params?.second ?: SearchHistory())
            .flowOn(dispatcher)
    }
}