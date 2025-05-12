package com.example.myjob.domain.entities.announcement

data class AnnouncementParams(
    var idUserConnected: Int = 0,
    var announcementModel: AnnouncementModel = AnnouncementModel()
)