package com.example.myjob.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.seenInvitation
import com.example.myjob.common.GlobalEntries.seenMessage
import com.example.myjob.common.GlobalEntries.seenNotifications
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.HomeEntity
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.feature.demands.findActivity
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.posts.FilterPostBottomSheet
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernHomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    clearData: () -> Unit = {}
) {

    val listHomeEntity by homeViewModel.listHomeEntity.collectAsState()
    val fcmToken by homeViewModel.fcmToken.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }
    var showTypeSheet by remember { mutableStateOf(false) }
    var selectedSearch by remember { mutableStateOf(JobType.NORMAL) }

    val invitationCount by homeViewModel.invitationCount.collectAsState()
    val seen by seenInvitation.collectAsState()

    LaunchedEffect(seen) {
        if (seen) homeViewModel.resetCountInvitation()
    }

    val messageCount by homeViewModel.messageCount.collectAsState()
    val seenMessages by seenMessage.collectAsState()

    LaunchedEffect(seenMessages) {
        if (seenMessages) homeViewModel.resetCountMessages()
    }

    val notificationCount by homeViewModel.notificationCount.collectAsState()
    val seenNotification by seenNotifications.collectAsState()

    LaunchedEffect(seenNotification) {
        if (seenNotification) homeViewModel.resetCountNotifications()
    }

    val invitations = homeViewModel.invitations.collectAsLazyPagingItems()
    var count by remember { mutableIntStateOf(0) }
    val badgeCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(invitations.itemCount) {
        invitations.itemSnapshotList.map {
            val status = it?.status ?: ""
            if (status == InvitationStatus.ON_HOLD.name) {
                count += 1
            }
        }
    }


    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            homeViewModel.getUserToken()
        }
    }

    LaunchedEffect(fcmToken) {
        if (fcmToken.isEmpty()) {
            homeViewModel.updateToken()
        }
    }

    val context = LocalContext.current
    BackHandler(enabled = true) {
        context.findActivity()?.finish()
    }

    val demandText = stringResource(id = R.string.demand_text)
    val normalText = stringResource(id = R.string.normal_text)
    val logoutText = stringResource(id = R.string.logout_text)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA)),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {

                val userName = GlobalEntries.user.fullName ?: ""
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
                            text = userName,
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

                            Spacer(Modifier
                                .align(Alignment.TopCenter)
                                .height(20.dp)
                                .fillMaxWidth())

                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 10.dp)
                                    .align(Alignment.CenterStart)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        navController.navigate(Screen.NotificationCompanyScreen.route)
                                    }
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

                            Text(
                                text = when (selectedSearch) {
                                    JobType.NORMAL -> normalText
                                    JobType.GET -> demandText
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

                            Spacer(Modifier
                                .align(Alignment.BottomCenter)
                                .height(20.dp)
                                .fillMaxWidth())
                        }


                    }
                }




            }

            // Main Actions Grid
            item {
                Spacer(modifier = Modifier.height(8.dp))

                MainActionsGrid(
                    listHomeEntity = listHomeEntity,
                    invitationCount = invitationCount,
                    messageCount = messageCount,
                    notificationCount = notificationCount,
                    onMyInvitationsClick = {
                        navController.navigate(Screen.InvitationScreen.route)
                    },
                    onValidationClick = {
                        navController.navigate(Screen.NotificationCompanyScreen.route)
                    },
                    onSettingsClick = {
                        navController.navigate(Screen.SettingScreen.route)
                    },
                    onProfileClick = {
                        //navController.navigate(Screen.ProfileScreen.route)
                        navController.navigate(Screen.SearchWordScreen.route)
                    },
                    onPostClick = {
                        navController.navigate(Screen.CandidatePostScreen.route)
                    },
                    onMessageClick = {
                        navController.navigate(Screen.ListMessagesScreen.route)
                    },
                    invitationsCount = count
                )
            }
        }
    }

    if (showTypeSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTypeSheet = false }
        ) {
            FilterTypeBottomSheet(
                selectedFilter = selectedSearch,
                onFilterSelected = { filter ->
                    selectedSearch = filter
                    if (filter.name == JobType.LOGOUT.name) {
                        homeViewModel.logout()
                        clearData()
                        navController.navigate(Screen.LoginScreen.route)
                    } else if (filter.name == JobType.GET.name) {
                        homeViewModel.offerDemand()
                        navController.navigate(Screen.DemandServiceScreen.route)
                    }
                    showTypeSheet = false
                }
            )
        }
    }
}

