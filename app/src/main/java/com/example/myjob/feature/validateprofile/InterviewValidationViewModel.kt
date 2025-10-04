package com.example.myjob.feature.validateprofile

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Work
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.subscription.ForgotPasswordUseCase
import com.example.myjob.domain.usecase.subscription.ResetPasswordUseCase
import com.example.myjob.domain.usecase.verification.GetVerifiedCandidateStatusUseCase
import com.example.myjob.domain.usecase.verification.SendMailVerificationUseCase
import com.example.myjob.domain.usecase.verification.ValidateEmailUseCase
import com.example.myjob.domain.usecase.verification.ValidatePasswordUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewValidationViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val sendMailVerificationUseCase: SendMailVerificationUseCase,
    private val getVerifiedCandidateStatusUseCase: GetVerifiedCandidateStatusUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {

    val verificationSteps = MutableStateFlow<List<StepStatus>>(emptyList())

    val user = MutableStateFlow(User())

    private val _validationStatus = MutableStateFlow(ValidationProfileStatus())
    val validationStatus get(): StateFlow<ValidationProfileStatus> = _validationStatus

    fun getVerifiedCandidateStatus(list: List<StepStatus>) {
        val idUser = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            getVerifiedCandidateStatusUseCase.execute(idUser).collect { res ->
                res.data?.let {
                    when(it.typeValidation) {
                        "doc" -> updateStepsStatus(list, 0, it.status ?: "")
                        "interview" -> updateStepsStatus(list, 2, it.status ?: "")
                        else -> Log.i("fejhafj", "getVerifiedCandidateStatus: fekalghea")
                    }
                }?:run {
                    verificationSteps.update { list }
                }
            }
        }
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
                messageEmailed.update {
                    res.data?.message ?: ""
                }
            }
        }
    }

    init {
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
                VerificationStep.LINKEDIN_VERIFICATION,
                VerificationStatus.NOT_STARTED,
                "LinkedIn Profile",
                "Connect your LinkedIn for professional verification",
                Icons.Default.Link,
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
    }
}