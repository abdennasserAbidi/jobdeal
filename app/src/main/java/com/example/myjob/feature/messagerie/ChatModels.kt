package com.example.myjob.feature.messagerie

// ChatModels.kt
data class ChatMessage(
    val id: String? = null,
    val conversationId: String = "",
    val senderId: String = "",
    val senderName: String = "",
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