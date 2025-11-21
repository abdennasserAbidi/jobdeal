package com.example.myjob.remote.source.search

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse

interface SearchDataSource {
    suspend fun searchCandidates(
        word: String,
        id: Int,
        pageNumber: Int
    ): GenericResponse<SearchHistory>

    suspend fun searchUsers(criteria: CriteriaModel, pageNumber: Int): GenericResponse<User>

    suspend fun getAllSearch(id: Int, pageNumber: Int): GenericResponse<SearchHistory>

    suspend fun saveSearchHistory(idUserConnected: Int, searchHistory: SearchHistory): UserResponse

    suspend fun removeSearchHistory(
        idUserConnected: Int,
        idUserToDelete: Int
    ): UserResponse

    suspend fun getUserFiltered(word: String, id: Int, pageNumber: Int): GenericResponse<User>
}