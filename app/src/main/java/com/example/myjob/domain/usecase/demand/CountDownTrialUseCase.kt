package com.example.myjob.domain.usecase.demand

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.demand.DemandRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CountDownTrialUseCase @Inject constructor(
    private val repository: DemandRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<UserResponse>> {
        return repository.countDownTrial(params ?: -1).flowOn(dispatcher)
    }
}