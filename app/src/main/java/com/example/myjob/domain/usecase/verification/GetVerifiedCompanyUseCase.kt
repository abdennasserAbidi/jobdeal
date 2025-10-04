package com.example.myjob.domain.usecase.verification

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCaseOut
import com.example.myjob.data.notification.NotificationRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetVerifiedCompanyUseCase @Inject constructor(
    private val repository: NotificationRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCaseOut<List<String>>() {

    override suspend fun buildRequest(): Flow<Resource<List<String>>> {
        return repository.getCompaniesValidated().flowOn(dispatcher)
    }
}