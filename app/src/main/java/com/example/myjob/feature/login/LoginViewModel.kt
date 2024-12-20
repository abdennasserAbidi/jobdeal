package com.example.myjob.feature.login

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.DialogState
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.usecase.LoginUseCase
import com.example.myjob.domain.usecase.SaveUserUseCase
import com.example.myjob.domain.usecase.ValidateEmailUseCase
import com.example.myjob.domain.usecase.ValidatePasswordUseCase
import com.example.myjob.domain.usecase.VerificationEmailUseCase
import com.example.myjob.feature.login.gmail.SignInResult
import com.example.myjob.feature.login.gmail.SignInState
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginUseCase: LoginUseCase,
    private val verificationEmailUseCase: VerificationEmailUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val sharedPreferences: SharedPreference
) : ViewModel() {


    fun isFromLogin(fromLogin: Boolean) {
        sharedPreferences.putBoolean("isFromLogin", fromLogin)
    }

    val user = MutableStateFlow(User())
    val isEmailValid = MutableStateFlow(false)
    val isPasswordValid = MutableStateFlow(false)

    val isConfirmPasswordValid = MutableStateFlow(false)

    fun checkConfirmPassword(password: String, input: String): Boolean {
        isConfirmPasswordValid.update {
            password.isNotEmpty() && password == input
        }
        return password == input
    }

    fun changeUserEmail(email: String) {
        user.update {
            it.email = email
            it
        }
    }

    fun changeUserPassword(password: String) {
        user.update {
            it.password = password
            it
        }
    }

    fun validateEmail(text: String) : Boolean {
        var t = false
        viewModelScope.launch {
            isEmailValid.update {
                validateEmailUseCase.execute(text) ?: false
            }
            t = validateEmailUseCase.execute(text) ?: false
        }
        Log.i("validateEmailUseCase", "validateEmail: $t")

        return t
    }

    fun validatePassword(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isPasswordValid.update {
                validatePasswordUseCase.execute(text) ?: false
            }
            t = validatePasswordUseCase.execute(text) ?: false
        }
        return t
    }

    //sign in with gmail
    val verificationText = MutableStateFlow("")
    val userGmail = MutableStateFlow(User())
    val saveUserRes = MutableStateFlow(Resource<LoginResponse>(ResourceState.IDLE, null, null))

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun clearText() {
        verificationText.update { "" }
    }

    fun onSignInResult(result: SignInResult) {

        val user = User(
            firstName = result.data?.username?.split(" ")?.get(0),
            lastName = result.data?.username?.split(" ")?.get(1),
            email = result.data?.email,
            role = "Candidate",
            password = ""
        )

        userGmail.update {
            user
        }

        viewModelScope.launch {
            verificationEmailUseCase.execute(result.data?.email).collect { res ->
                verificationText.update { res.data?.messageError ?: "" }
            }
        }

        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun addPassword(userGmail: User) {
        viewModelScope.launch {
            saveUserUseCase.execute(userGmail).collect { res ->
                if (res.status == ResourceState.SUCCESS) {
                    sharedPreferences.putString("token", res.data?.token ?: "")
                    token.update { sharedPreferences.getString("token", "") ?: "" }

                    saveUserRes.update {
                        Resource(res.status, res.data?: LoginResponse(), null)
                    }
                }
            }
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    var token = MutableStateFlow("")
    var error = MutableStateFlow(DialogState())
    var errorText = MutableStateFlow("")
    val tokenNotEmpty = MutableStateFlow(false)
    var login = MutableStateFlow(LoginResponse())
    var stateToken = MutableStateFlow(ResourceState.IDLE)
    var isProgressing = MutableStateFlow(false)

    fun changeErrorText() {
        errorText.update { "" }
    }

    fun login(user: User) {
        viewModelScope.launch {
            stateToken.update { ResourceState.LOADING }
            isProgressing.update { true }
            loginUseCase.execute(user).collect { res ->
                stateToken.update { res.status }
                isProgressing.update { false }
                if (res.status == ResourceState.SUCCESS) {
                    sharedPreferences.putString("token", res.data?.token ?: "")
                    sharedPreferences.putString("role", res.data?.user?.role ?: "")
                    sharedPreferences.putInt("idUser", res.data?.user?.id ?: 0)

                    token.update { sharedPreferences.getString("token", "") ?: "" }

                    tokenNotEmpty.update { token.value.isNotEmpty() }

                    login.update { res.data ?: LoginResponse() }

                } else if (res.status == ResourceState.ERROR) {

                    error.update {
                        if (res.data?.messageError?.isNotEmpty() == true) DialogState("SHOWEN")
                        else DialogState("IDLE")
                    }

                    errorText.update { res.data?.messageError ?: "" }
                    login.update { LoginResponse(messageError = res.message) }
                }
            }
        }
    }

    private fun getToken() {
        token.update { sharedPreferences.getString("token", "") ?: "" }
    }

    init {
        getToken()
    }
}