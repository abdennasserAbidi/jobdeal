package com.example.myjob.common.network

import com.example.myjob.domain.response.ErrorResponse
import com.google.gson.Gson
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type

class ApiResultCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Call<ApiResult<T>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<T>): Call<ApiResult<T>> {
        return ApiResultCall(call)
    }

    private class ApiResultCall<T>(
        private val delegate: Call<T>
    ) : Call<ApiResult<T>> {

        override fun enqueue(callback: Callback<ApiResult<T>>) {
            delegate.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(ApiResult.Success(response.body()!!))
                        )
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val message = errorBody?.let {
                            try {
                                val gson = Gson()
                                val errorResponse = gson.fromJson(it, ErrorResponse::class.java)
                                errorResponse.messageError
                            } catch (e: Exception) {
                                "Failed to parse error"
                            }
                        } ?: "Unknown error"

                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(ApiResult.Error(message, response.code()))
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    val message = when (t) {
                        is IOException -> "Network error: ${t.localizedMessage}"
                        else -> "Unexpected error: ${t.localizedMessage}"
                    }
                    callback.onResponse(
                        this@ApiResultCall,
                        Response.success(ApiResult.Error(message))
                    )
                }
            })
        }

        override fun clone(): Call<ApiResult<T>> = ApiResultCall(delegate.clone())

        override fun execute(): Response<ApiResult<T>> {
            throw UnsupportedOperationException("ApiResultCall does not support execute")
        }

        override fun isExecuted() = delegate.isExecuted

        override fun isCanceled() = delegate.isCanceled

        override fun cancel() = delegate.cancel()

        override fun request() = delegate.request()
        override fun timeout(): Timeout {
            TODO("Not yet implemented")
        }
    }
}