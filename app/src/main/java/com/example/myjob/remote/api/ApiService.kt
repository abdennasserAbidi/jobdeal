package com.example.myjob.remote.api

import com.example.myjob.base.GenericResponse
import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * The main services that handles all endpoint processes
 */
interface
ApiService {

    @POST("auth/updatetoken")
    suspend fun updateToken(
        @Query("id") id: Int,
        @Query("token") token: String
    ): UserResponse

    @POST("auth/sendnotification")
    suspend fun sendNotification(@Body base: NotificationMessage): UserResponse

    @POST("auth/signup")
    suspend fun saveUser(@Body user: User): ApiResult<LoginResponse>

    @POST("auth/login")
    suspend fun authenticate(@Body user: User): ApiResult<LoginResponse>

    @POST("auth/verification")
    suspend fun verifyEmail(@Query("email") email: String): LoginResponse

    @FormUrlEncoded
    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Field("email") email: String): UserResponse

    @FormUrlEncoded
    @POST("/auth/reset-password")
    suspend fun resetPassword(
        @Field("token") token: String,
        @Field("newPassword") newPassword: String
    ): UserResponse

    @POST("auth/add")
    suspend fun saveExperience(@Body experience: Experience): UserResponse

    @GET("auth/getAllExperience")
    suspend fun getAllExperiences(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<Experience>

    @GET("auth/getAllExp")
    suspend fun getAllExp(
        @Query("id") id: Int
    ): List<Experience>

    @GET("auth/getAllEduc")
    suspend fun getAllEduc(
        @Query("id") id: Int
    ): List<Educations>

    @GET("auth/searchCandidate")
    suspend fun searchCandidate(
        @Query("word") word: String,
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<SearchHistory>

    @POST("auth/getByCriteria")
    suspend fun searchUsers(
        @Body criteria: CriteriaModel,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    @POST("auth/addEducation")
    suspend fun saveEducation(@Body educations: Educations): UserResponse

    @GET("auth/getAllEducation")
    suspend fun getAllEducations(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<Educations>

    @GET("auth/getAllJobs")
    suspend fun getAllUser(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    ///////////////////////////////////////////////////////////////////////////
    // INVITATION
    ///////////////////////////////////////////////////////////////////////////
    @POST("auth/sendInvitation")
    suspend fun sendInvitation(@Body invitationParams: InvitationParams): UserResponse

    @POST("auth/finishProcess")
    suspend fun finishProcess(@Body invitationParams: InvitationParams): InvitationParams

    @GET("auth/getInvitations")
    suspend fun getInvitations(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<InvitationModel>

    @GET("auth/getInvitationDetail")
    suspend fun getInvitationDetail(
        @Query("id") id: Int,
        @Query("idInvitation") idInvitation: Int
    ): InvitationModel

    @GET("auth/getCompanyInvitations")
    suspend fun getCompanyInvitations(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<InvitationModel>

    @POST("auth/acceptRejectInvitation")
    suspend fun acceptRejectInvitation(@Body invitationParams: InvitationParams): UserResponse

    ///////////////////////////////////////////////////////////////////////////
    // USER
    ///////////////////////////////////////////////////////////////////////////
    @POST("auth/updateuser")
    suspend fun savePersonalInfo(@Body user: User): UserResponse

    @POST("auth/updatecompany")
    suspend fun saveCompanyInfo(@Body user: User): UserResponse

    @POST("auth/removeExperience")
    suspend fun removeExperience(
        @Query("id") id: Int,
        @Query("experienceId") experienceId: Int
    ): UserResponse

    @POST("auth/removeSearchHistory")
    suspend fun removeSearchHistory(
        @Query("idUserConnected") idUserConnected: Int,
        @Query("idUserToDelete") idUserToDelete: Int
    ): UserResponse

    @POST("auth/updatefavorite")
    suspend fun saveToFavorite(
        @Query("idUserConnected") idUserConnected: Int,
        @Query("candidateId") candidateId: Int
    ): UserResponse

    /*@GET("auth/getFavorites")
    suspend fun getFavorites(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<FavoriteModel>*/

    @GET("auth/getAllFavoritesCandidates")
    suspend fun getFavorites(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    @POST("auth/removeEducation")
    suspend fun removeEducation(
        @Query("id") id: Int,
        @Query("educationId") educationId: Int
    ): UserResponse

    @GET("auth/getUser")
    suspend fun getUser(@Query("id") id: Int): User

    @Multipart
    @POST("auth/uploadCV")
    suspend fun uploadFile(@Part file: MultipartBody.Part): UserResponse

    @POST("auth/validate-profile")
    suspend fun validateProfile(@Query("email") email: String): UserResponse

    @GET("auth/download")
    suspend fun downloadFile(@Query("fileName") fileName: String): ResponseBody

    @GET("auth/isExisted")
    suspend fun verifyExisting(@Query("fileName") fileName: String): FileExistingResponse

    ///////////////////////////////////////////////////////////////////////////
    // SEARCH HISTORY
    ///////////////////////////////////////////////////////////////////////////
    @GET("auth/getUserFiltered")
    suspend fun getUserFiltered(
        @Query("word") word: String,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    @POST("auth/saveSearchHistory")
    suspend fun saveSearchHistory(
        @Query("idUserConnected") idUserConnected: Int,
        @Body searchHistory: SearchHistory
    ): UserResponse

    @GET("auth/getAllSearchHistory")
    suspend fun getAllSearch(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<SearchHistory>

    ///////////////////////////////////////////////////////////////////////////
    // ANNOUNCEMENT
    ///////////////////////////////////////////////////////////////////////////
    @POST("auth/makeAnnouncement")
    suspend fun makeAnnouncement(
        @Query("idUserConnected") idUserConnected: Int,
        @Body announcementModel: AnnouncementModel
    ): UserResponse

    @GET("auth/getCompanyAnnouncements")
    suspend fun getCompanyAnnouncements(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<AnnouncementModel>

    ///////////////////////////////////////////////////////////////////////////
    // NOTIFICATION
    ///////////////////////////////////////////////////////////////////////////
    @GET("auth/getCompanyNotifications")
    suspend fun getCompanyNotifications(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<NotificationModel>
}