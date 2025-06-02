package com.example.myjob.domain.usecase.home

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveToFavoriteUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, Int>>() {

    override suspend fun buildRequest(params: Pair<Int, Int>?): Flow<Resource<UserResponse>> {
        return repository.saveToFavorite(params?.first ?: -1, params?.second ?: -1)
            .flowOn(dispatcher)
    }
}