package com.example.myjob.feature.profile.test

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import com.example.myjob.common.CountryPicker
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.DateContainer
import com.example.myjob.feature.profile.ProfileViewModel
import kotlinx.serialization.Serializable

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

@Serializable
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateProfileFormScreen(
    initialData: CandidateFormData = CandidateFormData(),
    navController: NavController,
    clearData: () -> Unit = {},
    list: List<NewCountry>,
    allSubjects: MutableList<Subject>,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSaveProfile: (CandidateFormData) -> Unit = {},
    onSaveDraft: (CandidateFormData) -> Unit = {}
) {


    val user by profileViewModel.user.collectAsState()
    val allExp by profileViewModel.allExp.collectAsState()
    profileViewModel.getAllExp(user.id ?: 0)

    val allEduc by profileViewModel.allEduc.collectAsState()
    profileViewModel.getAllEduc(user.id ?: 0)


    var formData by remember { mutableStateOf(initialData) }
    var selectedTab by remember { mutableStateOf(0) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val tabs =
        listOf(
            stringResource(id = R.string.personal_info_text),
            "Professional",
            "Skills",
            "Experience",
            "Education",
            "Preferences"
        )
    val scrollState = rememberScrollState()

    val whatsAppGreen = colorResource(id = R.color.whatsapp)
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    var showCountryPicker by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(NewCountry("tn", "Tunisia", 216)) }
    val isShowed by profileViewModel.isCountryShowed.collectAsState()
    val isSearch by profileViewModel.isSearch.collectAsState()
    val isDateShowed by profileViewModel.isDateShowed.collectAsState()
    val listNames by profileViewModel.listNames.collectAsState()
    val listFlagLazy = profileViewModel.listFlag.collectAsLazyPagingItems()
    val listFlag = listFlagLazy.itemSnapshotList.items
    val isFirstTime = GlobalEntries.user.isFirstTime ?: true


    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            profileViewModel.changeListFlag(context)
            profileViewModel.getUserById()
            profileViewModel.mapperPersonalInfo(user)
            profileViewModel.mapperToListNames(list)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            if (isFirstTime) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Complete Profile",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            profileViewModel.logout()
                            clearData()
                            navController.navigate(Screen.LoginScreen.route)
                        }) {
                            Icon(Icons.Default.Logout, contentDescription = "Logout")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = "Complete Profile",
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }

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
                        profileViewModel = profileViewModel,
                        list = list,
                        allSubjects = allSubjects,
                        onSubmit = {
                            selectedTab += 1
                        },
                    )

                    1 -> ProfessionalForm(
                        profileViewModel = profileViewModel,
                        onDataChange = {
                            selectedTab += 1
                        }
                    )

                    2 -> SkillsForm(
                        profileViewModel = profileViewModel,
                        onDataChange = { selectedTab += 1 }
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
                        profileViewModel = profileViewModel,
                        onDataChange = { formData = it },
                        onNavigateToHome = {
                            navController.navigate(Screen.HomeScreen.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        DateContainer(
            isDateShowed = isDateShowed,
            changeDate = {
                profileViewModel.changeBirthDate(it)
                user.birthDate = it
            }, onDismiss = {
                //isDateShowed = false
                profileViewModel.changeVisibilityDate(false)
            })

        AnimatedVisibility(
            visible = isSearch,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val names = allSubjects.map {
                    it.libelly
                }
                GenericSearch(
                    mListOfJobs = names,
                    onDismissRequest = {
                        profileViewModel.changeVisibilitySearch(false)
                    },
                    onSelectedBank = { item, index ->
                        profileViewModel.changeVisibilitySearch(false)
                        profileViewModel.changeTitleGeneric(item)
                        user.preferredActivitySector = item
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        AnimatedVisibility(visible = showCountryPicker) {
            CountryPicker(
                listFlagLazy,
                profileViewModel,
                onClick = { newCountry ->
                    selectedCountry = newCountry
                    showCountryPicker = false
                }
            ) {
                showCountryPicker = false
            }
        }

        AnimatedVisibility(
            visible = isShowed,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                GenericSearch(
                    mListOfJobs = listNames,
                    onDismissRequest = {
                        profileViewModel.changeVisibilityCountry(false)
                    },
                    onSelectedBank = { item, index ->
                        profileViewModel.changeVisibilityCountry(false)
                        profileViewModel.changeCountryGeneric(item)
                        user.country = item
                    },
                    title = stringResource(id = R.string.country_text)
                )
            }
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








