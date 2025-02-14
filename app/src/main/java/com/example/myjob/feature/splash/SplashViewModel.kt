package com.example.myjob.feature.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.GetUserUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    val role = MutableStateFlow("")
    val user = MutableStateFlow(User())
    init {
        role.update {
            sharedPreferences.getString("role", "") ?: ""
        }
        GlobalEntries.role = role.value
        GlobalEntries.user.companyName = sharedPreferences.getString("companyName", "") ?: ""
        val id = sharedPreferences.getInt("idUser", -1)
        if (id != -1) getUser(id)
    }

    private fun getUser(id: Int) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect { res ->
                when(res.status) {
                    ResourceState.SUCCESS -> {
                        user.update { res.data?: User() }
                        GlobalEntries.user = user.value
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
        return sharedPreferences.getBoolean("isFinished", false) ?: false
    }
}