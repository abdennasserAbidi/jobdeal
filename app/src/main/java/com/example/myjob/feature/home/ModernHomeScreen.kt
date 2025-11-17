package com.example.myjob.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.HomeEntity
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun ModernHomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    clearData: () -> Unit = {}
) {

    GlobalEntries.scheduleFileDownload(LocalContext.current, homeViewModel.getPDFName())

    val listHomeEntity by homeViewModel.listHomeEntity.collectAsState()
    val fcmToken by homeViewModel.fcmToken.collectAsState()

    val invitations = homeViewModel.invitations.collectAsLazyPagingItems()
    var count by remember { mutableStateOf(0) }

    invitations.itemSnapshotList.map {
        val status = it?.status ?: ""
        if (status == stringResource(id = R.string.on_hold_text) || status == "Holding") {
            count += 1
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


    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA)),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Modern Header with Gradient
            item {
                ModernHeader {
                    homeViewModel.logout()
                    clearData()
                    navController.navigate(Screen.LoginScreen.route)
                }
            }

            // Main Actions Grid
            item {
                Spacer(modifier = Modifier.height(8.dp))

                MainActionsGrid(
                    listHomeEntity = listHomeEntity,
                    onMyInvitationsClick = {
                        navController.navigate(Screen.InvitationScreen.route)
                    },
                    onValidationClick = {
                        navController.navigate(Screen.ValidateProfileCandidateScreen.route)
                    },
                    onSettingsClick = {
                        navController.navigate(Screen.SettingScreen.route)
                    },
                    onProfileClick = {
                        //navController.navigate(Screen.ProfileScreen.route)
                        navController.navigate(Screen.SearchWordScreen.route)
                    },
                    invitationsCount = count
                )
            }
        }
    }
}

@Composable
fun ModernHeader(
    logout: () -> Unit
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = stringResource(id = R.string.hello),
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Action Icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = {
                        logout()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MainActionsGrid(
    listHomeEntity: List<HomeEntity>,
    onMyInvitationsClick: () -> Unit,
    onValidationClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
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
                showBadge = invitationsCount > 0,
                badgeCount = invitationsCount
            )

            val validationItem = listHomeEntity[1]
            ModernActionCard(
                modifier = Modifier.weight(1f),
                title = stringResource(id = validationItem.title),
                subtitle = stringResource(id = validationItem.subTitle),
                icon = Icons.Default.Verified,
                iconTint = Color(0xFF25D366),
                backgroundColor = Color.White,
                onClick = onValidationClick
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Second Row
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