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

class SearchCandidateUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<SearchHistory>, Pair<String, Int>>() {

    override suspend fun buildRequest(params: Pair<String, Int>?): Flow<Resource<PagingData<SearchHistory>>> {
        return repository.searchCandidate(params?.first ?: "", params?.second ?: -1)
            .flowOn(dispatcher)
    }
}