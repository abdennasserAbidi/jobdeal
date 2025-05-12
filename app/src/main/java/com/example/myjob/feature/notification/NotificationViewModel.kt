package com.example.myjob.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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