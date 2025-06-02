package com.example.myjob.remote.source.home

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class HomeDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : HomeDataSource {

    override suspend fun getFavorites(id: Int, pageNumber: Int): GenericResponse<User> =
        apiService.getFavorites(id, pageNumber)

    override suspend fun getAllUser(pageNumber: Int): GenericResponse<User> =
        apiService.getAllUser(pageNumber = pageNumber)

    override suspend fun getCompanyNotifications(
        id: Int,
        pageNumber: Int
    ): GenericResponse<NotificationModel> = apiService.getCompanyNotifications(id, pageNumber)

    override suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): UserResponse =
        apiService.saveToFavorite(idUserConnected, candidateId)

    override suspend fun getUser(id: Int): User = apiService.getUser(id)
    override suspend fun uploadFile(file: MultipartBody.Part): UserResponse =
        apiService.uploadFile(file)

    override suspend fun validateProfile(email: String): UserResponse =
        apiService.validateProfile(email)

    override suspend fun verifyExisting(fileName: String): FileExistingResponse =
        apiService.verifyExisting(fileName)
}