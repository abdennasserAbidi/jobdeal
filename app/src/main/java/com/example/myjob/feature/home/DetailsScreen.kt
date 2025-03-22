package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.profile.LicenceSection
import com.example.myjob.feature.profile.ProfileTopAppBar
import com.example.myjob.feature.profile.ResumeSection
import com.example.myjob.feature.profile.SkillSection
import com.example.myjob.feature.profile.SummarySection
import com.example.myjob.feature.profile.TabItem
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun DetailsScreen(
    navController: NavController,
    hideNavigation: () -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }

    val user = GlobalEntries.userForCompany
    Detail(navController, interactionSource, user, hideNavigation, detailViewModel)

}

@Composable
fun DetailPreview() {

}

@Composable
fun Detail(
    navController: NavController,
    interactionSource: MutableInteractionSource,
    user: User,
    hideNavigation: () -> Unit,
    detailViewModel: DetailViewModel
) {
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
            detailViewModel.getUserNameAbbreviation(user.fullName ?: "")
        }
    }

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

    val maxHeight = 200.dp
    val minHeight = 56.dp

    val scrollOffset = scrollState.value.toFloat()
    val topBarHeight = lerp(
        start = minHeight,
        stop = maxHeight,
        fraction = 1f - (scrollOffset / maxHeight.value).coerceIn(0f, 1f)
    )

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
            title = stringResource(id = R.string.tab7),
            unSelectedItem = Icons.Outlined.Settings,
            selectedIcon = Icons.Filled.Settings
        )
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var openDetails by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            topBar = {
                ProfileTopAppBar(
                    topBarHeight,
                    username,
                    userFullName,
                    shouldAutoScroll,
                    toolbarHeightPx,
                    tabItem,
                    selectedTabIndex,
                    navigate = { navController.popBackStack() },
                    changeIndex = { index ->
                        coroutineScope.launch {
                            selectedTabIndex = index
                            shouldAutoScroll = true
                            autoScroll = true
                            isProgrammaticScroll = true
                        }
                    }
                )
            },
            bottomBar = {}
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                val context = LocalContext.current
                val density = LocalDensity.current

                when(selectedTabIndex) {
                    0 -> SummarySection(showUser)
                    1 -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            experience.map { exp ->
                                //exp.showUser1()
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            openDetails = true
                                        },
                                    shape = RectangleShape,
                                    elevation = 5.dp
                                ) {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp)
                                    ) {
                                        Text(text = exp.title?:"",
                                            Modifier
                                                .padding(top = 10.dp)
                                                .padding(horizontal = 10.dp))
                                        Text(text = exp.place?:"", modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp))

                                        Text(text = exp.companyName?:"", modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp))
                                        Text(text = "${exp.salary?:0}", modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp))
                                    }

                                }

                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                    }
                    2 -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            education.map { exp ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            openDetails = true
                                        },
                                    shape = RectangleShape,
                                    elevation = 5.dp
                                ) {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp)
                                    ) {
                                        Text(text = exp.grade?:"",
                                            Modifier
                                                .padding(top = 10.dp)
                                                .padding(horizontal = 10.dp))
                                        Text(text = exp.place?:"", modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp))
                                        Text(text = exp.schoolName?:"", modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp))
                                        Text(text = exp.degree?:"", modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp))
                                        Text(text = exp.fieldStudy?:"", modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }

                    }
                    3 -> ResumeSection()
                }

                Spacer(modifier = Modifier.height(50.dp))

            }
            /*Log.i("", "Detail: $it")
            when(selectedTabIndex) {
                0 -> SummarySection()
                1 -> CareerSection(it, scrollState, experience)
                2 -> EducationSection()
                3 -> LicenceSection()
                4 -> SkillSection()
                5 -> ResumeSection()
            }*/
        }


        AnimatedVisibility(visible = openDetails,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                openDetails = false
                            },
                        tint = Color.Black,
                        contentDescription = ""
                    )

                    Text(
                        text = "title",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

            }

        }
    }
}


@Composable
@Preview
fun PreviewDetail() {
    DetailPreview()
}