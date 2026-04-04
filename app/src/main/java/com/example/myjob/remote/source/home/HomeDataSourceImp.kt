package com.example.myjob.remote.source.home

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.Company
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.json.CompanyModel
import com.example.myjob.domain.entities.json.GenericJsonModel
import com.example.myjob.domain.entities.json.InstituteModel
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.FilesResponse
import com.example.myjob.domain.response.UploadResponse
import com.example.myjob.domain.response.UserAuthResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import javax.inject.Inject

class HomeDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : HomeDataSource {

    ///////////////////////////////////////////////////////////////////////////
    // COMPANIES
    ///////////////////////////////////////////////////////////////////////////
    override suspend fun saveCompany(companyName: String): String =
        apiService.saveCompany(companyName)

    override suspend fun getAllCompanies(pageNumber: Int): GenericResponse<CompanyModel> =
        apiService.getAllCompanies(pageNumber)

    override suspend fun getAllCompanies(): List<CompanyModel> = apiService.getAllCompanies()

    ///////////////////////////////////////////////////////////////////////////
    // INSTITUTES
    ///////////////////////////////////////////////////////////////////////////
    override suspend fun saveInstitute(schoolName: String): String =
        apiService.saveInstitute(schoolName)
    override suspend fun getAllInstitutes(): List<InstituteModel> = apiService.getAllInstitutes()

    ///////////////////////////////////////////////////////////////////////////
    // ACTIVITIES
    ///////////////////////////////////////////////////////////////////////////
    override suspend fun saveActivity(activityName: String): String =
        apiService.saveActivity(activityName)
    override suspend fun getAllActivities(): List<GenericJsonModel> = apiService.getAllActivities()

    ///////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////
    override suspend fun saveField(fieldName: String): String =
        apiService.saveField(fieldName)
    override suspend fun getAllFields(): List<GenericJsonModel> = apiService.getAllFields()

    override suspend fun getFavorites(id: Int, pageNumber: Int): GenericResponse<User> =
        apiService.getFavorites(id, pageNumber)

    override suspend fun getAllUser(id: Int, pageNumber: Int): GenericResponse<User> =
        apiService.getAllUser(id, pageNumber = pageNumber)

    override suspend fun getAllCandidateService(id: Int, pageNumber: Int): GenericResponse<User> =
        apiService.getAllCandidateService(id, pageNumber = pageNumber)

    override suspend fun getUserServiceFiltered(
        word: String,
        pageNumber: Int
    ): GenericResponse<User> =
        apiService.getUserServiceFiltered(word, pageNumber = pageNumber)

    override suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): UserResponse =
        apiService.saveToFavorite(idUserConnected, candidateId)

    override suspend fun getUser(id: Int): UserAuthResponse = apiService.getUser(id)
    override suspend fun uploadFile(file: MultipartBody.Part): UserResponse =
        apiService.uploadFile(file)

    override suspend fun uploadFiles(idUser: Int, file: MultipartBody.Part): UploadResponse =
        apiService.upload(idUser, file)

    override suspend fun uploadChat(
        idFrom: Int,
        idTo: Int,
        file: MultipartBody.Part
    ): UploadResponse =
        apiService.uploadChat(idFrom, idTo, file)

    override suspend fun uploadDirect(
        idFrom: Int,
        idTo: Int,
        file: List<MultipartBody.Part>
    ): UploadResponse =
        apiService.uploadDirect(idFrom, idTo, file)

    override suspend fun getFiles(id: Int): FilesResponse = apiService.getFiles(id)

    override suspend fun validateProfile(email: String): UserResponse =
        apiService.validateProfile(email)

    override suspend fun verifyExisting(fileName: String): FileExistingResponse =
        apiService.verifyExisting(fileName)
}