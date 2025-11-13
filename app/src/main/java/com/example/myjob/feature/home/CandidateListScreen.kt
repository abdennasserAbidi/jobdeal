package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.myjob.common.GlobalEntries.preferredRole
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.common.view.CandidateCard
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.CandidateStatus
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.candidates
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.feature.home.filter.flowHandling
import com.example.myjob.feature.invitation.company.EnProcessForm
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateListScreen(
    navController: NavController,
    allSubjects: MutableList<Subject>,
    listSchools: MutableList<String>,
    listCountries: MutableList<String>,
    listCompany: MutableList<String>,
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

    //TODO("for v2 we add collaboration type invitation")

    val filteredCandidates = candidates.filter { candidate ->
        val matchesSearch = candidate.name.contains(searchQuery, ignoreCase = true) ||
                candidate.position.contains(searchQuery, ignoreCase = true) ||
                candidate.company.contains(searchQuery, ignoreCase = true) ||
                candidate.skills.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesFilter = when (selectedFilter) {
            FilterType.ALL -> true
            FilterType.INTERVIEWING -> candidate.status == CandidateStatus.INTERVIEWING
            FilterType.HIRED -> candidate.status == CandidateStatus.HIRED
            FilterType.NOT_INTERESTED -> candidate.status == CandidateStatus.NOT_INTERESTED
            else -> candidate.status == CandidateStatus.NOT_INTERESTED
        }

        matchesSearch && matchesFilter
    }

    var filterOpen by remember { mutableStateOf(false) }
    var itemRes by remember { mutableStateOf(R.string.item1) }
    var indexParent by remember { mutableStateOf(-1) }
    var titleParent by remember { mutableStateOf("") }
    var isSelectedParent by remember { mutableStateOf(false) }

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

    LaunchedEffect(fcmToken) {
        if (fcmToken.isNotEmpty()) {

            GlobalEntries.user.companyName?.let {
                val title = it
                val message = "This company have sended you an invitaion "
                homeViewModel.sendNotification(title, message)
            }
        }
    }

    val invitation by homeViewModel.invitation.collectAsState()

    var openFinishProcess by remember { mutableStateOf(false) }
    var invitationModel by remember { mutableStateOf(InvitationModel()) }

    if (openFinishProcess) {
        EnProcessForm(invitationModel,
            onDismissRequest = {
                openFinishProcess = false
            },
            onConfirmation = {
                homeViewModel.finishProcess(it)
                openFinishProcess = false
            })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.candidates_text),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* Handle notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { /* Handle menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.whatsapp),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )

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

                        val textColor = if (selectedCat[index]) Color.White else Color.Black
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

            val lazyPagingItems = homeViewModel.user.collectAsLazyPagingItems()

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
                        val statusInvitation = stringResource(id = R.string.holding)

                        CandidateCard(
                            user = user,
                            candidate = candidate,
                            onClick = {
                                GlobalEntries.userForCompany = user
                                navController.navigate(Screen.DetailScreen.route)
                            },
                            onSendInvitation = {

                                candidateUser = user
                                navController.navigate(Screen.SendInvitationScreen.route)
                            },
                            onTerminateInvitation = {
                                invitationModel = it
                                openFinishProcess = true
                            }
                        )

                        if (index >= lazyPagingItems.itemCount) {
                            Spacer(modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth())
                        }

                        Spacer(modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth())
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
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

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

                            isSelectedParent = l.filter { it }.none()

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
                                    Log.i("ljkljlkjkljlkgtr", "CandidateListScreen: $criteria")
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
                                text = "See results",
                                color = Color.White,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                        }
                    }
                }
            } ?: run {

                Box(modifier = Modifier.fillMaxSize()) {

                    val list = allSubjects.map {
                        it.libelly
                    }

                    val listFilter = when (itemRes) {
                        R.string.institution_text -> listSchools
                        R.string.location_text -> listCountries
                        R.string.company_name_text -> listCompany
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
                            Log.i("hahiwachbiki", "CandidateListScreen: $criteria")
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

                        isSelectedParent = l.filter { it }.none()

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
                                color = Color.White,
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
