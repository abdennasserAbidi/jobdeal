package com.example.myjob.feature.invitation.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.AnnouncementParams
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.usecase.announcement.GetAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.SaveAnnouncementUseCase
import com.example.myjob.domain.usecase.invitation.FinishProcessUseCase
import com.example.myjob.domain.usecase.invitation.GetCompanyInvitationUseCase
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
class InvitationViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getCompanyInvitationUseCase: GetCompanyInvitationUseCase,
    private val getAnnouncementUseCase: GetAnnouncementUseCase,
    private val saveAnnouncementUseCase: SaveAnnouncementUseCase,
    private val finishProcessUseCase: FinishProcessUseCase
) : ViewModel() {

    val choiceList = MutableStateFlow(listOf("Mes Invitations", "Mes Annonces"))

    private val _invitations: MutableStateFlow<PagingData<InvitationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val invitations: MutableStateFlow<PagingData<InvitationModel>> get() = _invitations
    private fun getCompanyInvitations(idUser: Int) {

        viewModelScope.launch {
            getCompanyInvitationUseCase.execute(idUser)
                .collectLatest { res ->
                    _invitations.update {
                        res.data ?: PagingData.empty()
                    }

                }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // FINISH PROCESS
    ///////////////////////////////////////////////////////////////////////////
    private val _invitation: MutableStateFlow<InvitationParams> =
        MutableStateFlow(InvitationParams())
    val invitation: MutableStateFlow<InvitationParams> get() = _invitation
    fun finishProcess(invitationModel: InvitationModel) {

        val id = sharedPreference.getInt("idUser", 0)
        val invitationParams = InvitationParams(
            idConnected = id,
            invitationModel = invitationModel
        )

        viewModelScope.launch {
            finishProcessUseCase.execute(invitationParams)
                .collectLatest { res ->
                    _invitation.update {
                        res.data ?: InvitationParams()
                    }

                }
        }
    }

    private val _announcement: MutableStateFlow<PagingData<AnnouncementModel>> =
        MutableStateFlow(value = PagingData.empty())
    val announcement: MutableStateFlow<PagingData<AnnouncementModel>> get() = _announcement
    private fun getCompanyAnnouncement(idUser: Int) {
        viewModelScope.launch {
            getAnnouncementUseCase.execute(idUser)
                .collectLatest { res ->
                    _announcement.update {
                        res.data ?: PagingData.empty()
                    }

                }
        }
    }

    val announcementModel = MutableStateFlow(AnnouncementModel())
    fun changePostName(name: String) {
        announcementModel.update {
            it.title = name
            it
        }
    }

    fun changeDescriptions(name: String) {
        announcementModel.update {
            it.description = name
            it
        }
    }

    fun saveCompanyAnnouncement() {
        val id = sharedPreference.getInt("idUser", 0)
        val announcementParams = AnnouncementParams(
            idUserConnected = id,
            announcementModel = announcementModel.value
        )
        viewModelScope.launch {
            saveAnnouncementUseCase.execute(announcementParams)
                .collectLatest { res ->
                    if (res.status == ResourceState.SUCCESS) {
                        getCompanyAnnouncement(id)
                    }
                }
        }
    }

    init {
        val id = sharedPreference.getInt("idUser", 0)
        getCompanyInvitations(id)
        getCompanyAnnouncement(id)

        // Collect messages from /topic/greetings
        /*CoroutineScope(Dispatchers.Main).launch {
            GlobalEntries.socket?.topicFlow("/topic/invitations")?.collect { message ->
                println("Received message in view models: $message")

                val pagedInvitation = Gson().fromJson(message, PagedInvitation::class.java)
                println("Received message in view models: ${pagedInvitation.content}")
            }
        }*/
    }

}