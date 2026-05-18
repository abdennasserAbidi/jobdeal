package com.example.myjob.common

import android.util.Log
import com.example.myjob.domain.response.PagingResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

class CoroutineWebSocketClient(
    private val url: String
) {

    private val gsonWithNan = GsonBuilder()
        .serializeSpecialFloatingPointValues() // Permits NaN during both serialization and deserialization
        .create()

    private val stompClient: StompClient = Stomp
        .over(Stomp.ConnectionProvider.OKHTTP, url)
        .withClientHeartbeat(25000)
        .withServerHeartbeat(25000)

    fun connect() {
        stompClient.connect()
    }

    fun disconnect() {
        stompClient.disconnect()
    }

    // Flow to observe lifecycle events as coroutine stream
    fun lifecycleFlow(): Flow<LifecycleEvent> = callbackFlow {
        val disposable = stompClient.lifecycle()
            .subscribe({ lifecycleEvent ->
                Log.d("STOMP", "Lifecycle: ${lifecycleEvent.type}")
                trySend(lifecycleEvent).isSuccess
            }, { error ->
                Log.e("STOMP", "Lifecycle error", error)
            })

        awaitClose { disposable.dispose() }
    }

    // Flow to receive messages from a topic
    fun topicFlowMessage(topic: String): Flow<String> = callbackFlow {
        val disposable = stompClient.topic(topic).subscribe { stompMessage ->
            trySend(stompMessage.payload).isSuccess
        }
        awaitClose { disposable.dispose() }
    }

    fun topicFlow(topic: String): Flow<PagingResponse> = callbackFlow {
        val disposable = stompClient.topic(topic).subscribe({ stompMessage ->
            val json = stompMessage.payload
            Log.i("dlmfgtnjro", "topicFlow: $json")

            val pagedData = Gson().fromJson(json, PagingResponse::class.java)
            Log.i("dlmfgtnjro", "topicFlow: $pagedData")
            trySend(pagedData).isSuccess
        }, { error ->
            Log.e("STOMP", "Error in topic subscription", error)
        })
        awaitClose { disposable.dispose() }
    }

    fun send(json: String): Flow<Any> = callbackFlow {
        val jsonPayload: String = gsonWithNan.toJson(json)

        val disposable = stompClient.send("/app/requestInvitations", jsonPayload).subscribe({
            Log.d("WS", "✅ Sent request for invitations")
        }, { error ->
            Log.e("STOMP", "Error in topic subscription", error)
        })
        awaitClose { disposable.dispose() }
    }

    suspend fun sendMessage(destination: String, payload: String) {
        stompClient.send(destination, payload).subscribe()
    }
}