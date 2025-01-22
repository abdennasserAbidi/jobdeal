package com.example.myjob.domain.usecase

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.Repository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveToFavoriteUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, Boolean>>() {

    override suspend fun buildRequest(params: Pair<Int, Boolean>?): Flow<Resource<UserResponse>> {
        return repository.saveToFavorite(params?.first?:-1, params?.second?: false).flowOn(dispatcher)
    }
}