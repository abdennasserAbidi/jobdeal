package com.example.myjob.data.search

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.CategoryModel
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun saveSearchHistory(
        idUserConnected: Int, searchHistory: SearchHistory
    ): Flow<Resource<UserResponse>>

    suspend fun searchCandidate(word: String, id: Int): Flow<Resource<PagingData<SearchHistory>>>
    suspend fun searchUsers(criteria: CriteriaModel): Flow<Resource<PagingData<User>>>
    suspend fun getUserServiceFilteredList(categoryModel: CategoryModel): Flow<Resource<PagingData<User>>>
    suspend fun removeSearchHistory(
        idUserConnected: Int,
        idUserToDelete: Int
    ): Flow<Resource<UserResponse>>

    suspend fun getAllSearch(id: Int): Flow<Resource<PagingData<SearchHistory>>>
    suspend fun countDownTrial(idUser: Int): Flow<Resource<UserResponse>>
    suspend fun getUserFiltered(word: String, id: Int): Flow<Resource<PagingData<User>>>
}