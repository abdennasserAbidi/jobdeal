package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.listIdToRemove
import com.example.myjob.domain.entities.HOME_ENTITY
import com.example.myjob.domain.entities.InvitationModel
import com.example.myjob.domain.entities.InvitationParams
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.SaveToFavoriteUseCase
import com.example.myjob.domain.usecase.SendInvitationUseCase
import com.example.myjob.domain.usecase.home.GetAllUserUseCase
import com.example.myjob.domain.usecase.home.SearchCandidateUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val sendInvitationUseCase: SendInvitationUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
    private val searchCandidateUseCase: SearchCandidateUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _words = MutableStateFlow<List<User>>(emptyList())
    val words: StateFlow<List<User>> = _words

    val users = MutableStateFlow<List<User>>(emptyList())
    val listHomeEntity = MutableStateFlow(HOME_ENTITY)

    var currentProfile by mutableStateOf(User())

    var lang = ""
    var langState = MutableStateFlow(lang)

    val resume = MutableStateFlow("")

    fun getResume(user: User) {
        resume.update { user.resumeUser() }
    }

    val invitationParam = MutableStateFlow(InvitationModel())
    fun changePostName(name: String) {
        invitationParam.update {
            it.message = name
            it
        }
    }
    fun changeDescriptions(name: String) {
        invitationParam.update {
            it.description = name
            it
        }
    }

    fun changeTypeContract(name: String) {
        invitationParam.update {
            it.typeContract = name
            it
        }
    }

    fun changeDisponibility(name: String) {
        invitationParam.update {
            it.disponibility = name
            it
        }
    }

    fun changeSalary(name: String) {
        invitationParam.update {
            it.tgm = name
            it
        }
    }

    fun matchCurrentProfile(id: Int) {
        invitationParam.update {
            it.idCompany = sharedPreference.getInt("idUser", -1)
            it.companyName = GlobalEntries.user.companyName ?: ""
            it.idTo = id
            it
        }
        val invitationParams = InvitationParams(
            idConnected = sharedPreference.getInt("idUser", -1),
            invitationModel = invitationParam.value
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
            getAllUserUseCase.execute().collectLatest { res ->
                _user.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    fun skipCurrentProfile(user: User) {
        // Handle skipping the profile (e.g., move to the next profile)
        viewModelScope.launch {
            val s = _user.value.filter {
                it.id != user.id
            }
            _user.update { s }
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

    fun updateQuery(newQuery: String) {
        _query.update { newQuery }
    }
}