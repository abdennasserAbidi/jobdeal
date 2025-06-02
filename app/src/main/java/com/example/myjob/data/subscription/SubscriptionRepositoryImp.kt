package com.example.myjob.data.subscription

import android.util.Log
import com.example.myjob.local.source.LocalDataSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.network.ApiResult
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.subscription.SubscriptionDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubscriptionRepositoryImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: SubscriptionDataSource,
    private val sharedPreference: SharedPreference
) : SubscriptionRepository {

    override suspend fun saveUser(user: User): Flow<Resource<LoginResponse>> = flow {
        when (val apiResult: ApiResult<LoginResponse> = remoteDataSource.saveUser(user)) {
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

    override suspend fun authenticate(user: User): Flow<Resource<LoginResponse>> = flow {

        when (val apiResult: ApiResult<LoginResponse> = remoteDataSource.authenticate(user)) {
            is ApiResult.Success -> {
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