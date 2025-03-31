package com.example.myjob.feature.home

import androidx.lifecycle.ViewModel
import com.example.myjob.R
import com.example.myjob.domain.entities.Availabilities
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