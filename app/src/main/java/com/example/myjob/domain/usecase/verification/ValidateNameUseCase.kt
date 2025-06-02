package com.example.myjob.domain.usecase.verification

import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.base.usecase.BaseUseCase
import com.example.myjob.data.home.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.util.regex.Pattern
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<Boolean, String>() {

    override suspend fun buildRequest(params: String?): Boolean {
        return Pattern.matches("([\\u00C0-\\u017Fa-zA-Z’]+[- _‘]?)+", params)
    }
}