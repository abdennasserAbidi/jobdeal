package com.example.myjob.domain.usecase.verification

import android.util.Patterns
import com.example.myjob.base.usecase.BaseUseCase
import com.example.myjob.data.home.HomeRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseUseCase<Boolean, String>() {

    override suspend fun buildRequest(params: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(params ?: "").matches()
    }
}