package com.example.myjob.base.messages

import android.annotation.SuppressLint
import android.util.Log
import com.example.myjob.domain.entities.notification.NotificationModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

object StompNotificationService {

    private const val WS_URL = "http://10.0.2.2:9090/ws"

    private val stompClient = Stomp.over(
        Stomp.ConnectionProvider.OKHTTP,
        WS_URL
    )

    private val _messages = MutableSharedFlow<NotificationModel>()
    val messages = _messages.asSharedFlow()

    private val _messagesDemand = MutableSharedFlow<NotificationModel>()
    val messagesDemand = _messagesDemand.asSharedFlow()

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
        stompClient.topic("/user/queue/notifications")
            .subscribe { msg ->
                val chat = gson.fromJson(msg.payload, NotificationModel::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    if (chat.idDemand != -1)
                        _messagesDemand.emit(chat)
                    else _messages.emit(chat)
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
