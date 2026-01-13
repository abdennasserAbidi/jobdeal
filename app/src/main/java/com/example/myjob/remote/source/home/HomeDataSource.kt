package com.example.myjob.remote.source.home

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.FilesResponse
import com.example.myjob.domain.response.UploadResponse
import com.example.myjob.domain.response.UserAuthResponse
import com.example.myjob.domain.response.UserResponse
import okhttp3.MultipartBody

interface HomeDataSource {
    suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): UserResponse
    suspend fun getUser(id: Int): UserAuthResponse
    suspend fun uploadFile(file: MultipartBody.Part): UserResponse
    suspend fun uploadFiles(idUser: Int, file: MultipartBody.Part): UploadResponse
    suspend fun uploadChat(idFrom: Int, idTo: Int, file: MultipartBody.Part): UploadResponse
    suspend fun uploadDirect(idFrom: Int, idTo: Int, file: List<MultipartBody.Part>): UploadResponse
    suspend fun getFiles(id: Int): FilesResponse
    suspend fun validateProfile(email: String): UserResponse
    suspend fun verifyExisting(fileName: String): FileExistingResponse
    suspend fun getAllUser(id: Int, pageNumber: Int): GenericResponse<User>
    suspend fun getFavorites(id: Int, pageNumber: Int): GenericResponse<User>
}