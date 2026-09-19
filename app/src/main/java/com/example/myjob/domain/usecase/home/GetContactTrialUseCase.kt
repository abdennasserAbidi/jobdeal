package com.example.myjob.domain.usecase.home

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.search.SearchRepository
import com.example.myjob.domain.entities.TrialModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetContactTrialUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<TrialModel, Pair<Int, String>>() {

    override suspend fun buildRequest(params: Pair<Int, String>?): Flow<Resource<TrialModel>> {
        return repository.getContactTrial(params?.first ?: 0, params?.second ?: "").flowOn(dispatcher)
    }
}