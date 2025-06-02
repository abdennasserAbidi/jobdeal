package com.example.myjob.remote.source.announcement

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.announcement.AnnouncementModel
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

    override suspend fun getCompanyAnnouncements(
        id: Int,
        pageNumber: Int
    ): GenericResponse<AnnouncementModel> = apiService.getCompanyAnnouncements(id, pageNumber)

}