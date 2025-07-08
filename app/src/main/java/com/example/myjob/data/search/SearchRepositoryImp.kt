package com.example.myjob.data.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myjob.base.GenericResponse
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.local.source.LocalDataSource
import com.example.myjob.remote.source.search.SearchDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: SearchDataSource,
    private val sharedPreference: SharedPreference
) : SearchRepository {

    override suspend fun saveSearchHistory(
        idUserConnected: Int, searchHistory: SearchHistory
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.saveSearchHistory(idUserConnected, searchHistory)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun getAllSearch(id: Int): Flow<Resource<PagingData<SearchHistory>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        remoteDataSource.getAllSearch(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonSearch", json)

                    educations
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.IO))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }

    override suspend fun searchUsers(criteria: CriteriaModel): Flow<Resource<PagingData<User>>> =
        flow {
            val pager = Pager(
                config = PagingConfig(pageSize = 10, prefetchDistance = 2),
                pagingSourceFactory = {
                    GenericSource { currentPage ->
                        val educations =
                            remoteDataSource.searchUsers(
                                criteria = criteria,
                                pageNumber = currentPage
                            )

                        val json = Gson().toJson(educations.content)
                        sharedPreference.putString("jsonFilter", json)

                        educations
                    }
                }
            ).flow.cachedIn(CoroutineScope(Dispatchers.IO))

            emitAll(
                pager.map { pagingData ->
                    Resource(ResourceState.SUCCESS, pagingData, null)
                }
            )
        }.catch { ex ->
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }

    override suspend fun searchCandidate(
        word: String,
        id: Int
    ): Flow<Resource<PagingData<SearchHistory>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    // Get data from RemoteDataSource
                    val educations = remoteDataSource.searchCandidates(
                        word = word,
                        id = id,
                        pageNumber = currentPage
                    )

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonSearchCandidate", json)

                    educations
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.IO))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }

    override suspend fun removeSearchHistory(
        idUserConnected: Int,
        idUserToDelete: Int
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.removeSearchHistory(idUserConnected, idUserToDelete)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    override suspend fun getUserFiltered(word: String): Flow<Resource<PagingData<User>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val users = remoteDataSource.getUserFiltered(word = word, pageNumber = currentPage)
                    val lang = sharedPreference.getString("lang", "") ?: ""
                    users.content.map {
                        val gender = it.sexe ?: ""
                        it.changeSex(gender, lang)

                        val situation = it.situation ?: ""
                        it.changeSituation(situation, lang)

                        val availability = it.availability ?: ""
                        it.changeAvailability(availability, lang)
                    }

                    val json = Gson().toJson(users.content)
                    sharedPreference.putString("jsonUserFiltered", json)

                    users
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.IO))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }
}