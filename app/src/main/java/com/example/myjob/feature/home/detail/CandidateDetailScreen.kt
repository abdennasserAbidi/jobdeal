package com.example.myjob.feature.home.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.CandidateStatus
import com.example.myjob.feature.navigation.Screen

// WhatsApp Green Theme Colors
val WhatsAppGreen = Color(0xFF25D366)
val WhatsAppDarkGreen = Color(0xFF128C7E)
val WhatsAppLightGreen = Color(0xFFDCF8C6)
val WhatsAppGreenSurface = Color(0xFFF0F9F0)

val sampleCandidate = Candidate(
    id = 1,
    name = "Sarah Johnson",
    position = "Senior Android Developer",
    company = "Google",
    experience = "5+ years",
    location = "San Francisco, CA",
    skills = listOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines", "Room", "Retrofit", "Dagger Hilt", "Firebase"),
    salary = "$120k - $150k",
    status = CandidateStatus.AVAILABLE
)

val sampleProfile = CandidateProfile(
    candidate = sampleCandidate,
    email = "sarah.johnson@email.com",
    phone = "+1 (555) 123-4567",
    linkedIn = "linkedin.com/in/sarahjohnson",
    github = "github.com/sarahjohnson",
    portfolio = "sarahjohnson.dev",
    bio = "Passionate Android developer with 5+ years of experience building scalable mobile applications. Expertise in modern Android development using Kotlin, Jetpack Compose, and clean architecture patterns. Led development teams and mentored junior developers. Strong advocate for clean code and test-driven development.",
    experience = listOf(
        WorkExperience(
            company = "Google",
            position = "Senior Android Developer",
            duration = "2022 - Present",
            description = "Lead Android development for Google Pay, implementing new features and improving app performance. Collaborated with cross-functional teams to deliver high-quality user experiences.",
            technologies = listOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines", "Room")
        ),
        WorkExperience(
            company = "Meta",
            position = "Android Developer",
            duration = "2020 - 2022",
            description = "Developed and maintained Facebook mobile app features. Implemented new UI components and optimized app performance for millions of users.",
            technologies = listOf("Java", "Kotlin", "RxJava", "Retrofit", "Dagger")
        ),
        WorkExperience(
            company = "Spotify",
            position = "Junior Android Developer",
            duration = "2019 - 2020",
            description = "Built music streaming features and worked on playlist management functionality. Gained experience in media playback and audio processing.",
            technologies = listOf("Java", "ExoPlayer", "MediaSession", "SQLite")
        )
    ),
    education = listOf(
        Education(
            institution = "Stanford University",
            degree = "Master of Science",
            field = "Computer Science",
            year = "2019",
            gpa = "3.8"
        ),
        Education(
            institution = "UC Berkeley",
            degree = "Bachelor of Science",
            field = "Computer Engineering",
            year = "2017",
            gpa = "3.6"
        )
    ),
    projects = listOf(
        Project(
            name = "TaskMaster Pro",
            description = "A comprehensive task management app with team collaboration features, built using Jetpack Compose and Firebase.",
            technologies = listOf("Kotlin", "Jetpack Compose", "Firebase", "Room"),
            link = "github.com/sarahjohnson/taskmaster"
        ),
        Project(
            name = "Weather Forecast App",
            description = "Beautiful weather app with location-based forecasts and interactive maps integration.",
            technologies = listOf("Kotlin", "MVVM", "Retrofit", "Google Maps"),
            link = "github.com/sarahjohnson/weather-app"
        ),
        Project(
            name = "Expense Tracker",
            description = "Personal finance app with budget tracking, expense categorization, and financial insights.",
            technologies = listOf("Kotlin", "Room", "Charts", "Material Design"),
            link = null
        )
    ),
    certifications = listOf(
        "Google Associate Android Developer",
        "AWS Certified Developer",
        "Scrum Master Certified"
    ),
    languages = listOf(
        Language("English", "Native"),
        Language("Spanish", "Conversational"),
        Language("French", "Basic")
    ),
    availability = "Available immediately",
    expectedSalary = "$140k - $170k",
    noticePeriod = "2 weeks"
)

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
    candidateProfile: CandidateProfile,
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
    val userFullName by detailViewModel.userFullName.collectAsState()
    val username by detailViewModel.username.collectAsState()

    val showUser by detailViewModel.showUser.collectAsState()

    val lazyPagingItems = detailViewModel.experience.collectAsLazyPagingItems()
    val experience = lazyPagingItems.itemSnapshotList.items

    val lazyPagingItemsEducation = detailViewModel.education.collectAsLazyPagingItems()
    val education = lazyPagingItemsEducation.itemSnapshotList.items

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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Candidate Profile",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                containerColor = WhatsAppGreen,
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
            CandidateHeaderCard(candidateProfile = user) {
                GlobalEntries.candidateUser = it
                navController.navigate(Screen.SendInvitationScreen.route)
            }

            // Contact Information Card
            ContactInformationCard(candidateProfile = candidateProfile)

            // Bio Card
            BioCard(bio = candidateProfile.bio)

            // Skills Card
            SkillsCard(skills = candidateProfile.candidate.skills)

            // Experience Card
            ExperienceCard(experience = user.experience ?: mutableListOf())

            // Projects Card
            //ProjectsCard(projects = candidateProfile.projects)

            // Education Card
            //EducationCard(education = user.education ?: mutableListOf())

            // Additional Info Card
            AdditionalInfoCard(
                certifications = candidateProfile.certifications,
                languages = candidateProfile.languages,
                availability = candidateProfile.availability,
                expectedSalary = candidateProfile.expectedSalary,
                noticePeriod = candidateProfile.noticePeriod
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


