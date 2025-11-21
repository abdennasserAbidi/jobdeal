package com.example.myjob.base

import com.example.myjob.feature.messagerie.ChatMessage
import com.example.myjob.feature.messagerie.TypingIndicator
import com.example.myjob.feature.messagerie.UserJoinedEvent
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import java.net.URISyntaxException

class SocketIOManager(
    private val serverUrl: String = "http://10.0.2.2:9092",
    private val userId: String
) {
    private var socket: Socket? = null
    private val gson = Gson()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _newMessages = MutableStateFlow<ChatMessage?>(null)
    val newMessages: StateFlow<ChatMessage?> = _newMessages

    private val _typingIndicators = MutableStateFlow<TypingIndicator?>(null)
    val typingIndicators: StateFlow<TypingIndicator?> = _typingIndicators

    private val _userJoined = MutableStateFlow<UserJoinedEvent?>(null)
    val userJoined: StateFlow<UserJoinedEvent?> = _userJoined

    fun connect() {
        try {
            val options = IO.Options().apply {
                query = "userId=$userId"
                reconnection = true
                reconnectionDelay = 1000
                reconnectionDelayMax = 5000
                timeout = 20000
                transports = arrayOf("websocket", "polling")
            }

            socket = IO.socket(serverUrl, options)

            socket?.apply {
                on(Socket.EVENT_CONNECT, onConnect)
                on(Socket.EVENT_DISCONNECT, onDisconnect)
                on(Socket.EVENT_CONNECT_ERROR, onConnectError)

                // Custom events
                on("new_message", onNewMessage)
                on("user_typing", onUserTyping)
                on("user_joined", onUserJoined)

                connect()
            }

        } catch (e: URISyntaxException) {
            e.printStackTrace()
            _connectionState.value = ConnectionState.ERROR
        }
    }

    private val onConnect = Emitter.Listener {
        println("Socket.IO Connected!")
        _connectionState.value = ConnectionState.CONNECTED
    }

    private val onDisconnect = Emitter.Listener {
        println("Socket.IO Disconnected!")
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    private val onConnectError = Emitter.Listener { args ->
        println("Socket.IO Connection Error: ${args.firstOrNull()}")
        _connectionState.value = ConnectionState.ERROR
    }

    private val onNewMessage = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            val message = gson.fromJson(data.toString(), ChatMessage::class.java)
            println("Received message: ${message.content}")
            _newMessages.value = message
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val onUserTyping = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            val indicator = gson.fromJson(data.toString(), TypingIndicator::class.java)
            _typingIndicators.value = indicator
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val onUserJoined = Emitter.Listener { args ->
        try {
            val data = args[0] as JSONObject
            val event = gson.fromJson(data.toString(), UserJoinedEvent::class.java)
            _userJoined.value = event
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun joinConversation(conversationId: String, userName: String) {
        val event = UserJoinedEvent(
            conversationId = conversationId,
            userId = userId,
            userName = userName
        )

        val jsonObject = JSONObject(gson.toJson(event))
        socket?.emit("join_conversation", jsonObject)
        println("Joined conversation: $conversationId")
    }

    fun sendMessage(message: ChatMessage) {
        val jsonObject = JSONObject(gson.toJson(message))
        socket?.emit("send_message", jsonObject)
        println("Sent message: ${message.content}")
    }

    fun sendTypingIndicator(indicator: TypingIndicator) {
        val jsonObject = JSONObject(gson.toJson(indicator))
        socket?.emit("typing", jsonObject)
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off()
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }
}

enum class ConnectionState {
    CONNECTED, DISCONNECTED, CONNECTING, ERROR
}