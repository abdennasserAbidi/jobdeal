package com.example.myjob.feature.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.domain.entities.NotificationBody
import com.example.myjob.domain.entities.SendMessageDto
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.usecase.notification.GetNotificationsUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    var state by mutableStateOf(NotificationState())
        private set


    fun onRemoteTokenChange(newToken: String) {
        state = state.copy(
            remoteToken = newToken
        )
    }

    fun onSubmitRemoteToken() {
        state = state.copy(
            isEnteringToken = false
        )
    }

    fun onMessageChange(message: String) {
        state = state.copy(
            messageText = message
        )
    }

    fun sendMessage(isBroadcast: Boolean) {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = if(isBroadcast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "New message!",
                    body = state.messageText
                )
            )

            /*try {
                if(isBroadcast) {
                    api.broadcast(messageDto)
                } else {
                    api.sendMessage(messageDto)
                }

                state = state.copy(
                    messageText = ""
                )
            } catch(e: HttpException) {
                e.printStackTrace()
            } catch(e: IOException) {
                e.printStackTrace()
            }*/
        }
    }







    private val _notifications: MutableStateFlow<PagingData<NotificationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val notifications: MutableStateFlow<PagingData<NotificationModel>> get() = _notifications

    private fun getNotifications() {
        val idConnected = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            getNotificationsUseCase.execute(idConnected).collect { res ->
                _notifications.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    init {
        getNotifications()
    }

}