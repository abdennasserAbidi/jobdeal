package com.example.myjob.feature.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.usecase.profile.SaveCompanyInfoUseCase
import com.example.myjob.domain.usecase.home.ValidateAccountUseCase
import com.example.myjob.domain.usecase.verification.GetVerifiedCandidateStatusUseCase
import com.example.myjob.feature.validateprofile.ValidationProfileStatus
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference,
    private val validateAccountUseCase: ValidateAccountUseCase,
    private val saveCompanyInfoUseCase: SaveCompanyInfoUseCase,
    private val getVerifiedCandidateStatusUseCase: GetVerifiedCandidateStatusUseCase
) : ViewModel() {

    val role = MutableStateFlow("")
    val username = MutableStateFlow("AA")
    val userFullName = MutableStateFlow("")
    val allLanguages = MutableStateFlow(
        if (sharedPreferences.getString(
                "lang",
                "English"
            ) == "English" || sharedPreferences.getString("lang", "English") == "Anglais"
        ) listOf("English", "French")
        else listOf("Anglais", "Français")
    )
    val language = MutableStateFlow(sharedPreferences.getString("lang", "English"))
    val user = MutableStateFlow(GlobalEntries.user)

    fun saveCompanyInfo() {
        viewModelScope.launch {
            saveCompanyInfoUseCase.execute(user.value).collect {
                Log.i("ffjlebfjkefbe", "saveCompanyInfo: ${it.data}")
            }
        }
    }

    fun changeCompanyWebsite(name: String) {
        user.update {
            it.linkWebsite = name
            it
        }
    }

    fun changeCompanyLinkedIn(name: String) {
        user.update {
            it.linkLinkedIn = name
            it
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // COMPANY NAME
    ///////////////////////////////////////////////////////////////////////////
    val isCompanyNameValid = MutableStateFlow(false)

    fun changeCompanyName(name: String) {
        user.update {
            it.companyName = name
            it
        }
    }

    /*fun validateCompanyName(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isCompanyNameValid.update {
                validateEmailUseCase.execute(text) ?: false
            }
            t = validateEmailUseCase.execute(text) ?: false
        }

        return t
    }*/

    fun changeCompanyEmail(name: String) {
        user.update {
            it.email = name
            it
        }
    }

    fun changeCompanyActivitySector(name: String) {
        user.update {
            it.companyActivitySector = name
            it
        }
    }

    fun changeCompanyDescription(name: String) {
        user.update {
            it.companyDescription = name
            it
        }
    }

    fun changeCompanyAddress(name: String) {
        user.update {
            it.companyAddress = name
            it
        }
    }

    fun changeCompanySecondAddress(name: String) {
        user.update {
            it.companySecondAddress = name
            it
        }
    }

    fun changeCompanySecondPhone(name: String) {
        user.update {
            it.secondPhoneCompany = name
            it
        }
    }

    fun changeCompanyPhone(name: String) {
        user.update {
            it.phoneCompany = name
            it
        }
    }

    fun changeCompanyNum(name: String, index: Int) {
        user.update {
            it.listNum?.set(index, name)
            it
        }
    }

    fun addCompanyNum(name: String) {
        user.update {
            if (it.listNum?.contains(name) == false) it.listNum.add(name)
            it
        }
    }

    fun getRole() {
        role.update {
            sharedPreferences.getString("role", "") ?: ""
        }
    }


    val verificationSteps = MutableStateFlow(ValidationProfileStatus())

    private fun getStatusValidation() {
        viewModelScope.launch {
            val idUser = sharedPreferences.getInt("idUser", -1)
            getVerifiedCandidateStatusUseCase.execute(idUser).collect { res ->
                verificationSteps.update {
                    res.data ?: ValidationProfileStatus()
                }
            }
        }
    }

    init {
        getStatusValidation()
        val fullName = sharedPreferences.getString("username", "") ?: ""
        userFullName.update { fullName }
        if (fullName.isNotEmpty() && fullName != " ") {
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

    var validationMessage = MutableStateFlow("")

    fun validateAccount() {
        viewModelScope.launch {
            validateAccountUseCase.execute("abidi.abdennasser@gmail.com").collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        Log.i("lktrdgvtd", "uploadCV: ${res.data}")
                        validationMessage.update {
                            res.data ?: ""
                        }
                    }

                    else -> {
                        Log.i("lktrdgvtd", "error: ${res.message}")
                    }
                }
            }
        }
    }

    fun logout() {
        sharedPreferences.putString("token", "")
    }
}