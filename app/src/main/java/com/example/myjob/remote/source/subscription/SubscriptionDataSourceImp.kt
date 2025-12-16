package com.example.myjob.remote.source.subscription

import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.feature.validateprofile.ValidationProfileStatus
import com.example.myjob.remote.api.ApiService
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject

class SubscriptionDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : SubscriptionDataSource {

    override suspend fun saveUser(user: User): ApiResult<LoginResponse> = apiService.saveUser(user)

    override suspend fun forgotPassword(email: String): UserResponse =
        apiService.forgotPassword(email)

    override suspend fun validateCandidateProfile(validationProfileStatus: ValidationProfileStatus): UserResponse =
        apiService.validateCandidateProfile(validationProfileStatus)

    override suspend fun statusCandidateValidation(id: Int): ValidationProfileStatus =
        apiService.statusCandidateValidation(id)

    override suspend fun statusListCandidateValidation(id: Int): List<ValidationProfileStatus> =
        apiService.statusListCandidateValidation(id)

    override suspend fun resetPassword(token: String, newPassword: String): UserResponse =
        apiService.resetPassword(token, newPassword)

    override suspend fun authenticate(user: User): LoginResponse =
        apiService.authenticate(user)

    override suspend fun verifyEmail(email: String): LoginResponse = apiService.verifyEmail(email)

}