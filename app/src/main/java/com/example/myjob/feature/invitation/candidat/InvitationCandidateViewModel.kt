package com.example.myjob.feature.invitation.candidat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.example.myjob.R
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.InvitationChoices
import com.example.myjob.domain.entities.InvitationFilter
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.invitation.AcceptRejectInvitationUseCase
import com.example.myjob.domain.usecase.invitation.GetAllInvitationsUseCase
import com.example.myjob.domain.usecase.invitation.GetFilteredInvitationsUseCase
import com.example.myjob.domain.usecase.invitation.GetOtherInvitationsUseCase
import com.example.myjob.domain.usecase.notification.SendNotificationsUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationCandidateViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllInvitationsUseCase: GetAllInvitationsUseCase,
    private val getOtherInvitationsUseCase: GetOtherInvitationsUseCase,
    private val getFilteredInvitationsUseCase: GetFilteredInvitationsUseCase,
    private val acceptRejectInvitationUseCase: AcceptRejectInvitationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sendNotificationsUseCase: SendNotificationsUseCase
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // FILTER INVITATION
    ///////////////////////////////////////////////////////////////////////////
    val filter = MutableStateFlow(InvitationFilter())
    val listFilterSv = MutableStateFlow(emptyList<String>())

    private val _invitationsFiltered: MutableStateFlow<PagingData<InvitationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val invitationsFiltered: MutableStateFlow<PagingData<InvitationModel>> get() = _invitationsFiltered

    private val allInvitationsFiltered: MutableStateFlow<List<InvitationModel>> =
        MutableStateFlow(emptyList())

    fun changeTypeStatus(type: String) {
        filter.update {
            it.type = type
            it
        }
    }

    private fun addTypeContract(type: String) {
        val list = filter.value.listTypeContract?.toMutableList() ?: mutableListOf()
        if (!list.contains(type)) list.add(type)

        val listFilter = listFilterSv.value.toMutableList()
        if (!listFilter.contains(type)) listFilter.add(type)

        listFilterSv.update {
            listFilter
        }

        filter.update {
            it.listTypeContract = list
            it
        }
    }

    private fun removeTypeContract(type: String) {
        val list = filter.value.listTypeContract?.toMutableList() ?: mutableListOf()
        if (list.contains(type)) list.remove(type)

        val listFilter = listFilterSv.value.toMutableList()
        if (listFilter.contains(type)) listFilter.remove(type)

        listFilterSv.update {
            listFilter
        }

        filter.update {
            it.listTypeContract = list
            it
        }
    }

    fun changeTypeContracts(type: List<String>) {
        filter.update {
            it.listTypeContract = type
            it
        }
    }


    val invitationChoices = MutableStateFlow(emptyList<InvitationChoices>())
    val selectionChoices = MutableStateFlow(emptyList<Boolean>())

    fun changeChoice(index: Int, selected: Boolean, title: String) {
        val selectionChoice = selectionChoices.value.toMutableList()
        selectionChoice[index] = selected
        selectionChoices.update {
            selectionChoice
        }

        invitationChoices.update {
            it[index].isSelected = selected
            it
        }

        if (selected) addTypeContract(title)
        else removeTypeContract(title)
    }

    init {

        val list = listOf(
            InvitationChoices(title = R.string.type1_text, isSelected = false),
            InvitationChoices(title = R.string.type2_text, isSelected = false),
            InvitationChoices(title = R.string.intern_user_text, isSelected = false),
            InvitationChoices(title = R.string.event_user_text, isSelected = false),
            InvitationChoices(title = R.string.trainer_user_text, isSelected = false)
        )

        invitationChoices.update { list }

        val selectedCat = MutableList(list.size) {
            false
        }

        selectionChoices.update { selectedCat }

        getInvitations()
        getOtherInvitations()
    }

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

    fun getInvitations() {
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

                        //sharedPreference.putInt("countInvitationBadge", count)

                        _invitations.update {
                            res.data ?: PagingData.empty()
                        }
                    }
                    //
                    else -> {

                    }
                }
            }
        }
    }

    private fun getFilteredInvitation(invitationFilter: InvitationFilter) {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            invitationFilter.idUser = idUser

            getFilteredInvitationsUseCase.execute(invitationFilter).collectLatest { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        val json = sharedPreference.getString("jsonInvitationsFiltered", "")
                        if (!json.isNullOrEmpty()) {
                            val objectList =
                                Gson().fromJson(json, Array<InvitationModel>::class.java).asList()
                            allInvitationsFiltered.update {
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

    fun validateFilter(invitationFilter: InvitationFilter) {
        Log.i("glrzhdhte", "type: ${invitationFilter.type}")
        Log.i("glrzhdhte", "listTypeContract: ${invitationFilter.listTypeContract}")

        if (invitationFilter.type == "All Candidates" && invitationFilter.listTypeContract?.isEmpty() == true) {
            getInvitations()
        } else {
            Log.i("glrzhdhte", "fefafae: ${invitationFilter.listTypeContract}")
            getFilteredInvitation(invitationFilter)
        }
    }

    private val _otherInvitations: MutableStateFlow<PagingData<InvitationModel>> =
        MutableStateFlow(value = PagingData.empty())

    private val _otherInvitation: MutableStateFlow<PagingData<InvitationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val otherInvitations: MutableStateFlow<PagingData<InvitationModel>> get() = _otherInvitations
    private val allOtherInvitations: MutableStateFlow<List<InvitationModel>> =
        MutableStateFlow(emptyList())

    fun searchListInvitation(text: String) {
        if (text.isEmpty() || text == "ALL"|| text == "Tous") {
            getOtherInvitations()
        } else {
            val s = _otherInvitation.value
            _otherInvitations.update {
                val list = s.filter { invitationModel -> invitationModel.nameContract == text}
                list
            }
        }
    }

    private fun getOtherInvitations(name: String = "") {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            getOtherInvitationsUseCase.execute(idUser).collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        val json = sharedPreference.getString("jsonOtherInvitations", "")
                        if (!json.isNullOrEmpty()) {
                            val objectList =
                                Gson().fromJson(json, Array<InvitationModel>::class.java).asList()
                            allOtherInvitations.update {
                                objectList
                            }
                        }
                        _otherInvitations.update {
                            res.data ?: PagingData.empty()
                        }

                        _otherInvitation.update {
                            res.data ?: PagingData.empty()
                        }
                    }

                    else -> {

                    }
                }

            }
        }
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