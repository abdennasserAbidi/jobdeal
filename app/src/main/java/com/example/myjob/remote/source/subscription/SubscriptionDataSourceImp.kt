package com.example.myjob.remote.source.subscription

import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class SubscriptionDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : SubscriptionDataSource {

    override suspend fun saveUser(user: User): ApiResult<LoginResponse> = apiService.saveUser(user)

    override suspend fun forgotPassword(email: String): UserResponse =
        apiService.forgotPassword(email)

    override suspend fun resetPassword(token: String, newPassword: String): UserResponse =
        apiService.resetPassword(token, newPassword)

    override suspend fun authenticate(user: User): ApiResult<LoginResponse> =
        apiService.authenticate(user)

    override suspend fun verifyEmail(email: String): LoginResponse = apiService.verifyEmail(email)

}