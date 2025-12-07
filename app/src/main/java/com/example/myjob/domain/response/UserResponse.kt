package com.example.myjob.domain.response

data class UserResponse(var message: String ?= "")
data class UploadResponse(var imageURL: String ?= "")
data class FilesResponse(var url: List<String>? = emptyList())