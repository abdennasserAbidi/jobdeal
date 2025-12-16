package com.example.myjob.feature.invitation.company

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.userForCompany
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.common.view.CompanyInvitationCard
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update

@Composable
fun InvitationCompanyScreen(
    navController: NavController,
    changeIndexTab: () -> Unit,
    invitationViewModel: InvitationViewModel = hiltViewModel()
) {

    val invitations: LazyPagingItems<InvitationModel> =
        invitationViewModel.invitations.collectAsLazyPagingItems()

    var openFinishProcess by remember { mutableStateOf(false) }
    var invitationModel by remember { mutableStateOf(InvitationModel()) }
    val invitation by invitationViewModel.invitation.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            changeIndexTab()
        }
    }

    val isRefreshing by GlobalEntries.isRefreshing.collectAsState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            invitationViewModel.getCompanyInvitations()
            GlobalEntries.isRefreshing.update { false }
        }
    }

    if (openFinishProcess) {
        EnProcessForm(invitationModel,
            onDismissRequest = {
                openFinishProcess = false
            },
            onConfirmation = {
                invitationViewModel.finishProcess(it)
                openFinishProcess = false
            })
    }

    Box(
        modifier = Modifier.fillMaxSize()

    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 66.dp)
                .padding(horizontal = 16.dp)
        ) {

            items(invitations.itemCount) { index ->
                var item = invitations[index] ?: InvitationModel()
                invitationModel = item

                if (item.idInvitation == invitation.invitationModel.idInvitation)
                    item = invitation.invitationModel

                CompanyInvitationCard(
                    invitationModel = item,
                    onClick = {
                        GlobalEntries.idInvitation = item.idInvitation
                        navController.navigate(Screen.NormalDetailInvitationScreen.route)
                    },
                    viewProfile = {
                        userForCompany = User()
                        userForCompany.id = it.idTo
                        userForCompany.fullName = it.fullName
                        navController.navigate(Screen.DetailScreen.route)
                    },
                    onTerminateInvitation = {
                        openFinishProcess = true
                    },
                    onDeleteInvitation = {

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
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
                    text = stringResource(id = R.string.invitations_text),
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
}