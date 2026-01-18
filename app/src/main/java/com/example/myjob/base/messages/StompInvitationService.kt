package com.example.myjob.base.messages

import android.annotation.SuppressLint
import android.util.Log
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.feature.messagerie.ChatMessage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

object StompInvitationService {

    private const val WS_URL = "http://192.168.1.129:9090/ws"

    private val stompClient = Stomp.over(
        Stomp.ConnectionProvider.OKHTTP,
        WS_URL
    )

    private val _messages = MutableSharedFlow<InvitationModel>()
    val messages = _messages.asSharedFlow()

    private val gson = Gson()

    @SuppressLint("CheckResult")
    fun connect(currentUserId: String) {
        val headers = listOf(
            StompHeader("user-id", currentUserId)
        )
        stompClient.connect(headers)
        stompClient.lifecycle().subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED ->
                    Log.d("STOMP", "Connected")

                LifecycleEvent.Type.ERROR ->
                    Log.e("STOMP", "Error", event.exception)

                LifecycleEvent.Type.CLOSED ->
                    Log.d("STOMP", "Disconnected")

                else -> {}
            }
        }

        subscribePrivateMessages()
    }

    @SuppressLint("CheckResult")
    private fun subscribePrivateMessages() {
        stompClient.topic("/user/queue/invitations")
            .subscribe { msg ->
                val chat = gson.fromJson(msg.payload, InvitationModel::class.java)
                Log.i("fzejhgrzg", "Received: $chat")
                CoroutineScope(Dispatchers.IO).launch {
                    _messages.emit(chat)
                }
            }
    }

    fun sendMessage(message: String) {
        stompClient.send(
            "/app/chat.send",
            message
        ).subscribe()
    }

    fun disconnect() {
        stompClient.disconnect()
    }
}
