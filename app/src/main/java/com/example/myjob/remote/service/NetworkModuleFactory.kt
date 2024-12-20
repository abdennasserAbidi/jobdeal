package com.example.myjob.remote.service

import com.example.myjob.remote.api.ApiService
import okhttp3.OkHttpClient

object NetworkModuleFactory : BaseNetworkModuleFactory() {

    const val LOG_INTERCEPTOR = "LogInterceptor"
    const val REQUEST_INTERCEPTOR = "RequestInterceptor"

    fun makeService(): ApiService = makeService(makeOkHttpClient())

    private fun makeService(okHttpClient: OkHttpClient): ApiService {
        val retrofit = buildRetrofitObject(okHttpClient)
        return retrofit.create(ApiService::class.java)
    }
}
