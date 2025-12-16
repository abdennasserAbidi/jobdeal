package com.example.myjob.data.chat

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.Conversation
import com.example.myjob.feature.messagerie.CreateConversationRequest
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun retrieveMessages(id: Int): Flow<Resource<PagingData<ChatMessage>>>

    suspend fun getUserConversations(userId: String): Flow<Resource<List<Conversation>>>

    suspend fun getConversation(idSender: Int, idReceiver: Int): Flow<Resource<List<ChatMessage>>>

    suspend fun createConversation(request: CreateConversationRequest): Flow<Resource<Conversation>>
    suspend fun findOrCreateConversation(
        user1Id: String,
        user2Id: String,
        user1Name: String,
        user2Name: String
    ): Flow<Resource<Conversation>>

    suspend fun getMessages(
        conversationId: String,
        limit: Int = 50
    ): Flow<Resource<List<ChatMessage>>>

    suspend fun saveMessage(message: ChatMessage): Flow<Resource<ChatMessage>>
}