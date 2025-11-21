package com.example.myjob.remote.source.chat

import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.Conversation
import com.example.myjob.feature.messagerie.CreateConversationRequest
import retrofit2.http.Body

interface ChatDataSource {

    suspend fun getUserConversations(userId: String): List<Conversation>

    suspend fun createConversation(request: CreateConversationRequest): Conversation
    suspend fun findOrCreateConversation(
        user1Id: String,
        user2Id: String,
        user1Name: String,
        user2Name: String
    ): Conversation
    suspend fun getMessages(conversationId: String, limit: Int = 50): List<ChatMessage>
    suspend fun saveMessage(@Body message: ChatMessage): ChatMessage
}