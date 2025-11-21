package com.example.myjob.remote.source.chat

import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.Conversation
import com.example.myjob.feature.messagerie.CreateConversationRequest
import com.example.myjob.remote.api.ApiService
import retrofit2.http.Body
import javax.inject.Inject

class ChatDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : ChatDataSource {

    override suspend fun getUserConversations(userId: String): List<Conversation> =
        apiService.getUserConversations(userId)

    override suspend fun createConversation(request: CreateConversationRequest): Conversation =
        apiService.createConversation(request)

    override suspend fun findOrCreateConversation(
        user1Id: String,
        user2Id: String,
        user1Name: String,
        user2Name: String
    ): Conversation = apiService.findOrCreateConversation(user1Id, user2Id, user1Name, user2Name)

    override suspend fun getMessages(conversationId: String, limit: Int): List<ChatMessage> =
        apiService.getMessages(conversationId, limit)

    override suspend fun saveMessage(@Body message: ChatMessage): ChatMessage =
        apiService.saveMessage(message)
}