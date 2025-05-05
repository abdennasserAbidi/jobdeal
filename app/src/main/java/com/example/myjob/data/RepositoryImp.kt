package com.example.myjob.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.local.source.LocalDataSource
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.InvitationModel
import com.example.myjob.domain.entities.InvitationParams
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.FileExistingResponse
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.RemoteDataSource
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

/**
 * Implementation class of [Repository]
 */
class RepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreference: SharedPreference
) : Repository {

    override suspend fun getExchangesRate(): Flow<Resource<ExchangeRates>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getExchangesRate()
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun changeBaseExchangesRate(base: String): Flow<Resource<ExchangeRates>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.changeBaseExchangesRate(base)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun authenticate(user: User): Flow<Resource<LoginResponse>> = flow {

        when (val apiResult: ApiResult<LoginResponse> = remoteDataSource.authenticate(user)) {
            is ApiResult.Success -> {
                // Handle the successful response
                val data = apiResult.data
                emit(Resource(ResourceState.SUCCESS, data, null))
            }

            is ApiResult.Error -> {
                // Handle the error
                val errorMessage = apiResult.message
                emit(Resource(ResourceState.ERROR, null, errorMessage))
            }

            else -> {}
        }
    }

    override suspend fun verifyEmail(email: String): Flow<Resource<LoginResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.verifyEmail(email)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun saveUser(user: User): Flow<Resource<LoginResponse>> = flow {
        val apiResult: ApiResult<LoginResponse> = remoteDataSource.saveUser(user)
        when (apiResult) {
            is ApiResult.Success -> {
                // Handle the successful response
                val data = apiResult.data
                Log.d("SUCCESS", "Data: $data")
                emit(Resource(ResourceState.SUCCESS, data, null))
            }

            is ApiResult.Error -> {
                // Handle the error
                val errorMessage = apiResult.message
                Log.e("ERROR", "Error: $errorMessage, Code: ${apiResult.code}")
                emit(Resource(ResourceState.ERROR, null, errorMessage))
            }

            else -> {}
        }
    }

    override suspend fun saveExperience(experience: Experience): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.saveExperience(experience)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun saveEducation(educations: Educations): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.saveEducation(educations)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

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

    override suspend fun sendInvitation(invitationParams: InvitationParams): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.sendInvitation(invitationParams)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    /*override suspend fun getAllUser(currentPage: Int): Flow<Resource<PagingData<User>>> = flow {

        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getAllUser(pageNumber = currentPage)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }*/

    override suspend fun getAllUser(): Flow<Resource<PagingData<User>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val users = remoteDataSource.getAllUser(pageNumber = currentPage)

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

    override suspend fun getAllEducations(id: Int): Flow<Resource<PagingData<Educations>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        remoteDataSource.getAllEducations(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonEducation", json)

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
    override suspend fun getInvitations(id: Int): Flow<Resource<PagingData<InvitationModel>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        remoteDataSource.getInvitations(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonInvitations", json)

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

    override suspend fun searchCandidate(word: String, id: Int): Flow<Resource<PagingData<SearchHistory>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    // Get data from RemoteDataSource
                    val educations = remoteDataSource.searchCandidates(word = word, id = id, pageNumber = currentPage)

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

    override suspend fun getAllExperiences(id: Int): Flow<Resource<PagingData<Experience>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->

                    val experiences =
                        remoteDataSource.getAllExperiences(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(experiences.content)
                    sharedPreference.putString("jsonExperience", json)

                    experiences
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.Default))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }
    override suspend fun getCompanyInvitations(id: Int): Flow<Resource<PagingData<InvitationModel>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->

                    val experiences =
                        remoteDataSource.getCompanyInvitations(id = id, pageNumber = currentPage)

                    val json = Gson().toJson(experiences.content)
                    sharedPreference.putString("jsonInvitation", json)

                    experiences
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.Default))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }

    override suspend fun getAllExp(id: Int): Flow<Resource<List<Experience>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getAllExp(id)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    override suspend fun verifyExisting(fileName: String): Flow<Resource<FileExistingResponse>> = flow {
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

    override suspend fun getAllEduc(id: Int): Flow<Resource<List<Educations>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getAllEduc(id)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun searchUsers(criteria: CriteriaModel): Flow<Resource<List<User>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.searchUsers(criteria)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun savePersonalInfo(user: User): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.savePersonalInfo(user)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
    override suspend fun saveCompanyInfo(user: User): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.saveCompanyInfo(user)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun removeExperience(
        id: Int,
        experienceId: Int
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.removeExperience(id, experienceId)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun removeEducation(id: Int, educationId: Int): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.removeEducation(id, educationId)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }
    override suspend fun saveToFavorite(idUserConnected: Int, candidateId: Int): Flow<Resource<UserResponse>> =
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
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
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

    override suspend fun forgotPassword(email: String): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.forgotPassword(email)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun resetPassword(
        token: String,
        newPassword: String
    ): Flow<Resource<UserResponse>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.resetPassword(token, newPassword)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
}