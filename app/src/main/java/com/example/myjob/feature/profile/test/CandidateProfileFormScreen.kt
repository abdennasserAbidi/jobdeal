package com.example.myjob.feature.profile.test

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.base.MyApp
import com.example.myjob.common.CountryPicker
import com.example.myjob.common.CustomPhoneKit
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.listEducations
import com.example.myjob.common.GlobalEntries.listExperience
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.School
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.DateContainer
import com.example.myjob.feature.profile.ProfileViewModel
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateProfileFormScreenTest(
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
    val whatsAppGreen = colorResource(id = R.color.whatsapp)
    val context = LocalContext.current

    val isFirstTime = GlobalEntries.user.firstTimeUse ?: true
    val user by profileViewModel.user.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs =
        listOf(
            stringResource(id = R.string.personal_info_text),
            "Professional",
            "Skills",
            "Experience",
            "Education"
        )
    val scrollState = rememberScrollState()

    var showSaveDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isCompanyVisible by remember { mutableStateOf(false) }
    val degreeList by profileViewModel.degreeList.collectAsState()

    var indexEducationToChange by remember { mutableStateOf(0) }

    var isActivitySectorVisible by remember { mutableStateOf(false) }
    var isGradeVisible by remember { mutableStateOf(false) }
    var isDegreeVisible by remember { mutableStateOf(false) }
    var isInstitutionVisible by remember { mutableStateOf(false) }

    //FORM 1
    var indexToChange by remember { mutableStateOf(0) }
    val interactionSource = remember { MutableInteractionSource() }
    var activatedEmailCheck by remember { mutableStateOf(false) }
    var activatedCheck by remember { mutableStateOf(false) }
    val isDateShowed by profileViewModel.isDateShowed.collectAsState()
    val isSearch by profileViewModel.isSearch.collectAsState()
    val isShowed by profileViewModel.isCountryShowed.collectAsState()
    val listNames by profileViewModel.listNames.collectAsState()
    val listFlagLazy = profileViewModel.listFlag.collectAsLazyPagingItems()
    val listFlag = listFlagLazy.itemSnapshotList.items
    var titleGlobal by remember { mutableStateOf("") }
    var showGlobal by remember { mutableStateOf(false) }
    var typeDate by remember { mutableStateOf("birthDay") }
    var selectedItemGlobal by remember { mutableStateOf("") }
    var globalList by remember { mutableStateOf(emptyList<Int>()) }
    var action: (name: String) -> Unit = {}


    var showGender by remember { mutableStateOf(false) }
    val userGender by profileViewModel.userSex.collectAsState()
    val gendersOptions by profileViewModel.gendersOptions.collectAsState()
    val selectedGender by remember(userGender) {
        mutableStateOf(
            profileViewModel.getGenderFromLang(userGender)
        )
    }

    //countries
    val countries by profileViewModel.countryGeneric.collectAsState()
    val countriesCheck by remember { derivedStateOf { countries.isNotEmpty() } }

    //activity sector
    val title by profileViewModel.titleGeneric.collectAsState()
    val activitySectorCheck by remember { derivedStateOf { title.isNotEmpty() } }

    // Name Fields
    val userName by profileViewModel.userFullName.collectAsState()
    val isFirstNameValid by profileViewModel.isFirstNameValid.collectAsState()
    val submitEnabled by remember { derivedStateOf { isFirstNameValid } }

    //EMAIL
    val userEmail by profileViewModel.userEmail.collectAsState()
    var email by remember { mutableStateOf(GlobalEntries.user.email) }
    val isEmailValid by profileViewModel.isEmailValid.collectAsState()
    val emailCheck by remember { derivedStateOf { isEmailValid } }

    //SITUATION
    var showSituation by remember { mutableStateOf(false) }
    val userSituation by profileViewModel.userSituation.collectAsState()
    val situationsOptions by profileViewModel.situationsOptions.collectAsState()
    val selectedSituation by remember(userSituation) {
        mutableStateOf(
            profileViewModel.getSituationFromLang(userSituation)
        )
    }

    //Date
    val birthDateUser by profileViewModel.birthDateUser.collectAsState()
    val birthDateUserCheck by remember { derivedStateOf { birthDateUser?.isNotEmpty() == true } }

    //TYPE EMPLOI
    var showType by remember { mutableStateOf(false) }
    val userEmploymentTypeChoice by profileViewModel.userEmploymentTypeChoice.collectAsState()
    val typesOptions by profileViewModel.typesOptions.collectAsState()
    val selectedType by remember {
        mutableStateOf(
            userEmploymentTypeChoice?.ifEmpty { "" })
    }

    var listPhones by remember(user.phoneList) {
        mutableStateOf(
            if (user.phoneList.isNullOrEmpty()) mutableListOf("") else user.phoneList
                ?: mutableListOf("")
        )
    }

    var listAddress by remember(user.addressList) {
        mutableStateOf(
            if (user.addressList.isNullOrEmpty()) mutableListOf("") else user.addressList
                ?: mutableListOf("")
        )
    }

    var showCountryPicker by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(NewCountry("tn", "Tunisia", 216)) }


    //FORM 2
    var showWorkType by remember { mutableStateOf(false) }
    val workType by profileViewModel.workType.collectAsState()
    val workTypeOptions by profileViewModel.workTypeOptions.collectAsState()
    val selectedWorkType by remember {
        mutableStateOf(
            workType?.ifEmpty { "" })
    }

    var showAvailability by remember { mutableStateOf(false) }
    val availability by profileViewModel.availability.collectAsState()
    val availabilityOptions by profileViewModel.availabilityOptions.collectAsState()
    val selectedAvailability by remember {
        mutableStateOf(
            availability?.ifEmpty { "" })
    }

    var showExperienceLevel by remember { mutableStateOf(false) }
    val experienceLevel by profileViewModel.experienceLevel.collectAsState()
    val experienceOptions by profileViewModel.experienceOptions.collectAsState()
    val selectedExperience by remember {
        mutableStateOf(
            experienceLevel?.ifEmpty { "" })
    }

    val expLevel = GlobalEntries.user.professionalStatus?.userExperience ?: ""
    val availabilities = GlobalEntries.user.professionalStatus?.availability ?: ""
    val workTypes = GlobalEntries.user.professionalStatus?.workType ?: ""
    var salaryPreferred by remember {
        mutableStateOf(user.professionalStatus?.preferredSalary ?: "")
    }

    val userMedium by profileViewModel.userMedium.collectAsState()
    val userGithub by profileViewModel.userGithub.collectAsState()
    val userPortFolio by profileViewModel.userPortFolio.collectAsState()

    val isSubmitProfessionalAction by GlobalEntries.isSubmitProfessionalAction.collectAsState()

    var hybridPreference by remember {
        mutableStateOf(GlobalEntries.user.professionalStatus?.hybridPreference)
    }

    var remotePreference by remember {
        mutableStateOf(GlobalEntries.user.professionalStatus?.remotePreference)
    }

    var onSitePreference by remember {
        mutableStateOf(GlobalEntries.user.professionalStatus?.onSitePreference)
    }

    //FORM 3
    var newSkill by remember { mutableStateOf("") }
    var newCertification by remember { mutableStateOf("") }
    var skills by remember {
        mutableStateOf(GlobalEntries.user.candidateSkills?.listSkills ?: mutableListOf())
    }
    var certifications by remember {
        mutableStateOf(GlobalEntries.user.candidateSkills?.listCertification ?: mutableListOf())
    }
    var languageSkills by remember {
        mutableStateOf(GlobalEntries.user.candidateSkills?.listLanguages ?: mutableListOf())
    }

    //FORM 4
    var experiences by remember {
        mutableStateOf(user.experience ?: mutableListOf())
    }

    var selectedExperiences by remember { mutableStateOf<Experience?>(null) }
    var selectedExperienceIndex by remember { mutableStateOf(-1) }

    listExperience = user.experience ?: mutableListOf()

    val listCompanies by profileViewModel.listCompanies.collectAsState()

    val app = context.applicationContext as MyApp
    LaunchedEffect(listCompanies) {
        app.listCompanies.removeAt(app.listCompanies.lastIndex)
        if (!app.listCompanies.containsAll(listCompanies)) app.listCompanies.addAll(listCompanies.distinctBy { it })
    }

    //FORM 4
    var educations by remember {
        mutableStateOf(user.education ?: mutableListOf())
    }

    var selectedEducations by remember { mutableStateOf<Educations?>(null) }
    var selectedEducationIndex by remember { mutableStateOf(-1) }

    listEducations = user.education ?: mutableListOf()


    val listInstitutes by profileViewModel.listInstitutes.collectAsState()

    LaunchedEffect(listInstitutes) {
        app.listSchools.removeAt(app.listSchools.lastIndex)
        val institutes = listInstitutes.map {
            val school = School()
            school.libelly = it
            school
        }
        if (!app.listSchools.containsAll(institutes)) app.listSchools.addAll(institutes.distinctBy { it.libelly })
    }


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
                    0 -> {
                        val saveUserState by profileViewModel.saveUserState.collectAsState()

                        LaunchedEffect(saveUserState) {
                            if (saveUserState == "saved successfully") {
                                selectedTab += 1
                                profileViewModel.clearPersoanlInfo()
                            }
                        }

                        Box(modifier = Modifier.fillMaxSize()) {
                            Column {

                                FormTextField(
                                    value = userName,
                                    borderColor = if (activatedCheck && !submitEnabled) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {
                                        if (activatedCheck) profileViewModel.validateFullName(it)
                                        profileViewModel.changeUserName(it)
                                    },
                                    label = stringResource(id = R.string.full_name_text),
                                    isRequired = true
                                )

                                listAddress.mapIndexed { index, userAddress ->
                                    val pad = if (index == 0) 16.dp else 8.dp
                                    FormTextField(
                                        value = userAddress,
                                        modifier = Modifier.padding(top = pad),
                                        borderColor = if (activatedCheck && userAddress.isEmpty()) Color.Red else colorResource(
                                            id = R.color.whatsapp
                                        ),
                                        onValueChange = {
                                            if (activatedCheck) it.isNotEmpty()
                                            listAddress = listAddress.mapIndexed { i, value ->
                                                if (index == i) it else value
                                            }
                                        },
                                        label = stringResource(id = R.string.address_text),
                                        leadingIcon = Icons.Default.LocationOn,
                                        isRequired = true
                                    )

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "Ajouter une adresse",
                                            color = whatsAppGreen,
                                            modifier = Modifier
                                                .align(CenterEnd)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null
                                                ) {
                                                    listAddress = (listAddress + "").toMutableList()
                                                }
                                        )
                                    }
                                }

                                FormTextField(
                                    value = email ?: "",
                                    modifier = Modifier.padding(top = 16.dp),
                                    borderColor = if (activatedCheck && !emailCheck) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {
                                        email = it
                                        if (activatedCheck) profileViewModel.validateEmail(it)
                                        profileViewModel.changeAddressMail(it)
                                    },
                                    label = "Email",
                                    keyboardType = KeyboardType.Email,
                                    leadingIcon = Icons.Default.Email,
                                    isRequired = true
                                )

                                FormTextField(
                                    value = title,
                                    modifier = Modifier.padding(top = 16.dp),
                                    borderColor = if (activatedCheck && !activitySectorCheck) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {},
                                    label = stringResource(id = R.string.activity_text),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        profileViewModel.changeVisibilitySearch(true)
                                    }
                                )

                                FormTextField(
                                    value = countries,
                                    modifier = Modifier.padding(top = 16.dp),
                                    borderColor = if (activatedCheck && !countriesCheck) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {
                                    },
                                    label = stringResource(id = R.string.country_text),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        profileViewModel.changeVisibilityCountry(true)
                                    }
                                )

                                FormTextField(
                                    value = birthDateUser ?: "",
                                    modifier = Modifier.padding(top = 16.dp),
                                    borderColor = if (activatedCheck && !birthDateUserCheck) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {},
                                    leadingIcon = Icons.Filled.CalendarMonth,
                                    label = stringResource(id = R.string.birth_text),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        typeDate = "birthDay"
                                        profileViewModel.changeVisibilityDate(true)
                                    }
                                )

                                //GENDER
                                val titleGender = stringResource(id = R.string.sexe_text)
                                val titleGenderCheck by remember { derivedStateOf { selectedGender.isNotEmpty() } }

                                FormTextField(
                                    value = selectedGender,
                                    modifier = Modifier.padding(top = 16.dp),
                                    borderColor = if (activatedCheck && !titleGenderCheck) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {},
                                    leadingIcon = Icons.Filled.Person,
                                    label = stringResource(id = R.string.sexe_text),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        titleGlobal = titleGender
                                        showGender = true
                                    }
                                )

                                //Situation
                                val titleSituation = stringResource(id = R.string.situation_text)
                                val titleSituationCheck by remember { derivedStateOf { selectedSituation.isNotEmpty() } }

                                FormTextField(
                                    value = selectedSituation,
                                    modifier = Modifier.padding(top = 16.dp),
                                    borderColor = if (activatedCheck && !titleSituationCheck) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {},
                                    leadingIcon = Icons.Filled.Person,
                                    label = stringResource(id = R.string.situation_text),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        titleGlobal = titleSituation
                                        showSituation = true
                                    }
                                )

                                //Type emploi
                                val titleWork = stringResource(id = R.string.employment_type_choice_text)
                                val titleWorkCheck by remember { derivedStateOf { userEmploymentTypeChoice?.isNotEmpty() == true } }

                                FormTextField(
                                    value = userEmploymentTypeChoice ?: "",
                                    modifier = Modifier.padding(top = 16.dp),
                                    borderColor = if (activatedCheck && !titleWorkCheck) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {},
                                    leadingIcon = Icons.Filled.Person,
                                    label = stringResource(id = R.string.employment_type_choice_text),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        titleGlobal = titleWork
                                        showType = true
                                    }
                                )

                                listPhones.mapIndexed { index, phone ->
                                    val pad = if (index == 0) 16.dp else 8.dp
                                    CustomPhoneKit(
                                        modifier = Modifier.padding(top = pad),
                                        selectedCountry = selectedCountry,
                                        hint = "Numéro téléphone",
                                        defaultPhone = if (phone.contains(" ")) phone.split(" ")[1] else phone,
                                        onClick = {
                                            showCountryPicker = true
                                        },
                                        onValueChanged = {
                                            val phoneComplete = "+${selectedCountry.code} $it"

                                            listPhones = listPhones.mapIndexed { i, value ->
                                                if (index == i) it else value
                                            }
                                        }
                                    )

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "Ajouter un numéro de téléphone",
                                            color = whatsAppGreen,
                                            modifier = Modifier
                                                .align(CenterEnd)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null
                                                ) {
                                                    listPhones = (listPhones + "").toMutableList()
                                                }
                                        )
                                    }
                                }

                                // Bio
                                val bio by profileViewModel.bio.collectAsState()

                                FormTextField(
                                    value = bio,
                                    modifier = Modifier.padding(top = 16.dp),
                                    onValueChange = {
                                        profileViewModel.changeBio(it)
                                    },
                                    label = stringResource(id = R.string.professional_bio_text),
                                    placeholder = stringResource(id = R.string.tell_us_text),
                                    maxLines = 5,
                                    minLines = 3
                                )
                            }
                        }
                    }

                    1 -> {

                        val saveCandidateProfessionalState by profileViewModel.saveCandidateProfessionalState.collectAsState()
                        LaunchedEffect(saveCandidateProfessionalState) {
                            if (saveCandidateProfessionalState == "saved successfully") {
                                selectedTab += 1
                                profileViewModel.clearProfessionalInfo()
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            WorkTypePreferences(
                                remoteWork = remotePreference ?: false,
                                hybridWork = hybridPreference ?: false,
                                onSiteWork = onSitePreference ?: false,
                                onRemoteChange = {
                                    profileViewModel.changePreferenceRemote(it)
                                    remotePreference = it
                                    GlobalEntries.user.professionalStatus?.remotePreference = it
                                },
                                onHybridChange = {
                                    hybridPreference = it
                                    GlobalEntries.user.professionalStatus?.hybridPreference = it
                                    profileViewModel.changePreferenceHybrid(it)
                                },
                                onOnSiteChange = {
                                    onSitePreference = it
                                    GlobalEntries.user.professionalStatus?.onSitePreference = it
                                    profileViewModel.changePreferenceSite(it)
                                }
                            )
                            val workValidator =
                                onSitePreference == true || hybridPreference == true || remotePreference == true

                            if (activatedCheck && !workValidator) {
                                Text(
                                    text = stringResource(id = R.string.error_work_type),
                                    color = Color.Red
                                )
                            }

                            // Experience Level
                            val titleExpLevel = stringResource(id = R.string.experience_level_text)
                            FormTextField(
                                value = expLevel,
                                borderColor = if (activatedCheck && expLevel.isEmpty()) Color.Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                onValueChange = {},
                                leadingIcon = Icons.Filled.Person,
                                label = stringResource(id = R.string.experience_level_text),
                                isRequired = true,
                                readOnly = true,
                                onClick = {
                                    titleGlobal = titleExpLevel
                                    showExperienceLevel = true
                                }
                            )

                            val titleAvailability = stringResource(id = R.string.availability_text)
                            FormTextField(
                                value = availabilities,
                                borderColor = if (activatedCheck && !availabilities.isNotEmpty()) Color.Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                onValueChange = {},
                                leadingIcon = Icons.Filled.Person,
                                label = stringResource(id = R.string.availability_text),
                                isRequired = true,
                                readOnly = true,
                                onClick = {
                                    titleGlobal = titleAvailability
                                    showAvailability = true
                                }
                            )

                            val titleTypeJob = stringResource(id = R.string.employment_type_text)
                            FormTextField(
                                value = workTypes,
                                borderColor = if (activatedCheck && workTypes.isEmpty()) Color.Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                onValueChange = {},
                                leadingIcon = Icons.Filled.Business,
                                label = stringResource(id = R.string.employment_type_text),
                                isRequired = true,
                                readOnly = true,
                                onClick = {
                                    titleGlobal = titleTypeJob
                                    showWorkType = true
                                }
                            )

                            // Salary Information
                            FormTextField(
                                value = salaryPreferred,
                                borderColor = if (activatedCheck && salaryPreferred.isEmpty()) Color.Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                onValueChange = {
                                    if (activatedCheck) it.isNotEmpty()
                                    profileViewModel.changePreferredSalary(it)
                                    salaryPreferred = it
                                    GlobalEntries.user.professionalStatus?.preferredSalary = it
                                },
                                label = stringResource(id = R.string.preferred_salary_text),
                                keyboardType = KeyboardType.Number,
                                leadingIcon = Icons.Default.TrendingUp,
                                placeholder = "e.g., $100,000",
                                isRequired = true
                            )


                            FormTextField(
                                value = userMedium ?: "",
                                borderColor = colorResource(id = R.color.whatsapp),
                                onValueChange = {
                                    profileViewModel.changeMedium(it)
                                },
                                label = "Medium",
                                leadingIcon = Icons.Default.Link,
                                placeholder = "@yourusername"
                            )


                            FormTextField(
                                value = userGithub ?: "",
                                borderColor = colorResource(id = R.color.whatsapp),
                                onValueChange = {
                                    profileViewModel.changeGithub(it)
                                },
                                label = "GitHub Profile",
                                leadingIcon = Icons.Default.Code,
                                placeholder = "github.com/yourusername"
                            )


                            FormTextField(
                                value = userPortFolio ?: "",
                                borderColor = colorResource(id = R.color.whatsapp),
                                onValueChange = {
                                    profileViewModel.changePortFolio(it)
                                },
                                label = "Portfolio Website",
                                leadingIcon = Icons.Default.Web,
                                placeholder = "yourportfolio.com"
                            )
                        }
                    }

                    2 -> {

                        val saveCandidateSkillsState by profileViewModel.saveCandidateSkillsState.collectAsState()
                        LaunchedEffect(saveCandidateSkillsState) {
                            if (saveCandidateSkillsState == "saved successfully") {
                                selectedTab += 1
                                profileViewModel.clearSkillsState()
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Skills Section
                            SkillsInputSection(
                                title = stringResource(id = R.string.technical_skills_text),
                                skills = skills,
                                newSkill = newSkill,
                                onNewSkillChange = { newSkill = it },
                                onAddSkill = {
                                    //skills.add(newSkill)
                                    skills = (skills + newSkill).toMutableList()
                                    GlobalEntries.user.candidateSkills?.listSkills = skills
                                    profileViewModel.addSkills(newSkill)
                                    newSkill = ""
                                },
                                onRemoveSkill = { skill ->
                                    //skills.remove(newSkill)
                                    skills = (skills - skill).toMutableList()
                                    GlobalEntries.user.candidateSkills?.listSkills = skills
                                    profileViewModel.removeSkills(skill)
                                },
                                placeholder = "e.g., Kotlin, React, Python..."
                            )

                            // Certifications Section
                            SkillsInputSection(
                                title = "Certifications",
                                skills = certifications,
                                newSkill = newCertification,
                                onNewSkillChange = { newCertification = it },
                                onAddSkill = {
                                    certifications = (certifications + newCertification).toMutableList()
                                    GlobalEntries.user.candidateSkills?.listCertification = certifications
                                    profileViewModel.addCertification(newCertification)
                                    newCertification = ""
                                },
                                onRemoveSkill = { cert ->
                                    certifications = (certifications - cert).toMutableList()
                                    GlobalEntries.user.candidateSkills?.listCertification = certifications
                                    profileViewModel.removeCertification(cert)
                                },
                                placeholder = "e.g., AWS Certified Developer..."
                            )

                            // Languages Section
                            LanguagesSection(
                                languages = languageSkills.toMutableList(),
                                onLanguagesChange = {
                                    languageSkills = it.toMutableList()
                                    GlobalEntries.user.candidateSkills?.listLanguages = languageSkills
                                    profileViewModel.addLanguageSkills(it)
                                }
                            )
                        }
                    }

                    3 -> {

                        val saveExpState by profileViewModel.saveExpState.collectAsState()
                        LaunchedEffect(saveExpState) {
                            if (saveExpState == "saved successfully") {
                                profileViewModel.clearExpState()
                                selectedTab += 1
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            FormSectionHeader(
                                title = stringResource(id = R.string.work_experience_text),
                                subtitle = stringResource(id = R.string.pro_journey_text)
                            )

                            // Add Experience Button
                            OutlinedButton(
                                onClick = {
                                    val newExperience = Experience()
                                    experiences = (experiences + newExperience).toMutableList()
                                    profileViewModel.addNewExperience(newExperience)
                                },
                                border = BorderStroke(1.dp, colorResource(id = R.color.whatsapp)),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    tint = colorResource(id = R.color.whatsapp),
                                    contentDescription = "Add Experience"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(id = R.string.add_experience_pro_text))
                            }

                            experiences.forEachIndexed { index, item ->

                                WorkExperienceCard(
                                    index = index,
                                    experience = item,
                                    profileViewModel = profileViewModel,
                                    onExperienceTypeChange = {
                                        if (it == "Contract") {
                                            profileViewModel.changePreferenceContract(index, it)
                                        } else {
                                            profileViewModel.changePreferenceFreelance(index, it)
                                        }
                                    },
                                    onCompanyNameChange = {
                                        indexToChange = index
                                        isCompanyVisible = true
                                    },
                                    onFreelanceSalaryChange = {
                                        if (it.isNotEmpty()) {
                                            profileViewModel.changeFreelanceSalary(index, it.toInt())
                                        }
                                    },
                                    onHourPaymentChange = {
                                        profileViewModel.changePerHourWorkMethod(index, it)
                                        profileViewModel.changePerDayWorkMethod(index, false)
                                        profileViewModel.changePerProjectWorkMethod(index, false)
                                    },
                                    onDayPaymentChange = {
                                        profileViewModel.changePerHourWorkMethod(index, false)
                                        profileViewModel.changePerDayWorkMethod(index, it)
                                        profileViewModel.changePerProjectWorkMethod(index, false)
                                    },
                                    onProjectPaymentChange = {
                                        profileViewModel.changePerHourWorkMethod(index, false)
                                        profileViewModel.changePerDayWorkMethod(index, false)
                                        profileViewModel.changePerProjectWorkMethod(index, it)
                                    },
                                    onDateStartChange = {
                                        typeDate = "start"
                                        indexToChange = index
                                        profileViewModel.changeVisibilityDate(true)
                                    },
                                    onDateEndChange = {
                                        typeDate = "end"
                                        indexToChange = index
                                        profileViewModel.changeVisibilityDate(true)
                                    },
                                    onChangeCurrent = {
                                        profileViewModel.changeCurrent(index, it)
                                    },
                                    onTitleChange = {
                                        profileViewModel.changeTitleExperience(index, it)
                                    },
                                    onDescriptionChange = {
                                        profileViewModel.changeDescriptionExperience(index, it)
                                    },
                                    addTechnologiesChanged = {
                                        profileViewModel.addTechnologyExperience(index, it)
                                    },
                                    onRemove = {
                                        experiences = (experiences - experiences[index]).toMutableList()
                                        profileViewModel.removeExperience(item.id)
                                    },
                                    onSubmit = {
                                        selectedExperiences = item
                                        selectedExperienceIndex = index
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    4 -> {
                        val saveCompletedState by profileViewModel.saveCompletedState.collectAsState()
                        LaunchedEffect(saveCompletedState) {
                            if (saveCompletedState == "saved successfully") {
                                navController.navigate(Screen.UpdateDetailsScreen.route)
                                profileViewModel.clearComplete()
                            }
                        }

                        val saveEducationState by profileViewModel.saveEducationState.collectAsState()
                        LaunchedEffect(saveEducationState) {
                            if (saveEducationState == "saved successfully") {
                                profileViewModel.saveIsCompletedProfileCandidate()
                                profileViewModel.clearEducationState()
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            FormSectionHeader(
                                title = stringResource(id = R.string.education_title_text),
                                subtitle = stringResource(id = R.string.academic_text)
                            )

                            // Add Education Button
                            OutlinedButton(
                                onClick = {
                                    val education = Educations()
                                    educations = (educations + education).toMutableList()
                                    profileViewModel.addNewEducation(education)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, colorResource(id = R.color.whatsapp)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    tint = colorResource(id = R.color.whatsapp),
                                    contentDescription = "Add Education"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(id = R.string.education_add_text))
                            }

                            // Education Items
                            educations.forEachIndexed { index, education ->
                                selectedEducations = education
                                selectedEducationIndex = index
                                EducationCard(
                                    index = index,
                                    profileViewModel = profileViewModel,
                                    education = education,
                                    onChangeDegree = {
                                        indexEducationToChange = index
                                        isDegreeVisible = true
                                    },
                                    onChangeFieldOfStudy = {
                                        indexEducationToChange = index
                                        isActivitySectorVisible = true
                                    },
                                    onChangeInstitution = {
                                        indexEducationToChange = index
                                        isInstitutionVisible = true
                                    },
                                    onChangeGrade = {
                                        profileViewModel.changeGradeEducation(index, it)
                                    },
                                    onSubmit = {

                                    },
                                    onRemove = {
                                        educations = (educations - education).toMutableList()
                                        profileViewModel.removeEducation(education.id)
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        showGlobal = showAvailability || showExperienceLevel || showWorkType || showGender || showSituation || showType
        if (showAvailability) {
            globalList = availabilityOptions
            selectedItemGlobal = selectedAvailability ?: ""
            action = { name ->
                GlobalEntries.user.professionalStatus?.availability = name
                profileViewModel.changeAvailability(name)
            }
        }

        if (showGender) {
            globalList = gendersOptions
            selectedItemGlobal = selectedGender ?: ""
            action = { name -> profileViewModel.changeSex(name) }
        }

        if (showSituation) {
            globalList = situationsOptions
            selectedItemGlobal = selectedSituation
            action = { name -> profileViewModel.changeSituation(name) }
        }

        if (showType) {
            globalList = typesOptions
            selectedItemGlobal = selectedType ?: ""
            action = { name -> profileViewModel.changeEmploymentTypeChoice(name) }
        }

        if (showWorkType) {
            globalList = workTypeOptions
            selectedItemGlobal = selectedWorkType ?: ""
            action = { name ->
                GlobalEntries.user.professionalStatus?.workType = name
                profileViewModel.changeWorkType(name)
            }
        }

        if (showExperienceLevel) {
            globalList = experienceOptions
            selectedItemGlobal = selectedExperience ?: ""
            action = { name ->
                GlobalEntries.user.professionalStatus?.userExperience = name
                profileViewModel.changeExperienceLevel(name)
            }
        }

        if (showGlobal) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAvailability = false
                    showExperienceLevel = false
                    showWorkType = false
                    showGender = false
                    showSituation = false
                    showType = false
                }
            ) {
                ItemBottomSheet(
                    items = globalList,
                    title = titleGlobal,
                    selectedItem = selectedItemGlobal,
                    onItemSelected = { name ->
                        selectedItemGlobal = name
                        action(name)
                        showAvailability = false
                        showExperienceLevel = false
                        showWorkType = false
                        showGender = false
                        showSituation = false
                        showType = false
                    }
                )
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
                            0 -> {
                                val fullNameValidator = profileViewModel.validateFullName(userName)
                                val addressValidator = profileViewModel.validateAddress(listAddress)
                                val phoneValidator = profileViewModel.validatePhones(listPhones)
                                val emailValidator = profileViewModel.validateEmail(email ?: "")

                                val activitySectorValidator = title.isNotEmpty()
                                val countryValidator = countries.isNotEmpty()
                                val birthDateUserValidator = birthDateUser?.isNotEmpty() == true
                                val userGenderValidator = selectedGender.isNotEmpty()
                                val userSituationValidator = selectedSituation.isNotEmpty()
                                val userEmploymentTypeChoiceValidator =
                                    userEmploymentTypeChoice?.isNotEmpty() == true

                                if (!fullNameValidator || !addressValidator || !emailValidator
                                    || !activitySectorValidator
                                    || !countryValidator
                                    || !birthDateUserValidator
                                    || !userGenderValidator
                                    || !userSituationValidator
                                    || !userEmploymentTypeChoiceValidator
                                    || !phoneValidator
                                ) {
                                    activatedCheck = true
                                }


                                if (fullNameValidator && addressValidator && emailValidator
                                    && activitySectorValidator
                                    && countryValidator
                                    && birthDateUserValidator
                                    && userGenderValidator
                                    && userSituationValidator
                                    && userEmploymentTypeChoiceValidator
                                    && phoneValidator
                                ) {
                                    profileViewModel.changeAddress(listAddress)
                                    profileViewModel.changeCompletePhone(listPhones)
                                    profileViewModel.saveUserPersonalInfo()
                                    profileViewModel.getInitialDetail()
                                }
                            }
                            1 -> {
                                val expValidator = expLevel.isNotEmpty()
                                val availabilityValidator = availabilities.isNotEmpty()
                                val workTypesValidator = workTypes.isNotEmpty()
                                val salaryValidator = salaryPreferred.isNotEmpty()
                                val workValidator =
                                    onSitePreference == true || hybridPreference == true || remotePreference == true
                                if (!expValidator || !availabilityValidator || !workValidator || !salaryValidator || !workTypesValidator) {
                                    activatedCheck = true
                                    GlobalEntries.isSubmitProfessionalAction.update { false }
                                }

                                if (expValidator && availabilityValidator && workValidator && salaryValidator && workTypesValidator) {
                                    GlobalEntries.user.professionalStatus?.let {
                                        profileViewModel.saveUserProfessionalInfo(it)
                                        GlobalEntries.isSubmitProfessionalAction.update { false }
                                    }
                                }
                            }
                            2 -> profileViewModel.saveCandidateSkills()
                            3 -> {
                                selectedExperiences?.companyName?.let {
                                    if (it.contains(",")) {
                                        profileViewModel.changeCompanyExperience(selectedExperienceIndex, it.split(",")[1])
                                    }
                                }
                                profileViewModel.saveExperiences(user.experience ?: mutableListOf())
                            }
                            4 -> {
                                selectedEducations?.schoolName?.let {
                                    if (it.contains(",")) {
                                        profileViewModel.changeInstitution(selectedEducationIndex, it.split(",")[1])
                                    }
                                }
                                profileViewModel.saveEducations(user.education ?: mutableListOf())
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