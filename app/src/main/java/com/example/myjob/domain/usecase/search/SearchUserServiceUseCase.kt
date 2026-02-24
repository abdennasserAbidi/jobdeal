package com.example.myjob.domain.usecase.search

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.search.SearchRepository
import com.example.myjob.domain.entities.CategoryModel
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchUserServiceUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<User>, CategoryModel>() {

    override suspend fun buildRequest(params: CategoryModel?): Flow<Resource<PagingData<User>>> {
        return repository.getUserServiceFilteredList(params ?: CategoryModel()).flowOn(dispatcher)
    }
}