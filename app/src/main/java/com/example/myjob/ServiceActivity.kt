package com.example.myjob

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertInvitation
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertInvitation
import androidx.compose.material.icons.outlined.LocalPostOffice
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.langState
import com.example.myjob.feature.demands.MarketDemandScreen
import com.example.myjob.feature.favorites.CompanyFavorites
import com.example.myjob.feature.forgotpassword.ForgotPasswordScreen
import com.example.myjob.feature.home.CandidateListScreen
import com.example.myjob.feature.home.HomeCompany
import com.example.myjob.feature.home.ModernHomeScreen
import com.example.myjob.feature.home.detail.CandidateDetailScreen
import com.example.myjob.feature.home.detail.CompanyDetailScreen
import com.example.myjob.feature.home.filter.FilterScreenUpdated
import com.example.myjob.feature.home.filter.FilteredHome
import com.example.myjob.feature.invitation.candidat.InvitationCareerScreen
import com.example.myjob.feature.invitation.candidat.InvitationScreen
import com.example.myjob.feature.invitation.company.InvitationCompanyScreen
import com.example.myjob.feature.invitation.company.SendInvitationCompany
import com.example.myjob.feature.invitation.detail.DetailInviScreen
import com.example.myjob.feature.invitation.detail.DetailInvitationScreen
import com.example.myjob.feature.login.LoginScreen
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServiceActivity : ComponentActivity() {

    lateinit var navController: NavController

    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val context = LocalContext.current
            val app = context.applicationContext as MyApp

            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
            var startRoute by rememberSaveable { mutableStateOf(Screen.SplashScreen.route) }

            var isVisibleNav by remember { mutableStateOf(true) }

            // creating our navController
            navController = rememberNavController()

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


                    composable(route = Screen.DemandMarketScreen.route) {
                        isVisibleNav = false
                        MarketDemandScreen(
                            navController = navController,
                            clearData = {
                                selectedTabIndex = 0
                            })
                    }
                }

            }
        }
    }
}