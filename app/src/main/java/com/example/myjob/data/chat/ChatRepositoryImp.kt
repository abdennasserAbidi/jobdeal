package com.example.myjob.data.chat

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.Conversation
import com.example.myjob.feature.messagerie.CreateConversationRequest
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.chat.ChatDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImp @Inject constructor(
    private val remoteDataSource: ChatDataSource,
    private val sharedPreference: SharedPreference
) : ChatRepository {
    override suspend fun getUserConversations(userId: String): Flow<Resource<List<Conversation>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getUserConversations(userId)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun createConversation(request: CreateConversationRequest): Flow<Resource<Conversation>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.createConversation(request)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun findOrCreateConversation(
        user1Id: String,
        user2Id: String,
        user1Name: String,
        user2Name: String
    ): Flow<Resource<Conversation>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.findOrCreateConversation(
                user1Id,
                user2Id,
                user1Name,
                user2Name
            )
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun getMessages(
        conversationId: String,
        limit: Int
    ): Flow<Resource<List<ChatMessage>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getMessages(conversationId, limit)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }

    override suspend fun saveMessage(message: ChatMessage): Flow<Resource<ChatMessage>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.saveMessage(message)
            // Emit data
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            // Emit error
            emit(Resource(ResourceState.ERROR, null, ex.message))
        }
    }
}