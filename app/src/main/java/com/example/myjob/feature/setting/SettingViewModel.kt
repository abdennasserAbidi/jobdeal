package com.example.myjob.feature.setting

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.FileReader
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.usecase.UploadCVUseCase
import com.example.myjob.domain.usecase.ValidateAccountUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val sharedPreferences: SharedPreference,
    private val uploadCVUseCase: UploadCVUseCase,
    private val validateAccountUseCase: ValidateAccountUseCase
): ViewModel() {

    val role = MutableStateFlow("")
    val username = MutableStateFlow("AA")
    val userFullName = MutableStateFlow("")
    val allLanguages = MutableStateFlow(listOf("English", "French"))
    val language = MutableStateFlow(sharedPreferences.getString("lang", "English"))

    fun getRole() {
        role.update {
            sharedPreferences.getString("role", "") ?: ""
        }
    }


    init {
        val fullName = sharedPreferences.getString("username", "") ?: ""
        userFullName.update { fullName }
        if (fullName.isNotEmpty()) {
            val s = fullName.trimStart().split(" ")
            val name = "${s[0][0].uppercaseChar()}${s[1][0].uppercaseChar()}"
            username.update { name }
        }
    }

    fun getPDFName(): String {
        val fullName = sharedPreferences.getString("username", "") ?: ""
        return if (fullName.contains(" "))
            "${fullName.replace(" ", "").trim()}Detail.pdf" else ""
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
        Log.i("lktrdgvtd", "validateAccount: ${GlobalEntries.user.email}")
        viewModelScope.launch {
            validateAccountUseCase.execute("abidi.abdennasser@gmail.com").collect { res ->
                when(res.status) {
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

    var uploadMessage = MutableStateFlow("")

    fun uploadCV(context: Context, fileUri: Uri, pdfName: String) {
        val file = FileReader.getFile(context, fileUri) // Helper function to convert URI to File

        val requestBody: RequestBody = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
        val expectedName = if (pdfName.contains("(")) pdfName.split(" ")[0] else pdfName
        val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("file", expectedName, requestBody)

        viewModelScope.launch {
            uploadCVUseCase.execute(multipartBody).collect { res ->
                when(res.status) {
                    ResourceState.SUCCESS -> {
                        Log.i("lktrdgvtd", "uploadCV: ${res.data}")
                        uploadMessage.update {
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