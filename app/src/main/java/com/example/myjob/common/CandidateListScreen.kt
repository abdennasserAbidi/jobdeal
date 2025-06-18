package com.example.myjob.common

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.CandidateStatus
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.candidates
import com.example.myjob.feature.home.HomeViewModel
import com.example.myjob.feature.home.SendInvitation
import com.example.myjob.feature.home.filter.flowHandling
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// WhatsApp Green Theme Colors
val WhatsAppDarkGreen = Color(0xFF128C7E)
val WhatsAppLightGreen = Color(0xFFDCF8C6)
val WhatsAppGreenSurface = Color(0xFFF0F9F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateListScreen(
    navController: NavController,
    allSubjects: MutableList<Subject>,
    listSchools: MutableList<String>,
    listCountries: MutableList<String>,
    listCompany: MutableList<String>,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(FilterType.ALL) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    // Filter candidates based on search and filter
    val filteredCandidates = candidates.filter { candidate ->
        val matchesSearch = candidate.name.contains(searchQuery, ignoreCase = true) ||
                candidate.position.contains(searchQuery, ignoreCase = true) ||
                candidate.company.contains(searchQuery, ignoreCase = true) ||
                candidate.skills.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesFilter = when (selectedFilter) {
            FilterType.ALL -> true
            FilterType.AVAILABLE -> candidate.status == CandidateStatus.AVAILABLE
            FilterType.INTERVIEWING -> candidate.status == CandidateStatus.INTERVIEWING
            FilterType.HIRED -> candidate.status == CandidateStatus.HIRED
            FilterType.NOT_INTERESTED -> candidate.status == CandidateStatus.NOT_INTERESTED
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

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            GlobalEntries.isVisibleNav.update { true }

            homeViewModel.addToList(contractText, freelanceText)
            //onResumed(0)
            if (GlobalEntries.isFromFilter) {
                homeViewModel.validateFilter(GlobalEntries.criteriaModel)
                GlobalEntries.isFromFilter = false
            } else {
                homeViewModel.getAllUser()
            }
        }
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
                        text = "Candidates",
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
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search candidates...") },
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
                    /*item {
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            tint = White,
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    navController.navigate(Screen.FilterScreen.route)
                                }
                        )
                    }*/

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
                                    //filterOpen = true
                                    showFilterSheet = true
                                    GlobalEntries.isVisibleNav.update { false }
                                    itemRes = item.title
                                    indexParent = index
                                    titleParent = title
                                    isSelectedParent = !selectedCat[index]
                                    homeViewModel.changeOption(title, item.title)
                                    /*homeViewModel.changeSelectionParentChoices(
                                        index,
                                        item.title,
                                        title,
                                        !selectedCat[index]
                                    )*/
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

                Text(
                    text = "${filteredCandidates.size} candidates found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                /*Text(
                                text = when (selectedFilter) {
                                    FilterType.ALL -> "All"
                                    FilterType.AVAILABLE -> "Available"
                                    FilterType.INTERVIEWING -> "Interviewing"
                                    FilterType.HIRED -> "Hired"
                                    FilterType.NOT_INTERESTED -> "Not Interested"
                                }
                            )*/
            }

            val lazyPagingItems = homeViewModel.user.collectAsLazyPagingItems()

            // Candidates List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lazyPagingItems.itemCount) { index ->
                    val user = lazyPagingItems[index] ?: User()
                    //user.getYearsExp()
                    val exp = user.experience ?: emptyList()

                    val lastExperience = if (exp.isNotEmpty()) exp[exp.lastIndex]
                    else Experience()

                    val candidate = Candidate(
                        name = user.fullName ?: "",
                        position = user.preferredActivitySector ?: "",
                        company = lastExperience.companyName ?: "",
                        experience = "5+ years",
                        location = lastExperience.place ?: "",
                        salary = "${lastExperience.salary ?: 0} DT",
                        skills = emptyList()
                    )
                    val statusInvitation = stringResource(id = R.string.holding)

                    CandidateCard(
                        candidate = candidate,
                        onClick = { },
                        onSendInvitation = {

                            candidateUser = user
                            navController.navigate(Screen.SendInvitationScreen.route)
                        }
                    )
                }
                lazyPagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = lazyPagingItems.loadState.refresh as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier.fillParentMaxSize(),
                                    message = error.error.localizedMessage ?: "",
                                    onClickRetry = { retry() })
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item { LoadingNextPageItem(modifier = Modifier) }
                        }

                        loadState.append is LoadState.Error -> {
                            val error = lazyPagingItems.loadState.append as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier,
                                    message = error.error.localizedMessage!!,
                                    onClickRetry = { retry() })
                            }
                        }
                    }
                }

                /*items(filteredCandidates) { candidate ->
                    CandidateCard(
                        candidate = candidate,
                        onClick = {  }
                    )
                }*/

                item {
                    Spacer(modifier = Modifier.height(16.dp))
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


                    GenericMultipleSearch(
                        mListOfJobs = listFilter,
                        savedList = savedList,
                        onDismissRequest = {
                            filterOpen = false
                            showFilterSheet = false
                        },
                        onSelectedBank = { list ->
                            filterOpen = false
                            showFilterSheet = false

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
                onDismissRequest = { showFilterSheet = false }
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
            filterOpen = true
        }
    }


}

