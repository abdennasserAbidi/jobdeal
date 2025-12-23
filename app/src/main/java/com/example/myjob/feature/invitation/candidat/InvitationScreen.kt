package com.example.myjob.feature.invitation.candidat

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.base.MyApp
import com.example.myjob.base.StateApp
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.view.InvitationCard
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationScreen(
    navController: NavController,
    invitationViewModel: InvitationCandidateViewModel = hiltViewModel()
) {

    val invitations = invitationViewModel.invitations.collectAsLazyPagingItems()

    val invitationChoices by invitationViewModel.invitationChoices.collectAsState()
    val selectionChoices by invitationViewModel.selectionChoices.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    var selectedFilter by remember { mutableStateOf(FilterType.ALL) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var isFilterContract by remember { mutableStateOf(false) }
    var isFilterStatus by remember { mutableStateOf(false) }
    var acceptRejectInvitation by remember { mutableStateOf("") }

    val fcmToken by invitationViewModel.fcmToken.collectAsState()
    val filter by invitationViewModel.filter.collectAsState()
    val listFilter by invitationViewModel.listFilterSv.collectAsState()

    LaunchedEffect(listFilter) {
        isFilterContract = listFilter.isNotEmpty()
        invitationViewModel.validateFilter(filter)
    }

    LaunchedEffect(filter.type) {
        isFilterStatus = filter.type != "All Candidates"
        invitationViewModel.validateFilter(filter)
    }

    val isRefreshing by GlobalEntries.isRefreshing.collectAsState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            invitationViewModel.getInvitations()
            GlobalEntries.isRefreshing.update { false }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.whatsapp))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.invitations_text),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val invitationFound = stringResource(id = R.string.invitation_found_text)
                Text(
                    text = "${invitations.itemCount} $invitationFound",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FilterChip(
                    onClick = { showFilterSheet = true },
                    label = {
                        Text(
                            text = when (selectedFilter) {
                                FilterType.ALL -> stringResource(id = R.string.all_user_text)
                                FilterType.INTERVIEWING -> stringResource(id = R.string.interviewing_text)
                                FilterType.HIRED -> stringResource(id = R.string.hired_text)
                                FilterType.NOT_INTERESTED -> stringResource(id = R.string.not_interested_text)
                                FilterType.ON_HOLD -> stringResource(id = R.string.on_hold_text)
                                FilterType.REJECTED -> stringResource(id = R.string.Rejected)
                            }
                        )
                    },
                    selected = false,
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

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                itemsIndexed(
                    items = invitationChoices
                ) { index, item ->
                    val title = stringResource(id = item.title)

                    val textColor = if (selectionChoices[index]) Color.White else Color.Black
                    val color =
                        colorResource(id = if (selectionChoices[index]) R.color.whatsapp else R.color.lighter_gray)

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
                                invitationViewModel.changeChoice(
                                    index,
                                    !selectionChoices[index],
                                    title
                                )
                                GlobalEntries.isVisibleNav.update { false }
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

            val statusInvitations by invitationViewModel.statusInvitations.collectAsState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
            ) {

                items(invitations.itemCount) { index ->
                    val item = invitations[index] ?: InvitationModel()
                    val statusCandidate = if (item.status != InvitationStatus.ON_HOLD.name)
                        item.status else statusInvitations

                    InvitationCard(
                        statusInvitations = statusCandidate ?: "",
                        invitationModel = item,
                        onClick = {
                            GlobalEntries.idInvitation = item.idInvitation
                            navController.navigate(Screen.NormalDetailInvitationScreen.route)
                        },
                        onAcceptInvitation = {
                            item.status = InvitationStatus.IN_PROCESS.name
                            invitationViewModel.acceptRejectInvitation(item)

                            invitationViewModel.clearToken()
                            invitationViewModel.getUserToken(item.idCompany)
                            acceptRejectInvitation = "accept"
                        },
                        onRejectInvitation = {
                            item.status = InvitationStatus.NOT_INTERESTED.name
                            invitationViewModel.acceptRejectInvitation(item)

                            invitationViewModel.clearToken()
                            invitationViewModel.getUserToken(item.idCompany)
                            acceptRejectInvitation = "refuse"
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

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
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false }
        ) {
            FilterBottomSheet(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter, name ->
                    selectedFilter = filter
                    invitationViewModel.changeTypeStatus(name)
                    showFilterSheet = false
                }
            )
        }
    }
}

@Composable
fun FilterBottomSheet(
    selectedFilter: FilterType,
    onFilterSelected: (FilterType, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.filter_status_text),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val allCandidatesText = stringResource(id = R.string.all_candidates_text)
        val inProcessText = stringResource(id = R.string.in_process_long_text)
        val hiredText = stringResource(id = R.string.hired_text)
        val notInterestedText = stringResource(id = R.string.not_interested_text)
        val onHoldText = stringResource(id = R.string.on_hold_text)
        val rejectedText = stringResource(id = R.string.Rejected)
        FilterType.values().forEach { filter ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val text = when (filter) {
                            FilterType.ALL -> allCandidatesText
                            FilterType.INTERVIEWING -> InvitationStatus.IN_PROCESS.name
                            FilterType.HIRED -> InvitationStatus.HIRED.name
                            FilterType.NOT_INTERESTED -> InvitationStatus.NOT_INTERESTED.name
                            FilterType.ON_HOLD -> InvitationStatus.ON_HOLD.name
                            FilterType.REJECTED -> InvitationStatus.REJECTED.name
                        }
                        onFilterSelected(filter, text)
                    }
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedFilter == filter,
                    onClick = {
                        val text = when (filter) {
                            FilterType.ALL -> allCandidatesText
                            FilterType.INTERVIEWING -> InvitationStatus.IN_PROCESS.name
                            FilterType.HIRED -> InvitationStatus.HIRED.name
                            FilterType.NOT_INTERESTED -> InvitationStatus.NOT_INTERESTED.name
                            FilterType.ON_HOLD -> InvitationStatus.ON_HOLD.name
                            FilterType.REJECTED -> InvitationStatus.REJECTED.name
                        }
                        onFilterSelected(filter, text)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.whatsapp),
                        unselectedColor = colorResource(id = R.color.whatsapp)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (filter) {
                        FilterType.ALL -> allCandidatesText
                        FilterType.INTERVIEWING -> inProcessText
                        FilterType.HIRED -> hiredText
                        FilterType.NOT_INTERESTED -> notInterestedText
                        FilterType.ON_HOLD -> onHoldText
                        FilterType.REJECTED -> rejectedText
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}