@Composable
fun FilterTypeBottomSheet(
    selectedFilter: JobType,
    onFilterSelected: (JobType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.type_change_text),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val demandText = stringResource(id = R.string.demand_text)
        val normalText = stringResource(id = R.string.normal_text)
        val logoutText = stringResource(id = R.string.logout_text)
        JobType.entries.forEach { filter ->

            val color = if (selectedFilter == filter) colorResource(id = R.color.whatsapp) else Transparent

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, color, RoundedCornerShape(10.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onFilterSelected(filter)
                    }
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (filter) {
                        JobType.NORMAL -> normalText
                        JobType.GET -> demandText
                        else -> logoutText
                    },
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainActionsGrid(
    listHomeEntity: List<HomeEntity>,
    invitationCount: Int,
    messageCount: Int,
    notificationCount: Int,
    onMyInvitationsClick: () -> Unit,
    onValidationClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPostClick: () -> Unit,
    onMessageClick: () -> Unit,
    invitationsCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // First Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val invitationItem = listHomeEntity[0]

            ModernActionCard(
                modifier = Modifier.weight(1f),
                title = stringResource(id = invitationItem.title),
                subtitle = stringResource(id = invitationItem.subTitle),
                icon = Icons.Default.Mail,
                iconTint = Color(0xFF25D366),
                backgroundColor = Color.White,
                onClick = onMyInvitationsClick,
                showBadge = invitationCount > 0,
                badgeCount = invitationCount
            )

            val validationItem = listHomeEntity[1]
            ModernActionCard(
                modifier = Modifier.weight(1f),
                title = stringResource(id = validationItem.title),
                subtitle = stringResource(id = validationItem.subTitle),
                icon = Icons.Default.Notifications,
                iconTint = Color(0xFF25D366),
                backgroundColor = Color.White,
                onClick = onValidationClick,
                showBadge = notificationCount > 0,
                badgeCount = notificationCount
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Second Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val postsItem = listHomeEntity[4]
            ModernActionCard(
                modifier = Modifier.weight(1f),
                title = stringResource(id = postsItem.title),
                subtitle = stringResource(id = postsItem.subTitle),
                icon = Icons.Default.Public,
                iconTint = Color(0xFF25D366),
                backgroundColor = Color.White,
                onClick = onPostClick
            )

            val settingsItem = listHomeEntity[5]

            ModernActionCard(
                modifier = Modifier.weight(1f),
                title = stringResource(id = settingsItem.title),
                subtitle = stringResource(id = settingsItem.subTitle),
                icon = Icons.Default.Message,
                iconTint = Color(0xFF25D366),
                backgroundColor = Color.White,
                onClick = onMessageClick,
                showBadge = messageCount > 0,
                badgeCount = messageCount
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Third Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val postsItem = listHomeEntity[3]
            ModernActionCard(
                modifier = Modifier.weight(1f),
                title = stringResource(id = postsItem.title),
                subtitle = stringResource(id = postsItem.subTitle),
                icon = Icons.Default.Person,
                iconTint = Color(0xFF25D366),
                backgroundColor = Color.White,
                onClick = onProfileClick
            )

            val settingsItem = listHomeEntity[2]

            ModernActionCard(
                modifier = Modifier.weight(1f),
                title = stringResource(id = settingsItem.title),
                subtitle = stringResource(id = settingsItem.subTitle),
                icon = Icons.Default.Settings,
                iconTint = Color(0xFF25D366),
                backgroundColor = Color.White,
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
fun ModernActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    showBadge: Boolean = false,
    badgeCount: Int = 0
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "scale"
    )

    Card(
        modifier = modifier
            .height(160.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Badge
            if (showBadge && badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Text
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.padding(top = 4.dp),
                        letterSpacing = 0.5.sp
                    )
                }
            }

            LaunchedEffect(isPressed) {
                if (isPressed) {
                    delay(100)
                    isPressed = false
                }
            }
        }
    }
}