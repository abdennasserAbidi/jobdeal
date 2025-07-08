package com.example.myjob.feature.invitation.candidat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.invitation.AcceptRejectInvitationUseCase
import com.example.myjob.domain.usecase.invitation.GetAllInvitationsUseCase
import com.example.myjob.domain.usecase.notification.SendNotificationsUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationCandidateViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllInvitationsUseCase: GetAllInvitationsUseCase,
    private val acceptRejectInvitationUseCase: AcceptRejectInvitationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sendNotificationsUseCase: SendNotificationsUseCase
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // ACCEPT AND REJECT INVITATION
    ///////////////////////////////////////////////////////////////////////////
    private val _statusInvitations: MutableStateFlow<String> =
        MutableStateFlow(InvitationStatus.ON_HOLD.name)
    val statusInvitations: MutableStateFlow<String> get() = _statusInvitations

    fun acceptRejectInvitation(invitation: InvitationModel) {
        val invitationParams = InvitationParams()
        invitationParams.idConnected = invitation.idCompany
        invitationParams.invitationModel = invitation
        viewModelScope.launch {
            acceptRejectInvitationUseCase.execute(invitationParams).collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        _statusInvitations.update {
                            res.data?.message ?: InvitationStatus.ON_HOLD.name
                        }


                    }

                    else -> {}
                }
            }
        }
    }

    private val _invitations: MutableStateFlow<PagingData<InvitationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val invitations: MutableStateFlow<PagingData<InvitationModel>> get() = _invitations
    private val allInvitations: MutableStateFlow<List<InvitationModel>> =
        MutableStateFlow(emptyList())

    private fun getInvitations() {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            getAllInvitationsUseCase.execute(idUser).collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        val json = sharedPreference.getString("jsonInvitations", "")
                        if (!json.isNullOrEmpty()) {
                            val objectList =
                                Gson().fromJson(json, Array<InvitationModel>::class.java).asList()
                            allInvitations.update {
                                objectList
                            }
                        }
                        _invitations.update {
                            res.data ?: PagingData.empty()
                        }
                    }

                    else -> {

                    }
                }

            }
        }
    }

    init {
        getInvitations()
    }

    ///////////////////////////////////////////////////////////////////////////
    // NOTIFICATION
    ///////////////////////////////////////////////////////////////////////////

    val invitationSent = MutableStateFlow(false)
    val idUserTo = MutableStateFlow(-1)
    val fcmToken = MutableStateFlow("")
    fun clearToken() {
        fcmToken.update { "" }
    }

    fun getUserToken(id: Int? = sharedPreference.getInt("idUser", -1)) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    fcmToken.update { u.fcmToken ?: "" }
                }
            }
        }
    }

    fun sendNotification(title: String, message: String) {
        viewModelScope.launch {
            val notificationMessage = NotificationMessage(
                recipientToken = fcmToken.value,
                title = title,
                body = message,
                data = mapOf("idUser" to "85")
            )
            sendNotificationsUseCase.execute(notificationMessage).collect {

            }
        }
    }
}