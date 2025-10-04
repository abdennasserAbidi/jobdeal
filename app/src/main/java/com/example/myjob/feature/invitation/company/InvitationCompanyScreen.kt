package com.example.myjob.feature.invitation.company

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.feature.navigation.Screen

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun InvitationCompanyScreen(
    navController: NavController,
    changeIndexTab: () -> Unit,
    invitationViewModel: InvitationViewModel = hiltViewModel()
) {

    val invitations: LazyPagingItems<InvitationModel> =
        invitationViewModel.invitations.collectAsLazyPagingItems()

    val announcement: LazyPagingItems<AnnouncementModel> =
        invitationViewModel.announcement.collectAsLazyPagingItems()


    var openAnnounceForm by remember { mutableStateOf(false) }
    var openFinishProcess by remember { mutableStateOf(false) }
    var invitationModel by remember { mutableStateOf(InvitationModel()) }
    val selected by remember { mutableStateOf(0) }
    val choiceList by invitationViewModel.choiceList.collectAsState()
    val invitation by invitationViewModel.invitation.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            changeIndexTab()
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
                        //navController.navigate("${Screen.DetailInvitationScreen.route}/${item.idInvitation}")
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

        AnimatedVisibility(
            visible = openAnnounceForm,
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                elevation = 5.dp,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .padding(top = 20.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Close,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    openAnnounceForm = false
                                },
                            contentDescription = ""
                        )

                        Text(
                            text = "Announce",
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val announcementModel by invitationViewModel.announcementModel.collectAsState()
                        var postName by remember { mutableStateOf("Dveloppeur Android") }

                        Column {
                            Text(
                                text = "Post name",
                                modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                                style = TextStyle(
                                    color = colorResource(id = R.color.whatsapp),
                                    fontFamily = FontFamily.Default,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 10.dp)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.whatsapp),
                                        shape = RoundedCornerShape(30.dp)
                                    )
                                    .clip(shape = RoundedCornerShape(30.dp)),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                value = postName,
                                onValueChange = {
                                    postName = it
                                    invitationViewModel.changePostName(it)
                                },
                                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                            )
                        }

                        var descriptions by remember { mutableStateOf("Creer une application pour connecter les entreprises avec les candidats facilement.") }

                        Column {
                            Text(
                                text = "Description",
                                modifier = Modifier.padding(top = 10.dp, start = 20.dp),
                                style = TextStyle(
                                    color = colorResource(id = R.color.whatsapp),
                                    fontFamily = FontFamily.Default,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 10.dp)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.whatsapp),
                                        shape = RoundedCornerShape(30.dp)
                                    )
                                    .clip(shape = RoundedCornerShape(30.dp)),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                value = descriptions,
                                onValueChange = {
                                    descriptions = it
                                    invitationViewModel.changeDescriptions(it)
                                },
                                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(top = 20.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {

                                    invitationViewModel.saveCompanyAnnouncement()
                                    openAnnounceForm = false

                                }
                                .background(
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(30.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Send",
                                modifier = Modifier.padding(
                                    vertical = 20.dp,
                                    horizontal = 20.dp
                                ),
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = FontFamily.Default,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                }
            }

        }
    }
}