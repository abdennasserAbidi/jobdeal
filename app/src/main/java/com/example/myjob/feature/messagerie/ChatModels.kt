package com.example.myjob.feature.messagerie

import android.view.View

// ChatModels.kt
data class ChatMessage(
    val id: Int = View.generateViewId(),
    val userReceivedId: Int = 0,
    val userReceivedName: String = "",
    val userConnectedId: Int = 0,
    val userConnectedName: String = "",
    val content: String = "",
    val timestamp: String = "",
    val type: MessageType = MessageType.CHAT
)

enum class MessageType {
    CHAT, JOIN, LEAVE, TYPING
}

data class Conversation(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String? = null,
    val lastMessageTime: String? = null
)

data class TypingIndicator(
    val conversationId: String,
    val userId: String,
    val userName: String,
    val isTyping: Boolean
)

data class UserJoinedEvent(
    val conversationId: String,
    val userId: String,
    val userName: String
)

data class CreateConversationRequest(
    val user1Id: String = "",
    val user2Id: String = "",
    val user1Name: String = "",
    val user2Name: String = ""
)