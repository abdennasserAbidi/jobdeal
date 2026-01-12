package com.example.myjob.feature.validateprofile

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Work
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.R
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.FileReader
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.home.GetFilesUseCase
import com.example.myjob.domain.usecase.home.UploadCVUseCase
import com.example.myjob.domain.usecase.home.UploadFileUseCase
import com.example.myjob.domain.usecase.verification.GetVerifiedCandidateStatusUseCase
import com.example.myjob.domain.usecase.verification.SendMailVerificationUseCase
import com.example.myjob.domain.usecase.verification.ValidateEmailUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class InterviewValidationViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val sendMailVerificationUseCase: SendMailVerificationUseCase,
    private val getVerifiedCandidateStatusUseCase: GetVerifiedCandidateStatusUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val getFilesUseCase: GetFilesUseCase
) : ViewModel() {

    val verificationSteps = MutableStateFlow<List<StepStatus>>(emptyList())

    val user = MutableStateFlow(User())

    private val _validationStatus = MutableStateFlow(ValidationProfileStatus())
    val validationStatus get(): StateFlow<ValidationProfileStatus> = _validationStatus

    private fun getVerifiedCandidateStatus(list: List<StepStatus>) {
        val idUser = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            getVerifiedCandidateStatusUseCase.execute(idUser).collect { res ->
                res.data?.let {
                    when(it.typeValidation) {
                        "doc" -> updateStepsStatus(list, 0, it.status ?: "")
                        "interview" -> updateStepsStatus(list, 2, it.status ?: "")
                        else -> verificationSteps.update { list }
                    }
                }?:run {
                    verificationSteps.update { list }
                }
            }
        }
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

    fun getTypeDoc(name: String): String {
        val uri: Uri = name.toUri()
        val imageName = uri.lastPathSegment ?: ""
        if (imageName.isNotEmpty() && imageName.contains(".")) {
            return imageName.split(".")[1]
        }
        return ""
    }

    fun fromPathToUri(path: String): Uri {
        return path.toUri()
    }

    fun getNameDocFromLink(imageName: String): String {
        // Convert string to Uri
        val uri: Uri = imageName.toUri()
        return uri.lastPathSegment ?: ""
    }

    fun getTypeDocFromLink(imageName: String): String {
        if (imageName.isNotEmpty() && imageName.contains("/upload/")) {
            val name = imageName.split("/upload/")[1]
            return getTypeDoc(name)
        }
        return ""
    }

    /*fun getVerifiedCandidateStatus(list: List<StepStatus>) {
        val idUser = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            getVerifiedCandidateStatusUseCase.execute(idUser).collectLatest { res ->
                res.data?.let { data ->
                    data.map {
                        when(it.typeValidation) {
                            "doc" -> updateStepsStatus(list, 0, it.status)
                            "interview" -> updateStepsStatus(list, 2, it.status)
                            else -> Log.i("fejhafj", "getVerifiedCandidateStatus: fekalghea")
                        }
                    }
                }?:run {
                    verificationSteps.update { list }
                }
            }
        }
    }*/

    fun changeDocs(docs: List<String>) {
        user.update {
            it.docs = docs
            it
        }
    }

    fun updateSteps(list : List<StepStatus>) {
        verificationSteps.update { list }
    }

    fun updateStepsStatus(list: List<StepStatus>, index: Int, step: String) {

        val stepVerification = when(step) {
            VerificationStatus.PENDING_REVIEW.name -> VerificationStatus.PENDING_REVIEW
            else -> VerificationStatus.NOT_STARTED
        }

        list[index].status = stepVerification
        verificationSteps.update { list }
    }

    fun updateStepsIndex(index: Int) {
        val list = verificationSteps.value.toMutableList()
        list[index].status = VerificationStatus.PENDING_REVIEW
        verificationSteps.update { list }
    }

    val isEmailValid = MutableStateFlow(false)

    fun clearValidity() {
        isEmailValid.update { false }
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

    private val messageEmailed = MutableStateFlow("")
    var message = messageEmailed.asStateFlow()

    fun validate(validation: ValidationProfileStatus) {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            validation.id = idUser
            sendMailVerificationUseCase.execute(validation).collect { res ->
                if (res.status == ResourceState.SUCCESS) {
                    messageEmailed.update { res.data?.message ?: "" }
                } else if (res.status == ResourceState.ERROR) {
                    messageEmailed.update { res.message ?: "" }
                }
            }
        }
    }

    fun initList(context: Context) {
        val list = listOf(
            StepStatus(
                VerificationStep.ID_DOCUMENT,
                VerificationStatus.NOT_STARTED,
                context.resources.getString(R.string.id_document_text),
                context.resources.getString(R.string.desc_document_text),
                Icons.Default.Badge,
                isRequired = false
            ),
            /*StepStatus(
                VerificationStep.WORK_EMAIL,
                VerificationStatus.NOT_STARTED,
                context.resources.getString(R.string.interview_text),
                context.resources.getString(R.string.interview_explanation_text),
                Icons.Default.Work,
                isRequired = false
            ),*/
            StepStatus(
                VerificationStep.VIDEO_INTRO,
                VerificationStatus.NOT_STARTED,
                context.resources.getString(R.string.video_intro_text),
                context.resources.getString(R.string.video_explanation_text),
                Icons.Default.VideoCall,
                isRequired = false
            )
        )

        getVerifiedCandidateStatus(list)
    }

    var uploadMessage = MutableStateFlow("")

    fun uploadDoc(context: Context, fileUri: Uri, index: Int, documents: Documents) {
        val file = FileReader.getFile(context, fileUri) // Helper function to convert URI to File

        val requestBody: RequestBody =
            RequestBody.create("application/*".toMediaTypeOrNull(), file)

        val expectedName = "document${documents.id}"
        val multipartBody: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", expectedName, requestBody)

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

    /*init {
        getFiles()
    }*/

    /*init {

        val list = listOf(
            StepStatus(
                VerificationStep.ID_DOCUMENT,
                VerificationStatus.NOT_STARTED,
                "ID Document",
                "Upload a government-issued ID (optional)",
                Icons.Default.Badge,
                isRequired = false
            ),
            StepStatus(
                VerificationStep.WORK_EMAIL,
                VerificationStatus.NOT_STARTED,
                "Interview",
                "Verify after interviewing",
                Icons.Default.Work,
                isRequired = false
            ),
            StepStatus(
                VerificationStep.VIDEO_INTRO,
                VerificationStatus.NOT_STARTED,
                "Video Introduction",
                "Record a 30-second video introduction",
                Icons.Default.VideoCall,
                isRequired = false
            )
        )

        getVerifiedCandidateStatus(list)
    }*/
}