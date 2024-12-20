package com.example.myjob.remote.service

//import TestFlavor.apiKey
import android.annotation.SuppressLint
import okhttp3.*
import java.io.IOException

class RequestInterceptor internal constructor() : Interceptor {

    @SuppressLint("LogConditional")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = provideHeaders()
        val httpUrl = request.url
            .newBuilder()
            .addQueryParameter("access_key","apiKey")
            .build()
        val newRequest = provideRequest(request, headers, httpUrl)
        return chain.proceed(newRequest)
    }

    companion object {
        /* Headers */
        private const val AUTHORIZATION = "Authorization"

        private fun provideRequest(original: Request, headers: Headers, httpUrl: HttpUrl): Request {
            val requestBuilder = original.newBuilder()
                .headers(headers)
                .url(httpUrl)
                .method(original.method, original.body)
            return requestBuilder.build()
        }

        /**
         * Provide headers with token as authorisation
         *
         * @param token the token
         * @return the headers
         */
        fun provideHeaders(): Headers {
            val headersBuilder = Headers.Builder()
//            headersBuilder.add(AUTHORIZATION, token)
            return headersBuilder.build()
        }
    }
}
