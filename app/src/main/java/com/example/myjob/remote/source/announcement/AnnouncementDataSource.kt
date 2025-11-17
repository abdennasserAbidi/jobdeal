package com.example.myjob.remote.source.announcement

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.response.UserResponse

interface AnnouncementDataSource {
    suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): UserResponse

    suspend fun removeLike(
        idAnnounce: Int,
        idConnected: Int
    ): UserResponse

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
}