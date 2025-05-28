package com.example.myjob.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GenericMultipleSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.isFromFilter
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.home.filter.flowHandling
import com.example.myjob.feature.navigation.Screen

@OptIn(
    ExperimentalSwipeableCardApi::class, ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun HomeCompany(
    navController: NavController,
    allSubjects: MutableList<Subject>,
    listSchools: MutableList<String>,
    listCountries: MutableList<String>,
    listCompany: MutableList<String>,
    onResumed: (index: Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    var visibleUser by remember { mutableStateOf(User()) }
    var isListed by remember { mutableStateOf(false) }
    var openFormInvitation by remember { mutableStateOf(false) }
    var us by remember { mutableStateOf<List<Pair<User, SwipeableCardState>>>(emptyList()) }

    val interactionSource = remember { MutableInteractionSource() }

    var search by remember { mutableStateOf(false) }

    val resume by homeViewModel.resume.collectAsState()

    val lazyPagingItems = homeViewModel.user.collectAsLazyPagingItems()
    val users = lazyPagingItems.itemSnapshotList.items

    val scope = rememberCoroutineScope()

    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx().toInt()
    }

    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp.toPx().toInt()
    }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            onResumed(0)
            if (isFromFilter) {
                homeViewModel.validateFilter(GlobalEntries.criteriaModel)
                isFromFilter = false
            } else {
                homeViewModel.getAllUser()
            }
        }
    }

    var filterOpen by remember { mutableStateOf(false) }
    var itemRes by remember { mutableStateOf(R.string.item1) }
    val listChoiceParentSelect by homeViewModel.listChoiceParentSelect.collectAsState()
    val listChoiceParentSelected by homeViewModel.listChoiceParentSelected.collectAsState()

    val choiceParentSelect by homeViewModel.choiceParentSelect.collectAsState()
    val categories by homeViewModel.parentChoices.collectAsState()
    val selectedCat by homeViewModel.selectedParentChoices.collectAsState()

    val criteria by homeViewModel.criteria.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        val colorCard = if (isListed) White else Color.Transparent
        val colorText = if (!isListed) White else colorResource(id = R.color.whatsapp)
        val elevation = if (isListed) 5.dp else 0.dp

        /*Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(vertical = 15.dp, horizontal = 15.dp)
        ) {

            Spacer(modifier = Modifier.width(50.dp))

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "${stringResource(id = R.string.hello)}, ${GlobalEntries.user.companyName}",
                color = colorText,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.rubikbold,
                            weight = FontWeight.Bold
                        )
                    )
                )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        isListed = !isListed
                    }
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(10.dp),
                    imageVector = Icons.Filled.Menu,
                    contentDescription = ""
                )

            }
        }*/

        Column(modifier = Modifier.fillMaxWidth()) {

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
                    .background(White)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                    },
                elevation = 5.dp
            ) {
                TextField(
                    value = "",
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = colorResource(id = R.color.lighter_gray),
                        focusedLabelColor = colorResource(id = R.color.whatsapp),
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        //isSearching = it.isNotEmpty()
                        homeViewModel.updateQuery(it)
                    },
                    placeholder = { Text("Rechercher un mot") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 10.dp, top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Icon(
                        painter = painterResource(id = R.drawable.filter),
                        tint = colorResource(id = R.color.whatsapp),
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
                }

                itemsIndexed(
                    items = categories
                ) { index, item ->

                    val title = stringResource(id = item.title)

                    val textColor = if (selectedCat[index]) White else Color.Black
                    val color =
                        colorResource(id = if (selectedCat[index]) R.color.whatsapp else R.color.lighter_gray)

                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .padding(start = 10.dp)
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
                                filterOpen = true
                                itemRes = item.title
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
                val height = screenHeight/2
                val heightDp = with(density) { height.toDp() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightDp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 15.dp
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = choiceParentSelect,
                            fontWeight = FontWeight.SemiBold,
                            color = Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 1.dp
                        )

                        flowHandling(it, listChoiceParentSelected) { index, title, isSelected ->
                            homeViewModel.changeUnKnown(itemRes, index, title, isSelected)
                        }
                    }
                }
            } ?: run {

                Box(modifier = Modifier.fillMaxSize()) {

                    val list = allSubjects.map {
                        it.libelly
                    }

                    val listFilter = when(itemRes) {
                        R.string.institution_text -> listSchools
                        R.string.location_text -> listCountries
                        R.string.company_name_text -> listCompany
                        R.string.activity_text -> list
                        else -> emptyList()
                    }

                    val savedList = when(itemRes) {
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
                        },
                        onSelectedBank = { list ->
                            filterOpen = false

                            when(itemRes) {
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

    /*Box(modifier = Modifier.fillMaxSize()) {

        val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
        val c = if (isListed) White else colorResource(id = R.color.whatsapp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .background(
                    color = c,
                    shape = shape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val colorCard = if (isListed) White else Color.Transparent
            val colorText = if (!isListed) White else colorResource(id = R.color.whatsapp)
            val elevation = if (isListed) 5.dp else 0.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp)
            ) {

                /*Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigate(Screen.SearchWordScreen.route)
                        },
                    imageVector = Icons.Filled.Search,
                    tint = White,
                    contentDescription = ""
                )*/

                Spacer(modifier = Modifier.width(50.dp))

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "${stringResource(id = R.string.hello)}, ${GlobalEntries.user.companyName}",
                    color = colorText,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.rubikbold,
                                weight = FontWeight.Bold
                            )
                        )
                    )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            isListed = !isListed
                        }
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(10.dp),
                        imageVector = Icons.Filled.Menu,
                        contentDescription = ""
                    )

                }
            }

            if (users.isNotEmpty()) {

                val userState = users.reversed().map { it to rememberSwipeableCardState() }
                visibleUser = users[0]

                us = userState

                AnimatedContent(
                    targetState = isListed,
                    transitionSpec = {
                        fadeIn(tween(300)) with fadeOut(tween(300))
                    },
                    label = "ListToBoxTransition"
                ) { isList ->

                    if (isList) {
                        LazyColumn(modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxSize()) {

                            items(lazyPagingItems.itemCount) { index ->
                                val user = lazyPagingItems[index] ?: User()

                                Card(
                                    elevation = 10.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp, horizontal = 10.dp),
                                    shape = RoundedCornerShape(30.dp)
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                GlobalEntries.userForCompany = user
                                                navController.navigate(Screen.DetailScreen.route)
                                            }
                                    ) {

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            val gender =
                                                if (user.sexe == "Homme" || user.sexe == "Male") R.drawable.menavatar
                                                else R.drawable.femaleavatar

                                            val color =
                                                if (user.sexe == "Homme" || user.sexe == "Male") Color.Cyan
                                                else Color(0xFFFF8C00)

                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = color,
                                                        shape = CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painterResource(id = gender),
                                                    modifier = Modifier
                                                        .size(70.dp)
                                                        .padding(10.dp),
                                                    contentDescription = ""
                                                )
                                            }

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .weight(0.4f)
                                                    .padding(start = 20.dp)
                                                    .padding(horizontal = 10.dp)
                                            ) {

                                                Text(
                                                    text = user.fullName ?: "",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                )
                                                val exp = user.experience?.let {
                                                    homeViewModel.extractExp(it)
                                                } ?: "new"

                                                Text(
                                                    text = exp,
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = 14.sp
                                                    ),
                                                    color = Color.LightGray,
                                                    modifier = Modifier.padding(top = 5.dp)
                                                )

                                            }

                                        }
                                    }

                                }
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
                                        /*item {
                                            ErrorMessage(
                                                modifier = Modifier,
                                                message = error.error.localizedMessage!!,
                                                onClickRetry = { retry() })
                                        }*/
                                    }
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                        ) {

                            userState.forEach { (user, state) ->

                                homeViewModel.getResume(user)
                                if (state.swipedDirection == null) {
                                    ProfileCard(
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .fillMaxHeight(0.85f)
                                            .align(Alignment.TopCenter)
                                            .background(
                                                color = White,
                                                shape = RoundedCornerShape(20.dp)
                                            ),
                                        openProfile = {
                                            val gson = Gson()
                                            val userJson = gson.toJson(user, User::class.java)
                                            GlobalEntries.userForCompany = user
                                            navController.navigate(Screen.DetailScreen.route)
                                            //navController.navigate("${Screen.DetailScreen.route}/$userJson")
                                        },
                                        lang = resume,
                                        matchProfile = user,
                                        onSave = {
                                            homeViewModel.saveToFavorites(
                                                GlobalEntries.user.id ?: -1,
                                                visibleUser.id ?: 0
                                            )
                                        },
                                        onSkip = {
                                            scope.launch {
                                                val last = userState.reversed()
                                                    .firstOrNull {
                                                        it.second.offset.value == Offset(0f, 0f)
                                                    }?.second
                                                last?.swipe(Direction.Left)
                                            }

                                            homeViewModel.updateCurrentPage()

                                            homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                                            homeViewModel.skipCurrentProfile(visibleUser)

                                        },
                                        onMatch = {
                                            openFormInvitation = true
                                        }
                                    )
                                }
                                LaunchedEffect(user, state.swipedDirection) {
                                    if (state.swipedDirection != null) {
                                        //hint = "You swiped ${stringFrom(state.swipedDirection!!)}"
                                    }
                                }
                            }


                        }
                    }

                }



            } else Text("No more profiles!")
        }


        AnimatedVisibility(
            visible = openFormInvitation,
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
                    .verticalScroll(rememberScrollState())
                    .background(White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(top = 20.dp)
                ) {

                    Icon(
                        imageVector = Icons.Filled.Close,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                openFormInvitation = false
                            },
                        contentDescription = ""
                    )

                    Text(
                        text = "Send Invitation",
                        modifier = Modifier.align(Alignment.Center),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val invitationParam by homeViewModel.invitationParam.collectAsState()
                    var postName by remember { mutableStateOf("Dveloppeur Android") }

                    Text(
                        text = "Post name",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = postName,
                        onValueChange = {
                            postName = it
                            homeViewModel.changePostName(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var descriptions by remember { mutableStateOf("Creer une application pour connecter les entreprises avec les candidats facilement.") }

                    Text(
                        text = "Description",
                        modifier = Modifier.padding(top = 10.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = descriptions,
                        onValueChange = {
                            descriptions = it
                            homeViewModel.changeDescriptions(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var type by remember { mutableStateOf("CDI") }

                    Text(
                        text = "Type de contrat",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = type,
                        onValueChange = {
                            type = it
                            homeViewModel.changeTypeContract(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var disponibility by remember { mutableStateOf("Immidiat") }

                    Text(
                        text = "Disponibilité",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = disponibility,
                        onValueChange = {
                            disponibility = it
                            homeViewModel.changeDisponibility(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var salary by remember { mutableStateOf("1000") }

                    Text(
                        text = "TGM",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = salary,
                        onValueChange = {
                            salary = it
                            homeViewModel.changeSalary(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )
                }

                val statusInvitation = stringResource(id = R.string.holding)

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .padding(top = 20.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {

                            scope.launch {
                                val last = us
                                    .reversed()
                                    .firstOrNull {
                                        it.second.offset.value == Offset(0f, 0f)
                                    }?.second

                                last?.swipe(Direction.Right)
                            }

                            homeViewModel.updateCurrentPage()
                            homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                            homeViewModel.matchCurrentProfile(visibleUser, statusInvitation)
                            openFormInvitation = false

                        }
                        .background(
                            color = colorResource(id = R.color.whatsapp),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Send",
                        modifier = Modifier.padding(
                            vertical = 20.dp,
                            horizontal = 20.dp
                        ),
                        style = TextStyle(
                            color = White,
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }*/
}