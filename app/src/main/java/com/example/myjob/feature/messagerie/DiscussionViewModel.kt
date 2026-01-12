package com.example.myjob.feature.messagerie

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.example.myjob.base.ConnectionState
import com.example.myjob.base.messages.StompChatService
import com.example.myjob.base.messages.WebsocketService
import com.example.myjob.base.messages.WebsocketService.close
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.FileReader
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.common.GlobalEntries.otherUserId
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.chat.FindConversationUseCase
import com.example.myjob.domain.usecase.chat.GetUserConversationsUseCase
import com.example.myjob.domain.usecase.chat.SaveMessageUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.home.UploadFileChatUseCase
import com.example.myjob.domain.usecase.home.UploadFileUseCase
import com.example.myjob.feature.validateprofile.Documents
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class DiscussionViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getUserConversationsUseCase: GetUserConversationsUseCase,
    private val findConversationUseCase: FindConversationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val uploadFileUseCase: UploadFileChatUseCase
) : ViewModel() {

    fun readTextFromUri(context: Context, uri: Uri): String {
        return context.contentResolver.openInputStream(uri)?.bufferedReader()
            .use { it?.readText() ?: "" }
    }

    var messages by mutableStateOf(listOf<ChatMessage>())
        private set

    private val _conversations: MutableStateFlow<PagingData<ChatMessage>> =
        MutableStateFlow(value = PagingData.empty())
    val conversations: StateFlow<PagingData<ChatMessage>> = _conversations.asStateFlow()

    private val _typingUsers = MutableStateFlow<Set<String>>(emptySet())
    val typingUsers: StateFlow<Set<String>> = _typingUsers.asStateFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val _messagesShared = MutableSharedFlow<String>()
    val messagesShared = _messagesShared.asSharedFlow()

    fun getUserById(id: Int) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    candidateUser = u
                    _user.update { u }
                }
            }
        }
    }

    fun isOwnMessage(id: Int): Boolean {
        val currentUserId = sharedPreference.getInt("idUser", 0)
        return id != -1 && id == currentUserId
    }

    private fun loadConversations() {
        viewModelScope.launch {
            try {
                val id = sharedPreference.getInt("idUser", 0)
                getUserConversationsUseCase.execute(id).collectLatest { res ->

                    res.data?.map {
                        it.userConnectedName
                    }

                    _conversations.update {
                        res.data ?: PagingData.empty()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _listMessages: MutableStateFlow<List<ChatMessage>> = MutableStateFlow(emptyList())
    val listMessages: StateFlow<List<ChatMessage>> = _listMessages.asStateFlow()

    fun findConversations(userId: Int) {
        viewModelScope.launch {
            try {
                val id = sharedPreference.getInt("idUser", 0)
                val params = Pair(id, userId)
                findConversationUseCase.execute(params).collectLatest { res ->
                    val data = res.data ?: emptyList()
                    _listMessages.update {
                        data
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getUserName(user: User): String? {
        return if (user.role == "Candidate" || user.role == "Candidat")
            user.fullName
        else user.companyName
    }

    fun connect() {
        val id = sharedPreference.getInt("idUser", 0)
        WebsocketService.connect("")
        StompChatService.connect("$id")

        viewModelScope.launch {
            StompChatService.messages.collect { msg ->
                messages = messages + msg
                val list = _listMessages.value.toMutableList()
                list.add(msg)
                Log.i("jekagfea", "connect: $msg")
                _listMessages.update {
                    list
                }
            }
        }
    }

    var uploadMessage = MutableStateFlow("")

    fun uploadDoc(context: Context, fileUri: Uri, index: Int, documents: Documents) {
        val file = FileReader.getFile(context, fileUri)

        val requestBody: RequestBody =
            RequestBody.create("application/*".toMediaTypeOrNull(), file)

        val expectedName = "document${documents.id}"
        val multipartBody: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", expectedName, requestBody)

        viewModelScope.launch {
            val idFrom = sharedPreference.getInt("idUser", -1)
            val idTo = otherUserId
            val params = Triple(idFrom, idTo,  multipartBody)

            uploadFileUseCase.execute(params).collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        uploadMessage.update {
                            res.data ?: ""
                        }
                    }

                    else -> {
                        uploadMessage.update { "" }
                    }
                }
            }
        }
    }

    fun sendMessage(content: String) {
        val currentUserId = sharedPreference.getInt("idUser", 0)
        Log.i("receivedUser", "sendMessage: $candidateUser")
        val message = ChatMessage(
            userReceivedId = otherUserId,
            userReceivedName = getUserName(candidateUser) ?: "",
            userConnectedId = currentUserId,
            userConnectedName = getUserName(GlobalEntries.user) ?: "",
            content = content
        )

        val list = _listMessages.value.toMutableList()
        list.add(message)
        _listMessages.update {
            list
        }

        StompChatService.sendMessage(Gson().toJson(message))
        messages = messages + message
    }

    override fun onCleared() {
        close()
        StompChatService.disconnect()
        super.onCleared()
    }

    /*fun sendTypingIndicator(isTyping: Boolean) {
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
    }*/

    init {
        loadConversations()
    }


}