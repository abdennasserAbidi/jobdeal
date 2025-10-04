package com.example.myjob.feature.validateprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.verification.VerificationCompanyUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ValidateProfileViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val verificationCompanyUseCase: VerificationCompanyUseCase
) : ViewModel() {


    val user = MutableStateFlow(User())

    fun changeNumSecuritySocial(number: String) {
        user.update {
            it.numSecuritySocial = number
            it
        }
    }

    fun changeDocs(docs: List<String>) {
        user.update {
            it.docs = docs
            it
        }
    }

    fun validateCompany() {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            verificationCompanyUseCase.execute(idUser).collect {

            }
        }
    }

}