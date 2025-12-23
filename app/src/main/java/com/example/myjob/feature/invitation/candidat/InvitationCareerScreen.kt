package com.example.myjob.feature.invitation.candidat

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.view.InvitationCard
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun InvitationCareerScreen(
    navController: NavController,
    invitationViewModel: InvitationCandidateViewModel = hiltViewModel()
) {

    val invitations = invitationViewModel.otherInvitations.collectAsLazyPagingItems()
    val interactionSource = remember { MutableInteractionSource() }

    var selectedFilter by remember { mutableStateOf(FilterType.ALL) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var acceptRejectInvitation by remember { mutableStateOf("") }

    val fcmToken by invitationViewModel.fcmToken.collectAsState()

    LaunchedEffect(fcmToken) {
        if (fcmToken.isNotEmpty()) {
            val title = GlobalEntries.user.fullName ?: ""
            val message = "This candidate has $acceptRejectInvitation your invitaion"
            //invitationViewModel.sendNotification(title, message)
        }
    }

    var searchContractOpen by remember { mutableStateOf(false) }
    var searchContract by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {


        Column(modifier = Modifier.fillMaxWidth()) {

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

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                expanded = searchContractOpen,
                onExpandedChange = {
                    searchContractOpen = !searchContractOpen
                }
            ) {

                OutlinedTextField(
                    value = searchContract,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.all_user_text)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = searchContractOpen) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.whatsapp),
                        focusedLabelColor = colorResource(id = R.color.whatsapp)
                    )
                )
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
                        onClick = { /*TODO*/ },
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

        val list = listOf(
            stringResource(id = R.string.intern_user_text),
            stringResource(id = R.string.trainer_user_text),
            stringResource(id = R.string.event_user_text),
            stringResource(id = R.string.all_user_text),
        )

        AnimatedVisibility(
            visible = searchContractOpen,
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
            GenericSearch(
                mListOfJobs = list,
                onDismissRequest = {
                    searchContractOpen = false
                },
                onSelectedBank = { text, index ->
                    searchContract = text
                    invitationViewModel.searchListInvitation(text)
                    searchContractOpen = false
                },
                title = "Select contract"
            )
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
                    showFilterSheet = false
                }
            )
        }
    }
}