package com.example.myjob.base.usecase

import androidx.annotation.Nullable

/**
 * Base Use Case class
 */
abstract class BaseUseCase<Model, Params> {

    abstract suspend fun buildRequest(@Nullable params: Params?): Model?

    suspend fun execute(@Nullable params: Params?): Model? {
        return try {
            buildRequest(params)
        } catch (exception: Exception) {
            null
        }
    }
}