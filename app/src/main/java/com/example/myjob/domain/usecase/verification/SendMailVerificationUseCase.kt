package com.example.myjob.domain.usecase.verification

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.subscription.SubscriptionRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.feature.validateprofile.ValidationProfileStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SendMailVerificationUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, ValidationProfileStatus>() {

    override suspend fun buildRequest(params: ValidationProfileStatus?): Flow<Resource<UserResponse>> {
        return repository.validateCandidateProfile(params ?: ValidationProfileStatus())
            .flowOn(dispatcher)
    }
}