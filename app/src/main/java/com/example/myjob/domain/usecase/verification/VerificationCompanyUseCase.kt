package com.example.myjob.domain.usecase.verification

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.notification.NotificationRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerificationCompanyUseCase @Inject constructor(
    private val repository: NotificationRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, Boolean>>() {

    override suspend fun buildRequest(params: Pair<Int, Boolean>?): Flow<Resource<UserResponse>> {
        return repository.verifyAccountCompany(
            params?.first ?: -1,
            params?.second ?: false
        ).flowOn(dispatcher)
    }
}