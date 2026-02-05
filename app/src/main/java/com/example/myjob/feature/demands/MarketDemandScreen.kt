package com.example.myjob.feature.demands

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.feature.home.FilterTypeBottomSheet
import com.example.myjob.feature.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketDemandScreen(
    navController: NavController,
    makeCall: (String) -> Unit = {},
    clearData: () -> Unit = {},
    demandsViewModel: DemandsViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }
    var selectedItem by remember { mutableStateOf(MarketDemandModel()) }
    val interactionSource = remember { MutableInteractionSource() }
    var showSearchSheet by remember { mutableStateOf(false) }

    var showTypeSheet by remember { mutableStateOf(false) }
    var showContact by remember { mutableStateOf(false) }
    var selectedPhone by remember { mutableStateOf("") }
    var selectedSearch by remember { mutableStateOf(PostType.ALL) }
    var selectedType by remember { mutableStateOf(JobType.NORMAL) }
    val badgeCountNormal by remember { mutableIntStateOf(0) }
    val badgeCountService by remember { mutableIntStateOf(1) }
    var isUpdating by remember { mutableStateOf(false) }

    val demands = demandsViewModel.demands.collectAsLazyPagingItems()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            selectedType = demandsViewModel.getType()
            demandsViewModel.getDemands()
        }
    }

    /*val filteredDemands = demands.filter { demand ->
        val matchesSearch = demand.title.contains(searchQuery, ignoreCase = true) ||
                demand.description.contains(searchQuery, ignoreCase = true) ||
                demand.location.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == null || demand.category == selectedCategory
        matchesSearch && matchesCategory
    }*/

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demandes de services") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormMarketScreen.route)
                },
                containerColor = Color(0xFF049344),
                contentColor = Color.White
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
                    onValueChange = { searchQuery = it },
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

            // Category Filter
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Tous") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF049344),
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(ServiceCategory.entries.size) { index ->
                    val category = ServiceCategory.entries[index]
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = if (selectedCategory == category) null else category },
                        label = {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(category.icon)
                                Text(category.displayName)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF049344),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Demands List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(demands.itemCount) { index ->
                    val demand = demands[index] ?: MarketDemandModel()
                    DemandCard(demand = demand, onClick = {
                        showContact = true
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
        }
    }

    if (showContact) {
        ModalBottomSheet(
            onDismissRequest = { showContact = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
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
                                    demandsViewModel.countDownTrial(selectedItem.id)
                                    makeCall(item)
                                    showContact = false
                                }
                                .padding(vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPhone == item,
                                onClick = {
                                    demandsViewModel.countDownTrial(selectedItem.id)
                                    makeCall(item)
                                    showContact = false
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
}