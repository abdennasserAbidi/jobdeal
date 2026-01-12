package com.example.myjob.feature.messagerie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.common.GlobalEntries.otherUserId
import com.example.myjob.common.GlobalEntries.otherUserName
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.feature.navigation.Screen
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ListMessageScreen(
    navController: NavController,
    viewModel: DiscussionViewModel = hiltViewModel(),
    hideNavigation: () -> Unit = {}
) {

    val conversations = viewModel.conversations.collectAsLazyPagingItems()

    val refreshing = conversations.loadState.refresh is LoadState.Loading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { conversations.refresh() }
    )

    val interactionSource = remember { MutableInteractionSource() }

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
                    verticalAlignment = Alignment.CenterVertically
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
                        text = "Messages",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 20.dp),
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)
                .pullRefresh(pullRefreshState)
        ) {

            if (conversations.itemCount > 0) {
                val lazyListState = rememberLazyListState()
                LazyColumn(
                    state = lazyListState
                ) {
                    items(conversations.itemCount) { index ->
                        val item = conversations[index] ?: ChatMessage()
                        if (!viewModel.isOwnMessage(item.userConnectedId)) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    val isOwnUser = viewModel.isOwnMessage(item.userConnectedId)
                                    otherUserId = if (isOwnUser) item.userReceivedId
                                    else item.userConnectedId

                                    viewModel.getUserById(item.userConnectedId)

                                    otherUserName = if (isOwnUser) item.userReceivedName
                                    else item.userConnectedName

                                    navController.navigate(Screen.SendMessageScreen.route)
                                }
                            ) {

                                Row(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(colorResource(id = R.color.whatsapp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item.userConnectedName.split(" ").mapNotNull { it.firstOrNull() }.take(2)
                                                .joinToString(""),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(text = item.userConnectedName)
                                }

                                Icon(
                                    imageVector = Icons.Default.ArrowForwardIos,
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    tint = colorResource(id = R.color.whatsapp),
                                    contentDescription = "Forward")
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    conversations.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { PageLoader(Modifier.fillParentMaxSize()) }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = loadState.refresh as LoadState.Error
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
                                val error = loadState.append as LoadState.Error
                                item {
                                    ErrorMessage(
                                        modifier = Modifier,
                                        message = error.error.localizedMessage ?: "",
                                        onClickRetry = { retry() })
                                }
                            }

                            else -> {

                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

            } else {
                Text(
                    text = "Vous n'avez pas notifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

    }


}