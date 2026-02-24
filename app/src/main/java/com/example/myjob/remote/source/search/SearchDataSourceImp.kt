package com.example.myjob.remote.source.search

import android.util.Log
import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.CategoryModel
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class SearchDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : SearchDataSource {

    override suspend fun getAllSearch(id: Int, pageNumber: Int): GenericResponse<SearchHistory> =
        apiService.getAllSearch(id, pageNumber)

    override suspend fun saveSearchHistory(
        idUserConnected: Int,
        searchHistory: SearchHistory
    ): UserResponse = apiService.saveSearchHistory(idUserConnected, searchHistory)

    override suspend fun removeSearchHistory(
        idUserConnected: Int,
        idUserToDelete: Int
    ): UserResponse = apiService.removeSearchHistory(idUserConnected, idUserToDelete)

    override suspend fun searchCandidates(
        word: String,
        id: Int,
        pageNumber: Int
    ): GenericResponse<SearchHistory> = apiService.searchCandidate(word, id, pageNumber)

    override suspend fun searchUsers(
        criteria: CriteriaModel,
        pageNumber: Int
    ): GenericResponse<User> = apiService.searchUsers(criteria, pageNumber)

    override suspend fun getUserServiceFilteredList(
        categoryModel: CategoryModel,
        pageNumber: Int
    ): GenericResponse<User> = apiService.getUserServiceFilteredList(categoryModel, pageNumber)

    override suspend fun countDownTrialUser(idUser: Int): UserResponse =
        apiService.countDownTrialUser(idUser)

    override suspend fun getUserFiltered(
        word: String,
        id: Int,
        pageNumber: Int
    ): GenericResponse<User> = apiService.getUserFiltered(word, id, pageNumber)
}