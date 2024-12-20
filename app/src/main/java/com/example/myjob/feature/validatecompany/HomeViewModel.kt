package com.example.myjob.feature.validatecompany

import androidx.lifecycle.ViewModel
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference
) : ViewModel() {


    fun logout() {
        sharedPreferences.putString("token", "")
    }
}