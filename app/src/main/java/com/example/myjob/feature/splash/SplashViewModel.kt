package com.example.myjob.feature.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myjob.common.GlobalEntries
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference
) : ViewModel() {

    val role = MutableStateFlow("")
    init {
        role.update {
            sharedPreferences.getString("role", "") ?: ""
        }
        GlobalEntries.role = role.value
        GlobalEntries.user.companyName = sharedPreferences.getString("companyName", "") ?: ""

    }

    fun getToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }

    fun isOnBoardingFinished(): Boolean {
        return sharedPreferences.getBoolean("isFinished", false) ?: false
    }
}