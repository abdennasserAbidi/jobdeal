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
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.ExchangeRates
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
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