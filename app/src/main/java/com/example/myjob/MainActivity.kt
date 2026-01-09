package com.example.myjob

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertInvitation
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertInvitation
import androidx.compose.material.icons.outlined.LocalPostOffice
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.myjob.base.MyApp
import com.example.myjob.common.FileReader
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.langState
import com.example.myjob.common.default
import com.example.myjob.common.loadJSONFromAsset
import com.example.myjob.common.phonekit.toCountryList
import com.example.myjob.domain.entities.AllCities
import com.example.myjob.domain.entities.AllCompanies
import com.example.myjob.domain.entities.AllFields
import com.example.myjob.domain.entities.AllSchools
import com.example.myjob.domain.entities.CountryPickerViewState
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.favorites.CompanyFavorites
import com.example.myjob.feature.forgotpassword.ForgotPasswordScreen
import com.example.myjob.feature.home.CandidateListScreen
import com.example.myjob.feature.home.HomeCompany
import com.example.myjob.feature.home.ModernHomeScreen
import com.example.myjob.feature.home.detail.CandidateDetailScreen
import com.example.myjob.feature.home.filter.FilterScreenUpdated
import com.example.myjob.feature.home.filter.FilteredHome
import com.example.myjob.feature.invitation.candidat.InvitationCareerScreen
import com.example.myjob.feature.invitation.candidat.InvitationScreen
import com.example.myjob.feature.invitation.company.InvitationCompanyScreen
import com.example.myjob.feature.invitation.company.SendInvitationCompany
import com.example.myjob.feature.invitation.detail.DetailInviScreen
import com.example.myjob.feature.invitation.detail.DetailInvitationScreen
import com.example.myjob.feature.login.LoginScreen
import com.example.myjob.feature.login.gmail.GoogleAuthUiClient
import com.example.myjob.feature.messagerie.DiscussionScreen
import com.example.myjob.feature.messagerie.ListMessageScreen
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.notification.NotificationScreen
import com.example.myjob.feature.onboarding.OnBoardingScreen
import com.example.myjob.feature.posts.CandidatePostScreen
import com.example.myjob.feature.posts.DetailPostScreen
import com.example.myjob.feature.posts.PostScreen
import com.example.myjob.feature.profile.AllCareer
import com.example.myjob.feature.profile.AllEducation
import com.example.myjob.feature.profile.CandidateProfile
import com.example.myjob.feature.profile.CareerFormScreen
import com.example.myjob.feature.profile.CompanyProfile
import com.example.myjob.feature.profile.CountryCodeScreen
import com.example.myjob.feature.profile.EducationForm
import com.example.myjob.feature.profile.PersonalForm
import com.example.myjob.feature.profile.ProfileScreen
import com.example.myjob.feature.profile.test.CandidateProfileFormScreen
import com.example.myjob.feature.profile.test.CompanyProfileFormScreen
import com.example.myjob.feature.profile.test.UpdateDetailScreen
import com.example.myjob.feature.setting.ModernSettingScreen
import com.example.myjob.feature.signup.SignUpScreen
import com.example.myjob.feature.splash.SplashScreen
import com.example.myjob.feature.validateprofile.InterviewValidationScreen
import com.example.myjob.feature.validateprofile.ValidateDocScreen
import com.example.myjob.feature.validateprofile.ValidateProfileCandidate
import com.example.myjob.feature.validateprofile.ValidateProfileCompany
import com.example.myjob.local.database.SharedPreference
import com.google.android.gms.auth.api.identity.Identity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URISyntaxException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreference: SharedPreference

    private var studyField: MutableList<String> = mutableListOf()
    private var allSubjects: MutableList<Subject> = mutableListOf()
    private var listSchools: MutableList<String> = mutableListOf()
    private var listCompany: MutableList<String> = mutableListOf()
    private var listCountries: MutableList<String> = mutableListOf()

    //TODO("send message get notification")
    //TODO("notification check")
    //TODO("search with experience in home company")
    //TODO("validation account review")
    //TODO("upload images")

    var mSocket: Socket? = null
    private var imageUri = mutableStateOf<Uri?>(null)
    private var textChanged = mutableStateOf("Scanned text will appear here..")


    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun generateStudyFieldList() {
        CoroutineScope(Dispatchers.Default).launch {
            langState.collect {
                val nameJson =
                    if (it == "English" || it == "Anglais") "studyfield.json" else "studyfieldfr.json"

                try {
                    val allFields =
                        Gson().fromJson(loadJSONFromAsset(nameJson), AllFields::class.java)
                    val studyFields = allFields.studyfields.map { field ->
                        field.libelly ?: ""
                    }
                    studyField = studyFields.toMutableList()
                } catch (ex: java.lang.Exception) {
                    Log.i("Error", "Exception: ${ex.message}")
                }
            }
        }
    }

    private fun generateCountriesList() {
        try {
            val regions =
                Gson().fromJson(loadJSONFromAsset("cities.json"), AllCities::class.java)
            val nameCities = regions.regions.map { it.name }
            listCountries = nameCities.toMutableList()
        } catch (ex: java.lang.Exception) {
            Log.i("Error", "Exception: ${ex.message}")
        }
    }

    private fun generateSchoolList() {
        CoroutineScope(Dispatchers.Default).launch {
            langState.collect {
                val nameJson =
                    if (it == "English" || it == "Anglais") "schoolsen.json" else "schools.json"

                try {
                    val school =
                        Gson().fromJson(loadJSONFromAsset(nameJson), AllSchools::class.java)
                    val nameSchools = school.school.map { schools ->
                        schools.libelly ?: ""
                    }
                    listSchools = nameSchools.toMutableList()
                } catch (ex: java.lang.Exception) {
                    Log.i("Error", "Exception: ${ex.message}")
                }
            }
        }
    }

    private fun generateCompanyList() {
        try {
            val companies =
                Gson().fromJson(loadJSONFromAsset("companies.json"), AllCompanies::class.java)
            val nameCompanies = companies.companies.map {
                it.libelly ?: ""
            }
            listCompany = nameCompanies.toMutableList()
        } catch (ex: java.lang.Exception) {
            Log.i("Error", "Exception: ${ex.message}")
        }
    }

    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(supervisorJob + Dispatchers.Main)
    private val viewState: MutableStateFlow<CountryPickerViewState> = MutableStateFlow(
        CountryPickerViewState(emptyList())
    )
    private var listCountry = emptyList<NewCountry>()

    private fun fetchData() = scope.launch {
        val countries = default {
            FileReader.readAssetFile(this@MainActivity, "countries.json").toCountryList()
        }
        listCountry = countries

        viewState.value = CountryPickerViewState(countries)
    }

    suspend fun repeatEvery(duration: Long, block: suspend () -> Unit) {
        while (true) {
            block()
            delay(duration)
        }
    }

    private val newIntentListeners = mutableListOf<(Intent) -> Unit>()

    lateinit var navController: NavController

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        for (listener in newIntentListeners) {
            listener.invoke(intent)
        }

        Log.i("DeepLink", "onNewIntent: ${intent.extras}")

        if (::navController.isInitialized) {

            val data: Uri? = intent.data

            data?.let {
                val token = data.getQueryParameter("token") ?: ""
                if (token.isNotEmpty()) GlobalEntries.tokenForgetPassword = token
                Log.i("DeepLink", "main activity: $token")
                navController.navigate(Screen.ForgotPasswordScreen.route)
            }

            val idInvitation = intent.extras?.getString("idInvitation")
            val idAnnounce = intent.extras?.getString("idAnnounce")
            val idCompany = intent.extras?.getString("idCompany")

            idInvitation?.let {
                Log.i("DeepLink", "Navigating to: $it")
                GlobalEntries.idInvitation = it.toInt()
                navController.navigate(Screen.NormalDetailInvitationScreen.route)
            }

            idAnnounce?.let {
                Log.i("DeepLink", "Navigating to: $it")
                GlobalEntries.idAnnounce = it.toInt()
                GlobalEntries.idCompany = idCompany?.toInt() ?: 0
                navController.navigate(Screen.DetailPostScreen.route)
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        //setupSocket()

        fetchData()

        lifecycleScope.launch(Dispatchers.IO) {
            val fiveMinutesInMillis: Long = 2 * 60 * 1000L
            repeatEvery(fiveMinutesInMillis) {
                GlobalEntries.isRefreshing.update { true }
            }
        }

        allSubjects = (applicationContext as MyApp).allSubjectList

        langState.update {
            sharedPreference.getString("lang", "") ?: ""
        }

        CoroutineScope(Dispatchers.Default).launch {
            generateCountriesList()
        }

        CoroutineScope(Dispatchers.Default).launch {
            generateStudyFieldList()
        }

        CoroutineScope(Dispatchers.Default).launch {
            generateCompanyList()
        }

        CoroutineScope(Dispatchers.Default).launch {
            generateSchoolList()
        }

        setContent {

            val context = LocalContext.current
            val app = context.applicationContext as MyApp

            app.listCompanies.addAll(listCompany)

            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
            var startRoute by rememberSaveable { mutableStateOf(Screen.SplashScreen.route) }

            // creating our navController
            navController = rememberNavController()

            if (::navController.isInitialized) {
                val idInvitation = intent?.getStringExtra("idInvitation")
                val idAnnounce = intent?.getStringExtra("idAnnounce")
                val idCompany = intent?.getStringExtra("idCompany")

                idInvitation?.let {
                    GlobalEntries.isFromNotification = true
                    GlobalEntries.idInvitation = it.toInt()
                    startRoute = Screen.NormalDetailInvitationScreen.route
                }

                idAnnounce?.let {
                    GlobalEntries.isFromNotification = true
                    GlobalEntries.idAnnounce = it.toInt()
                    GlobalEntries.idCompany = idCompany?.toInt() ?: 0
                    startRoute = Screen.DetailPostScreen.route
                }
            }

            var isVisibleNav by remember { mutableStateOf(true) }

            val homeTab = TabBarItem(
                title = stringResource(id = R.string.item1),
                tag = "home_screen",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )

            val alertsTab = TabBarItem(
                title = stringResource(id = R.string.item2),
                tag = "company_invitation_screen",
                selectedIcon = Icons.Filled.InsertInvitation,
                unselectedIcon = Icons.Outlined.InsertInvitation
            )

            val settingsTab = TabBarItem(
                title = stringResource(id = R.string.item7),
                tag = "post_screen",
                selectedIcon = Icons.Filled.LocalPostOffice,
                unselectedIcon = Icons.Outlined.LocalPostOffice
            )
            val moreTab = TabBarItem(
                title = stringResource(id = R.string.item4),
                tag = "settings_screen",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings
            )

            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, alertsTab, settingsTab, moreTab)

            Scaffold(bottomBar = {

                AnimatedVisibility(
                    visible = isVisibleNav,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    TabView(
                        tabBarItems,
                        defaultIndex = selectedTabIndex,
                        changeIndex = {
                            selectedTabIndex = it
                        },
                        navController = navController
                    )
                }
            }) { padding ->
                Log.i("", "onCreate: $padding")

                val role = GlobalEntries.role
                isVisibleNav = role == "Company" || role == "Entreprise"

                NavHost(
                    navController = navController as NavHostController,
                    startDestination = startRoute
                ) {

                    //SUBSCRIPTIONS
                    composable(route = Screen.SplashScreen.route) {

                        isVisibleNav = false

                        SplashScreen(navController = navController)
                    }

                    composable(route = Screen.OnBoardingScreen.route) {

                        isVisibleNav = false

                        OnBoardingScreen(navController = navController)
                    }

                    composable(route = Screen.LoginScreen.route) {

                        isVisibleNav = false

                        LoginScreen(
                            navController = navController,
                            lifecycleScope = lifecycleScope,
                            googleAuthUiClient = googleAuthUiClient
                        ) {
                            navController.navigate(Screen.galleryScreen.route)
                        }
                    }

                    composable(route = Screen.SignupScreen.route) {
                        isVisibleNav = false
                        SignUpScreen(
                            navController = navController,
                            lifecycleScope = lifecycleScope,
                            googleAuthUiClient = googleAuthUiClient
                        )

                    }
                    //1f358f2d-f78b-43c1-a20d-543a2ea0ae5b
                    composable(
                        route = Screen.ForgotPasswordScreen.route,
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "https://jobdeal?token={token}"
                                action = Intent.ACTION_VIEW
                            }
                        ),
                        arguments = listOf(
                            navArgument("token") {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )
                    ) { entry ->
                        isVisibleNav = false
                        val token = entry.arguments?.getString("token") ?: ""
                        if (token.isNotEmpty()) GlobalEntries.tokenForgetPassword = token
                        Log.i("DeepLink", "main activity  2: $token")
                        ForgotPasswordScreen(navController = navController)
                    }
                    //END SUBSCRIPTION


                    //INVITATION
                    composable(route = Screen.InvitationCompanyScreen.route) {
                        isVisibleNav = true
                        InvitationCompanyScreen(
                            navController = navController,
                            changeIndexTab = {
                                selectedTabIndex = 1
                            }
                        )
                    }

                    composable(
                        route = "${Screen.DetailInvitationScreen.route}/{idInvitation}",
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "https://jobdeal/{idInvitation}"
                                action = Intent.ACTION_VIEW
                            }
                        ),
                        arguments = listOf(navArgument("idInvitation") {
                            type = NavType.StringType
                        })
                    ) {
                        isVisibleNav = false
                        val arguments = it.arguments
                        arguments?.getString("idInvitation")?.let { idInvitation ->
                            DetailInvitationScreen(
                                navController = navController,
                                idInvitation = idInvitation
                            )
                        }
                    }

                    composable(
                        route = Screen.NormalDetailInvitationScreen.route
                    ) {
                        isVisibleNav = false
                        DetailInviScreen(navController = navController)
                    }

                    composable(route = Screen.SendInvitationScreen.route) {
                        isVisibleNav = false
                        //SendInvitationScreen(navController)
                        SendInvitationCompany(navController)
                    }

                    composable(route = Screen.InvitationScreen.route) {
                        if (role == "Candidate" || role == "Candidat") isVisibleNav = false
                        InvitationScreen(navController = navController)
                    }

                    composable(route = Screen.InvitationBoostScreen.route) {
                        if (role == "Candidate" || role == "Candidat") isVisibleNav = false
                        InvitationCareerScreen(navController = navController)
                    }
                    //END INVITATION



                    /*composable(route = Screen.textRecognitionScreen.route) {
                        if (cameraPermissionState.status.isGranted) CameraScreen(navController = navController)
                        else NoPermissionScreen(cameraPermissionState::launchPermissionRequest)
                    }*/


                    //MESSAGERIE
                    composable(
                        route = Screen.SendMessageScreen.route,
                    ) {
                        isVisibleNav = false
                        DiscussionScreen(navController, hideNavigation = {
                            isVisibleNav = false
                        })
                    }

                    composable(
                        route = Screen.ListMessagesScreen.route,
                    ) {
                        isVisibleNav = false
                        ListMessageScreen(navController, hideNavigation = {
                            isVisibleNav = false
                        })
                    }

                    composable(route = Screen.NotificationCompanyScreen.route) {
                        isVisibleNav = !(role == "Candidate" || role == "Candidat")
                        NotificationScreen(navController)
                    }

                    composable(
                        route = Screen.DetailScreen.route,
                    ) {
                        isVisibleNav = false

                        CandidateDetailScreen(navController, hideNavigation = {
                            isVisibleNav = false
                        })
                    }
                    //END MESSAGERIE

                    //VALIDATION
                    composable(route = Screen.ValidateDocCandidateScreen.route) {
                        isVisibleNav = false
                        ValidateDocScreen(
                            navController = navController
                        )
                    }

                    composable(route = Screen.ValidateProfileCandidateScreen.route) {
                        isVisibleNav = false
                        ValidateProfileCandidate(navController)
                    }

                    composable(route = Screen.ValidateProfileCompanyScreen.route) {
                        isVisibleNav = false
                        ValidateProfileCompany(navController)
                    }

                    composable(route = Screen.ValidationInterviewScreen.route) {
                        isVisibleNav = true
                        InterviewValidationScreen(navController)
                    }
                    //END VALIDATION



                    //HOME
                    composable(route = Screen.FilterScreen.route) {
                        isVisibleNav = false
                        FilterScreenUpdated(
                            navController,
                            allSubjects = allSubjects,
                            listSchools = listSchools,
                            listCountries = listCountries,
                            listCompany = app.listCompanies
                        )
                    }

                    composable(route = Screen.HomeScreen.route) {

                        if (GlobalEntries.role == "Company" || GlobalEntries.role == "Entreprise") {

                            CoroutineScope(Dispatchers.Main).launch {
                                GlobalEntries.isVisibleNav.collect {
                                    isVisibleNav = it
                                }
                            }
                            CandidateListScreen(
                                navController = navController,
                                allSubjects = allSubjects,
                                listSchools = listSchools,
                                listCountries = listCountries,
                                listCompany = app.listCompanies,
                                clearData = {
                                    selectedTabIndex = 0
                                },
                                changeIndexTab = {
                                    selectedTabIndex = 0
                                }
                            )
                        } else {
                            ModernHomeScreen(
                                navController = navController,
                                clearData = {
                                    selectedTabIndex = 0
                                }
                            )
                        }
                    }

                    composable(route = Screen.FilteredHome.route) {
                        isVisibleNav = false
                        FilteredHome(navController)
                    }

                    composable(route = Screen.PostScreen.route) {
                        CoroutineScope(Dispatchers.Main).launch {
                            GlobalEntries.isVisibleNav.collect {
                                isVisibleNav = it
                            }
                        }

                        PostScreen(navController = navController)
                    }

                    composable(route = Screen.CandidatePostScreen.route) {
                        isVisibleNav = false
                        CandidatePostScreen(navController = navController)
                    }

                    composable(route = Screen.DetailPostScreen.route) {
                        isVisibleNav = false
                        DetailPostScreen(navController = navController)
                    }

                    composable(route = Screen.SettingScreen.route) {

                        CoroutineScope(Dispatchers.Main).launch {
                            GlobalEntries.isVisibleNav.collect {
                                isVisibleNav = if (role == "Candidate" || role == "Candidat") false
                                else it
                            }
                        }

                        ModernSettingScreen(
                            navController = navController,
                            clearData = {
                                selectedTabIndex = 0
                            },
                            onResumed = { index ->
                                if (role == "Candidate" || role == "Candidat") isVisibleNav = false
                                selectedTabIndex = index
                            }
                        )

                        /*SettingScreen(
                            navController = navController,
                            clearData = {
                                selectedTabIndex = 0
                            },
                            onResumed = { index ->
                                if (role == "Candidate" || role == "Candidat") isVisibleNav = false
                                selectedTabIndex = index
                            }
                        )*/
                    }

                    composable(route = Screen.HomeCompanyScreen.route) {

                        HomeCompany(navController = navController,
                            allSubjects = allSubjects,
                            listSchools = listSchools,
                            listCountries = listCountries,
                            listCompany = app.listCompanies,
                            onResumed = { index ->
                                selectedTabIndex = index
                            })
                    }
                    //END HOME



                    //PROFILE
                    composable(route = Screen.CareerScreen.route) {
                        isVisibleNav = false
                        AllCareer(navController = navController)
                    }

                    composable(route = Screen.CareerFormScreen.route) {
                        isVisibleNav = false
                        CareerFormScreen(
                            navController = navController,
                            allSubjects = allSubjects,
                            listCountries = listCountries,
                            listCompany = app.listCompanies
                        )
                    }

                    composable(route = Screen.PersonalFormScreen.route) {
                        isVisibleNav = false
                        PersonalForm(
                            navController = navController,
                            list = listCountry,
                            allSubjects = allSubjects
                        )
                    }

                    composable(route = Screen.CountryCodeScreen.route) {
                        CountryCodeScreen(navController = navController)
                    }

                    composable(route = Screen.EducationScreen.route) {
                        isVisibleNav = false
                        AllEducation(navController = navController)
                    }

                    composable(route = Screen.EducationFormScreen.route) {
                        isVisibleNav = false
                        EducationForm(
                            navController = navController,
                            listStudyField = studyField,
                            listSchools = listSchools,
                            listGrade = listCountries
                        )
                    }

                    composable(route = Screen.ProfileScreen.route) {
                        isVisibleNav = false
                        if (GlobalEntries.role == "Company" || GlobalEntries.role == "Entreprise") CompanyProfile(
                            navController
                        )
                        else CandidateProfile(navController)
                    }

                    composable(route = Screen.FavoritesScreen.route) {
                        isVisibleNav = true
                        CompanyFavorites(navController)
                    }

                    composable(route = Screen.CompanyProfileScreen.route) {
                        isVisibleNav = true
                        ProfileScreen(navController)
                    }

                    composable(route = Screen.UpdateDetailsScreen.route) {
                        isVisibleNav = false
                        UpdateDetailScreen(navController)
                    }

                    //new
                    composable(route = Screen.SearchWordScreen.route) {
                        isVisibleNav = false
                        //SearchScreen(navController)
                        //CandidateCompleteProfileApp()
                        app.listCompanies.add(stringResource(id = R.string.other_text))
                        listCompany = app.listCompanies.distinctBy { it }.toMutableList()

                        CandidateProfileFormScreen(
                            navController = navController,
                            list = listCountry,
                            clearData = {
                                selectedTabIndex = 0
                            },
                            allSubjects = allSubjects,
                            listStudyField = studyField,
                            listSchools = listSchools,
                            listGrade = listCountries,
                            listCompany = app.listCompanies
                        )
                    }

                    composable(route = Screen.CompanyProfileForm.route) {
                        isVisibleNav = false

                        CompanyProfileFormScreen(
                            navController = navController,
                            clearData = {
                                selectedTabIndex = 0
                            }
                        )
                    }
                    //END PROFILE
                }

            }
        }
    }

    val onConnect = Emitter.Listener {
        runOnUiThread {
            Log.d("Socket.IO", "Connected to server")
            mSocket?.emit("message", "Hello from Android!")
        }
    }

    val onNewMessage = Emitter.Listener {
        runOnUiThread {
            val message = it.get(0) as String
            Log.d("Socket.IO", "New message from server: $message")
            // Update UI with the received message
        }
    }

    val onDisconnect = Emitter.Listener {
        runOnUiThread {
            Log.d("Socket.IO", "Disconnected from server")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
        mSocket?.off(Socket.EVENT_CONNECT, onConnect)
        mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.off("message", onNewMessage)
    }

    private fun setupSocket() {
        try {
            mSocket =
                IO.socket("http://YOUR_SERVER_IP:9092")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.on("message", onNewMessage)


        mSocket?.connect()
    }
}

@Composable
fun TabView(
    tabBarItems: List<TabBarItem>, defaultIndex: Int = 0,
    changeIndex: (index: Int) -> Unit,
    navController: NavController
) {

    NavigationBar(
        modifier = Modifier.border(1.dp, Color.LightGray),
        containerColor = Color.White
    ) {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = defaultIndex == index,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    indicatorColor = colorResource(id = R.color.lighter_gray)
                ),
                onClick = {
                    //selectedTabIndex = index
                    changeIndex(index)
                    navController.navigate(tabBarItem.tag)
                },
                icon = {
                    TabBarIconView(
                        isSelected = defaultIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = { Text(tabBarItem.title) })
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) {
                selectedIcon
            } else {
                unselectedIcon
            },
            contentDescription = title
        )
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}


data class TabBarItem(
    val title: String,
    val tag: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)