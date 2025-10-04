package com.example.myjob.domain.usecase.verification

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.subscription.SubscriptionRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.feature.validateprofile.ValidationProfileStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetVerifiedCandidateStatusUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<ValidationProfileStatus, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<ValidationProfileStatus>> {
        return repository.statusCandidateValidation(params ?: -1)
            .flowOn(dispatcher)
    }
}