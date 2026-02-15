package com.example.myjob.feature.home

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.feature.demands.EmptyState
import com.example.myjob.feature.demands.ServiceCategory
import com.example.myjob.feature.demands.ToolCategory
import com.example.myjob.feature.demands.UserServiceCard
import com.example.myjob.feature.demands.findActivity

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceUserScreen(
    navController: NavController,
    makeCall: (String) -> Unit = {},
    clearData: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val user = homeViewModel.userService.collectAsLazyPagingItems()

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

    LaunchedEffect(listFilter) {
        if (listFilter.isNotEmpty())
            homeViewModel.searchUserService(listFilter)
        else homeViewModel.getAllUserService()
    }

    var selectedItem by remember { mutableStateOf(User()) }
    val interactionSource = remember { MutableInteractionSource() }
    var showSearchSheet by remember { mutableStateOf(false) }

    var showContact by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var selectedPhone by remember { mutableStateOf("") }
    var selectedSearch by remember { mutableStateOf(PostType.ALL) }
    var selectedType by remember { mutableStateOf(JobType.NORMAL) }
    val badgeCountNormal by remember { mutableIntStateOf(0) }
    val badgeCountService by remember { mutableIntStateOf(1) }
    var isUpdating by remember { mutableStateOf(false) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            homeViewModel.getAllUserService()
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
                ) {
                    Text(
                        text = "Services",
                        fontSize = 24.sp,
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }
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
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    placeholder = { Text("Rechercher un candidat...", color = Color(0xFF9CA3AF)) },
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

                // Category Filter
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                                            listFilter =
                                                (listFilter + category.displayName).toMutableList()
                                    } else {
                                        if (listFilter.contains(category.displayName))
                                            listFilter =
                                                (listFilter - category.displayName).toMutableList()
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
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (user.itemCount > 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(user.itemCount) { index ->
                        val userItem = user[index] ?: User()
                        UserServiceCard(
                            user = userItem,
                            isNotMe = homeViewModel.isNotMe(userItem.id ?: -1),
                            showContacts = {
                                selectedItem = userItem
                                showContact = true
                            },
                            openMenu = {
                                selectedItem = userItem
                                showMenu = true
                            },
                            onClick = {
                                selectedItem = userItem
                                /*idDemand = userItem.id
                                navController.navigate(Screen.DemandMarketDetailScreen.route)*/
                            })
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    user.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = user.loadState.refresh as LoadState.Error
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
                                val error = user.loadState.append as LoadState.Error
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
                    selectedItem.phoneList?.forEach { item ->
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
                                homeViewModel.countDownTrial(selectedItem.id ?: -1)
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
}