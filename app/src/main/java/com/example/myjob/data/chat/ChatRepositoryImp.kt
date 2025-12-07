package com.example.myjob.data.chat

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.Conversation
import com.example.myjob.feature.messagerie.CreateConversationRequest
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.chat.ChatDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImp @Inject constructor(
    private val remoteDataSource: ChatDataSource,
    private val sharedPreference: SharedPreference
) : ChatRepository {

    override suspend fun retrieveMessages(id: Int): Flow<Resource<PagingData<ChatMessage>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val educations =
                        remoteDataSource.retrieveMessages(id, pageNumber = currentPage)

                    val json = Gson().toJson(educations.content)
                    sharedPreference.putString("jsonChatMessages", json)

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

    override suspend fun getConversation(idSender: Int, idReceiver: Int): Flow<Resource<List<ChatMessage>>> = flow {
        try {
            // Get data from RemoteDataSource
            val data = remoteDataSource.getConversation(idSender, idReceiver)
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