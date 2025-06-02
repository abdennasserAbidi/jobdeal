package com.example.myjob.data.announcement

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    suspend fun makeAnnouncement(
        idUserConnected: Int,
        announcementModel: AnnouncementModel
    ): Flow<Resource<UserResponse>>

    suspend fun getCompanyAnnouncements(
        id: Int,
    ): Flow<Resource<PagingData<AnnouncementModel>>>
}