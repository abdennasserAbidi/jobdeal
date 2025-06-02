package com.example.myjob.remote.source.announcement

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.response.UserResponse

interface AnnouncementDataSource {
    suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): UserResponse

    suspend fun getCompanyAnnouncements(
        id: Int,
        pageNumber: Int
    ): GenericResponse<AnnouncementModel>
}