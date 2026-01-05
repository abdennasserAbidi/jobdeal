package com.example.myjob.remote.source.home

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.FilesResponse
import com.example.myjob.domain.response.UploadResponse
import com.example.myjob.domain.response.UserAuthResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class HomeDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : HomeDataSource {

    override suspend fun getFavorites(id: Int, pageNumber: Int): GenericResponse<User> =
        apiService.getFavorites(id, pageNumber)

    override suspend fun getAllUser(id: Int, pageNumber: Int): GenericResponse<User> =
        apiService.getAllUser(id, pageNumber = pageNumber)

    override suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): UserResponse =
        apiService.saveToFavorite(idUserConnected, candidateId)

    override suspend fun getUser(id: Int): UserAuthResponse = apiService.getUser(id)
    override suspend fun uploadFile(file: MultipartBody.Part): UserResponse =
        apiService.uploadFile(file)
    override suspend fun uploadFiles(idUser: Int, file: MultipartBody.Part): UploadResponse =
        apiService.upload(idUser, file)
    override suspend fun getFiles(id: Int): FilesResponse = apiService.getFiles(id)

    override suspend fun validateProfile(email: String): UserResponse =
        apiService.validateProfile(email)

    override suspend fun verifyExisting(fileName: String): FileExistingResponse =
        apiService.verifyExisting(fileName)
}