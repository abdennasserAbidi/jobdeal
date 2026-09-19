package com.example.myjob.feature.invitation.candidat

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.countInvitationPending
import com.example.myjob.common.GlobalEntries.seenInvitation
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun InvitationScreen(
    navController: NavController,
    invitationViewModel: InvitationCandidateViewModel = hiltViewModel()
) {

    val invitations = invitationViewModel.invitations.collectAsLazyPagingItems()

    val refreshing = invitations.loadState.refresh is LoadState.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { invitations.refresh() }
    )


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

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            seenInvitation.update { true }
            countInvitationPending.update { 0 }
        }
    }

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
    val statusInvitations by invitationViewModel.statusInvitations.collectAsState()

    InvitationsPreviewTheme {
        InvitationsListScreen(
            invitations = invitations,
            onBack = {
                navController.popBackStack()
            },
            onInvitationClick = { item ->
                GlobalEntries.idInvitation = item
                navController.navigate(Screen.NormalDetailInvitationScreen.route)

            },
            onAccept = { item ->
                item.status = InvitationStatus.IN_PROCESS.name
                invitationViewModel.acceptRejectInvitation(item)
                invitationViewModel.clearToken()
                invitationViewModel.getUserToken(item.idCompany)
                acceptRejectInvitation = "accept"

            },
            onReject = { item ->
                item.status = InvitationStatus.NOT_INTERESTED.name
                invitationViewModel.acceptRejectInvitation(item)

                invitationViewModel.clearToken()
                invitationViewModel.getUserToken(item.idCompany)
                acceptRejectInvitation = "refuse"
            }
        )
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