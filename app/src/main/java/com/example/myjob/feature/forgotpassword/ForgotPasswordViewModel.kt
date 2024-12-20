package com.example.myjob.feature.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.domain.entities.ResetPasswordParam
import com.example.myjob.domain.usecase.ForgotPasswordUseCase
import com.example.myjob.domain.usecase.ResetPasswordUseCase
import com.example.myjob.domain.usecase.ValidateEmailUseCase
import com.example.myjob.domain.usecase.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    ) : ViewModel() {

    val isEmailValid = MutableStateFlow(false)

    fun clearValidity() {
        isEmailValid.update { false }
    }

    fun validateEmail(text: String) : Boolean {
        var t = false
        viewModelScope.launch {
            isEmailValid.update {
                validateEmailUseCase.execute(text) ?: false
            }
            t = validateEmailUseCase.execute(text) ?: false
        }
        return t
    }

    val isPasswordValid = MutableStateFlow(false)
    val isConfirmPasswordValid = MutableStateFlow(false)

    fun validatePassword(text: String): Boolean  {
        var t = false
        viewModelScope.launch {
            isPasswordValid.update {
                validatePasswordUseCase.execute(text) ?: false
            }
            t = validatePasswordUseCase.execute(text) ?: false
        }
        return t
    }

    fun checkConfirmPassword(password: String, input: String): Boolean {
        isConfirmPasswordValid.update {
            password == input
        }
        return password == input
    }


    private val messageEmailed = MutableStateFlow("")
    var message = messageEmailed.asStateFlow()

    fun clearMessage() {
        messageEmailed.update {
            ""
        }
    }

    fun forgotPasswordUseCase(email: String) {
        viewModelScope.launch {
            forgotPasswordUseCase.execute(email).collect { res ->
                messageEmailed.update {
                    res.data?.message ?: ""
                }
            }
        }
    }

    private val messageReset = MutableStateFlow("")
    val resetMessage = messageReset.asStateFlow()

    fun changeMessageText() {
        messageReset.update { "" }
    }

    fun resetPassword(resetPasswordParam: ResetPasswordParam) {
        viewModelScope.launch {
            resetPasswordUseCase.execute(resetPasswordParam).collect { res ->
                messageReset.update {
                    res.data?.message ?: ""
                }
            }
        }
    }

}