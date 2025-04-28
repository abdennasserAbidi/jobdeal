package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun FilteredHome(
    navController: NavController,
    filterViewModel: FilterViewModel = hiltViewModel()
) {

    val filteredUser by filterViewModel.filteredUser.collectAsState()
    Log.i("kafhrzufgrfhzr", "FilteredHome: $filteredUser")

    Box(modifier = Modifier.fillMaxSize()) {

    }


}