package com.example.myjob.feature.messagerie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.ConnectionState
import com.example.myjob.base.SocketIOManager
import com.example.myjob.domain.usecase.chat.CreateConversationUseCase
import com.example.myjob.domain.usecase.chat.FindConversationUseCase
import com.example.myjob.domain.usecase.chat.GetMessageUseCase
import com.example.myjob.domain.usecase.chat.GetUserConversationsUseCase
import com.example.myjob.domain.usecase.chat.SaveMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscussionViewModel @Inject constructor(
    private val getUserConversationsUseCase: GetUserConversationsUseCase,
    private val createConversationUseCase: CreateConversationUseCase,
    private val findConversationUseCase: FindConversationUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val saveMessageUseCase: SaveMessageUseCase
) : ViewModel() {


    private val currentUserId = "user123" // Get from auth
    private val currentUserName = "Current User" // Get from auth

    private var socketManager: SocketIOManager? = null

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _typingUsers = MutableStateFlow<Set<String>>(emptySet())
    val typingUsers: StateFlow<Set<String>> = _typingUsers.asStateFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private var currentConversationId: String? = null
    private var typingJob: Job? = null

    fun connectSocket(serverUrl: String = "http://10.0.2.2:9092") {
        socketManager = SocketIOManager(serverUrl, currentUserId)
        socketManager?.connect()

        // Observe connection state
        viewModelScope.launch {
            socketManager?.connectionState?.collect { state ->
                _connectionState.update {
                    state
                }
            }
        }

        // Observe new messages
        viewModelScope.launch {
            socketManager?.newMessages
                ?.filterNotNull()
                ?.collect { message ->
                    _messages.value = _messages.value + message
                }
        }

        // Observe typing indicators
        viewModelScope.launch {
            socketManager?.typingIndicators
                ?.filterNotNull()
                ?.collect { indicator ->
                    if (indicator.userId != currentUserId) {
                        if (indicator.isTyping) {
                            _typingUsers.value = _typingUsers.value + indicator.userName

                            // Auto remove after 3 seconds
                            delay(3000)
                            _typingUsers.value = _typingUsers.value - indicator.userName
                        } else {
                            _typingUsers.value = _typingUsers.value - indicator.userName
                        }
                    }
                }
        }
    }

    fun loadConversations(userId: String = currentUserId) {
        viewModelScope.launch {
            try {
                getUserConversationsUseCase.execute(userId).collectLatest { res ->
                    _conversations.update {
                        res.data ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectConversation(conversationId: String) {
        currentConversationId = conversationId

        // Join conversation via Socket.IO
        socketManager?.joinConversation(conversationId, currentUserName)

        // Load message history
        loadMessages(conversationId)
    }

    private fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            try {
                getMessageUseCase.execute(conversationId).collectLatest { res ->
                    _messages.update {
                        res.data ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendMessage(content: String) {
        currentConversationId?.let { convId ->
            val message = ChatMessage(
                conversationId = convId,
                senderId = currentUserId,
                senderName = currentUserName,
                content = content
            )

            viewModelScope.launch {
                try {
                    // Save to backend
                    saveMessageUseCase.execute(message).collectLatest {

                    }
                    // Send via Socket.IO for real-time delivery
                    socketManager?.sendMessage(message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun sendTypingIndicator(isTyping: Boolean) {
        currentConversationId?.let { convId ->
            val indicator = TypingIndicator(
                conversationId = convId,
                userId = currentUserId,
                userName = currentUserName,
                isTyping = isTyping
            )

            socketManager?.sendTypingIndicator(indicator)

            // Auto-stop typing after 2 seconds
            if (isTyping) {
                typingJob?.cancel()
                typingJob = viewModelScope.launch {
                    delay(2000)
                    sendTypingIndicator(false)
                }
            }
        }
    }

    fun createConversation(otherUserId: String, otherUserName: String) {
        viewModelScope.launch {
            try {
                val request = CreateConversationRequest(
                    user1Id = currentUserId,
                    user2Id = otherUserId,
                    user1Name = currentUserName,
                    user2Name = otherUserName
                )

                createConversationUseCase.execute(request).collectLatest { res ->
                    _conversations.update {
                        it + (res.data ?: Conversation())
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun findOrCreateConversation(otherUserId: String, otherUserName: String, onConversationFound: (String) -> Unit) {
        viewModelScope.launch {
            try {

                val list = listOf(currentUserId, otherUserId, currentUserName, otherUserName)

                findConversationUseCase.execute(list).collectLatest { res ->
                    val conversation = res.data ?: Conversation()
                    onConversationFound(conversation.id)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        socketManager?.disconnect()
    }





























}