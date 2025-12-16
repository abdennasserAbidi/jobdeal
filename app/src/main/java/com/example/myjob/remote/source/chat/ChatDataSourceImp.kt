package com.example.myjob.remote.source.chat

import com.example.myjob.base.GenericResponse
import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.Conversation
import com.example.myjob.feature.messagerie.CreateConversationRequest
import com.example.myjob.remote.api.ApiService
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject

class ChatDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : ChatDataSource {

    override suspend fun retrieveMessages(
        id: Int,
        pageNumber: Int
    ): GenericResponse<ChatMessage> = apiService.retrieveMessages(id, pageNumber)

    override suspend fun getUserConversations(userId: String): List<Conversation> =
        apiService.getUserConversations(userId)

    override suspend fun getConversation(idSender: Int, idReceiver: Int): List<ChatMessage> =
        apiService.getConversation(idSender, idReceiver)

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