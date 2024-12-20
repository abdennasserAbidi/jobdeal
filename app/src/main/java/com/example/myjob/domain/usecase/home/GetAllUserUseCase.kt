package com.example.myjob.domain.usecase.home

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCaseOut
import com.example.myjob.data.Repository
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllUserUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCaseOut<PagingData<User>>() {

    override suspend fun buildRequest(): Flow<Resource<PagingData<User>>> {
        return repository.getAllUser().flowOn(dispatcher)
    }
}