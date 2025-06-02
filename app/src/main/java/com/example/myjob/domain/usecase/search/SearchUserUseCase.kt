package com.example.myjob.domain.usecase.search

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.search.SearchRepository
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<User>, CriteriaModel>() {

    override suspend fun buildRequest(params: CriteriaModel?): Flow<Resource<PagingData<User>>> {
        return repository.searchUsers(params ?: CriteriaModel()).flowOn(dispatcher)
    }
}