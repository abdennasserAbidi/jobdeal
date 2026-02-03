package com.example.myjob.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.base.CustomTextField
import com.example.myjob.common.CollapsibleThing
import com.example.myjob.common.CollapsingAppBarNestedScrollConnection
import com.example.myjob.common.Direction
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GenericMultipleSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.isFromFilter
import com.example.myjob.common.GlobalEntries.isVisibleNav
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.SwipeableCardState
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.home.filter.flowHandling
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    val isListed by remember { mutableStateOf(false) }
    var openFormInvitation by remember { mutableStateOf(false) }
    var us by remember { mutableStateOf<List<Pair<User, SwipeableCardState>>>(emptyList()) }

    val interactionSource = remember { MutableInteractionSource() }

    var search by remember { mutableStateOf(false) }

    val invitationSent by homeViewModel.invitationSent.collectAsState()
    val fcmToken by homeViewModel.fcmToken.collectAsState()

    LaunchedEffect(fcmToken) {
        if (fcmToken.isNotEmpty()) {
            GlobalEntries.user.companyName?.let {
                val title = it
                val message = "This company have sended you an invitaion "
                //homeViewModel.sendNotification(title, message)
            } ?: run {
                val title = GlobalEntries.user.fullName ?: ""
                val message = "This company have sended you an invitaion "
                //homeViewModel.sendNotification(title, message)
            }

        }
    }

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

    val contractText = stringResource(id = R.string.type1_text)
    val freelanceText = stringResource(id = R.string.type2_text)

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            homeViewModel.addToList(contractText, freelanceText)
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

        val connection = CollapsingAppBarNestedScrollConnection()

        CollapsibleThing(
            connection,
            content = {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxSize()
                    ) {

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
                                            visibleUser = user
                                            //navController.navigate(Screen.DetailScreen.route)
                                            openFormInvitation = true
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
                                    item {
                                        ErrorMessage(
                                            modifier = Modifier,
                                            message = error.error.localizedMessage!!,
                                            onClickRetry = { retry() })
                                    }
                                }
                            }
                        }
                    }
                }
            }, headerContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {

                    val shapeInit = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                color = colorResource(id = R.color.whatsapp),
                                shape = shapeInit
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 75.dp, bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Surface(
                            elevation = 2.dp,
                            color = MaterialTheme.colors.surface,
                            shape = RoundedCornerShape(40.dp),
                        ) {
                            Card(
                                shape = RoundedCornerShape(40.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .wrapContentHeight()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(40.dp)
                                    )
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                    },
                                elevation = 5.dp
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    TextField(
                                        value = "",
                                        colors = TextFieldDefaults.textFieldColors(
                                            containerColor = Color.Transparent,
                                            focusedLabelColor = colorResource(id = R.color.whatsapp),
                                            disabledTextColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent
                                        ),
                                        onValueChange = {
                                            homeViewModel.updateQuery(it)
                                        },
                                        placeholder = { Text("Search") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .background(White)
                                    )
                                }
                            }
                        }

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp, top = 20.dp),
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

                                val textColor = if (selectedCat[index]) White else Black
                                val color =
                                    colorResource(id = if (selectedCat[index]) R.color.whatsapp else R.color.lighter_gray)

                                Row(
                                    modifier = Modifier
                                        .wrapContentHeight()
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
                                            isVisibleNav.update { false }
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

                    }
                }


            }, filterBar = {

                var alphaValue by remember { mutableFloatStateOf(0f) }
                alphaValue = (3 * (1f - connection.progress)).coerceIn(0f, 1f)
                val colorCard = if (alphaValue == 1f) White.copy(alpha = alphaValue)
                else colorResource(id = R.color.whatsapp)

                Column(
                    modifier = Modifier.background(
                        White.copy(alpha = alphaValue)
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentHeight()
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(40.dp)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                            }
                            .background(White.copy(alpha = alphaValue)),
                        contentColor = White.copy(alpha = alphaValue),
                        elevation = 5.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .background(White.copy(alpha = alphaValue))
                        ) {
                            TextField(
                                value = "",
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedLabelColor = colorResource(id = R.color.whatsapp),
                                    disabledTextColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                onValueChange = {
                                    homeViewModel.updateQuery(it)
                                },
                                placeholder = { Text("Search") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(White.copy(alpha = alphaValue))
                            )
                        }
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp, top = 10.dp)
                            .background(White.copy(alpha = alphaValue)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        itemsIndexed(
                            items = categories
                        ) { index, item ->

                            val title = stringResource(id = item.title)

                            val textColor = if (selectedCat[index]) White else Black
                            val color =
                                colorResource(id = if (selectedCat[index]) R.color.whatsapp else R.color.lighter_gray)

                            Row(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .padding(start = 10.dp)
                                    .border(
                                        width = 1.dp,
                                        color = color.copy(alpha = alphaValue),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .background(
                                        color = color.copy(alpha = alphaValue),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        filterOpen = true
                                        isVisibleNav.update { false }
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
                                    color = textColor.copy(alpha = alphaValue),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                        }
                    }
                }
            })


        /*Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 70.dp)
                .background(White)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                },
            elevation = 10.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {



            }
        }*/



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

                Card(
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
                                    .background(Red, RoundedCornerShape(20.dp)),
                                text = ""
                            )

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
                                    isVisibleNav.update { true }
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
                        },
                        onSelectedBank = { list ->
                            filterOpen = false

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
            val statusInvitation = stringResource(id = R.string.holding)

            SendInvitation(homeViewModel, validate = {

                scope.launch {
                    val last = us
                        .reversed()
                        .firstOrNull {
                            it.second.offset.value == Offset(0f, 0f)
                        }?.second

                    last?.swipe(Direction.Right)
                }

                homeViewModel.clearToken()
                homeViewModel.getUserToken(visibleUser.id ?: -1)
                /*homeViewModel.updateCurrentPage()
                homeViewModel.removeFromGlobal(visibleUser.id ?: 0)*/
                homeViewModel.matchCurrentProfile(visibleUser, statusInvitation, "", "")
            },
                openFormInvitation = {
                    openFormInvitation = false
                })

        }
    }


}