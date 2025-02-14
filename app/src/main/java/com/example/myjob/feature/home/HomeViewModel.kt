package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries.listIdToRemove
import com.example.myjob.domain.entities.HOME_ENTITY
import com.example.myjob.domain.entities.InvitationModel
import com.example.myjob.domain.entities.InvitationParams
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.SaveToFavoriteUseCase
import com.example.myjob.domain.usecase.SendInvitationUseCase
import com.example.myjob.domain.usecase.home.GetAllUserUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val sendInvitationUseCase: SendInvitationUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase
) : ViewModel() {

    val users = MutableStateFlow<List<User>>(emptyList())
    val listHomeEntity = MutableStateFlow(HOME_ENTITY)

    var currentProfile by mutableStateOf(User())

    var lang = ""
    var langState = MutableStateFlow(lang)

    val resume = MutableStateFlow("")

    fun getResume(user: User) {
        resume.update { user.resumeUser() }
    }

    fun skipCurrentProfile(list: List<User>) {
        // Handle skipping the profile (e.g., move to the next profile)
        viewModelScope.launch {
            println("Profile skipped: ${currentProfile.fullName}")
            moveToNextProfile(list)
        }
    }

    fun matchCurrentProfile(id: Int) {
        val invitationModel = InvitationModel(
            idTo = id,
            message = "",
            typeContract = "CDI"
        )
        val invitationParams = InvitationParams(
            idConnected = sharedPreference.getInt("idUser", -1),
            invitationModel = invitationModel
        )
        viewModelScope.launch {
            sendInvitationUseCase.execute(invitationParams).collect { res ->
                when(res.status) {
                    ResourceState.SUCCESS -> {
                        Log.i("responseDataMessage", "matchCurrentProfile: ${res.data?.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    val qs = MutableStateFlow(emptyList<Int>())

    fun removeFromGlobal(id: Int) {
        listIdToRemove.add(id)
        qs.update {
            listIdToRemove
        }
    }

    val filterdUser = MutableStateFlow(emptyList<User>())

    fun filtering(list: List<User>) {
        val s = list.filter { user ->
            !listIdToRemove.contains(user.id)
        }

        filterdUser.update { s }
    }

    // Load next profile from the list
    private fun moveToNextProfile(list: List<User>) {
        // Shift the list to show the next profile


        filtering(list)
        val s = users.value.toMutableList()
        users.update {
            s.removeLast()
            s
        }
        currentProfile = users.value.firstOrNull() ?: User()
    }


    private val _user: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(value = PagingData.empty())
    val user: MutableStateFlow<PagingData<User>> get() = _user

    fun getPDFName(): String {
        val fullName = sharedPreference.getString("username", "") ?: ""
        return if (fullName.contains(" "))
            "${fullName.replace(" ", "").trim()}Detail.pdf" else ""
    }


    val currentPage = MutableStateFlow(1)

    fun updateCurrentPage() {
        var page = currentPage.value
        currentPage.update {
            page ++
            page
        }
    }

    private fun getAllUser(currentPage: Int) {
        viewModelScope.launch {
            getAllUserUseCase.execute(currentPage).collectLatest { res ->

                users.update {
                    res.data ?: emptyList()
                }

                /*_user.update {
                    res.data ?: PagingData.empty()
                }*/
            }
        }
    }

    /*private fun getAllUser() {
        viewModelScope.launch {
            getAllUserUseCase.execute().collectLatest { res ->

                val json = sharedPreference.getString("jsonUser", "") ?: ""
                if (json.isNotEmpty()) {
                    val objectList = Gson().fromJson(json, Array<User>::class.java).asList()

                    users.update {
                        objectList
                    }
                }

                _user.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }*/

    val updateFavoriteState = MutableStateFlow(false)

    fun saveToFavorites(idUserConnected: Int, candidateId: Int) {
        viewModelScope.launch {
            saveToFavoriteUseCase.execute(Pair(idUserConnected, candidateId)).collect { res ->
                updateFavoriteState.update { res.data?.message == "saved successfully" }
            }
        }
    }

    init {
        lang = sharedPreference.getString("lang", "") ?: ""
        langState.update { lang }
        getAllUser(1)
    }
}