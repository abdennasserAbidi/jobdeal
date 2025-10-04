package com.example.myjob.feature.profile.test

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.ProfileViewModel

data class CandidateFormData(
    // Basic Information
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var phone: String = "",
    var position: String = "",
    var currentCompany: String = "",
    var location: String = "",
    var bio: String = "",

    // Professional Details
    var experience: String = "",
    var expectedSalary: String = "",
    var currentSalary: String = "",
    var noticePeriod: String = "",
    var availability: String = "",

    // Skills & Certifications
    var skills: MutableList<String> = mutableListOf(),
    var certifications: MutableList<String> = mutableListOf(),
    var languages: MutableList<LanguageForm> = mutableListOf(),

    // Work Preferences
    var remoteWork: Boolean = false,
    var hybridWork: Boolean = false,
    var onSiteWork: Boolean = false,
    var travelWillingness: String = "",
    var preferredWorkingHours: String = "",

    // Social & Portfolio
    var linkedIn: String = "",
    var github: String = "",
    var portfolio: String = "",
    var twitter: String = "",
    var medium: String = "",

    // Education
    var education: MutableList<EducationForm> = mutableListOf(),

    // Experience
    var workExperience: MutableList<WorkExperienceForm> = mutableListOf(),

    // Projects
    var projects: MutableList<ProjectForm> = mutableListOf(),

    // Job Preferences
    var preferredRoles: MutableList<String> = mutableListOf(),
    var preferredIndustries: MutableList<String> = mutableListOf(),
    var companySizePreference: String = "",

    // Personal Information
    var nationality: String = "",
    var visaStatus: String = "",
    var address: String = ""
)

data class LanguageForm(
    var name: String = "",
    var proficiency: String = ""
)

data class EducationForm(
    var institution: String = "",
    var degree: String = "",
    var field: String = "",
    var year: String = "",
    var gpa: String = ""
)

data class WorkExperienceForm(
    var company: String = "",
    var position: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var current: Boolean = false,
    var description: String = "",
    var technologies: MutableList<String> = mutableListOf()
)

data class ProjectForm(
    var name: String = "",
    var description: String = "",
    var technologies: MutableList<String> = mutableListOf(),
    var link: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateProfileFormScreen(
    initialData: CandidateFormData = CandidateFormData(),
    navController: NavController,
    list: List<NewCountry>,
    allSubjects: MutableList<Subject>,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSaveProfile: (CandidateFormData) -> Unit = {},
    onSaveDraft: (CandidateFormData) -> Unit = {}
) {
    var formData by remember { mutableStateOf(initialData) }
    var selectedTab by remember { mutableStateOf(0) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val tabs =
        listOf("Basic Info", "Professional", "Skills", "Experience", "Education", "Preferences")
    val scrollState = rememberScrollState()

    val whatsAppGreen = colorResource(id = R.color.whatsapp)
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val user by profileViewModel.user.collectAsState()
    var showCountryPicker by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(NewCountry("tn", "Tunisia", 216)) }
    val isShowed by profileViewModel.isCountryShowed.collectAsState()
    val isSearch by profileViewModel.isSearch.collectAsState()
    val isDateShowed by profileViewModel.isDateShowed.collectAsState()
    val listNames by profileViewModel.listNames.collectAsState()
    val listFlagLazy = profileViewModel.listFlag.collectAsLazyPagingItems()
    val listFlag = listFlagLazy.itemSnapshotList.items


    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            profileViewModel.changeListFlag(context)
            profileViewModel.getUserById()
            profileViewModel.mapperPersonalInfo(user)
            profileViewModel.mapperToListNames(list)
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
                    text = "Complete Profile",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.whatsapp),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )

        // Progress Indicator
        LinearProgressIndicator(
            progress = (selectedTab + 1) / tabs.size.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            color = whatsAppGreen,
            trackColor = whatsAppGreen
        )

        // Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = whatsAppGreen
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    selectedContentColor = whatsAppGreen,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> BasicInfoForm(
                    formData = formData,
                    profileViewModel = profileViewModel,
                    onDataChange = { formData = it }
                )

                1 -> ProfessionalForm(
                    formData = formData,
                    onDataChange = { formData = it }
                )

                2 -> SkillsForm(
                    formData = formData,
                    onDataChange = { formData = it }
                )

                3 -> ExperienceForm(
                    formData = formData,
                    onDataChange = { formData = it }
                )

                4 -> EducationFormSection(
                    formData = formData,
                    onDataChange = { formData = it }
                )

                5 -> PreferencesForm(
                    formData = formData,
                    onDataChange = { formData = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Save Confirmation Dialog
    if (showSaveDialog) {
        SaveProfileDialog(
            onConfirm = {
                isLoading = true
                showSaveDialog = false
                onSaveProfile(formData)
            },
            onDismiss = { showSaveDialog = false }
        )
    }
}








