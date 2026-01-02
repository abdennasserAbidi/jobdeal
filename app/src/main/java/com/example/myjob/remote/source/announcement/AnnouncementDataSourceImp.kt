package com.example.myjob.remote.source.announcement

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.response.AnnounceResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class AnnouncementDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : AnnouncementDataSource {

    override suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): UserResponse = apiService.makeAnnouncement(idUserConnected, announcementModel)

    override suspend fun findAnnounceCompany(
        type: String,
        idCompany: Int
    ): AnnounceResponse = apiService.findAnnounceCompany(type, idCompany)

    override suspend fun findAnnounceCandidate(
        type: String,
        pageNumber: Int
    ): GenericResponse<AnnouncementModel> = apiService.findAnnounceCandidate(type, pageNumber)

    override suspend fun deletePostCompany(
        idAnnounce: Int,
        idConnected: Int
    ): UserResponse = apiService.deletePostCompany(idAnnounce, idConnected)

    override suspend fun getAnnouncement(idAnnounce: Int, idCompany: Int): AnnouncementModel =
        apiService.getAnnouncement(idAnnounce, idCompany)

    override suspend fun removeLike(
        idAnnounce: Int,
        idConnected: Int
    ): UserResponse = apiService.removeLike(idAnnounce, idConnected)

    ///////////////////////////////////////////////////////////////////////////
    // CANDIDATE
    ///////////////////////////////////////////////////////////////////////////
    override suspend fun checkUserLike(
        idAnnounce: Int,
        idConnected: Int
    ): Boolean = apiService.checkUserLike(idAnnounce, idConnected)

    override suspend fun checkUserLikeAllPost(
        idConnected: Int
    ): List<Boolean> = apiService.checkUserLikeAllPost(idConnected)

    override suspend fun getNumberLikeAllPosts(
        idConnected: Int
    ): List<Int> = apiService.getNumberLikeAllPosts()

    override suspend fun getNumberCommentAllPosts(
        idConnected: Int
    ): List<Int> = apiService.getNumberCommentAllPosts()

    ///////////////////////////////////////////////////////////////////////////
    // COMPANY
    ///////////////////////////////////////////////////////////////////////////

    override suspend fun checkUserLikeAllPostCompany(
        idConnected: Int
    ): List<Boolean> = apiService.checkUserLikeAllPostCompany(idConnected)

    override suspend fun getNumberLikeAllPostsCompany(
        idConnected: Int
    ): List<Int> = apiService.getNumberLikeAllPostsCompany(idConnected)

    override suspend fun getNumberCommentAllPostsCompany(
        idConnected: Int
    ): List<Int> = apiService.getNumberCommentAllPostsCompany(idConnected)

    override suspend fun getCommentAllPostsCompany(
        idAnnounce: Int
    ): List<CommentsPost> = apiService.getCommentAllPostsCompany(idAnnounce)

    override suspend fun addLikes(
        idAnnounce: Int,
        likesPost: LikesPost
    ): UserResponse = apiService.addLikes(idAnnounce, likesPost)

    override suspend fun addComment(
        idAnnounce: Int,
        commentsPost: CommentsPost
    ): UserResponse = apiService.addComment(idAnnounce, commentsPost)

    override suspend fun getCompanyAnnouncements(
        id: Int,
        pageNumber: Int
    ): GenericResponse<AnnouncementModel> = apiService.getCompanyAnnouncements(id, pageNumber)

    override suspend fun getAnnouncementsCandidate(
        pageNumber: Int
    ): GenericResponse<AnnouncementModel> = apiService.getAnnouncementsCandidate(pageNumber)

}