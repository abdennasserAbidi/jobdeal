package com.example.myjob.feature.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GenericMultipleSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.common.GlobalEntries.otherUserId
import com.example.myjob.common.GlobalEntries.otherUserName
import com.example.myjob.common.GlobalEntries.preferredRole
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.common.view.CandidateCard
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.feature.demands.findActivity
import com.example.myjob.feature.home.filter.flowHandling
import com.example.myjob.feature.invitation.company.EnProcessForm
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun CandidateListScreen(
    navController: NavController,
    listCountries: MutableList<String>,
    clearData: () -> Unit = {},
    changeIndexTab: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedFilter by remember { mutableStateOf(FilterType.ALL) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var pickOption by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val fcmToken by homeViewModel.fcmToken.collectAsState()
    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            changeIndexTab()
            homeViewModel.getCurrent()
            homeViewModel.getUserToken()
        }
    }

    LaunchedEffect(fcmToken) {
        if (fcmToken.isEmpty()) {
            homeViewModel.updateToken()
        }
    }

    val isRefreshing by GlobalEntries.isRefreshing.collectAsState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            homeViewModel.getCurrent()
            GlobalEntries.isRefreshing.update { false }
        }
    }

    var filterOpen by remember { mutableStateOf(false) }
    var itemRes by remember { mutableIntStateOf(R.string.item1) }
    var indexParent by remember { mutableIntStateOf(-1) }
    var titleParent by remember { mutableStateOf("") }
    var isSelectedParent by remember { mutableStateOf(false) }

    var showTypeSheet by remember { mutableStateOf(false) }
    var selectedSearch by remember(homeViewModel.getType()) { mutableStateOf(homeViewModel.getType()) }

    val listCompany by homeViewModel.listCompany.collectAsState()
    val listInstitutes by homeViewModel.listInstitutes.collectAsState()
    val allSubjects by homeViewModel.listActivities.collectAsState()
    val listFields by homeViewModel.listFields.collectAsState()


    val listChoiceParentSelect by homeViewModel.listChoiceParentSelect.collectAsState()
    val listChoiceParentSelected by homeViewModel.listChoiceParentSelected.collectAsState()

    val choiceParentSelect by homeViewModel.choiceParentSelect.collectAsState()
    val categories by homeViewModel.parentChoices.collectAsState()
    val selectedCat by homeViewModel.selectedParentChoices.collectAsState()

    val selectedCategories by homeViewModel.selectedCat.collectAsState()

    val selectedStatus by homeViewModel.selectedStatus.collectAsState()
    val selectedStatusChoice by homeViewModel.selectedStatusChoice.collectAsState()


    val selectedRole by homeViewModel.selectedCategory.collectAsState()
    val selectedAvailability by homeViewModel.selectedAvailability.collectAsState()
    val selectedExp by homeViewModel.selectedExp.collectAsState()
    val selectedType by homeViewModel.selectedType.collectAsState()
    val selectedSituation by homeViewModel.selectedSituation.collectAsState()
    val selectedSex by homeViewModel.selectedSex.collectAsState()

    val criteria by homeViewModel.criteria.collectAsState()

    val badgeCountNormal by remember { mutableIntStateOf(0) }
    val badgeCountService by remember { mutableIntStateOf(1) }

    val scope = rememberCoroutineScope()

    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx().toInt()
    }

    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp.toPx().toInt()
    }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    val contractText = stringResource(id = R.string.type1_text)
    val freelanceText = stringResource(id = R.string.type2_text)

    val parentChoices = selectedRole.findLast { it.isSelected }?.title ?: R.string.holding
    preferredRole = stringResource(id = parentChoices)

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            GlobalEntries.isVisibleNav.update { true }

            homeViewModel.addToList(contractText, freelanceText)
            if (GlobalEntries.isFromFilter) {
                homeViewModel.validateFilter(GlobalEntries.criteriaModel)
                GlobalEntries.isFromFilter = false
            } else {
                homeViewModel.getAllUser()
            }
        }
    }

    val invitation by homeViewModel.invitation.collectAsState()
    val lazyPagingItems = homeViewModel.user.collectAsLazyPagingItems()

    val refreshing = lazyPagingItems.loadState.refresh is LoadState.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { lazyPagingItems.refresh() }
    )

    var openFinishProcess by remember { mutableStateOf(false) }
    var invitationModel by remember { mutableStateOf(InvitationModel()) }

    if (openFinishProcess) {
        EnProcessForm(
            invitationModel,
            onDismissRequest = {
                openFinishProcess = false
            },
            onConfirmation = {
                homeViewModel.finishProcess(it)
                openFinishProcess = false
            })
    }

    val context = LocalContext.current
    BackHandler(enabled = true) {
        context.findActivity()?.finish()
    }

    val demandText = stringResource(id = R.string.demand_text)
    val normalText = stringResource(id = R.string.normal_text)
    val logoutText = stringResource(id = R.string.logout_text)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.whatsapp)
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.candidates_text),
                        fontSize = 24.sp,
                        color = White,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                showTypeSheet = true
                            }
                            .clip(RoundedCornerShape(30.dp))
                            .background(White.copy(alpha = 0.2f))
                    ) {

                        Spacer(Modifier
                            .align(Alignment.TopCenter)
                            .height(20.dp)
                            .fillMaxWidth())

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterStart)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    navController.navigate(Screen.NotificationCompanyScreen.route)
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = White,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterEnd)
                            )
                        }

                        val badgeCount = if (selectedSearch == JobType.NORMAL) badgeCountNormal
                        else badgeCountService

                        if (badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 5.dp, start = 5.dp)
                                    .align(Alignment.TopStart)
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White
                                )
                            }
                        }

                        Text(
                            text = when (selectedSearch) {
                                JobType.NORMAL -> normalText
                                JobType.GET -> demandText
                                JobType.LOGOUT -> logoutText
                            },
                            color = White,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "ArrowDropDown",
                            tint = White,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 10.dp)
                                .align(Alignment.CenterEnd)
                        )

                        Spacer(Modifier
                            .align(Alignment.BottomCenter)
                            .height(20.dp)
                            .fillMaxWidth())
                    }


                }
            }

            // Search and Filter Section
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        homeViewModel.filterUser(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("${stringResource(id = R.string.search_candidates_text)}...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.whatsapp),
                        focusedLabelColor = colorResource(id = R.color.whatsapp)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    itemsIndexed(
                        items = categories
                    ) { index, item ->

                        val title = stringResource(id = item.title)

                        val textColor = if (selectedCat[index]) White else Color.Black
                        val color =
                            colorResource(id = if (selectedCat[index]) R.color.whatsapp else R.color.lighter_gray)

                        val paddStart = if (index == 0) 0.dp else 10.dp

                        Row(
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(start = paddStart)
                                .border(
                                    width = 1.dp,
                                    color = color,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .background(
                                    color = color,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    showFilterSheet = true
                                    GlobalEntries.isVisibleNav.update { false }
                                    itemRes = item.title
                                    indexParent = index
                                    titleParent = title
                                    isSelectedParent = !selectedCat[index]
                                    homeViewModel.changeOption(title, item.title)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = title,
                                color = textColor,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (lazyPagingItems.itemCount > 0) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lazyPagingItems.itemCount) { index ->
                        val user = lazyPagingItems[index] ?: User()

                        val exp = user.experience ?: emptyList()

                        val lastExperience = if (exp.isNotEmpty()) exp[exp.lastIndex]
                        else Experience()

                        val candidate = Candidate(
                            name = user.fullName?.trimStart() ?: "",
                            position = user.preferredActivitySector ?: "",
                            company = lastExperience.companyName ?: "",
                            experience = "5+ years",
                            location = lastExperience.place ?: "",
                            salary = "${lastExperience.salary ?: 0} DT",
                            skills = emptyList()
                        )

                        CandidateCard(
                            user = user,
                            candidate = candidate,
                            onClick = {
                                GlobalEntries.userForCompany = user
                                GlobalEntries.isFromDemand = false
                                val route =
                                    if (user.role == "Candidat" || user.role == "Candidate") Screen.DetailScreen.route
                                    else Screen.DetailCompanyScreen.route
                                navController.navigate(route)
                            },
                            onSendInvitation = {
                                candidateUser = user
                                navController.navigate(Screen.SendInvitationScreen.route)
                            },
                            onSendMessage = {
                                candidateUser = it
                                otherUserId = it.id ?: -1
                                otherUserName =
                                    if (it.role == "Candidate" || it.role == "Candidat") it.fullName ?: ""
                                    else it.companyName ?: ""

                                navController.navigate(Screen.SendMessageScreen.route)
                            },
                            onTerminateInvitation = {
                                invitationModel = it
                                openFinishProcess = true
                            }
                        )

                        if (index >= lazyPagingItems.itemCount) {
                            Spacer(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth()
                        )
                    }
                    lazyPagingItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                            }

                            loadState.refresh is LoadState.Error -> {
                                //val error = lazyPagingItems.loadState.refresh as LoadState.Error
                                val error = (loadState.refresh as? LoadState.Error)?.error

                                /*item {
                                    ErrorMessage(
                                        modifier = Modifier.fillParentMaxSize(),
                                        message = error?.localizedMessage ?: "",
                                        onClickRetry = { retry() })
                                }*/
                            }

                            loadState.append is LoadState.Loading -> {
                                item { LoadingNextPageItem(modifier = Modifier) }
                            }

                            loadState.append is LoadState.Error -> {
                                //val error = lazyPagingItems.loadState.append as LoadState.Error
                                val error = (loadState.append as? LoadState.Error)?.error

                                /*item {
                                    ErrorMessage(
                                        modifier = Modifier,
                                        message = error?.localizedMessage?:"",
                                        onClickRetry = { retry() })
                                }*/
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "There is no data",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        AnimatedVisibility(
            visible = filterOpen,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {

            listChoiceParentSelect?.let {
                val height = screenHeight / 2
                val heightDp = with(density) { height.toDp() }

                androidx.compose.material.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightDp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 15.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                modifier = Modifier
                                    .height(2.dp)
                                    .width(70.dp)
                                    .padding(top = 20.dp)
                                    .background(Color.Red, RoundedCornerShape(20.dp)),
                                text = ""
                            )

                            Text(
                                text = choiceParentSelect,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                thickness = 1.dp
                            )

                            val l = when (itemRes) {
                                R.string.status_type_text -> selectedStatusChoice
                                R.string.categories_text -> selectedCategories
                                R.string.experience_text -> selectedExp
                                R.string.disponibility_text -> selectedAvailability
                                R.string.employment_type_text -> selectedType
                                R.string.situation_text -> selectedSituation
                                R.string.sexe_text -> selectedSex
                                else -> selectedAvailability
                            }

                            isSelectedParent = l.none { it }

                            flowHandling(it, l) { index, title, isSelected ->
                                homeViewModel.changeUnKnown(itemRes, index, title, isSelected)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .fillMaxWidth(0.8f)
                                .align(Alignment.BottomCenter)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    filterOpen = false
                                    GlobalEntries.isVisibleNav.update { true }
                                    homeViewModel.validateFilter(criteria)
                                    homeViewModel.changeSelectionParentChoices(
                                        indexParent,
                                        titleParent,
                                        !isSelectedParent
                                    )
                                    filterOpen = false
                                }
                                .background(
                                    color = colorResource(id = R.color.whatsapp),
                                    RoundedCornerShape(30.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.show_result_text),
                                color = White,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                        }
                    }
                }
            } ?: run {

                Box(modifier = Modifier.fillMaxSize()) {

                    val list = allSubjects.map {
                        it.name
                    }

                    val listFilter = when (itemRes) {
                        R.string.institution_text -> listInstitutes.map { it.name }
                        R.string.location_text -> listCountries
                        R.string.company_name_text -> listCompany.map { it.name }
                        R.string.activity_text -> list
                        else -> emptyList()
                    }

                    val savedList = when (itemRes) {
                        R.string.institution_text -> criteria.institutions
                        R.string.location_text -> criteria.location
                        R.string.company_name_text -> criteria.companies
                        R.string.activity_text -> criteria.preferredActivitySector
                        else -> emptyList()
                    }

                    LaunchedEffect(pickOption) {
                        if (pickOption) {
                            homeViewModel.validateFilter(criteria)
                            pickOption = false
                        }
                    }

                    GenericMultipleSearch(
                        mListOfJobs = listFilter,
                        savedList = savedList,
                        onDismissRequest = {
                            filterOpen = false
                            showFilterSheet = false
                            GlobalEntries.isVisibleNav.update { true }
                        },
                        onSelectedBank = { list ->
                            pickOption = true
                            filterOpen = false
                            showFilterSheet = false
                            GlobalEntries.isVisibleNav.update { true }
                            //

                            when (itemRes) {
                                R.string.institution_text -> homeViewModel.changeInstitutions(list)
                                R.string.location_text -> homeViewModel.changeLocation(list)
                                R.string.company_name_text -> homeViewModel.changeCompanies(list)
                                R.string.activity_text -> homeViewModel.changeActivitySector(list)
                            }
                        },
                        title = choiceParentSelect
                    )
                }
            }
        }

    }

    if (showTypeSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTypeSheet = false }
        ) {
            FilterTypeBottomSheet(
                selectedFilter = selectedSearch,
                onFilterSelected = { filter ->
                    selectedSearch = filter
                    if (filter.name == JobType.LOGOUT.name) {
                        homeViewModel.logout()
                        clearData()
                        navController.navigate(Screen.LoginScreen.route)
                    } else if (filter.name == JobType.GET.name) {
                        homeViewModel.offerDemand()

                        navController.navigate(Screen.DemandServiceScreen.route)
                    }
                    showTypeSheet = false
                }
            )
        }
    }

    if (showFilterSheet) {
        listChoiceParentSelect?.let {
            ModalBottomSheet(
                onDismissRequest = {
                    showFilterSheet = false
                    GlobalEntries.isVisibleNav.update { true }
                }
            ) {
                val height = screenHeight / 2
                val heightDp = with(density) { height.toDp() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            modifier = Modifier
                                .height(2.dp)
                                .width(70.dp)
                                .padding(top = 20.dp)
                                .background(Color.Red, RoundedCornerShape(20.dp)),
                            text = ""
                        )

                        Text(
                            text = choiceParentSelect,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 1.dp
                        )

                        val l = when (itemRes) {
                            R.string.status_type_text -> selectedStatusChoice
                            R.string.categories_text -> selectedCategories
                            R.string.experience_text -> selectedExp
                            R.string.disponibility_text -> selectedAvailability
                            R.string.employment_type_text -> selectedType
                            R.string.situation_text -> selectedSituation
                            R.string.sexe_text -> selectedSex
                            else -> selectedAvailability
                        }

                        isSelectedParent = l.none { it }

                        flowHandling(it, l) { index, title, isSelected ->
                            homeViewModel.changeUnKnown(itemRes, index, title, isSelected)
                        }

                        Box(
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .fillMaxWidth(0.8f)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    filterOpen = false
                                    GlobalEntries.isVisibleNav.update { true }
                                    homeViewModel.validateFilter(criteria)
                                    homeViewModel.changeSelectionParentChoices(
                                        indexParent,
                                        titleParent,
                                        !isSelectedParent
                                    )
                                }
                                .background(
                                    color = colorResource(id = R.color.whatsapp),
                                    RoundedCornerShape(30.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.show_result_text),
                                color = White,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                        }
                    }
                }
            }
        } ?: run {
            filterOpen = true
        }
    }


}
