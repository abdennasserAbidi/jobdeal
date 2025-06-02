package com.example.myjob.remote.source.subscription

import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse

interface SubscriptionDataSource {
    suspend fun forgotPassword(email: String): UserResponse
    suspend fun resetPassword(token: String, newPassword: String): UserResponse
    suspend fun authenticate(user: User): ApiResult<LoginResponse>
    suspend fun verifyEmail(email: String): LoginResponse
    suspend fun saveUser(user: User): ApiResult<LoginResponse>

}