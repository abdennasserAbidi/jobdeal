package com.example.myjob.domain.usecase.home

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.Repository
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<List<User>, CriteriaModel>() {

    override suspend fun buildRequest(params: CriteriaModel?): Flow<Resource<List<User>>> {
        return repository.searchUsers(params ?: CriteriaModel()).flowOn(dispatcher)
    }
}