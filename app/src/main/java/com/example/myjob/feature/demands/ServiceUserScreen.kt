package com.example.myjob.feature.demands

import FreelanceFilterSectorScreen
import FreelanceSector
import FreelanceService
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ContextualFlowRowOverflowScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowOverflowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.common.GlobalEntries.otherUserId
import com.example.myjob.common.GlobalEntries.otherUserName
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.CategoryModel
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.Rate
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.domain.usecase.avis.LikeEnum
import com.example.myjob.feature.home.FilterTypeBottomSheet
import com.example.myjob.feature.home.HomeViewModel
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.test.FormTextField
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import getAllFreelanceSectors
import getAllFreelanceServices

@SuppressLint("MutableCollectionMutableState")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ServiceUserScreen(
    navController: NavController,
    makeCall: (String) -> Unit = {},
    clearData: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val userService = homeViewModel.userService.collectAsLazyPagingItems()

    val refreshing = userService.loadState.refresh is LoadState.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { userService.refresh() }
    )


    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var selectedTypeCategory by remember { mutableStateOf("Services") }

    var listFilter by remember {
        mutableStateOf(
            mutableListOf<ServiceCategory>()
        )
    }

    var listFilterSector by remember {
        mutableStateOf(
            mutableListOf<FreelanceSector>()
        )
    }

    var listFilterService by remember {
        mutableStateOf(
            mutableListOf<FreelanceService>()
        )
    }

    var listSelectedSector by remember {
        mutableStateOf(List(getAllFreelanceSectors().size) { false })
    }

    var listSelectedService by remember {
        mutableStateOf(List(getAllFreelanceServices().size) { false })
    }

    var selectedItem by remember { mutableStateOf(User()) }
    val interactionSource = remember { MutableInteractionSource() }
    var showSearchSheet by remember { mutableStateOf(false) }

    var showContact by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showRate by remember { mutableStateOf(false) }
    var onRate by remember { mutableStateOf(false) }
    var isFilterOpened by remember { mutableStateOf(false) }
    var selectedPhone by remember { mutableStateOf("") }
    var selectedSearch by remember { mutableStateOf(PostType.ALL) }
    var selectedType by remember { mutableStateOf(JobType.NORMAL) }
    val badgeCountNormal by remember { mutableIntStateOf(0) }
    val badgeCountService by remember { mutableIntStateOf(1) }
    var isUpdating by remember { mutableStateOf(false) }

    var showTypeSheet by remember { mutableStateOf(false) }
    val demandText = stringResource(id = R.string.demand_text)
    val normalText = stringResource(id = R.string.normal_text)
    val logoutText = stringResource(id = R.string.logout_text)

    val notificationCount by homeViewModel.notificationCount.collectAsState()
    val allAvis = homeViewModel.allAvis.collectAsLazyPagingItems()
    val itemState by homeViewModel.itemState.collectAsState()

    LaunchedEffect(allAvis) {
        if (allAvis.itemCount > 0) {
        }
    }


    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            selectedType = homeViewModel.getType()
            homeViewModel.getCurrent()
            homeViewModel.getAllUserService()
            homeViewModel.getUserToken()
        }
    }

    val fcmToken by homeViewModel.fcmToken.collectAsState()
    LaunchedEffect(fcmToken) {
        if (fcmToken.isEmpty()) {
            homeViewModel.updateToken()
        }
    }

    LaunchedEffect(pullRefreshState.progress) {
        if (pullRefreshState.progress > 0f) {
            homeViewModel.getAllUserService()
            listFilterSector.clear()
            listFilterService.clear()
            listSelectedSector.map { false }
            listSelectedService.map { false }
        }
    }

    BackHandler(enabled = true) {
        context.findActivity()?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.whatsapp)
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                if (GlobalEntries.user.role != "Services") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Services",
                            fontSize = 24.sp,
                            color = White,
                            fontWeight = Bold
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

                            Spacer(
                                Modifier
                                    .align(Alignment.TopCenter)
                                    .height(20.dp)
                                    .fillMaxWidth()
                            )

                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 10.dp)
                                    .align(CenterStart)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        navController.navigate(Screen.NotificationDemandScreen.route)
                                    }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = White,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(CenterEnd)
                                )
                            }

                            if (notificationCount > 0) {
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
                                        text = if (notificationCount > 9) "9+" else notificationCount.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = Bold,
                                        color = White
                                    )
                                }
                            }

                            Text(
                                text = when (selectedType) {
                                    JobType.NORMAL -> normalText
                                    JobType.GET -> demandText
                                    else -> logoutText
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
                                    .align(CenterEnd)
                            )

                            Spacer(
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .height(20.dp)
                                    .fillMaxWidth()
                            )
                        }


                    }
                } else {
                    Text(
                        text = "Services",
                        fontSize = 24.sp,
                        color = White,
                        fontWeight = Bold,
                        modifier = Modifier.align(CenterStart)
                    )

                    IconButton(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(CenterEnd),
                        onClick = {
                            navController.navigate(Screen.SettingScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            tint = White,
                            contentDescription = ""
                        )
                    }
                }
            }
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            homeViewModel.filterUserService(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = {
                            Text(
                                "Rechercher un candidat...",
                                color = Color(0xFF9CA3AF)
                            )
                        },
                        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color(0xFF049344)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Close, null, tint = Color(0xFF6B7280))
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF049344),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (searchQuery.isEmpty()) {
                        IconButton(
                            modifier = Modifier.align(CenterEnd),
                            enabled = userService.itemCount > 0,
                            onClick = {
                                isFilterOpened = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = "Filter"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            var maxLines by rememberSaveable { mutableIntStateOf(3) }
            var remainsItemCount by rememberSaveable { mutableIntStateOf(0) }

            val moreIndicator =
                @Composable { scope: ContextualFlowRowOverflowScope ->
                    remainsItemCount = scope.totalItemCount - scope.shownItemCount
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Text(
                            text = "+${remainsItemCount} Tout afficher",
                            color = colorResource(id = R.color.whatsapp),
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                maxLines += 3
                            })
                    }
                }

            val lessIndicator = @Composable { _: FlowRowOverflowScope ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Tout masquer",
                        color = colorResource(id = R.color.whatsapp),
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            maxLines = 3
                        })
                }
            }

            ContextualFlowRow(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                maxLines = maxLines,
                overflow = ContextualFlowRowOverflow.expandOrCollapseIndicator(
                    minRowsToShowCollapse = 3,
                    expandIndicator = moreIndicator,
                    collapseIndicator = lessIndicator
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                itemCount = itemState.size
            ) {
                if (it <= itemState.lastIndex) {
                    val txt = itemState[it]
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                colorResource(id = R.color.whatsapp),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                            },
                        contentAlignment = Center
                    ) {
                        Text(
                            text = txt,
                            color = White,
                            fontWeight = Medium,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(10.dp)
                        )

                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            if (userService.itemCount > 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(userService.itemCount) { index ->
                        val userItem = userService[index] ?: User()
                        UserServiceCard(
                            user = userItem,
                            isNotMe = homeViewModel.isNotMe(userItem.id ?: -1),
                            showContacts = {
                                selectedItem = userItem
                                showContact = true
                            },
                            onRate = {
                                selectedItem = userItem
                                onRate = true
                            },
                            openMenu = {
                                selectedItem = userItem
                                showMenu = true
                            },
                            onShowNotice = {
                                selectedItem = userItem
                                homeViewModel.getAllAvis(userItem.id ?: -1)
                                showRate = true
                            },
                            onClick = {
                                selectedItem = userItem
                                /*idDemand = userItem.id
                                navController.navigate(Screen.DemandMarketDetailScreen.route)*/
                            })
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    userService.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = userService.loadState.refresh as LoadState.Error
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
                                val error = userService.loadState.append as LoadState.Error
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
            } else EmptyState()
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        val isFilterFinished by homeViewModel.isFilterFinished.collectAsState()

        LaunchedEffect(isFilterFinished) {
            if (isFilterFinished) isFilterOpened = false
        }

        if (itemState.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                elevation = CardDefaults.cardElevation(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White,
                    contentColor = White
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Center
                ) {
                    Button(
                        modifier = Modifier.padding(vertical = 15.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Red,
                            containerColor = Red
                        ),
                        onClick = {
                            homeViewModel.clearItems()
                        }
                    ) {
                        Text(
                            text = "Supprimer tous",
                            fontSize = 12.sp,
                            fontWeight = Medium,
                            color = White
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isFilterOpened,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                FreelanceFilterSectorScreen(
                    listFilterSector = listFilterSector,
                    listFilterService = listFilterService,
                    onSelect = { index, sector, service ->

                        sector?.let {
                            listSelectedSector = listSelectedSector.mapIndexed { i, item ->
                                if (i == index) {
                                    !item
                                } else item
                            }.toMutableList()

                            if (listSelectedSector[index]) {
                                if (!listFilterSector.contains(sector))
                                    listFilterSector =
                                        (listFilterSector + sector).toMutableList()
                            } else {
                                if (listFilterSector.contains(sector))
                                    listFilterSector =
                                        (listFilterSector - sector).toMutableList()
                            }
                        } ?: run {
                            val services = service ?: FreelanceService()
                            listSelectedService = listSelectedService.mapIndexed { i, item ->
                                if (i == index) {
                                    !item
                                } else item
                            }.toMutableList()

                            if (listSelectedService[index]) {
                                if (!listFilterService.contains(services))
                                    listFilterService =
                                        (listFilterService + services).toMutableList()
                            } else {
                                if (listFilterService.contains(service))
                                    listFilterService =
                                        (listFilterService - services).toMutableList()
                            }
                        }
                    },
                    onSelectListSector = { listSector, listService ->

                        val categoryModel = CategoryModel()

                        listSector?.let {
                            categoryModel.listSector = it.toMutableList()
                            categoryModel.listService =
                                listService?.toMutableList() ?: mutableListOf()
                        } ?: run {
                            categoryModel.listService =
                                listService?.toMutableList() ?: mutableListOf()
                        }

                        homeViewModel.searchUserService(categoryModel)

                    },
                    dismiss = {
                        isFilterOpened = false
                    }
                )
            }
        }
    }

    AnimatedVisibility(
        visible = showRate,
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
                .background(White)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${allAvis.itemCount} Avis",
                    fontSize = 22.sp,
                    fontWeight = Bold,
                    modifier = Modifier.align(CenterStart)
                )

                IconButton(
                    modifier = Modifier.align(CenterEnd),
                    onClick = {
                        showRate = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = ""
                    )
                }


            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(allAvis.itemCount) { index ->
                    val rate = allAvis[index] ?: Rate()

                    val username = rate.candidateName

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF049344)),
                                contentAlignment = Center
                            ) {
                                Text(
                                    text = username.first().toString(),
                                    color = White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = username,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )


                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(rate.note, modifier = Modifier.padding(start = 20.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 10.dp, bottom = 10.dp),
                                contentAlignment = BottomEnd
                            ) {

                                if (rate.like == LikeEnum.LIKE.name)
                                    Text("👍", fontSize = 20.sp)
                                else Text("👎", fontSize = 20.sp)
                            }
                        }
                    }


                }
            }
        }

    }


    if (onRate) {
        ModalBottomSheet(
            onDismissRequest = { onRate = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Rate",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Log.i("fezjghrgnrlz", "ServiceUserScreen: $selectedItem")
                val username = selectedItem.userServiceName ?: "TEST"

                Box(modifier = Modifier.fillMaxWidth()) {
                    if (username.isNotEmpty()) {
                        Row(
                            modifier = Modifier.align(CenterStart),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF049344)),
                                contentAlignment = Center
                            ) {
                                Text(
                                    text = username.first().toString(),
                                    color = White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = username,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                }

                var message by remember { mutableStateOf("") }
                var like by remember { mutableStateOf(LikeEnum.IDLE) }
                var rateModel by remember { mutableStateOf(Rate()) }

                val savedStatus by homeViewModel.savedStatus.collectAsState()
                LaunchedEffect(savedStatus) {
                    if (savedStatus == "saved successfully") onRate = false
                }

                FormTextField(
                    value = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    onValueChange = {
                        message = it
                    },
                    label = "",
                    placeholder = "Description",
                    maxLines = 5,
                    minLines = 3
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 15.dp)
                ) {
                    Log.i("fzkorghrgrzl", "ServiceUserScreen: $like")
                    if (like == LikeEnum.LIKE) {
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(5.dp),
                            border = BorderStroke(1.dp, WhatsAppDarkGreen),
                            colors = CardDefaults.cardColors(
                                contentColor = White,
                                containerColor = White
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        like = LikeEnum.LIKE
                                    },
                                contentAlignment = Center
                            ) {
                                Text("👍", fontSize = 20.sp, modifier = Modifier.padding(10.dp))
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(5.dp),
                            colors = CardDefaults.cardColors(
                                contentColor = White,
                                containerColor = White
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        like = LikeEnum.LIKE
                                    },
                                contentAlignment = Center
                            ) {
                                Text("👍", fontSize = 20.sp, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }


                    if (like == LikeEnum.DISLIKE) {
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(5.dp),
                            border = BorderStroke(1.dp, WhatsAppDarkGreen),
                            colors = CardDefaults.cardColors(
                                contentColor = White,
                                containerColor = White
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        like = LikeEnum.DISLIKE
                                    },
                                contentAlignment = Center
                            ) {
                                Text("👎", fontSize = 20.sp, modifier = Modifier.padding(10.dp))
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier.weight(1f),
                            elevation = CardDefaults.cardElevation(5.dp),
                            colors = CardDefaults.cardColors(
                                contentColor = White,
                                containerColor = White
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        like = LikeEnum.DISLIKE
                                    },
                                contentAlignment = Center
                            ) {
                                Text("👎", fontSize = 20.sp, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 10.dp),
                    onClick = {
                        rateModel.idCandidate = selectedItem.id ?: -1
                        rateModel.candidateName = username
                        rateModel.note = message
                        rateModel.like = like.name
                        homeViewModel.saveAvis(rateModel)
                    }) {
                    Text("Valider")
                }

            }
        }
    }

    if (showContact) {
        ModalBottomSheet(
            onDismissRequest = { showContact = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Contact",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val isShown = if (GlobalEntries.user.paidUser == true) true
                else if (GlobalEntries.user.countTrial > 0) true
                else false

                if (isShown) {
                    selectedItem.phoneList?.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    selectedPhone = item
                                }
                                .padding(vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPhone == item,
                                onClick = {
                                    selectedPhone = item
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.whatsapp),
                                    unselectedColor = colorResource(id = R.color.whatsapp)
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Button(
                                onClick = {
                                    homeViewModel.countDownTrial()
                                    makeCall(selectedPhone)
                                    showContact = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF049344)
                                ),
                                enabled = selectedPhone.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Appeler",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(Modifier.width(10.dp))

                            Button(
                                onClick = {
                                    candidateUser = selectedItem
                                    otherUserId = selectedItem.id ?: -1
                                    otherUserName = selectedItem.userServiceName ?: ""

                                    navController.navigate(Screen.SendMessageScreen.route)
                                    showContact = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF049344)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Message,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Contacter",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                } else
                    Text(
                        text = stringResource(id = R.string.end_trial_text),
                        style = MaterialTheme.typography.titleLarge,
                        color = Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }

    if (showTypeSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTypeSheet = false }
        ) {
            FilterTypeBottomSheet(
                selectedFilter = selectedType,
                onFilterSelected = { filter ->
                    selectedType = filter
                    if (filter.name == JobType.LOGOUT.name) {
                        homeViewModel.logout()
                        clearData()
                        navController.navigate(Screen.LoginScreen.route)
                    } else if (filter.name == JobType.NORMAL.name) {
                        homeViewModel.changeToJobDeal()
                        navController.navigate(Screen.HomeScreen.route)
                    }
                    showTypeSheet = false
                }
            )
        }
    }

    // Category Selection Dialog
    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Choisir une catégorie") },
            text = {
                LazyColumn {
                    items(ServiceCategory.entries.toTypedArray()) { category ->
                        CategoryDialogItem(
                            category = category,
                            onClick = {
                                selectedCategory = category

                                if (!listFilter.contains(category))
                                    listFilter =
                                        (listFilter + category).toMutableList()
                                else listFilter =
                                    (listFilter - category).toMutableList()

                                showCategoryDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Annuler", color = Color(0xFF049344))
                }
            }
        )
    }
}