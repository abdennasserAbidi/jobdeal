package com.example.myjob.feature.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    // Scroll state to track the scroll position of the Column
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var firstVisibleItem by remember { mutableStateOf("None") }
    var shouldAutoScroll by remember { mutableStateOf(false) }
    var autoScroll by remember { mutableStateOf(false) }
    var scrollStateColumn by remember { mutableStateOf(0) }

    var isSwiping by remember { mutableStateOf(false) } // Track if user is swiping
    var isProgrammaticScroll by remember { mutableStateOf(false) } // Track if it's programmatic scroll

    // Constants for the toolbar height
    val toolbarHeight = 200.dp
    val minHeightPx = with(LocalDensity.current) { 85.dp.toPx() } // Collapsed height
    val maxHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() } // Expanded height

    var previousScrollValue by remember { mutableStateOf(0) } // Previous scroll position to detect changes

    // LaunchedEffect to detect scroll changes and determine if it's user-driven
    LaunchedEffect(scrollState.value) {
        // Detect if the scroll change was user-driven (vs programmatic scroll)
        isSwiping = (scrollState.value != previousScrollValue) && !isProgrammaticScroll
        previousScrollValue = scrollState.value

        scrollStateColumn = if (!isSwiping) 0 else scrollState.value
    }

    LaunchedEffect(isSwiping) {
        Log.i("zzzzzz", "ProfileScreen: $isSwiping")

    }

    // Calculate toolbar height based on scroll position
    val toolbarHeightPx = max(maxHeightPx - scrollStateColumn, minHeightPx)

    val tabItem = listOf(
        TabItem(
            title = stringResource(id = R.string.tab1),
            unSelectedItem = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ), TabItem(
            title = stringResource(id = R.string.tab2),
            unSelectedItem = Icons.Outlined.ShoppingCart,
            selectedIcon = Icons.Filled.ShoppingCart
        ), TabItem(
            title = stringResource(id = R.string.tab3),
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        ), TabItem(
            title = stringResource(id = R.string.tab4),
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        ), TabItem(
            title = stringResource(id = R.string.tab5),
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        ), TabItem(
            title = stringResource(id = R.string.tab6),
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        ), TabItem(
            title = stringResource(id = R.string.tab7),
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        ), TabItem(
            title = stringResource(id = R.string.tab8),
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        )
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CustomTopAppBar(shouldAutoScroll, toolbarHeightPx, tabItem, selectedTabIndex) { index ->
                coroutineScope.launch {
                    //isSwiping = false
                    selectedTabIndex = index
                    shouldAutoScroll = true
                    autoScroll = true
                    isProgrammaticScroll = true
                }
            }
        }
    ) {
        // Scrollable Column
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(scrollState) // Enable scrolling
                .padding(16.dp)
        ) {

            SummarySection()
            CareerSection()
            EducationSection()
            LicenceSection()
            SkillSection()
            LanguageSection()
            ResumeSection()
            RoleSection(profileViewModel)
            Spacer(modifier = Modifier.height(50.dp))

            val density = LocalDensity.current

            LaunchedEffect(shouldAutoScroll) {
                if (shouldAutoScroll) {

                    val summaryHeightPx = with(density) { 150.dp.toPx() } // Height of SummarySection
                    val careerPosition = summaryHeightPx.roundToInt() // Position of CareerSection (after 1 SummarySection)

                    val careerHeightPx = with(density) { 150.dp.toPx() }
                    val educationPosition = careerPosition + careerHeightPx.roundToInt()

                    val educationHeightPx = with(density) { 150.dp.toPx() }
                    val licencePosition = educationPosition + educationHeightPx.roundToInt()

                    val licenceHeightPx = with(density) { 150.dp.toPx() }
                    val skillPosition = licencePosition + licenceHeightPx.roundToInt()

                    val skillHeightPx = with(density) { 150.dp.toPx() }
                    val langPosition = skillPosition + skillHeightPx.roundToInt()

                    val langHeightPx = with(density) { 150.dp.toPx() }
                    val resumePosition = langPosition + langHeightPx.roundToInt()

                    val resumeHeightPx = with(density) { 200.dp.toPx() }
                    val rolePosition = resumePosition + resumeHeightPx.roundToInt()

                    when (selectedTabIndex) {
                        0 -> {
                            scrollState.animateScrollTo(0)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }
                        1 -> {
                            scrollState.animateScrollTo(careerPosition)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }

                        2 -> {
                            scrollState.animateScrollTo(educationPosition)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }

                        3 -> {
                            scrollState.animateScrollTo(licencePosition)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }

                        4 -> {
                            scrollState.animateScrollTo(skillPosition)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }

                        5 -> {
                            scrollState.animateScrollTo(langPosition)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }

                        6 -> {
                            scrollState.animateScrollTo(resumePosition)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }

                        7 -> {
                            scrollState.animateScrollTo(rolePosition)
                            autoScroll = true
                            // Reset auto-scroll condition after action
                            shouldAutoScroll = false
                            isProgrammaticScroll = false // Reset flag after scrolling
                        }
                    }


                }
            }

            // Detect the first visible item after the sections
            LaunchedEffect(scrollState.value) {

                val summaryHeightPx = with(density) { 100.dp.toPx() }
                val careerHeightPx = with(density) { 150.dp.toPx() }
                val educationHeightPx = with(density) { 150.dp.toPx() }
                val licenceHeightPx = with(density) { 150.dp.toPx() }
                val skillsHeightPx = with(density) { 150.dp.toPx() }
                val langHeightPx = with(density) { 150.dp.toPx() }
                val resumeHeightPx = with(density) { 150.dp.toPx() }
                val roleHeightPx = with(density) { 200.dp.toPx() }

                // Calculate the current scroll position
                val scrollY = scrollState.value

                // Check for visibility of the sections
                when {
                    scrollY < summaryHeightPx -> {
                        firstVisibleItem = "SummarySection"
                        selectedTabIndex = 0
                    }

                    scrollY < (summaryHeightPx + careerHeightPx) -> {
                        firstVisibleItem = "CareerSection"
                        selectedTabIndex = 1
                    }

                    scrollY < (summaryHeightPx + careerHeightPx + educationHeightPx) -> {
                        firstVisibleItem = "EducationSection"
                        selectedTabIndex = 2
                    }

                    scrollY < (summaryHeightPx + careerHeightPx + educationHeightPx + licenceHeightPx) -> {
                        firstVisibleItem = "LicenceSection"
                        selectedTabIndex = 3
                    }

                    scrollY < (summaryHeightPx + careerHeightPx + educationHeightPx + licenceHeightPx + skillsHeightPx) -> {
                        firstVisibleItem = "SkillsSection"
                        selectedTabIndex = 4
                    }

                    scrollY < (summaryHeightPx + careerHeightPx + educationHeightPx + licenceHeightPx + skillsHeightPx + langHeightPx) -> {
                        firstVisibleItem = "LanguageSection"
                        selectedTabIndex = 5
                    }

                    scrollY < (summaryHeightPx + careerHeightPx + educationHeightPx + licenceHeightPx + skillsHeightPx + langHeightPx + resumeHeightPx) -> {
                        firstVisibleItem = "ResumeSection"
                        selectedTabIndex = 6
                    }

                    scrollY < (summaryHeightPx + careerHeightPx + educationHeightPx + licenceHeightPx + skillsHeightPx + langHeightPx + resumeHeightPx + roleHeightPx) -> {
                        firstVisibleItem = "RoleSection"
                        selectedTabIndex = 7
                    }

                    else -> {
                        // Determine the visible SummarySection after the three main sections
                        val indexVisibleSection =
                            ((scrollY - (summaryHeightPx + careerHeightPx + educationHeightPx)) / summaryHeightPx).roundToInt()
                        firstVisibleItem =
                            "SummarySection ${indexVisibleSection + 1}" // Adjust index as needed
                    }
                }
            }
        }
    }
}
