package com.example.myjob.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.usecase.GetAllEducationUseCase
import com.example.myjob.domain.usecase.GetAllExperienceUseCase
import com.example.myjob.domain.usecase.SaveToFavoriteUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
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
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase
) : ViewModel() {

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