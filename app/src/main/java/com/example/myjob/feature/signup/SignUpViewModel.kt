package com.example.myjob.feature.signup

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.usecase.SaveUserUseCase
import com.example.myjob.domain.usecase.ValidateEmailUseCase
import com.example.myjob.domain.usecase.ValidateNameUseCase
import com.example.myjob.domain.usecase.ValidatePasswordUseCase
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
class SignUpViewModel @Inject constructor(
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val sharedPreferences: SharedPreference
) : ViewModel() {
    val user = MutableStateFlow(User())
    val confirmPassword = MutableStateFlow(false)
    val isFirstNameValid = MutableStateFlow(false)
    val isCompanyNameValid = MutableStateFlow(false)
    val isLastNameValid = MutableStateFlow(false)
    val isEmailValid = MutableStateFlow(false)
    val isPasswordValid = MutableStateFlow(false)
    val isConfirmPasswordValid = MutableStateFlow(false)
    val saveUserRes = MutableStateFlow(LoginResponse())

    //sign in with gmail
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun checkConfirmPassword(password: String, input: String): Boolean {
        isConfirmPasswordValid.update {
            password == input
        }
        return password == input
    }
    //it.role = "company"
    fun changeRole(role: String) {
        user.update {
            it.role = role
            it
        }
    }

    fun changeCompanyName(firstName: String) {
        user.update {
            it.companyName = firstName
            it
        }
    }

    fun changeUserFirstName(firstName: String) {
        user.update {
            it.firstName = firstName
            it
        }
    }

    fun changeUserLastName(lastName: String) {
        user.update {
            it.lastName = lastName
            it
        }
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

    fun confirmPassword(password: String) {
        confirmPassword.update { user.value.password == password }
    }

    fun validateFirstName(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isFirstNameValid.update {
                validateNameUseCase.execute(text) ?: false
            }
            t = validateNameUseCase.execute(text) ?: false
        }
        return t
    }

    fun validateCompanyName(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isCompanyNameValid.update {
                validateNameUseCase.execute(text) ?: false
            }
            t = validateNameUseCase.execute(text) ?: false
        }
        return t
    }

    fun validateLastName(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isLastNameValid.update {
                validateNameUseCase.execute(text) ?: false
            }
            t = validateNameUseCase.execute(text) ?: false
        }
        return t
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

    var token = MutableStateFlow("")

    fun saveUser(user: User) {
        user.id = View.generateViewId()
        user.fullName = "${user.firstName} ${user.lastName}"
        viewModelScope.launch {
            saveUserUseCase.execute(user).collect { res ->
                if (res.status == ResourceState.SUCCESS) {
                    Log.i("saveUserRes", "saveUser: ${res.data?.user}")
                    sharedPreferences.putString("token", res.data?.token ?: "")
                    sharedPreferences.putString("role", res.data?.user?.role ?: "")
                    sharedPreferences.putInt("idUser", res.data?.user?.id ?: 0)
                    sharedPreferences.putString("username", res.data?.user?.fullName ?: "")

                    GlobalEntries.user = res.data?.user ?: User()
                    token.update { sharedPreferences.getString("token", "") ?: "" }

                    saveUserRes.update { res.data ?: LoginResponse() }
                } else if (res.status == ResourceState.ERROR) {
                    saveUserRes.update { LoginResponse(messageError = res.message) }
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