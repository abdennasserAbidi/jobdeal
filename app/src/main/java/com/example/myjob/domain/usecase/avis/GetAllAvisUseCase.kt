package com.example.myjob.domain.usecase.avis

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.avis.RateRepository
import com.example.myjob.domain.entities.Rate
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllAvisUseCase @Inject constructor(
    private val repository: RateRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<Rate>, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<PagingData<Rate>>> {
        return repository.getCandidateAvis(params ?: -1).flowOn(dispatcher)
    }
}