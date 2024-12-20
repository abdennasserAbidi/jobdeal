package com.example.myjob.feature.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompanyProfile(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val expanded = remember { mutableStateListOf(true, false, false) }
    val enableNextCard = remember { mutableStateListOf(true, false, false) }

}