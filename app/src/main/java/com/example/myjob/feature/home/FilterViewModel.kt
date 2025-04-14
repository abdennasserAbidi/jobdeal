package com.example.myjob.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.R
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.Availabilities
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.DEFAULT_TYPE
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.USER_EXP
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.home.SearchUserUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

    val itemState = MutableStateFlow(emptyList<String>())

    val experiences = MutableStateFlow(USER_EXP)
    val employmentType = MutableStateFlow(DEFAULT_TYPE)
    val availabilities = MutableStateFlow(emptyList<Availabilities>())
    val selectedAvailability = MutableStateFlow(listOf(false, false, false, false))

    fun changeSelection(index: Int, isSelected: Boolean) {
        val availability = availabilities.value.toMutableList()
        availability[index].isSelected = isSelected
        availabilities.update {
            availability
        }

        val selectedAvailabilities = selectedAvailability.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedAvailability.update {
            selectedAvailabilities
        }

    }

    fun addToFlow(item: String) {
        val list = itemState.value.toMutableList()
        if (!list.contains(item)) list.add(item)
        itemState.update {
            list
        }
    }

    fun removeFromFlow(item: String) {
        val list = itemState.value.toMutableList()
        if (list.contains(item)) list.remove(item)
        itemState.update {
            list
        }
    }

    val listSubject = MutableStateFlow(emptyList<Subject>())
    val criteria = MutableStateFlow(CriteriaModel())

    fun changeActivitySector(list: List<String>) {

        val listSector = criteria.value.preferredActivitySector
        list.map {
            if (!listSector.contains(it)) listSector.add(it)
        }

        criteria.update {
            it.preferredActivitySector = listSector
            it
        }
    }

    fun changeInstitutions(list: List<String>) {

        val listInstitution = criteria.value.institutions
        list.map {
            if (!listInstitution.contains(it)) listInstitution.add(it)
        }

        criteria.update {
            it.institutions= listInstitution
            it
        }
    }


    val filteredUser = MutableStateFlow(emptyList<User>())
    fun validateFilter() {
        viewModelScope.launch {
            searchUserUseCase.execute(criteria.value).collect { res ->
                when(res.status) {
                    ResourceState.SUCCESS -> {
                        filteredUser.update { res.data ?: emptyList() }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    init {

        val list = listOf(
            Availabilities(title = R.string.disponibility1_text, isSelected = false),
            Availabilities(title = R.string.disponibility2_text, isSelected = false),
            Availabilities(title = R.string.disponibility3_text, isSelected = false),
            Availabilities(title = R.string.disponibility4_text, isSelected = false)
        )

        availabilities.update {
            list
        }

    }

}