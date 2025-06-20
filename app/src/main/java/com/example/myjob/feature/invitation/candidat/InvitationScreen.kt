package com.example.myjob.feature.invitation.candidat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
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
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.common.view.InvitationCard
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationScreen(
    navController: NavController,
    invitationViewModel: InvitationCandidateViewModel = hiltViewModel()
) {

    val invitations = invitationViewModel.invitations.collectAsLazyPagingItems()
    val interactionSource = remember { MutableInteractionSource() }

    var selectedFilter by remember { mutableStateOf(FilterType.ALL) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val fcmToken by invitationViewModel.fcmToken.collectAsState()

    LaunchedEffect(fcmToken) {
        if (fcmToken.isNotEmpty()) {
            invitationViewModel.sendNotification()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.padding(16.dp)) {

            // Filter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "5 candidates found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FilterChip(
                    onClick = { showFilterSheet = true },
                    label = {
                        Text(
                            text = when (selectedFilter) {
                                FilterType.ALL -> "All"
                                FilterType.AVAILABLE -> "Available"
                                FilterType.INTERVIEWING -> "Interviewing"
                                FilterType.HIRED -> "Hired"
                                FilterType.NOT_INTERESTED -> "Not Interested"
                            }
                        )
                    },
                    selected = selectedFilter != FilterType.ALL,
                    trailingIcon = {
                        Icon(
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
            }

            val statusInvitations by invitationViewModel.statusInvitations.collectAsState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
            ) {

                items(invitations.itemCount) { index ->
                    val item = invitations[index] ?: InvitationModel()

                    InvitationCard(
                        statusInvitations = statusInvitations,
                        invitationModel = item,
                        onClick = { /*TODO*/ },
                        onAcceptInvitation = {
                            item.status = InvitationStatus.IN_PROCESS.name
                            invitationViewModel.acceptRejectInvitation(item)

                            invitationViewModel.clearToken()
                            invitationViewModel.getUserToken(item.idCompany)
                        },
                        onRejectInvitation = {
                            item.status = InvitationStatus.NOT_INTERESTED.name
                            invitationViewModel.acceptRejectInvitation(item)

                            invitationViewModel.clearToken()
                            invitationViewModel.getUserToken(item.idCompany)
                        }
                    )
                }

                invitations.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = invitations.loadState.refresh as LoadState.Error
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
                            val error = invitations.loadState.append as LoadState.Error
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = colorResource(id = R.color.whatsapp)),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.popBackStack()
                        },
                    contentDescription = ""
                )

                Text(
                    text = "My Invitations",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false }
        ) {
            FilterBottomSheet(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    selectedFilter = filter
                    showFilterSheet = false
                }
            )
        }
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