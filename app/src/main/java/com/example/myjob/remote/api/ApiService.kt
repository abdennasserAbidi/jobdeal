package com.example.myjob.remote.api

import com.example.myjob.base.GenericResponse
import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.CandidateSkills
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.InvitationFilter
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationUser
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.AnnounceResponse
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.FilesResponse
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UploadResponse
import com.example.myjob.domain.response.UserAuthResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.Conversation
import com.example.myjob.feature.messagerie.CreateConversationRequest
import com.example.myjob.feature.validateprofile.ValidationProfileStatus
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * The main services that handles all endpoint processes
 */
interface
ApiService {

    @POST("auth/verifyAccountCompany")
    suspend fun verifyAccountCompany(
        @Query("id") id: Int,
        @Query("isAccepted") isAccepted: Boolean,
    ): UserResponse

    @POST("auth/verifyAccountCandidate")
    suspend fun verifyAccountCandidate(
        @Query("id") id: Int,
        @Query("isAccepted") isAccepted: Boolean,
    ): UserResponse

    @GET("auth/getCompaniesValidated")
    suspend fun getCompaniesValidated(): List<String>

    @GET("auth/getInstitutesValidated")
    suspend fun getInstitutesValidated(): List<String>

    @POST("auth/updatetoken")
    suspend fun updateToken(
        @Query("id") id: Int,
        @Query("token") token: String
    ): UserResponse

    @POST("auth/signup")
    suspend fun saveUser(@Body user: User): ApiResult<LoginResponse>

    @POST("auth/login")
    suspend fun authenticate(@Body user: User): LoginResponse

    @POST("auth/verification")
    suspend fun verifyEmail(@Query("email") email: String): LoginResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): UserResponse

    @POST("/auth/validate-profile-candidate")
    suspend fun validateCandidateProfile(@Body validationProfileStatus: ValidationProfileStatus): UserResponse

    @GET("/auth/statusListCandidateValidation")
    suspend fun statusListCandidateValidation(@Query("id") id: Int): List<ValidationProfileStatus>

    @GET("/auth/statusCandidateValidation")
    suspend fun statusCandidateValidation(@Query("id") id: Int): ValidationProfileStatus

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

    @POST("auth/countDownTrialUser")
    suspend fun countDownTrialUser(@Query("idUser") idUser: Int): UserResponse

    @POST("auth/addEducation")
    suspend fun saveEducation(@Body educations: Educations): UserResponse

    @GET("auth/getAllEducation")
    suspend fun getAllEducations(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<Educations>

    @GET("auth/getAllCandidate")
    suspend fun getAllUser(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    @GET("auth/getAllCandidateService")
    suspend fun getAllCandidateService(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    @GET("auth/getUserServiceFiltered")
    suspend fun getUserServiceFiltered(
        @Query("word") word: String,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>


    @GET("auth/getNewCandidate")
    suspend fun getNewCandidate(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<User>

    ///////////////////////////////////////////////////////////////////////////
    // INVITATION
    ///////////////////////////////////////////////////////////////////////////
    @POST("auth/sendInvitation")
    suspend fun sendInvitation(
        @Body invitationParams: InvitationParams
    ): UserResponse

    @POST("auth/deleteInvitation")
    suspend fun deleteInvitation(
        @Query("idInvitation") idInvitation: Int,
        @Query("idInvitationFrom") idInvitationFrom: Int
    ): UserResponse

    @POST("auth/finishProcess")
    suspend fun finishProcess(@Body invitationParams: InvitationParams): InvitationParams

    @GET("auth/getInvitations")
    suspend fun getInvitations(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<InvitationModel>

    @GET("auth/getInvitationsByTag")
    suspend fun getInvitationsByTag(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<InvitationModel>

    @GET("auth/getInvitationDetail")
    suspend fun getInvitationDetail(
        @Query("id") id: Int,
        @Query("idInvitation") idInvitation: Int
    ): InvitationUser

    @POST("auth/getFilteredInvitation")
    suspend fun getFilteredInvitations(
        @Body invitationFiltered: InvitationFilter,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<InvitationModel>

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
    @POST("auth/updateCandidateProfessional")
    suspend fun updateCandidateProfessional(@Body user: ProfessionalStatus): UserResponse

    @POST("auth/updateCandidateSkills")
    suspend fun updateCandidateSkills(@Body user: CandidateSkills): UserResponse

    @POST("auth/updateCandidateCompleted")
    suspend fun updateCandidateCompleted(@Query("id") id: Int): UserResponse

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
    suspend fun getUser(@Query("id") id: Int): UserAuthResponse

    @Multipart
    @POST("auth/uploadCV")
    suspend fun uploadFile(@Part file: MultipartBody.Part): UserResponse

    @Multipart
    @POST("auth/upload")
    suspend fun upload(
        @Query("idUser") idUser: Int,
        @Part image: MultipartBody.Part
    ): UploadResponse

    @Multipart
    @POST("auth/uploadChat")
    suspend fun uploadChat(
        @Query("idFrom") idFrom: Int,
        @Query("idTo") idTo: Int,
        @Part image: MultipartBody.Part
    ): UploadResponse

    @Multipart
    @POST("auth/uploadDirect")
    suspend fun uploadDirect(
        @Query("idFrom") idFrom: Int,
        @Query("idTo") idTo: Int,
        @Part image: List<MultipartBody.Part>
    ): UploadResponse

    @GET("auth/getFiles")
    suspend fun getFiles(@Query("id") id: Int): FilesResponse

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
        @Query("id") id: Int,
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

    @POST("auth/updateAnnouncement")
    suspend fun updateAnnouncement(
        @Query("idUserConnected") idUserConnected: Int,
        @Body announcementModel: AnnouncementModel
    ): UserResponse

    @GET("auth/findAnnounceCompany")
    suspend fun findAnnounceCompany(
        @Query("type") type: String,
        @Query("idCompany") idCompany: Int,
    ): AnnounceResponse

    @GET("auth/findAnnounceCandidate")
    suspend fun findAnnounceCandidate(
        @Query("type") type: String,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<AnnouncementModel>

    @POST("auth/deletePostCompany")
    suspend fun deletePostCompany(
        @Query("idAnnounce") idAnnounce: Int,
        @Query("idConnected") idConnected: Int,
    ): UserResponse

    @GET("auth/getAnnouncement")
    suspend fun getAnnouncement(
        @Query("idAnnounce") idAnnounce: Int,
        @Query("idCompany") idCompany: Int,
    ): AnnouncementModel

    @POST("auth/removeLike")
    suspend fun removeLike(
        @Query("idAnnounce") idAnnounce: Int,
        @Query("idConnected") idConnected: Int
    ): UserResponse

    @GET("auth/checkUserLike")
    suspend fun checkUserLike(
        @Query("idAnnounce") idAnnounce: Int,
        @Query("idConnected") idConnected: Int
    ): Boolean

    ///////////////////////////////////////////////////////////////////////////
    // CANDIDATE
    ///////////////////////////////////////////////////////////////////////////
    @GET("auth/checkUserLikeAllPost")
    suspend fun checkUserLikeAllPost(
        @Query("idConnected") idConnected: Int
    ): List<Boolean>

    @GET("auth/getNumberLikeAllPosts")
    suspend fun getNumberLikeAllPosts(): List<Int>

    @GET("auth/getNumberCommentAllPosts")
    suspend fun getNumberCommentAllPosts(): List<Int>

    ///////////////////////////////////////////////////////////////////////////
    // COMPANY
    ///////////////////////////////////////////////////////////////////////////
    @GET("auth/checkUserLikeAllPostCompany")
    suspend fun checkUserLikeAllPostCompany(
        @Query("idConnected") idConnected: Int
    ): List<Boolean>

    @GET("auth/getNumberLikeAllPostsCompany")
    suspend fun getNumberLikeAllPostsCompany(
        @Query("idConnected") idConnected: Int
    ): List<Int>

    @GET("auth/getNumberCommentAllPostsCompany")
    suspend fun getNumberCommentAllPostsCompany(
        @Query("idConnected") idConnected: Int
    ): List<Int>

    @GET("auth/getCommentAllPostsCompany")
    suspend fun getCommentAllPostsCompany(
        @Query("idAnnounce") idAnnounce: Int
    ): List<CommentsPost>


    @POST("auth/addLikes")
    suspend fun addLikes(
        @Query("idAnnounce") idAnnounce: Int,
        @Body likesPost: LikesPost
    ): UserResponse

    @POST("auth/addComment")
    suspend fun addComment(
        @Query("idAnnounce") idAnnounce: Int,
        @Body commentsPost: CommentsPost
    ): UserResponse

    @GET("auth/getCompanyAnnouncements")
    suspend fun getCompanyAnnouncements(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<AnnouncementModel>

    @GET("auth/getAnnouncementsCandidate")
    suspend fun getAnnouncementsCandidate(
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<AnnouncementModel>

    ///////////////////////////////////////////////////////////////////////////
    // DEMANDS
    ///////////////////////////////////////////////////////////////////////////
    @GET("auth/getAllDemands")
    suspend fun getAllDemands(
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<MarketDemandModel>

    @DELETE("auth/deleteDemand")
    suspend fun deleteDemand(
        @Query("idDemand") idDemand: Int
    ): UserResponse

    @GET("auth/getDemandFiltered")
    suspend fun getDemandFiltered(
        @Query("word") word: String,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<MarketDemandModel>

    @GET("auth/getDemand")
    suspend fun getDemand(
        @Query("idDemand") idDemand: Int
    ): MarketDemandModel

    @POST("auth/saveDemand")
    suspend fun saveDemand(@Body marketDemandModel: MarketDemandModel): UserResponse

    @POST("auth/countDownTrial")
    suspend fun countDownTrial(@Query("idDemand") idDemand: Int): UserResponse

    ///////////////////////////////////////////////////////////////////////////
    // NOTIFICATION
    ///////////////////////////////////////////////////////////////////////////
    @GET("auth/getNotifications")
    suspend fun getCompanyNotifications(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<NotificationModel>

    @GET("auth/getDemandNotifications")
    suspend fun getDemandNotifications(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<NotificationModel>

    @POST("auth/seenNotification")
    suspend fun seenNotification(
        @Query("idNotification") idNotification: Int
    ): UserResponse

    @POST("auth/removeNotification")
    suspend fun removeNotification(
        @Query("idNotification") idNotification: Int
    ): UserResponse

    @POST("auth/sendnotification")
    suspend fun sendNotification(@Body base: NotificationMessage): UserResponse

    ///////////////////////////////////////////////////////////////////////////
    // REAL TIME CHAT
    ///////////////////////////////////////////////////////////////////////////
    @GET("conversations/{userId}")
    suspend fun getUserConversations(@Path("userId") userId: String): List<Conversation>

    @GET("auth/getConversation")
    suspend fun getConversation(
        @Query("idSender") idSender: Int,
        @Query("idReceiver") idReceiver: Int
    ): List<ChatMessage>

    @POST("conversations")
    suspend fun createConversation(@Body request: CreateConversationRequest): Conversation

    @GET("conversations/find")
    suspend fun findOrCreateConversation(
        @Query("user1Id") user1Id: String,
        @Query("user2Id") user2Id: String,
        @Query("user1Name") user1Name: String,
        @Query("user2Name") user2Name: String
    ): Conversation

    @GET("conversations/{conversationId}/messages")
    suspend fun getMessages(
        @Path("conversationId") conversationId: String,
        @Query("limit") limit: Int = 50
    ): List<ChatMessage>

    @POST("messages")
    suspend fun saveMessage(@Body message: ChatMessage): ChatMessage

    @GET("auth/list_messages")
    suspend fun retrieveMessages(
        @Query("id") id: Int,
        @Query("page") pageNumber: Int,
        @Query("size") size: Int = 10
    ): GenericResponse<ChatMessage>
}