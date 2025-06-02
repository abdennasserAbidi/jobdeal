package com.example.myjob.domain.usecase.verification

import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.base.usecase.BaseUseCase
import com.example.myjob.data.home.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.util.regex.Pattern
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<Boolean, String>() {

    override suspend fun buildRequest(params: String?): Boolean {
        return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}\$", params)
    }
}