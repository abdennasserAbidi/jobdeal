package com.example.myjob.feature.invitation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.AnnouncementParams
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.usecase.announcement.GetAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.SaveAnnouncementUseCase
import com.example.myjob.domain.usecase.invitation.GetAllInvitationsUseCase
import com.example.myjob.domain.usecase.invitation.GetCompanyInvitationUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
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
    }

}

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllInvitationsUseCase: GetAllInvitationsUseCase
) : ViewModel() {

    private val _invitations: MutableStateFlow<PagingData<InvitationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val invitations: MutableStateFlow<PagingData<InvitationModel>> get() = _invitations
    private val allInvitations: MutableStateFlow<List<InvitationModel>> =
        MutableStateFlow(emptyList())

    init {
        getInvitations()
    }
    private fun getInvitations() {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            getAllInvitationsUseCase.execute(idUser).collect { res ->
                when(res.status) {
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
}