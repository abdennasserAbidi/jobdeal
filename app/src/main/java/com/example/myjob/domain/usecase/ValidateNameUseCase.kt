package com.example.myjob.domain.usecase

import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.BaseUseCase
import com.example.myjob.data.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.util.regex.Pattern
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<Boolean, String>() {

    override suspend fun buildRequest(params: String?): Boolean {
        return Pattern.matches("([\\u00C0-\\u017Fa-zA-Z’]+[- _‘]?)+", params)
    }
}