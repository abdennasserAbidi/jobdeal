package com.example.myjob.remote.source.announcement

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
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

    override suspend fun removeLike(
        idAnnounce: Int,
        idConnected: Int
    ): UserResponse = apiService.removeLike(idAnnounce, idConnected)

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