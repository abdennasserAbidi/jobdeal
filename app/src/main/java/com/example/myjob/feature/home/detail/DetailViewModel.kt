package com.example.myjob.feature.home.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.invitation.FinishProcessUseCase
import com.example.myjob.domain.usecase.profile.GetAllEducationUseCase
import com.example.myjob.domain.usecase.profile.GetAllExperienceUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllExperienceUseCase: GetAllExperienceUseCase,
    private val getAllEducationUseCase: GetAllEducationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val finishProcessUseCase: FinishProcessUseCase
) : ViewModel() {

    var lang = ""
    init {
        lang = sharedPreference.getString("lang", "") ?: ""
        //getUser(lang, sharedPreference.getInt("idUser", 0))
    }

    val userFullName = MutableStateFlow("")
    val username = MutableStateFlow("AA")

    fun getUserNameAbbreviation(userName: String) {
        userFullName.update { userName }
        if (userName.isNotEmpty()) {
            val s = userName.trimStart().split(" ")
            val name = "${s[0][0].uppercaseChar()}${s[1][0].uppercaseChar()}"
            username.update { name }
        }
    }

    val showUser = MutableStateFlow(mapOf<String, String>())
    val user = MutableStateFlow(User(id = 0))
    val experienceYears = MutableStateFlow(0)

    fun getUserById(id: Int) {
        getUser(lang, id)
    }

    private fun getCurrent(id: Int) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    Log.i("klhjehrzjgezjgz", "getCurrent: $u")
                    GlobalEntries.user = u
                }
            }
        }
    }

    private fun getUser(lang: String, id: Int) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    user.update { u }
                    GlobalEntries.userCandidate = u
                    sharedPreference.putString("username", u.fullName ?: "")
                    showUser.update {
                        u.showUser(lang)
                    }

                    val experiences = u.experience ?: mutableListOf()
                    if (experiences.isNotEmpty()) {
                        val firstExp = experiences[0]
                        val start = firstExp.dateStart ?: ""

                        if (start.isNotEmpty()) {
                            val startArray = start.split(", ")
                            val yearStart = startArray[2].toInt()

                            val calendar: Calendar = Calendar.getInstance()
                            val currentYear: Int = calendar.get(Calendar.YEAR)

                            val diffYear = currentYear - yearStart
                            experienceYears.update { diffYear }
                        }
                    }
                }
            }
        }
    }

    private val _experience: MutableStateFlow<PagingData<Experience>> =
        MutableStateFlow(value = PagingData.empty())
    val experience: MutableStateFlow<PagingData<Experience>> get() = _experience

    fun getAllExperience(id: Int) {
        viewModelScope.launch {
            getAllExperienceUseCase.execute(id)
                .collectLatest { res ->
                    _experience.update {
                        res.data ?: PagingData.empty()
                    }

                }
        }
    }

    private val _education: MutableStateFlow<PagingData<Educations>> =
        MutableStateFlow(value = PagingData.empty())
    val education: MutableStateFlow<PagingData<Educations>> get() = _education

    fun getAllEducations(idUser: Int) {
        viewModelScope.launch {
            getAllEducationUseCase.execute(idUser)
                .collectLatest { res ->
                    _education.update {
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

    init {
        getCurrent(sharedPreference.getInt("idUser", 0))
    }
}