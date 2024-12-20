package com.example.myjob.remote.source

import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

/**
 * Implementation of [RemoteDataSource] class
 */
class RemoteDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun getExchangesRate(): ExchangeRates = apiService.getExchangeRates()
    override suspend fun changeBaseExchangesRate(base: String): ExchangeRates =
        apiService.changeBaseExchangeRates(base)

    override suspend fun saveUser(user: User): ApiResult<LoginResponse> = apiService.saveUser(user)
    override suspend fun saveExperience(experience: Experience): UserResponse = apiService.saveExperience(experience)
    override suspend fun saveEducation(educations: Educations): UserResponse = apiService.saveEducation(educations)
    override suspend fun getAllExperiences(id: Int, pageNumber: Int): GenericResponse<Experience> =
        apiService.getAllExperiences(id, pageNumber)
    override suspend fun getAllEducations(id: Int, pageNumber: Int): GenericResponse<Educations> = apiService.getAllEducations(id, pageNumber)
    override suspend fun getAllUser(pageNumber: Int): GenericResponse<User> = apiService.getAllUser(pageNumber = pageNumber)

    override suspend fun savePersonalInfo(user: User): UserResponse = apiService.savePersonalInfo(user)
    override suspend fun removeExperience(id: Int, experienceId: Int): UserResponse = apiService.removeExperience(id, experienceId)
    override suspend fun removeEducation(id: Int, educationId: Int): UserResponse = apiService.removeEducation(id, educationId)
    override suspend fun getUser(id: Int): User = apiService.getUser(id)

    override suspend fun forgotPassword(email: String): UserResponse =
        apiService.forgotPassword(email)

    override suspend fun resetPassword(token: String, newPassword: String): UserResponse =
        apiService.resetPassword(token, newPassword)

    override suspend fun authenticate(user: User): ApiResult<LoginResponse> = apiService.authenticate(user)
    override suspend fun verifyEmail(email: String): LoginResponse = apiService.verifyEmail(email)
}