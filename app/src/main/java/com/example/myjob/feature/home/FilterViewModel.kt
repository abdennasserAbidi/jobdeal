package com.example.myjob.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myjob.R
import com.example.myjob.domain.entities.Availabilities
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.DEFAULT_TYPE
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.USER_EXP
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val sharedPreference: SharedPreference
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
        Log.i("aljhgfrhzkgrkzjg", "changeActivitySector: $list")

        val listSector = criteria.value.activitySectors
        list.map {
            if (!listSector.contains(it)) listSector.add(it)
        }

        criteria.update {
            it.activitySectors = listSector
            it
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