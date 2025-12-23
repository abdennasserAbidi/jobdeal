package com.example.myjob.feature.notification

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.NotificationBody
import com.example.myjob.domain.entities.SendMessageDto
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.domain.usecase.notification.GetNotificationsUseCase
import com.example.myjob.domain.usecase.notification.RemoveNotificationUseCase
import com.example.myjob.domain.usecase.notification.SeenNotificationUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val seenNotificationUseCase: SeenNotificationUseCase,
    private val removeNotificationUseCase: RemoveNotificationUseCase
) : ViewModel() {

    private fun isNotOwn(idSender: Int) = idSender != sharedPreference.getInt("idUser", -1)

    fun getSenderData(item: NotificationModel): Pair<Int, String> {
        if (isNotOwn(item.idSender)) {
            return if (item.idSender == item.idCompany) {
                //company
                Pair(item.idCompany, item.companyName)
            } else {
                //candidate
                Pair(item.idCandidate, item.username)
            }
        }
        return Pair(-1, "")
    }

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

    val seenNotification = MutableStateFlow("")

    fun seenNotification(item: Int) {
        viewModelScope.launch {
            seenNotificationUseCase.execute(item).collect { res ->
                if (res.status == ResourceState.SUCCESS) {
                    seenNotification.update {
                        res.data?.message ?: ""
                    }
                }
            }
        }
    }

    private val _localItemRemoves = MutableStateFlow(-1)

    private val _notifications: MutableStateFlow<PagingData<NotificationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val notifications: MutableStateFlow<PagingData<NotificationModel>> get() = _notifications

    private fun getNotifications() {
        val idConnected = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            getNotificationsUseCase.execute(idConnected).collect { res ->
                val data = res.data ?: PagingData.empty()
                data.map {
                    Log.i("jkzghrjkzgrzkgkz", "getNotifications: ${it.read}")
                }
                _notifications.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    val removeNotification = MutableStateFlow("")

    fun removeNotification(item: Int) {
        viewModelScope.launch {
            removeNotificationUseCase.execute(item).collect { res ->
                if (res.status == ResourceState.SUCCESS) {
                    removeNotification.update { res.data?.message ?: "" }

                    _localItemRemoves.update { item }

                    _notifications.combine(_localItemRemoves) { pagingData, updates ->
                        pagingData.filter { notif ->
                            notif.idNotification != updates
                        }
                    }.collect { data ->
                        notifications.update { data }
                    }


                }
            }
        }
    }

    init {
        getNotifications()
    }

}