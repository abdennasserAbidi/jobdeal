package com.example.myjob.feature.invitation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.domain.entities.InvitationModel
import com.example.myjob.domain.usecase.invitation.GetCompanyInvitationUseCase
import com.example.myjob.local.database.SharedPreference
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

    init {
        val id = sharedPreference.getInt("idUser", 0)
        getCompanyInvitations(id)
    }

}