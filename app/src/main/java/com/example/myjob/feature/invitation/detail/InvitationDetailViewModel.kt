package com.example.myjob.feature.invitation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries.idInvitation
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.domain.entities.invitation.InvitationUser
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.invitation.AcceptRejectInvitationUseCase
import com.example.myjob.domain.usecase.invitation.DeleteInvitationUseCase
import com.example.myjob.domain.usecase.invitation.GetDetailInvitationUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationDetailViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getDetailInvitationUseCase: GetDetailInvitationUseCase,
    private val deleteInvitationUseCase: DeleteInvitationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val acceptRejectInvitationUseCase: AcceptRejectInvitationUseCase
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

    private val _invitations = MutableStateFlow(value = InvitationUser())
    val invitations: StateFlow<InvitationUser> get() = _invitations

    //candidate
    val language = MutableStateFlow(value = "Français")
    private val _candidate = MutableStateFlow(value = User())
    val candidate: MutableStateFlow<User> get() = _candidate

    fun getInvitationDetail(idInvitation: Int) {
        val idUser = sharedPreference.getInt("idUser", 0)
        val pair = Pair(idUser, idInvitation)
        viewModelScope.launch {
            getDetailInvitationUseCase.execute(pair)
                .collect { res ->
                    if (res.status == ResourceState.SUCCESS) {

                        val i = res.data ?: InvitationUser()
                        val withStatus = i.invitationModel.copy(
                            status = i.invitationModel.status ?: InvitationStatus.ON_HOLD.name
                        )

                        i.invitationModel = withStatus

                        _invitations.update {
                            i
                        }

                        _candidate.update {
                            res.data?.userCandidate ?: User()
                        }

                    } else {
                        Log.d("VM_INSTANCE_DEBUG", "Error fetching data: ${res.message}")
                    }
                }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DELETE INVITATION
    ///////////////////////////////////////////////////////////////////////////
    private val _deletedStatus = MutableStateFlow(value = "")
    val deletedStatus: MutableStateFlow<String> get() = _deletedStatus
    fun deleteInvitation(idInvitation: Int) {
        val idUser = sharedPreference.getInt("idUser", 0)
        val pair = Pair(idInvitation, idUser)
        viewModelScope.launch {
            deleteInvitationUseCase.execute(pair)
                .collect { res ->
                    if (res.status == ResourceState.SUCCESS) {
                        _deletedStatus.update {
                            res.data?.message ?: ""
                        }
                    } else {
                        Log.d("VM_INSTANCE_DEBUG", "Error fetching data: ${res.message}")
                    }
                }
        }
    }

    init {
        language.update {
            sharedPreference.getString("lang", "Français") ?: "Français"
        }
        getInvitationDetail(idInvitation)
    }
}