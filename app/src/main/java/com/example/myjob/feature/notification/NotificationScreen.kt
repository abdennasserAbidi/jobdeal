package com.example.myjob.feature.notification

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.domain.entities.notification.NotificationModel
import com.example.myjob.feature.navigation.Screen

@Composable
fun NotificationScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }

    val notifications = notificationViewModel.notifications.collectAsLazyPagingItems()
    val seenNotification by notificationViewModel.seenNotification.collectAsState()

    LaunchedEffect(seenNotification) {
        if (seenNotification == "saved successfully") {
            navController.navigate(Screen.PostScreen.route)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

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
                        text = stringResource(id = R.string.notification_text),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        AnimatedContent(targetState = notifications.itemCount > 0, label = "") {
            if (it) {
                LazyColumn {
                    items(notifications.itemCount) { index ->
                        val item = notifications[index] ?: NotificationModel()
                        var isRead by remember { mutableStateOf(item.read) }
                        val data = notificationViewModel.getSenderData(item)
                        var textName = ""
                        if (data.first != -1) {
                            textName = data.second.split(" ").mapNotNull { text ->
                                text.firstOrNull()
                            }.take(2).joinToString("")

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isRead = true
                                        notificationViewModel.seenNotification(item.idNotification)
                                    }
                                    .background(
                                        if (isRead) Color.Transparent else Color(0xFF25D366).copy(
                                            alpha = 0.1f
                                        )
                                    )
                            ) {

                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .padding(horizontal = 10.dp, vertical = 20.dp),
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
                                                text = textName,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        Column(
                                            modifier = Modifier.padding(start = 10.dp)
                                        ) {
                                            Text(
                                                text = data.second,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(modifier = Modifier.height(5.dp))

                                            Text(text = item.description)
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 10.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                notificationViewModel.removeNotification(item.idNotification)
                                            },
                                        tint = Color.Red,
                                        contentDescription = "delete"
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp
                                )

                            }
                        }

                    }
                    notifications.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { PageLoader(Modifier.fillParentMaxSize()) }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = notifications.refresh() as LoadState.Error
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
                                val error = notifications.loadState.append as LoadState.Error
                                item {
                                    ErrorMessage(
                                        modifier = Modifier,
                                        message = error.error.localizedMessage!!,
                                        onClickRetry = { retry() })
                                }
                            }

                            else -> {

                            }
                        }
                    }

                }
            } else {

            }
        }

    }
}