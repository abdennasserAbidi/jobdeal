package com.example.myjob.data.announcement

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.response.AnnounceResponse
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): Flow<Resource<UserResponse>>
    suspend fun findAnnounceCompany(type: String, idCompany: Int): Flow<Resource<AnnounceResponse>>

    suspend fun findAnnounceCandidate(
        type: String,
    ): Flow<Resource<PagingData<AnnouncementModel>>>

    suspend fun deletePostCompany(
        idAnnounce: Int,
        idConnected: Int
    ): Flow<Resource<UserResponse>>

    suspend fun getAnnouncement(
        idAnnounce: Int, idCompany: Int
    ): Flow<Resource<AnnouncementModel>>

    suspend fun removeLike(
        idAnnounce: Int,
        idConnected: Int
    ): Flow<Resource<UserResponse>>

    suspend fun checkUserLike(
        idAnnounce: Int,
        idConnected: Int
    ): Flow<Resource<Boolean>>

    suspend fun checkUserLikeAllPost(idConnected: Int): Flow<Resource<List<Boolean>>>

    suspend fun getNumberLikeAllPosts(idConnected: Int): Flow<Resource<List<Int>>>

    suspend fun getNumberCommentAllPosts(idConnected: Int): Flow<Resource<List<Int>>>

    ///////////////////////////////////////////////////////////////////////////
    // COMPANY
    ///////////////////////////////////////////////////////////////////////////
    suspend fun checkUserLikeAllPostCompany(idConnected: Int): Flow<Resource<List<Boolean>>>
    suspend fun getNumberLikeAllPostsCompany(idConnected: Int): Flow<Resource<List<Int>>>
    suspend fun getNumberCommentAllPostsCompany(idConnected: Int): Flow<Resource<List<Int>>>
    suspend fun getCommentAllPostsCompany(idAnnounce: Int): Flow<Resource<List<CommentsPost>>>

    suspend fun addLikes(
        idAnnounce: Int,
        likesPost: LikesPost
    ): Flow<Resource<UserResponse>>

    suspend fun addComment(
        idAnnounce: Int,
        commentsPost: CommentsPost
    ): Flow<Resource<UserResponse>>

    suspend fun getCompanyAnnouncements(
        id: Int,
    ): Flow<Resource<PagingData<AnnouncementModel>>>

    suspend fun getAnnouncementsCandidate(): Flow<Resource<PagingData<AnnouncementModel>>>
}