@Composable
fun CandidateCard1(
    candidate: Candidate,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row - Name, Position and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Name and Position
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = candidate.position,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status Badge
                StatusBadge(status = candidate.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Company and Experience
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = "Company",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.company,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Work,
                        contentDescription = "Experience",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.experience,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location and Salary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = candidate.salary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Skills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(candidate.skills.take(3)) { skill ->
                    SkillChip(skill = skill)
                }
                if (candidate.skills.size > 3) {
                    item {
                        Text(
                            text = "+${candidate.skills.size - 3} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CandidateCard3(
    candidate: Candidate,
    onClick: () -> Unit,
    onSendInvitation: (Candidate) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row - Name, Position and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Name and Position
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = candidate.position,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status Badge
                StatusBadge(status = candidate.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Company and Experience
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = "Company",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.company,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Work,
                        contentDescription = "Experience",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.experience,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location and Salary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = candidate.salary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Skills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(candidate.skills.take(3)) { skill ->
                    SkillChip(skill = skill)
                }
                if (candidate.skills.size > 3) {
                    item {
                        Text(
                            text = "+${candidate.skills.size - 3} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Handle view profile */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "View Profile",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Profile")
                }

                Button(
                    onClick = { onSendInvitation(candidate) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send Invitation",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Invite")
                }
            }
        }
    }
}

@Composable
fun CandidateCard(
    candidate: Candidate,
    onClick: () -> Unit,
    onSendInvitation: (Candidate) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row - Name, Position and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Name and Position
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = candidate.position,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status Badge
                StatusBadge(status = candidate.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Company and Experience
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = "Company",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.company,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Work,
                        contentDescription = "Experience",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.experience,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location and Salary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = candidate.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = candidate.salary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Skills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(candidate.skills.take(3)) { skill ->
                    SkillChip(skill = skill)
                }
                if (candidate.skills.size > 3) {
                    item {
                        Text(
                            text = "+${candidate.skills.size - 3} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Handle view profile */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "View Profile",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Profile")
                }

                Button(
                    onClick = { onSendInvitation(candidate) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send Invitation",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Invite")
                }
            }
        }
    }
}


@Composable
fun StatusBadge(status: CandidateStatus) {
    val (color, text) = when (status) {
        CandidateStatus.AVAILABLE -> Color(0xFF4CAF50) to "Available"
        CandidateStatus.INTERVIEWING -> Color(0xFFFF9800) to "Interviewing"
        CandidateStatus.HIRED -> Color(0xFF2196F3) to "Hired"
        CandidateStatus.NOT_INTERESTED -> Color(0xFF9E9E9E) to "Not Interested"
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SkillChip(skill: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = skill,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FilterBottomSheet(
    selectedFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = "Filter by Status",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        FilterType.values().forEach { filter ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFilterSelected(filter) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (filter) {
                        FilterType.ALL -> "All Candidates"
                        FilterType.AVAILABLE -> "Available"
                        FilterType.INTERVIEWING -> "Interviewing"
                        FilterType.HIRED -> "Hired"
                        FilterType.NOT_INTERESTED -> "Not Interested"
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
