package com.example.myjob.feature.validateprofile

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.FileReader
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.home.GetFilesUseCase
import com.example.myjob.domain.usecase.home.UploadFileUseCase
import com.example.myjob.domain.usecase.verification.GetVerifiedCandidateStatusUseCase
import com.example.myjob.domain.usecase.verification.SendMailVerificationUseCase
import com.example.myjob.domain.usecase.verification.ValidateEmailUseCase
import com.example.myjob.domain.usecase.verification.VerificationCompanyUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ValidateProfileViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val sendMailVerificationUseCase: SendMailVerificationUseCase,
    private val verificationCompanyUseCase: VerificationCompanyUseCase,
    private val getVerifiedCandidateStatusUseCase: GetVerifiedCandidateStatusUseCase,
    private val getFilesUseCase: GetFilesUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    val user = MutableStateFlow(User())

    fun imageInfo(context: Context, imageUri: Uri?): String {
        return imageUri?.let { uri ->
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && it.moveToFirst()) {
                    it.getString(nameIndex)
                } else {
                    null
                }
            }
        } ?: ""
    }

    fun getTypeDoc(imageName: String): String {
        if (imageName.isNotEmpty() && imageName.contains(".")) {
            return imageName.split(".")[1]
        }
        return ""
    }

    fun getNameDocFromLink(imageName: String): String {
        if (imageName.isNotEmpty() && imageName.contains("/upload/")) {
            return imageName.split("/upload/")[1]
        }
        return ""
    }

    fun getTypeDocFromLink(imageName: String): String {
        if (imageName.isNotEmpty() && imageName.contains("/upload/")) {
            val name = imageName.split("/upload/")[1]
            return getTypeDoc(name)
        }
        return ""
    }

    val filesList = MutableStateFlow(emptyList<String>())

    fun getFiles() {
        val idUser = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            getFilesUseCase.execute(idUser).collect { res ->
                filesList.update {
                    res.data ?: emptyList()
                }
            }
        }
    }

    val isEmailValid = MutableStateFlow(false)

    fun changeUserEmail(email: String) {
        user.update {
            it.email = email
            it
        }
    }

    fun validateEmail(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isEmailValid.update {
                validateEmailUseCase.execute(text) ?: false
            }
            t = validateEmailUseCase.execute(text) ?: false
        }

        return t
    }

    fun validateCompany() {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            verificationCompanyUseCase.execute(idUser).collect {

            }
        }
    }

    private val messageEmailed = MutableStateFlow("")
    var message = messageEmailed.asStateFlow()


    var uploadMessage = MutableStateFlow("")

    fun uploadDoc(context: Context, fileUri: Uri, index: Int, documents: Documents) {
        val file = FileReader.getFile(context, fileUri) // Helper function to convert URI to File

        val requestBody: RequestBody =
            RequestBody.create("application/*".toMediaTypeOrNull(), file)

        val expectedName = "document${documents.id}"
        val multipartBody: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", expectedName, requestBody)

        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            val params = Pair(idUser, multipartBody)

            uploadFileUseCase.execute(params).collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        uploadMessage.update {
                            res.data ?: ""
                        }
                    }

                    else -> {
                        uploadMessage.update { "" }
                    }
                }
            }
        }
    }

    fun validate(validation: ValidationProfileStatus) {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            validation.id = idUser
            sendMailVerificationUseCase.execute(validation).collect { res ->
                messageEmailed.update {
                    res.data?.message ?: ""
                }
            }
        }
    }
    val verificationSteps = MutableStateFlow(ValidationProfileStatus())

    private fun getStatusValidation() {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            getVerifiedCandidateStatusUseCase.execute(idUser).collect { res ->
                verificationSteps.update {
                    res.data ?: ValidationProfileStatus()
                }
            }
        }
    }

    init {
        getStatusValidation()
    }

}