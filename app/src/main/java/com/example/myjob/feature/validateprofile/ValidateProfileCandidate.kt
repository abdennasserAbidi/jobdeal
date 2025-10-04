package com.example.myjob.feature.validateprofile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateProfileCandidate(
    navController: NavController,
    viewModel: InterviewValidationViewModel = hiltViewModel()
) {
    var verificationData by remember { mutableStateOf(VerificationData()) }
    var currentStep by remember { mutableStateOf<VerificationStep?>(null) }

    val verificationSteps by viewModel.verificationSteps.collectAsState()

    val completedSteps = verificationSteps.count { it.status == VerificationStatus.VERIFIED }
    val requiredSteps = verificationSteps.count { it.isRequired }
    val requiredCompleted = verificationSteps.count { it.isRequired && it.status == VerificationStatus.VERIFIED }
    val isFullyVerified = requiredCompleted == requiredSteps

    val interactionSource = remember { MutableInteractionSource() }

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(colorResource(id = R.color.whatsapp))
        ) {
            Text(
                text = "Validate profile",
                modifier = Modifier
                    .align(Alignment.Center),
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.ArrowBack,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterStart)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        navController.popBackStack()
                    },
                tint = Color.White,
                contentDescription = ""
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFF),
                            Color.White,
                            Color(0xFFF3F4F6)
                        )
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            verificationSteps.forEachIndexed { index, step ->
                item {
                    VerificationStepCard(
                        stepStatus = step,
                        onClick = {
                            when(index) {
                                0 -> navController.navigate(Screen.ValidateDocCandidateScreen.route)
                                1 -> Log.i("", "ValidateProfileCandidate: fzkghrzg")
                                2 -> navController.navigate(Screen.ValidationInterviewScreen.route)
                                else -> Log.i("", "ValidateProfileCandidate: fzkghrzg")
                            }

                            currentStep = step.step
                        }
                    )
                }
            }

            // Verification Benefits
            item {
                VerificationBenefitsCard()
            }
        }
    }
}