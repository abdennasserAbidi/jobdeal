package com.example.myjob

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.myjob.base.MyApp
import com.example.myjob.common.FileReader
import com.example.myjob.common.GlobalEntries.langState
import com.example.myjob.common.VoiceToTextParser
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
import com.example.myjob.feature.favorites.CandidateFavorites
import com.example.myjob.feature.favorites.CompanyFavorites
import com.example.myjob.feature.forgotpassword.ForgotPasswordScreen
import com.example.myjob.feature.home.HomeCandidate
import com.example.myjob.feature.home.HomeCompany
import com.example.myjob.feature.login.LoginScreen
import com.example.myjob.feature.login.gmail.GoogleAuthUiClient
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.onboarding.OnBoardingScreen
import com.example.myjob.feature.profile.AllCareer
import com.example.myjob.feature.profile.AllEducation
import com.example.myjob.feature.profile.CandidateProfile
import com.example.myjob.feature.profile.CareerFormScreen
import com.example.myjob.feature.profile.CompanyProfile
import com.example.myjob.feature.profile.EducationForm
import com.example.myjob.feature.profile.PersonalForm
import com.example.myjob.feature.profile.ProfileScreen
import com.example.myjob.feature.setting.SettingScreen
import com.example.myjob.feature.signup.SignUpScreen
import com.example.myjob.feature.splash.SplashScreen
import com.example.myjob.local.database.SharedPreference
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.auth.api.identity.Identity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val voiceToTextParser by lazy {
        VoiceToTextParser(application)
    }

    @Inject
    lateinit var sharedPreference: SharedPreference

    var studyField: MutableList<String> = mutableListOf()
    var allSubjects: MutableList<Subject> = mutableListOf()
    var listSchools: MutableList<String> = mutableListOf()
    var listCompany: MutableList<String> = mutableListOf()
    var listCountries: MutableList<String> = mutableListOf()

    //text recognition
    private var imageUri = mutableStateOf<Uri?>(null)
    private var textChanged = mutableStateOf("Scanned text will appear here..")

    val selectImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri.value = uri
        }

    lateinit var launcher: ActivityResultLauncher<Intent>

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null && Intent.ACTION_VIEW == intent.action) {
            val data: Uri? = intent.data
            Log.d("DeepLink", "URI: $data") // Log the URI
            // Handle deep link data here, e.g., navigate using NavController
        }
    }

    private fun generateStudyFieldList() {
        CoroutineScope(Dispatchers.Default).launch {
            langState.collect {
                val nameJson =
                    if (it == "English" || it == "Anglais") "studyfield.json" else "studyfieldfr.json"

                try {

                    val regions =
                        Gson().fromJson(loadJSONFromAsset(nameJson), AllFields::class.java)
                    val nameCities = regions.studyfields.map {
                        it.libelly ?: ""
                    }
                    studyField = nameCities.toMutableList()
                } catch (ex: java.lang.Exception) {
                    Log.i("Alabaman", "Exception: ${ex.message}")
                }
            }
        }
    }

    private fun generateCountriesList() {
        try {
            val regions =
                Gson().fromJson(loadJSONFromAsset("cities.json"), AllCities::class.java)
            val nameCities = regions.regions.map {
                it.name
            }
            listCountries = nameCities.toMutableList()
        } catch (ex: java.lang.Exception) {
            Log.i("Alabaman", "Exception: ${ex.message}")
        }
    }

    private fun generateSchoolList() {
        CoroutineScope(Dispatchers.Default).launch {
            langState.collect {
                Log.i("why", "generateStudyFieldList: $it")
                val nameJson =
                    if (it == "English" || it == "Anglais") "schoolsen.json" else "schools.json"

                try {
                    val school =
                        Gson().fromJson(loadJSONFromAsset(nameJson), AllSchools::class.java)
                    val nameSchools = school.school.map {
                        it.libelly ?: ""
                    }
                    listSchools = nameSchools.toMutableList()
                } catch (ex: java.lang.Exception) {
                    Log.i("Alabaman", "Exception: ${ex.message}")
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
            Log.i("Alabaman", "Exception: ${ex.message}")
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


    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchData()

        allSubjects = (applicationContext as MyApp).allSubjectList
        //listCountries = (applicationContext as MyApp).listNameCountries

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

            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

            // creating our navController
            val navController = rememberNavController()

            var isVisibleNav by remember { mutableStateOf(true) }

            val homeTab = TabBarItem(
                title = stringResource(id = R.string.item1),
                tag = "home_screen",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
            val alertsTab = TabBarItem(
                title = stringResource(id = R.string.item2),
                tag = "detail_screen",
                selectedIcon = Icons.Filled.Notifications,
                unselectedIcon = Icons.Outlined.Notifications,
                badgeAmount = 7
            )
            val settingsTab = TabBarItem(
                title = stringResource(id = R.string.item3),
                tag = "favorites_screen",
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder
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
                if (isVisibleNav) TabView(
                    tabBarItems,
                    defaultIndex = selectedTabIndex,
                    changeIndex = {
                        selectedTabIndex = it
                    },
                    navController = navController
                )
            }) { padding ->
                Log.i("", "onCreate: $padding")

                val cameraPermissionState: PermissionState =
                    rememberPermissionState(Manifest.permission.CAMERA)

                val role = sharedPreference.getString("role", "") ?: ""
                isVisibleNav = role == "Company" || role == "Entreprise"

                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route
                ) {

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
                                uriPattern = "http://192.168.68.209/{token}"
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
                        ForgotPasswordScreen(navController = navController, token = token)
                    }

                    /*composable(route = Screen.textRecognitionScreen.route) {
                        if (cameraPermissionState.status.isGranted) CameraScreen(navController = navController)
                        else NoPermissionScreen(cameraPermissionState::launchPermissionRequest)
                    }*/

                    composable(route = Screen.SettingScreen.route) {
                        SettingScreen(
                            navController = navController,
                            clearData = {
                                selectedTabIndex = 0
                            },
                            onResumed = { index ->
                                isVisibleNav = true
                                selectedTabIndex = index
                            }
                        )
                    }

                    composable(route = Screen.ProfileScreen.route) {
                        isVisibleNav = false
                        //ProfileScreen(navController)
                        if (role == "Company" || role == "Entreprise") CompanyProfile(navController)
                        else CandidateProfile(navController)
                    }

                    composable(route = Screen.FavoritesScreen.route) {
                        isVisibleNav = true
                        if (role == "Company" || role == "Entreprise") CompanyFavorites(
                            navController
                        )
                        else CandidateFavorites(navController)
                    }

                    composable(route = Screen.CompanyProfileScreen.route) {
                        isVisibleNav = true
                        ProfileScreen(navController)
                    }

                    /*composable(route = Screen.galleryScreen.route) {
                        isVisibleNav = true
                        GalleryScreen(
                            navController = navController,
                            selectImage = selectImage,
                            imageUri = imageUri,
                            textChanged = textChanged
                        )
                    }*/

                    composable(route = Screen.HomeScreen.route) {
                        //isVisibleNav = true

                        if (role == "Company" || role == "Entreprise") HomeCompany(navController)
                        else HomeCandidate(navController)

                    }

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
                            listCompany = listCompany
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
                }

            }
        }

        handleIntent(intent)
    }
}

// ----------------------------------------
// This is a wrapper view that allows us to easily and cleanly
// reuse this component in any future project
@Composable
fun TabView(
    tabBarItems: List<TabBarItem>, defaultIndex: Int = 0,
    changeIndex: (index: Int) -> Unit,
    navController: NavController
) {
    //var selectedTabIndex by rememberSaveable { mutableStateOf(defaultIndex) }

    NavigationBar {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = defaultIndex == index,
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

// This component helps to clean up the API call from our TabView above,
// but could just as easily be added inside the TabView without creating this custom component
@OptIn(ExperimentalMaterial3Api::class)
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

// This component helps to clean up the API call from our TabBarIconView above,
// but could just as easily be added inside the TabBarIconView without creating this custom component
@Composable
@OptIn(ExperimentalMaterial3Api::class)
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