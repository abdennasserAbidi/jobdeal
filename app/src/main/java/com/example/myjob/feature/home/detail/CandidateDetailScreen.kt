package com.example.myjob.feature.home.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
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

data class CandidateProfile(
    val candidate: Candidate,
    val email: String,
    val phone: String,
    val linkedIn: String?,
    val github: String?,
    val portfolio: String?,
    val bio: String,
    val experience: List<WorkExperience>,
    val education: List<Education>,
    val projects: List<Project>,
    val certifications: List<String>,
    val languages: List<Language>,
    val availability: String,
    val expectedSalary: String,
    val noticePeriod: String
)

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

    val user = GlobalEntries.userForCompany
    val experienceYears by detailViewModel.experienceYears.collectAsState()
    val invitation by detailViewModel.invitation.collectAsState()

    var openFinishProcess by remember { mutableStateOf(false) }
    var invitationModel by remember { mutableStateOf(InvitationModel()) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            detailViewModel.getUserById(user.id ?: 0)
            detailViewModel.getAllExperience(user.id ?: 0)
            detailViewModel.getAllEducations(user.id ?: 0)
            hideNavigation()
            user.fullName?.let {
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
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.profile_candidate_type_text),
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        isSaved = !isSaved
                        onSaveCandidate()
                    }
                ) {
                    Icon(
                        if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) WhatsAppGreen else Color.White
                    )
                }
                IconButton(onClick = { /* Handle share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.whatsapp),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card with Basic Info
            CandidateHeaderCard(candidateProfile = user, experienceYears,
                sendInvitation = {
                    GlobalEntries.candidateUser = it
                    navController.navigate(Screen.SendInvitationScreen.route)
                },
                sendMessage = {
                    GlobalEntries.candidateUser = it
                    navController.navigate(Screen.SendMessageScreen.route)
                },
                onTerminateInvitation = {
                    invitationModel = it
                    openFinishProcess = true
                })

            // Contact Information Card
            ContactInformationCard(candidateProfile = user)

            // Bio Card
            if (user.bio?.isNotEmpty() == true) {
                BioCard(bio = user.bio ?: "")
            }

            // Skills Card
            if (user.candidateSkills?.listSkills?.isNotEmpty() == true)
                SkillsCard(skills = user.candidateSkills?.listSkills ?: mutableListOf())

            // Experience Card
            val experiences = user.experience ?: mutableListOf()
            if (experiences.isNotEmpty())
                ExperienceCard(experience = user.experience ?: mutableListOf())

            // Projects Card
            //ProjectsCard(projects = candidateProfile.projects)

            // Education Card
            val educations = user.education ?: mutableListOf()
            if (educations.isNotEmpty())
                EducationCard(educations = user.education ?: mutableListOf())

            // Additional Info Card
            AdditionalInfoCard(
                certifications = user.candidateSkills?.listCertification ?: mutableListOf(),
                languages = user.candidateSkills?.listLanguages ?: mutableListOf(),
                availability = user.professionalStatus?.availability ?: "",
                expectedSalary = "5000",
                noticePeriod = "15"
            )

            // Action Buttons
            /*ActionButtonsCard(
                onSendInvitation = onSendInvitation,
                onContactCandidate = onContactCandidate,
                onScheduleInterview = onScheduleInterview
            )*/

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


