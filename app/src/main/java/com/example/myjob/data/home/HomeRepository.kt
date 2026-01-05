package com.example.myjob.data.home

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface HomeRepository {
    suspend fun getFavorites(id: Int): Flow<Resource<PagingData<User>>>
    suspend fun getAllUser(id: Int): Flow<Resource<PagingData<User>>>
    suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): Flow<Resource<UserResponse>>
    suspend fun getUser(id: Int): Flow<Resource<User>>
    suspend fun uploadFile(file: MultipartBody.Part): Flow<Resource<String>>
    suspend fun uploadFiles(idUser: Int, file: MultipartBody.Part): Flow<Resource<String>>
    suspend fun getFiles(id: Int): Flow<Resource<List<String>>>
    suspend fun validateProfile(email: String): Flow<Resource<String>>
    suspend fun verifyExisting(fileName: String): Flow<Resource<FileExistingResponse>>
}