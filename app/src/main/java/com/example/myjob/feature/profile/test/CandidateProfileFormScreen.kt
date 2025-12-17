package com.example.myjob.feature.profile.test

import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.myjob.common.GlobalEntries.isSubmitAction
import com.example.myjob.common.GlobalEntries.isSubmitEducationAction
import com.example.myjob.common.GlobalEntries.isSubmitProfessionalAction
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.DateContainer
import com.example.myjob.feature.profile.ProfileViewModel
import kotlinx.coroutines.flow.update
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
    navController: NavController,
    clearData: () -> Unit = {},
    list: List<NewCountry>,
    allSubjects: MutableList<Subject>,
    listStudyField: MutableList<String>,
    listSchools: MutableList<String>,
    listGrade: MutableList<String>,
    listCompany: MutableList<String>,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {


    val user by profileViewModel.user.collectAsState()
    val allExp by profileViewModel.allExp.collectAsState()
    profileViewModel.getAllExp(user.id ?: 0)

    val allEduc by profileViewModel.allEduc.collectAsState()
    profileViewModel.getAllEduc(user.id ?: 0)

    val degreeList by profileViewModel.degreeList.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var isCompanyVisible by remember { mutableStateOf(false) }
    var typeDate by remember { mutableStateOf("birthDay") }
    var indexToChange by remember { mutableStateOf(0) }
    var indexEducationToChange by remember { mutableStateOf(0) }

    var isActivitySectorVisible by remember { mutableStateOf(false) }
    var isGradeVisible by remember { mutableStateOf(false) }
    var isDegreeVisible by remember { mutableStateOf(false) }
    var isInstitutionVisible by remember { mutableStateOf(false) }


    val tabs =
        listOf(
            stringResource(id = R.string.personal_info_text),
            "Professional",
            "Skills",
            "Experience",
            "Education"
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
    val isFirstTime = GlobalEntries.user.firstTimeUse ?: true


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
                            text = stringResource(id = R.string.complete_profile_text),
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
                            text = stringResource(id = R.string.complete_profile_text),
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
                        onBirthDateChange = {
                            typeDate = "birthDay"
                            profileViewModel.changeVisibilityDate(true)
                        },
                        onSubmit = {
                        },
                    )

                    1 -> ProfessionalForm(
                        profileViewModel = profileViewModel,
                        onDataChange = {
                        }
                    )

                    2 -> SkillsForm(
                        profileViewModel = profileViewModel,
                        onDataChange = {

                        }
                    )

                    3 -> ExperienceForm(
                        profileViewModel = profileViewModel,
                        onCompanyChange = {
                            indexToChange = it
                            isCompanyVisible = true
                        },
                        onDateStartChange = {
                            typeDate = "start"
                            indexToChange = it
                            profileViewModel.changeVisibilityDate(true)
                        },
                        onDateEndChange = {
                            typeDate = "end"
                            indexToChange = it
                            profileViewModel.changeVisibilityDate(true)
                        }
                    )

                    4 -> EducationFormSection(
                        profileViewModel = profileViewModel,
                        onChangeFieldOfStudy = {
                            indexEducationToChange = it
                            isActivitySectorVisible = true
                        },
                        onChangeDegree = {
                            indexEducationToChange = it
                            isDegreeVisible = true
                        },
                        onChangeInstitution = {
                            indexEducationToChange = it
                            isInstitutionVisible = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        val saveCompletedState by profileViewModel.saveCompletedState.collectAsState()
        LaunchedEffect(saveCompletedState) {
            if (saveCompletedState == "saved successfully") {
                navController.navigate(Screen.UpdateDetailsScreen.route)
                profileViewModel.clearComplete()
            }
        }

        val snackbarHostState = remember { SnackbarHostState() }

        val saveUserState by profileViewModel.saveUserState.collectAsState()
        LaunchedEffect(saveUserState) {
            if (saveUserState == "saved successfully") {
                profileViewModel.triggerPersonalCheck(false)
                selectedTab += 1
                profileViewModel.clearPersoanlInfo()
            }
        }

        val saveCandidateProfessionalState by profileViewModel.saveCandidateProfessionalState.collectAsState()
        LaunchedEffect(saveCandidateProfessionalState) {
            if (saveCandidateProfessionalState == "saved successfully") {
                profileViewModel.triggerProfessionalCheck(false)
                selectedTab += 1
                profileViewModel.clearProfessionalInfo()
            }
        }

        val saveExpState by profileViewModel.saveExpState.collectAsState()
        LaunchedEffect(saveExpState) {
            if (saveExpState == "saved successfully") {
                profileViewModel.triggerExperienceCheck(false)
                selectedTab += 1
                profileViewModel.clearExpState()
            }
        }

        val saveEducationState by profileViewModel.saveEducationState.collectAsState()
        LaunchedEffect(saveEducationState) {
            if (saveEducationState == "saved successfully") {
                profileViewModel.triggerEducationCheck(false)
                profileViewModel.saveIsCompletedProfileCandidate()
                profileViewModel.clearEducationState()
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        when(selectedTab) {
                            0 -> profileViewModel.triggerPersonalCheck(true)
                            1 -> {
                                isSubmitProfessionalAction.update { true }
                                profileViewModel.triggerProfessionalCheck(true)
                            }
                            2 -> profileViewModel.saveCandidateSkills()
                            3 -> {
                                isSubmitAction.update { true }
                                profileViewModel.triggerExperienceCheck(true)
                            }
                            4 -> {
                                isSubmitEducationAction.update { true }
                                profileViewModel.triggerEducationCheck(true)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 10.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.whatsapp)
                    )
                ) {
                    val text = if (selectedTab == 4) stringResource(id = R.string.save_text)
                    else stringResource(id = R.string.next_text)
                    Text(text)
                }
            }
        }

        DateContainer(
            isDateShowed = isDateShowed,
            changeDate = {
                when (typeDate) {
                    "birthDay" -> {
                        profileViewModel.changeBirthDate(it)
                        user.birthDate = it
                    }

                    "start" -> {
                        profileViewModel.changeStartDateExperience(indexToChange, profileViewModel.convertDate(it))
                        profileViewModel.changeEndDateExp(it)
                    }

                    "end" -> {
                        profileViewModel.changeEndDateExperience(indexToChange, profileViewModel.convertDate(it))
                        profileViewModel.changeEndDateExp(it)
                    }
                }
            },
            onDismiss = {
                profileViewModel.changeVisibilityDate(false)
            }
        )

        //EDUCATION
        AnimatedVisibility(
            visible = isInstitutionVisible,
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
                    mListOfJobs = listSchools,
                    onDismissRequest = {
                        isInstitutionVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isInstitutionVisible = false
                        profileViewModel.changeInstitution(indexEducationToChange, item)
                    },
                    title = stringResource(id = R.string.School_text)
                )
            }
        }


        AnimatedVisibility(
            visible = isActivitySectorVisible,
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
                    mListOfJobs = listStudyField,
                    onDismissRequest = {
                        isActivitySectorVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isActivitySectorVisible = false
                        profileViewModel.changeFieldOfStudyEducation(indexEducationToChange, item)
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        /*AnimatedVisibility(
            visible = isGradeVisible,
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
                    mListOfJobs = listGrade, //formation
                    onDismissRequest = {
                        isGradeVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isGradeVisible = false
                        profileViewModel.changeGradeEducation(indexEducationToChange, item)
                    },
                    title = stringResource(id = R.string.Grade_text)
                )
            }
        }*/

        AnimatedVisibility(
            visible = isDegreeVisible,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                androidx.compose.material.Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 5.dp
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ) {

                            Icon(imageVector = Icons.Default.ArrowBack,
                                contentDescription = "back",
                                modifier = Modifier
                                    .height(30.dp)
                                    .align(Alignment.CenterStart)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isDegreeVisible = false
                                    }
                                    .padding(start = 20.dp))

                            Text(
                                text = stringResource(id = R.string.degree_text),
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(items = degreeList) { index, item ->
                        Column {
                            Text(text = item.type,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isDegreeVisible = false
                                        profileViewModel.changeDegreeEducation(
                                            indexEducationToChange,
                                            item.type
                                        )
                                    }
                                    .fillMaxWidth(0.9f)
                                    .padding(top = 20.dp, start = 20.dp),
                                fontSize = 20.sp,
                                color = Color.Black)

                            if (index < degreeList.lastIndex)
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                        }
                    }
                }
            }
        }
        //END EDUCATION

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

        AnimatedVisibility(
            visible = isCompanyVisible,
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
                    mListOfJobs = listCompany,
                    onDismissRequest = {
                        isCompanyVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isCompanyVisible = false
                        profileViewModel.changeCompanyExperience(indexToChange, item)
                    },
                    title = stringResource(id = R.string.companies_text)
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
            },
            onDismiss = { showSaveDialog = false }
        )
    }
}








