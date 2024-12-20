package com.example.myjob.domain.usecase

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.Repository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.LoginResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerificationEmailUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<LoginResponse, String>() {

    override suspend fun buildRequest(params: String?): Flow<Resource<LoginResponse>> {
        return repository.verifyEmail(params ?: "").flowOn(dispatcher)
    }
}