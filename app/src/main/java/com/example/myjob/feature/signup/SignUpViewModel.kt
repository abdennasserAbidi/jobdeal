package com.example.myjob.feature.signup

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.R
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.CategoryChoices
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.response.LoginResponse
import com.example.myjob.domain.usecase.subscription.SaveUserUseCase
import com.example.myjob.domain.usecase.verification.GetVerifiedCompanyUseCase
import com.example.myjob.domain.usecase.verification.ValidateEmailUseCase
import com.example.myjob.domain.usecase.verification.ValidateNameUseCase
import com.example.myjob.domain.usecase.verification.ValidatePasswordUseCase
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
    private val getVerifiedCompanyUseCase: GetVerifiedCompanyUseCase,
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

    ///////////////////////////////////////////////////////////////////////////
    // FIRST ROLE
    ///////////////////////////////////////////////////////////////////////////
    val selectedCategory = MutableStateFlow(emptyList<CategoryChoices>())
    val selectedCat = MutableStateFlow(listOf(false, false))
    val selectedWorkPref = MutableStateFlow("")

    fun changeSelectionCategory(context: Context, index: Int, title: String, isSelected: Boolean) {
        val availability = selectedCategory.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        selectedCategory.update {
            availability
        }

        val selectedAvailabilities = selectedCat.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedCat.update {
            selectedAvailabilities
        }

        val list = availability.filter { it.isSelected }.map { context.getString(it.title) }.toMutableList()
        val workPref = if (list.isNotEmpty() && list.size > 1) list.joinToString(" & ")
        else if (list.size == 1) list[0]
        else ""

        selectedWorkPref.update {
            workPref
        }

        user.update {
            it.preferredWorkType = list
            it
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // SECOND ROLE
    ///////////////////////////////////////////////////////////////////////////
    val selectedSecondRole = MutableStateFlow(emptyList<CategoryChoices>())
    val selectedRole = MutableStateFlow(listOf(false, false, false))
    val selectedSecondWorkPref = MutableStateFlow("")

    fun changeSelectionSecondRole(context: Context, index: Int, title: String, isSelected: Boolean) {
        val availability = selectedSecondRole.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        selectedSecondRole.update {
            availability
        }

        val selectedAvailabilities = selectedRole.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedRole.update {
            selectedAvailabilities
        }

        val l = availability.filter { it.isSelected }.map { context.getString(it.title) }.toMutableList()
        val workPref = if (l.isNotEmpty() && l.size > 1) l.joinToString(" & ")
        else if (l.size == 1) l[0]
        else ""

        selectedSecondWorkPref.update {
            workPref
        }

        user.update {
            it.workPreferences = l
            it
        }

    }

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

    fun changeRoleIndex(roleIndex: Int) {
        user.update {
            if (roleIndex == 0) {
                it.candidate = false
                it.company = true
            } else {
                it.candidate = true
                it.company = false
            }
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
        //if (user.role == "Company") user.companyName = ""
        user.fullName = "${user.firstName} ${user.lastName}"
        viewModelScope.launch {
            saveUserUseCase.execute(user).collect { res ->
                if (res.status == ResourceState.SUCCESS) {

                    sharedPreferences.putString("token", res.data?.token ?: "")
                    sharedPreferences.putString("role", res.data?.user?.role ?: "")
                    sharedPreferences.putInt("idUser", res.data?.user?.id ?: 0)
                    sharedPreferences.putString("username", res.data?.user?.fullName ?: "")
                    sharedPreferences.putString("companyName", res.data?.user?.companyName ?: "")
                    GlobalEntries.role = res.data?.user?.role ?: ""
                    GlobalEntries.user = res.data?.user ?: User()
                    Log.i("saveUserRes", "saveUser: ${GlobalEntries.user}")
                    token.update { sharedPreferences.getString("token", "") ?: "" }

                    getCompaniesValidated()

                    saveUserRes.update { res.data ?: LoginResponse() }
                } else if (res.status == ResourceState.ERROR) {
                    saveUserRes.update { LoginResponse(messageError = res.message) }
                }
            }
        }
    }

    val listCompanies = MutableStateFlow(emptyList<String>())

    private fun getCompaniesValidated() {
        viewModelScope.launch {
            getVerifiedCompanyUseCase.execute().collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        val list = res.data ?: emptyList()
                        val l = list.toMutableList()
                        val language = sharedPreferences.getString("lang", "English")
                        if (language == "English" || language == "Anglais") l.add("Other")
                        else l.add("Autres")
                        listCompanies.update { l }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getToken() {
        token.update { sharedPreferences.getString("token", "") ?: "" }
    }

    init {
        getToken()

        val listCategories = listOf(
            CategoryChoices(title = R.string.type1_text, isSelected = false),
            CategoryChoices(title = R.string.type2_text, isSelected = false)
        )
        selectedCategory.update {
            listCategories
        }

        val listSecondRole = listOf(
            CategoryChoices(title = R.string.intern_user_text, isSelected = false),
            CategoryChoices(title = R.string.trainer_user_text, isSelected = false),
            CategoryChoices(title = R.string.event_user_text, isSelected = false)
        )
        selectedSecondRole.update {
            listSecondRole
        }
    }
}