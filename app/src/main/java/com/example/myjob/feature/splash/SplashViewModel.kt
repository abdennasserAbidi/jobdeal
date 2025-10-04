package com.example.myjob.feature.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.verification.GetVerifiedCompanyUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference,
    private val getUserUseCase: GetUserUseCase,
    private val getVerifiedCompanyUseCase: GetVerifiedCompanyUseCase
) : ViewModel() {

    val role = MutableStateFlow("")
    val user = MutableStateFlow(User())

    val listCompanies = MutableStateFlow(emptyList<String>())

    init {
        role.update {
            sharedPreferences.getString("role", "") ?: ""
        }
        GlobalEntries.role = role.value
        GlobalEntries.user.fullName = sharedPreferences.getString("username", "") ?: ""
        GlobalEntries.user.companyName = sharedPreferences.getString("companyName", "") ?: ""
        val id = sharedPreferences.getInt("idUser", -1)
        if (id != -1) getUser(id)
        getCompaniesValidated()
    }

    private fun getUser(id: Int) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        user.update { res.data ?: User() }
                        GlobalEntries.user = user.value
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getCompaniesValidated() {
        viewModelScope.launch {
            getVerifiedCompanyUseCase.execute().collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        val list = res.data ?: emptyList()
                        val l = list.toMutableList()
                        val language = sharedPreferences.getString("lang", "English")
                        if (language == "English" || language == "Anglais") l.add("Other")
                        else l.add("Autres")
                        listCompanies.update { l }
                    }

                    else -> {}
                }
            }
        }
    }

    fun getToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }

    fun isOnBoardingFinished(): Boolean {
        return sharedPreferences.getBoolean("isFinished", false)
    }
}