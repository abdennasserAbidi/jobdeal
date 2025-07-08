package com.example.myjob.feature.invitation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.AnnouncementParams
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.usecase.announcement.GetAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.SaveAnnouncementUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.invitation.FinishProcessUseCase
import com.example.myjob.domain.usecase.invitation.GetCompanyInvitationUseCase
import com.example.myjob.domain.usecase.invitation.GetDetailInvitationUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationDetailViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getDetailInvitationUseCase: GetDetailInvitationUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    var isSuccess = MutableStateFlow(false)

    private val _invitations = MutableStateFlow(value = InvitationModel())
    val invitations: MutableStateFlow<InvitationModel> get() = _invitations


    private val _candidate = MutableStateFlow(value = User())
    val candidate: MutableStateFlow<User> get() = _candidate

    fun getInvitationDetail(idInvitation: Int) {
        val idUser = sharedPreference.getInt("idUser", 0)
        val pair = Pair(idUser, idInvitation)
        viewModelScope.launch {
            getDetailInvitationUseCase.execute(pair)
                .collectLatest { res ->
                    if (res.status == ResourceState.SUCCESS) isSuccess.update { true }
                    _invitations.update {
                        res.data ?: InvitationModel()
                    }
                }
        }
        if (isSuccess.value) {
            viewModelScope.launch {
                val id = _invitations.value.idTo
                getUserUseCase.execute(id).collect {
                    it.data?.let { u ->
                        _candidate.update {
                            u
                        }
                    }
                }
            }
        }
    }
}