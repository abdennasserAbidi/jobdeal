package com.example.myjob.feature.splash

import androidx.lifecycle.ViewModel
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference
) : ViewModel() {

    fun getToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }

    fun isOnBoardingFinished(): Boolean {
        return sharedPreferences.getBoolean("isFinished", false) ?: false
    }
}