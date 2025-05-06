package com.example.myjob.remote.source

import com.example.myjob.base.GenericResponse
import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.InvitationModel
import com.example.myjob.domain.entities.InvitationParams
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import okhttp3.MultipartBody

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
    suspend fun getCompanyInvitations(id: Int, pageNumber: Int): GenericResponse<InvitationModel>
    suspend fun getAllExp(id: Int): List<Experience>
    suspend fun getAllEduc(id: Int): List<Educations>
    suspend fun getAllEducations(id: Int, pageNumber: Int): GenericResponse<Educations>
    suspend fun searchCandidates(word: String, id: Int, pageNumber: Int): GenericResponse<SearchHistory>
    suspend fun searchUsers(criteria: CriteriaModel, pageNumber: Int): GenericResponse<User>
    suspend fun getFavorites(id: Int, pageNumber: Int): GenericResponse<User>
    suspend fun getInvitations(id: Int, pageNumber: Int): GenericResponse<InvitationModel>
    suspend fun getAllSearch(id: Int, pageNumber: Int): GenericResponse<SearchHistory>
    suspend fun getAllUser(pageNumber: Int): GenericResponse<User>
    suspend fun sendInvitation(invitationParams: InvitationParams): UserResponse
    suspend fun saveSearchHistory(idUserConnected: Int, searchHistory: SearchHistory): UserResponse
    suspend fun forgotPassword(email: String): UserResponse
    suspend fun resetPassword(token: String, newPassword: String): UserResponse
    suspend fun authenticate(user: User): ApiResult<LoginResponse>
    suspend fun verifyEmail(email: String): LoginResponse
    suspend fun savePersonalInfo(user: User): UserResponse
    suspend fun saveCompanyInfo(user: User): UserResponse
    suspend fun removeSearchHistory(
        idUserConnected: Int,
        idUserToDelete: Int
    ): UserResponse
    suspend fun removeExperience(id: Int, experienceId: Int): UserResponse
    suspend fun removeEducation(id: Int, educationId: Int): UserResponse
    suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): UserResponse
    suspend fun getUser(id: Int): User
    suspend fun uploadFile(file: MultipartBody.Part): UserResponse
    suspend fun validateProfile(email: String): UserResponse

    suspend fun verifyExisting(fileName: String): FileExistingResponse
}