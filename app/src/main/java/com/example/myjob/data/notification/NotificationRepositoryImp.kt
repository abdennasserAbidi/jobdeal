package com.example.myjob.data.notification

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.notification.NotificationDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val remoteDataSource: NotificationDataSource,
    private val sharedPreference: SharedPreference
) : NotificationRepository {

    override suspend fun updateToken(id: Int, token: String): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.updateToken(id, token)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }

    override suspend fun sendNotification(base: NotificationMessage): Flow<Resource<UserResponse>> =
        flow {
            try {
                // Get data from RemoteDataSource
                val data = remoteDataSource.sendNotification(base)
                // Emit data
                emit(Resource(ResourceState.SUCCESS, data, null))
            } catch (ex: Exception) {
                // Emit error
                emit(Resource(ResourceState.ERROR, null, ex.message))
            }
        }
}