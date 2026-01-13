package com.example.myjob.data.home

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.UploadResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.home.HomeDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val remoteDataSource: HomeDataSource,
    private val sharedPreference: SharedPreference
) : HomeRepository {

    override suspend fun getAllUser(id: Int): Flow<Resource<PagingData<User>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val users = remoteDataSource.getAllUser(id, pageNumber = currentPage)
                    val lang = sharedPreference.getString("lang", "") ?: ""
                    users.content.map {
                        val gender = it.sexe ?: ""
                        it.changeSex(gender, lang)

                        val situation = it.situation ?: ""
                        it.changeSituation(situation, lang)

                        /*if (it.role == "Candidate" || it.role == "Candidat") {
                            val availability = it.professionalStatus.availability ?: ""
                            it.changeAvailability(availability, lang)
                        }*/
                    }

                    val json = Gson().toJson(users.content)
                    sharedPreference.putString("jsonUser", json)

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

    override suspend fun getFavorites(id: Int): Flow<Resource<PagingData<User>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        remoteDataSource.getFavorites(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonFavorites", json)

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

    override suspend fun verifyExisting(fileName: String): Flow<Resource<FileExistingResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.verifyExisting(fileName)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun saveToFavorite(
        idUserConnected: Int,
        candidateId: Int
    ): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.saveToFavorite(idUserConnected, candidateId)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun getUser(id: Int): Flow<Resource<User>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getUser(id)
            val user = data.user ?: User()
            val message = data.message ?: ""

            if (message.isNotEmpty()) {
                emit(Resource(ResourceState.ERROR, null, message))
            } else {
                val lang = sharedPreference.getString("lang", "") ?: ""

                val gender = user.sexe ?: ""
                user.changeSex(gender, lang)

                val situation = user.situation ?: ""
                user.changeSituation(situation, lang)

                /*val availability = data.professionalStatus.availability ?: ""
                data.changeAvailability(availability, lang)*/

                // Emit data
                emit(Resource(ResourceState.SUCCESS, user, null))
            }
        } catch (ex: Exception) {
            // Emit error
            Log.i("zlmezlmlezm", "ERROR: ${ex.message}")

            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun uploadFile(file: MultipartBody.Part): Flow<Resource<String>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.uploadFile(file)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data.message, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    override suspend fun uploadFiles(idUser: Int, file: MultipartBody.Part): Flow<Resource<String>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.uploadFiles(idUser, file)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data.imageURL, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    override suspend fun uploadChat(idFrom: Int, idTo: Int, file: MultipartBody.Part): Flow<Resource<String>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.uploadChat(idFrom, idTo, file)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data.imageURL, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun uploadDirect(idFrom: Int, idTo: Int, file: List<MultipartBody.Part>): Flow<Resource<String>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.uploadDirect(idFrom, idTo, file)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data.imageURL, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    override suspend fun getFiles(id: Int): Flow<Resource<List<String>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getFiles(id)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data.url, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun validateProfile(email: String): Flow<Resource<String>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.validateProfile(email)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data.message, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

}