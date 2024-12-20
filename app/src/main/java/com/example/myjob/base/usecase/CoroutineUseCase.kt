package com.example.myjob.base.usecase

interface CoroutineUseCase<out Output, in Parameters> {

    suspend fun invoke(parameters: Parameters? = null): Output

}