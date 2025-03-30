package com.example.myjob.feature.home

import androidx.lifecycle.ViewModel
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val sharedPreference: SharedPreference
) : ViewModel() {

    val itemState = MutableStateFlow(emptyList<String>())

}