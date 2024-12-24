package com.example.myjob.feature.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.common.GlobalEntries
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference
): ViewModel() {

    val username = MutableStateFlow("AA")
    val userFullName = MutableStateFlow("")
    val allLanguages = MutableStateFlow(listOf("English", "French"))
    val language = MutableStateFlow(sharedPreferences.getString("lang", "English"))

    init {
        val fullName = sharedPreferences.getString("username", "") ?: ""
        userFullName.update { fullName }
        if (fullName.isNotEmpty()) {
            val s = fullName.trimStart().split(" ")
            val name = "${s[0][0].uppercaseChar()}${s[1][0].uppercaseChar()}"
            username.update { name }
        }
    }

    fun changeLanguage(lang: String) {
        if (lang == "French") {
            val list = listOf("Anglais", "Français")
            allLanguages.update { list }
            language.update { "Français" }
            sharedPreferences.putString("lang", "Français")
            GlobalEntries.language = "Français"
            viewModelScope.launch {
                GlobalEntries.languageShared.emit("Français")
            }
        } else {
            val list = listOf("English", "French")
            allLanguages.update { list }
            language.update { "English" }
            sharedPreferences.putString("lang", "English")
            GlobalEntries.language = "English"
            viewModelScope.launch {
                GlobalEntries.languageShared.emit("English")
            }
        }

        GlobalEntries.langState.update {
            sharedPreferences.getString("lang", "English") ?: ""
        }
    }

    fun logout() {
        sharedPreferences.putString("token", "")
    }
}