package com.example.myjob.domain.response

import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.announcement.AnnouncementModel

data class UserAuthResponse(var user: User? = User(), var message: String? = "")
data class UserResponse(var message: String? = "")
data class RatePercentageResponse(var message: String? = "", var percent: Double ?= 0.0, var candidateId: Int ?= 0)
data class AnnounceResponse(
    var idAnnounceResponse: Int,
    var announceModel: List<AnnouncementModel>? = emptyList(),
    var message: String? = ""
)

data class UploadResponse(var imageURL: String? = "")
data class FilesResponse(var url: List<String>? = emptyList())