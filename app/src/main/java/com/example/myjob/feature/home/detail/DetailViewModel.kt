package com.example.myjob.feature.home.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.profile.GetAllEducationUseCase
import com.example.myjob.domain.usecase.profile.GetAllExperienceUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.home.SaveToFavoriteUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllExperienceUseCase: GetAllExperienceUseCase,
    private val getAllEducationUseCase: GetAllEducationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase
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

    fun getUserById(id: Int) {
        getUser(lang, id)
    }

    private fun getUser(lang: String, id: Int) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    user.update { u }
                    GlobalEntries.userCandidate = u
                    sharedPreference.putString("username", u.fullName ?: "")
                    Log.i("userValue", "getUser: $u")
                    Log.i("userValue", "getUser: ${u.showUser(lang)}")
                    showUser.update {
                        u.showUser(lang)
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

    val updateFavoriteState = MutableStateFlow(false)

    fun saveToFavorites(id: Int) {
        /*viewModelScope.launch {
            saveToFavoriteUseCase.execute(Pair(id, true)).collect { res ->
                updateFavoriteState.update { res.data?.message == "saved successfully" }
            }
        }*/
    }
}