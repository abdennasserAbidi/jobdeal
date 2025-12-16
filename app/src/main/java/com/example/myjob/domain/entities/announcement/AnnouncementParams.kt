package com.example.myjob.domain.entities.announcement

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementParams(
    var idUserConnected: Int = 0,
    var announcementModel: AnnouncementModel = AnnouncementModel()
)