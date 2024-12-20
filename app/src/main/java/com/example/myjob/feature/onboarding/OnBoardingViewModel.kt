package com.example.myjob.feature.onboarding

import androidx.lifecycle.ViewModel
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference
) : ViewModel() {

    fun finishOnBoarding() {
        sharedPreferences.putBoolean("isFinished", true)
    }

    fun getToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }
}