package com.example.myjob.common.network

import com.example.myjob.domain.response.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

object NetworkErrorHandler {
    fun parseError(responseBody: ResponseBody?): String {
        return try {
            val error = responseBody?.string()
            if (!error.isNullOrEmpty()) {
                val gson = Gson()
                val errorResponse = gson.fromJson(error, ErrorResponse::class.java)
                errorResponse.messageError
            } else {
                "Unknown error occurred"
            }
        } catch (e: Exception) {
            "Failed to parse error response"
        }
    }

    fun handleException(exception: Throwable): String {
        return when (exception) {
            is HttpException -> {
                val errorBody = exception.response()?.errorBody()
                parseError(errorBody)
            }
            is IOException -> "Network error: Check your internet connection"
            else -> "Unexpected error: ${exception.localizedMessage}"
        }
    }
}