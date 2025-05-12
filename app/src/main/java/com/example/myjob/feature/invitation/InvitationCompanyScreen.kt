package com.example.myjob.feature.invitation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.tablayout.CustomTab
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
    invitationViewModel: InvitationViewModel = hiltViewModel()
) {

    val invitations: LazyPagingItems<InvitationModel> =
        invitationViewModel.invitations.collectAsLazyPagingItems()

    val announcement: LazyPagingItems<AnnouncementModel> =
        invitationViewModel.announcement.collectAsLazyPagingItems()


    var openAnnounceForm by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(0) }
    val choiceList by invitationViewModel.choiceList.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.fillMaxSize()

    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTab(
                    items = choiceList,
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    selectedItemIndex = selected,
                    onClick = {
                        selected = it
                    }
                )
            }

            AnimatedContent(
                targetState = selected,
                transitionSpec = {
                    fadeIn(tween(300)) with fadeOut(tween(300))
                },
                label = "ListToBoxTransition"
            ) { index ->
                if (index == 0) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)
                    ) {

                        items(invitations.itemCount) { index ->
                            val user = invitations[index] ?: InvitationModel()

                            Card(
                                elevation = 10.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 10.dp),
                                shape = RectangleShape
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            /*GlobalEntries.userForCompany = user
                                            navController.navigate(Screen.DetailScreen.route)*/
                                        }
                                ) {

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = user.message,
                                            modifier = Modifier.align(Alignment.CenterStart),
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        )

                                        val color = when (user.status) {
                                            stringResource(id = R.string.holding) -> Color.Gray
                                            stringResource(id = R.string.Accepted) -> colorResource(
                                                R.color.whatsapp
                                            )

                                            else -> Color.Red
                                        }

                                        user.status?.let {
                                            Text(
                                                text = it,
                                                modifier = Modifier.align(Alignment.CenterEnd),
                                                color = color,
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp
                                                )
                                            )
                                        }
                                    }

                                    Text(
                                        text = "sended to ${user.fullName} - ${user.date}",
                                        modifier = Modifier.padding(top = 5.dp),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 16.sp
                                        )
                                    )

                                    Text(
                                        text = user.description,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp
                                        ),
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(top = 5.dp)
                                    )

                                }

                            }
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
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp)
                    ) {

                        items(announcement.itemCount) { index ->
                            val user = announcement[index] ?: AnnouncementModel()

                            Card(
                                elevation = 10.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 10.dp),
                                shape = RectangleShape
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            /*GlobalEntries.userForCompany = user
                                            navController.navigate(Screen.DetailScreen.route)*/
                                        }
                                ) {

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = user.title,
                                            modifier = Modifier.align(Alignment.CenterStart),
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        )
                                        user.date?.let { date ->
                                            Text(
                                                text = date,
                                                color = Color.LightGray,
                                                modifier = Modifier.align(Alignment.CenterEnd),
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp
                                                )
                                            )
                                        }
                                    }

                                    Text(
                                        text = user.description,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp
                                        ),
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(top = 5.dp)
                                    )

                                    Box(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                                        Button(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            shape = RoundedCornerShape(30.dp),
                                            contentPadding = PaddingValues(14.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorResource(id = R.color.whatsapp),
                                                contentColor = colorResource(id = R.color.whatsapp),
                                                disabledContainerColor = colorResource(id = R.color.whatsapp),
                                                disabledContentColor = colorResource(id = R.color.whatsapp)
                                            ),
                                            onClick = {
                                                //apply
                                            }) {

                                            Text(
                                                text = "Apply",
                                                color = Color.White,
                                                style = TextStyle(
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 14.sp
                                                )
                                            )

                                        }
                                    }
                                }

                            }
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


        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 90.dp),
            onClick = {
                if (selected == 0)
                    navController.navigate(Screen.HomeScreen.route)
                else openAnnounceForm = true
            },
            containerColor = colorResource(id = R.color.whatsapp),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = Color.White,
                contentDescription = "Small floating action button."
            )
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