package com.example.myjob.domain.response

import com.example.myjob.domain.entities.User

data class UserAuthResponse(var user: User? = User(), var message: String? = "")
data class UserResponse(var message: String? = "")
data class UploadResponse(var imageURL: String? = "")
data class FilesResponse(var url: List<String>? = emptyList())