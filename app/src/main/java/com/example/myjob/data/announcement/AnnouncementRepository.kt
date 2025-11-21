package com.example.myjob.data.announcement

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): Flow<Resource<UserResponse>>

    suspend fun removeLike(
        idAnnounce: Int,
        idConnected: Int
    ): Flow<Resource<UserResponse>>

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