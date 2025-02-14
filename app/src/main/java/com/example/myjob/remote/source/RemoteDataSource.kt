package com.example.myjob.remote.source

import com.example.myjob.base.GenericResponse
import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.FavoriteModel
import com.example.myjob.domain.entities.InvitationParams
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body

/**
 * Methods of Remote Data Source
 */
interface RemoteDataSource {

    suspend fun getExchangesRate(): ExchangeRates
    suspend fun changeBaseExchangesRate(base: String): ExchangeRates
    suspend fun saveUser(user: User): ApiResult<LoginResponse>
    suspend fun saveExperience(experience: Experience): UserResponse
    suspend fun saveEducation(educations: Educations): UserResponse
    suspend fun getAllExperiences(id: Int, pageNumber: Int): GenericResponse<Experience>
    suspend fun getAllExp(id: Int): List<Experience>
    suspend fun getAllEduc(id: Int): List<Educations>
    suspend fun getAllEducations(id: Int, pageNumber: Int): GenericResponse<Educations>
    suspend fun getFavorites(id: Int, pageNumber: Int): GenericResponse<FavoriteModel>
    suspend fun getAllUser(pageNumber: Int): List<User>
    suspend fun sendInvitation(@Body invitationParams: InvitationParams): UserResponse
    suspend fun forgotPassword(email: String): UserResponse
    suspend fun resetPassword(token: String, newPassword: String): UserResponse
    suspend fun authenticate(user: User): ApiResult<LoginResponse>
    suspend fun verifyEmail(email: String): LoginResponse
    suspend fun savePersonalInfo(user: User): UserResponse
    suspend fun removeExperience(id: Int, experienceId: Int): UserResponse
    suspend fun removeEducation(id: Int, educationId: Int): UserResponse
    suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): UserResponse
    suspend fun getUser(id: Int): User
    suspend fun uploadFile(file: MultipartBody.Part): UserResponse
    suspend fun validateProfile(email: String): UserResponse

    suspend fun verifyExisting(fileName: String): FileExistingResponse
}