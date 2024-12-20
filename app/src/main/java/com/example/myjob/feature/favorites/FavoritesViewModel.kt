package com.example.myjob.feature.favorites

import androidx.lifecycle.ViewModel
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    val sharedPreference: SharedPreference
): ViewModel() {
}