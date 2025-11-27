package com.example.myjob.remote.source.announcement

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.response.UserResponse
import retrofit2.http.Query

interface AnnouncementDataSource {
    suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): UserResponse

    suspend fun removeLike(
        idAnnounce: Int,
        idConnected: Int
    ): UserResponse

    suspend fun checkUserLike(
        idAnnounce: Int,
        idConnected: Int
    ): Boolean

    ///////////////////////////////////////////////////////////////////////////
    // CANDIDATE
    ///////////////////////////////////////////////////////////////////////////
    suspend fun checkUserLikeAllPost(idConnected: Int): List<Boolean>

    suspend fun getNumberLikeAllPosts(idConnected: Int): List<Int>

    suspend fun getNumberCommentAllPosts(idConnected: Int): List<Int>

    ///////////////////////////////////////////////////////////////////////////
    // COMPANY
    ///////////////////////////////////////////////////////////////////////////
    suspend fun checkUserLikeAllPostCompany(idConnected: Int): List<Boolean>
    suspend fun getNumberLikeAllPostsCompany(idConnected: Int): List<Int>
    suspend fun getNumberCommentAllPostsCompany(idConnected: Int): List<Int>
    suspend fun getCommentAllPostsCompany(
        idAnnounce: Int,
        idConnected: Int
    ): List<CommentsPost>

    suspend fun addLikes(
        idAnnounce: Int,
        likesPost: LikesPost
    ): UserResponse

    suspend fun addComment(
        idAnnounce: Int,
        commentsPost: CommentsPost
    ): UserResponse

    suspend fun getCompanyAnnouncements(
        id: Int,
        pageNumber: Int
    ): GenericResponse<AnnouncementModel>

    suspend fun getAnnouncementsCandidate(
        pageNumber: Int
    ): GenericResponse<AnnouncementModel>
}