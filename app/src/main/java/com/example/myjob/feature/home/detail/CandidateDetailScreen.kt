package com.example.myjob.feature.home.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.otherUserId
import com.example.myjob.common.GlobalEntries.otherUserName
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.feature.invitation.company.EnProcessForm
import com.example.myjob.feature.navigation.Screen

// WhatsApp Green Theme Colors
val WhatsAppGreen = Color(0xFF25D366)
val WhatsAppDarkGreen = Color(0xFF128C7E)
val WhatsAppLightGreen = Color(0xFFDCF8C6)
val WhatsAppGreenSurface = Color(0xFFF0F9F0)

data class WorkExperience(
    val company: String,
    val position: String,
    val duration: String,
    val description: String,
    val technologies: List<String>
)

data class Education(
    val institution: String,
    val degree: String,
    val field: String,
    val year: String,
    val gpa: String?
)

data class Project(
    val name: String,
    val description: String,
    val technologies: List<String>,
    val link: String?
)

data class Language(
    val name: String,
    val proficiency: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateDetailScreen(
    navController: NavController,
    detailViewModel: DetailViewModel = hiltViewModel(),
    hideNavigation: () -> Unit,
    onSendInvitation: () -> Unit = {},
    onSaveCandidate: () -> Unit = {},
    onContactCandidate: () -> Unit = {},
    onScheduleInterview: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var isSaved by remember { mutableStateOf(false) }

    val userCandidate = GlobalEntries.userForCompany
    val experienceYears by detailViewModel.experienceYears.collectAsState()
    val invitation by detailViewModel.invitation.collectAsState()
    val candidate by detailViewModel.user.collectAsState()

    var openFinishProcess by remember { mutableStateOf(false) }
    var invitationModel by remember { mutableStateOf(InvitationModel()) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            detailViewModel.getUserById(userCandidate.id ?: 0)
            detailViewModel.getAllExperience(userCandidate.id ?: 0)
            detailViewModel.getAllEducations(userCandidate.id ?: 0)
            hideNavigation()
            userCandidate.fullName?.let {
                if (it.isNotEmpty()) detailViewModel.getUserNameAbbreviation(it)
            }
        }
    }

    if (openFinishProcess) {
        EnProcessForm(invitationModel,
            onDismissRequest = {
                openFinishProcess = false
            },
            onConfirmation = {
                detailViewModel.finishProcess(it)
                openFinishProcess = false
            })
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        if (candidate.id != 0) {
            CandidateDetailsScreen(
                candidate = candidate,
                onBackClick = {
                    navController.popBackStack()
                },
                onContactClick = {
                    GlobalEntries.candidateUser = it
                    otherUserId = it.id ?: -1

                    otherUserName =
                        if (it.role == "Candidate" || it.role == "Candidat") it.fullName ?: ""
                        else it.companyName ?: ""

                    navController.navigate(Screen.SendMessageScreen.route)
                },
                onHireClick = {
                    GlobalEntries.candidateUser = it
                    navController.navigate(Screen.SendInvitationScreen.route)
                },
                onTerminateInvitation = {
                    invitationModel = it
                    openFinishProcess = true
                }
            )
        }
    }
}


