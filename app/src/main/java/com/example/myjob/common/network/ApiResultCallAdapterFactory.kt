package com.example.myjob.common.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(responseType) != ApiResult::class.java) return null
        val innerType = getParameterUpperBound(0, responseType as ParameterizedType)
        return ApiResultCallAdapter<Any>(innerType)
    }
}