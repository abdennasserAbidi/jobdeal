package com.example.myjob.feature.demands

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries.idDemand
import com.example.myjob.common.GlobalEntries.isUpdatingDemand
import com.example.myjob.common.GlobalEntries.marketDemand
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.feature.home.FilterTypeBottomSheet
import com.example.myjob.feature.navigation.Screen
import dagger.hilt.android.internal.managers.FragmentComponentManager.findActivity

fun Context.findActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is android.content.ContextWrapper -> baseContext.findActivity()
        else -> null
    }

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketDemandScreen(
    navController: NavController,
    makeCall: (String) -> Unit = {},
    clearData: () -> Unit = {},
    demandsViewModel: DemandsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }
    var filterType by remember { mutableStateOf(false) }
    var selectedTypeCategory by remember { mutableStateOf("Services") }
    var selectedTool by remember { mutableStateOf<ToolCategory?>(null) }

    var listFilter by remember {
        mutableStateOf(
            mutableListOf<String>()
        )
    }

    var listSelected by remember {
        mutableStateOf(List(ServiceCategory.entries.size) { false })
    }


    var listFilterTools by remember {
        mutableStateOf(
            mutableListOf<String>()
        )
    }

    var listSelectedTools by remember {
        mutableStateOf(List(ToolCategory.entries.size) { false })
    }

    val fcmToken by demandsViewModel.fcmToken.collectAsState()
    LaunchedEffect(fcmToken) {
        if (fcmToken.isEmpty()) {
            demandsViewModel.updateToken()
        }
    }

    LaunchedEffect(listFilter) {

        if (listFilter.isNotEmpty())
            demandsViewModel.searchDemands(listFilter)
        else demandsViewModel.getDemands()
    }

    LaunchedEffect(listFilterTools) {
        if (listFilterTools.isNotEmpty())
            demandsViewModel.searchDemands(listFilterTools)
        else demandsViewModel.getDemands()
    }

    val service = stringResource(R.string.service_text)
    val tools = stringResource(R.string.tools_text)

    val demandText = stringResource(id = R.string.demand_text)
    val normalText = stringResource(id = R.string.normal_text)
    val logoutText = stringResource(id = R.string.logout_text)

    val lists by remember {
        mutableStateOf(
            mutableListOf(
                service,
                tools
            )
        )
    }

    var selectedItem by remember { mutableStateOf(MarketDemandModel()) }
    val interactionSource = remember { MutableInteractionSource() }
    var showSearchSheet by remember { mutableStateOf(false) }

    var showTypeSheet by remember { mutableStateOf(false) }
    var showContact by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var selectedPhone by remember { mutableStateOf("") }
    var selectedSearch by remember { mutableStateOf(PostType.ALL) }
    var selectedType by remember(demandsViewModel.getType()) { mutableStateOf(JobType.NORMAL) }
    val badgeCountNormal by remember { mutableIntStateOf(0) }
    val badgeCountService by remember { mutableIntStateOf(1) }
    var isUpdating by remember { mutableStateOf(false) }

    val notificationCount by demandsViewModel.notificationCount.collectAsState()
    val demands = demandsViewModel.demands.collectAsLazyPagingItems()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            selectedType = demandsViewModel.getType()
            demandsViewModel.getDemands()
            demandsViewModel.getUserToken()
        }
    }

    BackHandler(enabled = true) {
        context.findActivity()?.finish()
    }

    Scaffold(
        topBar = {
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
                )
                {
                    Text(
                        text = "Services",
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
                    )
                    {

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
                                .align(Alignment.CenterStart)
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
                                    .align(Alignment.CenterEnd)
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
                                    fontWeight = FontWeight.Bold,
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
                                .align(Alignment.CenterEnd)
                        )

                        Spacer(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .height(20.dp)
                                .fillMaxWidth()
                        )
                    }


                }
            }


        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormMarketScreen.route)
                },
                containerColor = Color(0xFF049344),
                contentColor = White
            ) {
                Icon(Icons.Filled.Add, "Nouvelle demande")
            }
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        demandsViewModel.filterDemands(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Rechercher une demande...", color = Color(0xFF9CA3AF)) },
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
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = true,
                    onClick = { filterType = true },
                    label = { Text(selectedTypeCategory) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF049344),
                        selectedLabelColor = White
                    )
                )

                FilterChip(
                    modifier = Modifier.width(1.dp),
                    selected = false,
                    onClick = {  },
                    label = { Text("") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF049344),
                        selectedLabelColor = White
                    )
                )

                // Category Filter
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (selectedTypeCategory == service) {
                        items(ServiceCategory.entries.size) { index ->
                            if (index != 0) {
                                val category = ServiceCategory.entries[index]
                                FilterChip(
                                    selected = listSelected[index],
                                    onClick = {
                                        listSelected = listSelected.mapIndexed { i, item ->
                                            if (i == index) {
                                                !item
                                            } else item
                                        }.toMutableList()

                                        if (listSelected[index]) {
                                            if (!listFilter.contains(category.displayName))
                                                listFilter = (listFilter + category.displayName).toMutableList()
                                        } else {
                                            if (listFilter.contains(category.displayName))
                                                listFilter = (listFilter - category.displayName).toMutableList()
                                        }
                                    },
                                    label = {
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(category.icon)
                                            Text(category.displayName)
                                        }
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF049344),
                                        selectedLabelColor = White
                                    )
                                )
                            }
                        }
                    } else {
                        items(ToolCategory.entries.size) { index ->
                            if (index != 0) {
                                val tools = ToolCategory.entries[index]

                                FilterChip(
                                    selected = listSelectedTools[index],
                                    onClick = {
                                        listSelectedTools = listSelectedTools.mapIndexed { i, item ->
                                            if (i == index) {
                                                !item
                                            } else item
                                        }.toMutableList()

                                        if (listSelectedTools[index]) {
                                            if (!listFilterTools.contains(tools.displayName))
                                                listFilterTools = (listFilterTools + tools.displayName).toMutableList()
                                        } else {
                                            if (listFilterTools.contains(tools.displayName))
                                                listFilterTools = (listFilterTools - tools.displayName).toMutableList()
                                        }
                                    },
                                    label = {
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(tools.icon)
                                            Text(tools.displayName)
                                        }
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF049344),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (demands.itemCount > 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(demands.itemCount) { index ->
                        val demand = demands[index] ?: MarketDemandModel()
                        DemandCard(
                            demand = demand,
                            isNotMe = demandsViewModel.isNotMe(demand.idSender),
                            showContacts = {
                                selectedItem = demand
                                showContact = true
                            },
                            openMenu = {
                                selectedItem = demand
                                showMenu = true
                            },
                            onClick = {
                                selectedItem = demand
                                idDemand = demand.id
                                navController.navigate(Screen.DemandMarketDetailScreen.route)
                            })
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    demands.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = demands.loadState.refresh as LoadState.Error
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
                                val error = demands.loadState.append as LoadState.Error
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
                    text = stringResource(id = R.string.type_change_text),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val isShown = if (selectedItem.paidUser == true) true
                else if (selectedItem.countTrial > 0) true
                else false

                if (isShown) {
                    selectedItem.userSender?.phoneList?.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {

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

                        Button(
                            onClick = {
                                demandsViewModel.countDownTrial(selectedItem.id)
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

    if (showMenu) {
        ModalBottomSheet(
            onDismissRequest = { showMenu = false }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryTypeDialogItem(
                    category = "Modifier",
                    icon =  "✏️",
                    onClick = {
                        marketDemand = selectedItem
                        isUpdatingDemand = true
                        navController.navigate(Screen.FormMarketScreen.route)
                        showMenu = false
                    }
                )

                CategoryTypeDialogItem(
                    category = "Supprimer",
                    icon = "🗑️",
                    onClick = {
                        demandsViewModel.deleteDemand(selectedItem.id)
                        showMenu = false
                    }
                )

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
                        demandsViewModel.logout()
                        clearData()
                        navController.navigate(Screen.LoginScreen.route)
                    } else if (filter.name == JobType.NORMAL.name) {
                        demandsViewModel.changeToJobDeal()
                        navController.navigate(Screen.HomeScreen.route)
                    }
                    showTypeSheet = false
                }
            )
        }
    }

    // Category Selection Dialog
    if (filterType) {
        AlertDialog(
            onDismissRequest = { filterType = false },
            title = { Text("Choisir votre type") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    lists.mapIndexed { index, category ->
                        val icon = if (index == 0) "⚙️" else "🛠️"
                        CategoryTypeDialogItem(
                            category = category,
                            icon = icon,
                            onClick = {
                                selectedTypeCategory = category
                                filterType = false
                            }
                        )
                    }

                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { filterType = false }) {
                    Text("Annuler", color = Color(0xFF049344))
                }
            }
        )
    }
}