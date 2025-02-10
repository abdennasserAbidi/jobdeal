package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.listIdToRemove
import com.example.myjob.domain.entities.HOME_ENTITY
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.SaveToFavoriteUseCase
import com.example.myjob.domain.usecase.home.GetAllUserUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
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

    fun matchCurrentProfile() {
        // Handle matching the profile (e.g., send match notification, store match)
        viewModelScope.launch {
            println("Profile matched: ${currentProfile.fullName}")
            // Example: Notify user of the match
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

    private fun getAllUser() {
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
    }

    val updateFavoriteState = MutableStateFlow(false)

    fun saveToFavorites(id: Int) {
        viewModelScope.launch {
            saveToFavoriteUseCase.execute(Pair(id, true)).collect { res ->
                updateFavoriteState.update { res.data?.message == "saved successfully" }
            }
        }
    }

    init {
        lang = sharedPreference.getString("lang", "") ?: ""
        langState.update { lang }
        getAllUser()
    }
}