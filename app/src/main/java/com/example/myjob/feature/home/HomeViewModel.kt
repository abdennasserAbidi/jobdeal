package com.example.myjob.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.domain.entities.User
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
    private val getAllUserUseCase: GetAllUserUseCase
) : ViewModel() {

    val users = MutableStateFlow<List<User>>(emptyList())

    var currentProfile by mutableStateOf(User())

    fun skipCurrentProfile() {
        // Handle skipping the profile (e.g., move to the next profile)
        viewModelScope.launch {
            println("Profile skipped: ${currentProfile.fullName}")
            moveToNextProfile()
        }
    }

    fun matchCurrentProfile() {
        // Handle matching the profile (e.g., send match notification, store match)
        viewModelScope.launch {
            println("Profile matched: ${currentProfile.fullName}")
            // Example: Notify user of the match
        }
    }

    // Load next profile from the list
    private fun moveToNextProfile() {
        // Shift the list to show the next profile
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

    init {
        getAllUser()
    }
}