package com.example.myjob.remote.source

import com.example.myjob.base.GenericResponse
import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import okhttp3.MultipartBody
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
    override suspend fun saveExperience(experience: Experience): UserResponse =
        apiService.saveExperience(experience)

    override suspend fun saveEducation(educations: Educations): UserResponse =
        apiService.saveEducation(educations)

    override suspend fun getAllExperiences(id: Int, pageNumber: Int): GenericResponse<Experience> =
        apiService.getAllExperiences(id, pageNumber)

    override suspend fun getCompanyInvitations(
        id: Int,
        pageNumber: Int
    ): GenericResponse<InvitationModel> =
        apiService.getCompanyInvitations(id, pageNumber)

    override suspend fun getAllExp(id: Int): List<Experience> = apiService.getAllExp(id)
    override suspend fun getAllEduc(id: Int): List<Educations> = apiService.getAllEduc(id)
    override suspend fun getAllEducations(id: Int, pageNumber: Int): GenericResponse<Educations> =
        apiService.getAllEducations(id, pageNumber)

    override suspend fun searchCandidates(
        word: String,
        id: Int,
        pageNumber: Int
    ): GenericResponse<SearchHistory> = apiService.searchCandidate(word, id, pageNumber)

    override suspend fun searchUsers(
        criteria: CriteriaModel,
        pageNumber: Int
    ): GenericResponse<User> = apiService.searchUsers(criteria, pageNumber)

    override suspend fun getFavorites(id: Int, pageNumber: Int): GenericResponse<User> =
        apiService.getFavorites(id, pageNumber)

    override suspend fun getInvitations(
        id: Int,
        pageNumber: Int
    ): GenericResponse<InvitationModel> = apiService.getInvitations(id, pageNumber)

    override suspend fun getAllSearch(id: Int, pageNumber: Int): GenericResponse<SearchHistory> =
        apiService.getAllSearch(id, pageNumber)

    override suspend fun getAllUser(pageNumber: Int): GenericResponse<User> =
        apiService.getAllUser(pageNumber = pageNumber)

    override suspend fun sendInvitation(invitationParams: InvitationParams): UserResponse =
        apiService.sendInvitation(invitationParams)

    override suspend fun saveSearchHistory(
        idUserConnected: Int,
        searchHistory: SearchHistory
    ): UserResponse = apiService.saveSearchHistory(idUserConnected, searchHistory)

    ///////////////////////////////////////////////////////////////////////////
    // ANNOUNCEMENT
    ///////////////////////////////////////////////////////////////////////////
    override suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): UserResponse = apiService.makeAnnouncement(idUserConnected, announcementModel)

    override suspend fun getCompanyAnnouncements(
        id: Int,
        pageNumber: Int
    ): GenericResponse<AnnouncementModel> = apiService.getCompanyAnnouncements(id, pageNumber)

    ///////////////////////////////////////////////////////////////////////////
    // NOTIFICATION
    ///////////////////////////////////////////////////////////////////////////
    override suspend fun getCompanyNotifications(
        id: Int,
        pageNumber: Int
    ): GenericResponse<NotificationModel> = apiService.getCompanyNotifications(id, pageNumber)

    override suspend fun savePersonalInfo(user: User): UserResponse =
        apiService.savePersonalInfo(user)

    override suspend fun saveCompanyInfo(user: User): UserResponse =
        apiService.saveCompanyInfo(user)

    override suspend fun removeSearchHistory(
        idUserConnected: Int,
        idUserToDelete: Int
    ): UserResponse = apiService.removeSearchHistory(idUserConnected, idUserToDelete)

    override suspend fun removeExperience(id: Int, experienceId: Int): UserResponse =
        apiService.removeExperience(id, experienceId)

    override suspend fun removeEducation(id: Int, educationId: Int): UserResponse =
        apiService.removeEducation(id, educationId)

    override suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): UserResponse =
        apiService.saveToFavorite(idUserConnected, candidateId)

    override suspend fun getUser(id: Int): User = apiService.getUser(id)
    override suspend fun uploadFile(file: MultipartBody.Part): UserResponse =
        apiService.uploadFile(file)

    override suspend fun validateProfile(email: String): UserResponse =
        apiService.validateProfile(email)

    override suspend fun verifyExisting(fileName: String): FileExistingResponse =
        apiService.verifyExisting(fileName)

    override suspend fun forgotPassword(email: String): UserResponse =
        apiService.forgotPassword(email)

    override suspend fun resetPassword(token: String, newPassword: String): UserResponse =
        apiService.resetPassword(token, newPassword)

    override suspend fun authenticate(user: User): ApiResult<LoginResponse> =
        apiService.authenticate(user)

    override suspend fun verifyEmail(email: String): LoginResponse = apiService.verifyEmail(email)
}