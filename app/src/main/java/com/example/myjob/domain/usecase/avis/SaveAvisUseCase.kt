package com.example.myjob.domain.usecase.avis

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.avis.RateRepository
import com.example.myjob.domain.entities.Rate
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveAvisUseCase @Inject constructor(
    private val dataSource: RateRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): FlowBaseUseCase<String, Rate>() {
    override suspend fun buildRequest(params: Rate?): Flow<Resource<String>> =
        dataSource.saveAvis(params?: Rate()).flowOn(dispatcher)
}