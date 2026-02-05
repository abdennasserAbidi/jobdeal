package com.example.myjob.feature.demands

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.common.GlobalEntries.idDemand
import com.example.myjob.common.GlobalEntries.idHoster
import com.example.myjob.common.rememberLifecycleEvent

@Composable
fun DemandMarketDetailScreen(
    navController: NavController,
    demandsViewModel: DemandsViewModel = hiltViewModel()
) {

    val demand by demandsViewModel.demand.collectAsState()
    val userSender by demandsViewModel.userSender.collectAsState()

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            demandsViewModel.getDemandById(idDemand)
            demandsViewModel.getUserById(idHoster)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        DemandDetailScreen()
    }
}