package com.example.myjob.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

object WebsocketService {
    private var webSocket: WebSocket? = null
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    fun connect(token: String) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("wss://jobseeker-vy9q.onrender.com/ws")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                CoroutineScope(Dispatchers.IO).launch {
                    _messages.emit(text)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket Error: ${t.message}")
            }
        })
    }

    fun sendMessage(json: String) {
        webSocket?.send(json)
    }

    fun close() {
        webSocket?.close(1000, null)
    }

}