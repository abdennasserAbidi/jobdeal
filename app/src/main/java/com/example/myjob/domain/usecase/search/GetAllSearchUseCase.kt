package com.example.myjob.domain.usecase.search

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.search.SearchRepository
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllSearchUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<SearchHistory>, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<PagingData<SearchHistory>>> {
        return repository.getAllSearch(params ?: 0).flowOn(dispatcher)
    }
}