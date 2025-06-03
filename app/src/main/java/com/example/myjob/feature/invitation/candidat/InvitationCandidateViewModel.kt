package com.example.myjob.feature.invitation.candidat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.usecase.invitation.GetAllInvitationsUseCase
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
) : ViewModel() {

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
}