package com.example.myjob.base.usecase

import androidx.annotation.Nullable
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Base Use Case class
 */
abstract class FlowBaseUseCase<Model, Params> {

    abstract suspend fun buildRequest(@Nullable params: Params?): Flow<Resource<Model>>

    suspend fun execute(@Nullable params: Params?): Flow<Resource<Model>> {
        return try {
            buildRequest(params)
        } catch (exception: Exception) {
            flow { emit(Resource(ResourceState.ERROR, null, exception.message)) }
        }
    }
}