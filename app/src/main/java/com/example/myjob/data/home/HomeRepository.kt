package com.example.myjob.data.home

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.Company
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.json.CompanyModel
import com.example.myjob.domain.entities.json.GenericJsonModel
import com.example.myjob.domain.entities.json.InstituteModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface HomeRepository {
    suspend fun saveActivity(activityName: String): Flow<Resource<String>>
    suspend fun getAllActivities(): Flow<Resource<List<GenericJsonModel>>>
    suspend fun saveField(fieldName: String): Flow<Resource<String>>
    suspend fun getAllFields(): Flow<Resource<List<GenericJsonModel>>>
    suspend fun saveCompany(companyName: String): Flow<Resource<String>>
    suspend fun getAllCompanies(): Flow<Resource<PagingData<CompanyModel>>>
    suspend fun getAllCompaniesList(): Flow<Resource<List<CompanyModel>>>
    suspend fun saveInstitute(schoolName: String): Flow<Resource<String>>
    suspend fun getAllInstitutes(): Flow<Resource<List<InstituteModel>>>
    suspend fun getFavorites(id: Int): Flow<Resource<PagingData<User>>>
    suspend fun getAllUser(id: Int): Flow<Resource<PagingData<User>>>
    suspend fun getAllCandidateService(id: Int): Flow<Resource<PagingData<User>>>
    suspend fun getUserServiceFiltered(word: String): Flow<Resource<PagingData<User>>>
    suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): Flow<Resource<UserResponse>>
    suspend fun getUser(id: Int): Flow<Resource<User>>
    suspend fun uploadFile(file: MultipartBody.Part): Flow<Resource<String>>
    suspend fun uploadFiles(idUser: Int, file: MultipartBody.Part): Flow<Resource<String>>
    suspend fun uploadChat(idFrom: Int, idTo: Int, file: MultipartBody.Part): Flow<Resource<String>>
    suspend fun uploadDirect(
        idFrom: Int,
        idTo: Int,
        file: List<MultipartBody.Part>
    ): Flow<Resource<String>>

    suspend fun getFiles(id: Int): Flow<Resource<List<String>>>
    suspend fun validateProfile(email: String): Flow<Resource<String>>
    suspend fun verifyExisting(fileName: String): Flow<Resource<FileExistingResponse>>
}