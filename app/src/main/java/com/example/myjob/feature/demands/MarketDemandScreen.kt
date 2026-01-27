package com.example.myjob.feature.demands

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.feature.posts.FilterPostBottomSheet
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketDemandScreen(
    navController: NavController,
    demandsViewModel: DemandsViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }
    var showSearchSheet by remember { mutableStateOf(false) }

    var showTypeSheet by remember { mutableStateOf(false) }
    var selectedSearch by remember { mutableStateOf(PostType.ALL) }
    var selectedType by remember { mutableStateOf(JobType.NORMAL) }
    val badgeCountNormal by remember { mutableIntStateOf(0) }
    val badgeCountService by remember { mutableIntStateOf(1) }

    val demands = demandsViewModel.demands.collectAsLazyPagingItems()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            selectedType = demandsViewModel.getType()
            Log.i("frzgkrzhgrz", "selectedType: $selectedType")

        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
        }) {
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

                        Spacer(Modifier.align(Alignment.TopCenter).height(20.dp).fillMaxWidth())

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterStart)
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

                        val badgeCount = if (selectedType == JobType.NORMAL) badgeCountNormal
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
                        val demandText = stringResource(id = R.string.demand_text)
                        val offerText = stringResource(id = R.string.offer_text)
                        val normalText = stringResource(id = R.string.normal_text)
                        val logoutText = stringResource(id = R.string.logout_text)
                        Text(
                            text = when (selectedType) {
                                JobType.NORMAL -> normalText
                                JobType.GET -> demandText
                                JobType.SEND -> offerText
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

                        Spacer(Modifier.align(Alignment.BottomCenter).height(20.dp).fillMaxWidth())
                    }


                }
            }

            val allPostsText = stringResource(id = R.string.all_posts_text)
            val internshipText = stringResource(id = R.string.internship_text)
            val eventText = stringResource(id = R.string.type_event_text)
            val formationText = stringResource(id = R.string.type_formation_text)

            FilterChip(
                onClick = { showSearchSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                label = {
                    Text(
                        text = when (selectedSearch) {
                            PostType.ALL -> allPostsText
                            PostType.INTERNSHIP -> internshipText
                            PostType.EVENT -> eventText
                            PostType.WORKSHOP -> formationText
                        }
                    )
                },
                selected = false,
                trailingIcon = {
                    androidx.compose.material.Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Filter",
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WhatsAppLightGreen,
                    selectedLabelColor = WhatsAppDarkGreen
                )
            )

            if (demands.itemCount != 0) {
                val lazyListState = rememberLazyListState()
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                ) {

                    items(demands.itemCount) { index ->
                        val item = demands[index] ?: MarketDemandModel()
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    /*GlobalEntries.isFromNotification = false
                                    GlobalEntries.idAnnounce = item.idAnnounce
                                    GlobalEntries.idCompany = item.idCompany
                                    navController.navigate(Screen.DetailPostScreen.route)*/
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = White,
                                contentColor = White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
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
                                            text = item.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            color = Color.Black,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            text = "${stringResource(id = R.string.posted_by_text)} ${item.companyName}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Black,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Text(
                                        text = item.date ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = item.description,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))



                                Spacer(modifier = Modifier.height(8.dp))

                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp
                                )

                            }
                        }

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
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "nothing to show",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = colorResource(id = R.color.whatsapp)
                    )
                }
            }
        }

        if (showSearchSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSearchSheet = false }
            ) {
                FilterPostBottomSheet(
                    selectedFilter = selectedSearch,
                    onFilterSelected = { filter ->
                        selectedSearch = filter
                        //postsViewModel.getFilteredAnnounceCandidate(filter.name)
                        showSearchSheet = false
                    }
                )
            }
        }
    }
}