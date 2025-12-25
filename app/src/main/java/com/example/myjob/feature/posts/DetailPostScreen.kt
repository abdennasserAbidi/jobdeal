package com.example.myjob.feature.posts

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.isFromNotification
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.invitation.detail.InfoChip
import com.example.myjob.feature.invitation.detail.WhatsAppGreen
import com.example.myjob.feature.navigation.Screen

@Composable
fun DetailPostScreen(
    navController: NavController,
    postsViewModel: PostsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {

    val scrollState = rememberScrollState()
    val interactionSource = remember { MutableInteractionSource() }

    val userCompany by postsViewModel.companyDetail.collectAsStateWithLifecycle()
    val announcement by postsViewModel.post.collectAsStateWithLifecycle()

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            postsViewModel.getCompanyDetail(GlobalEntries.idCompany)
            postsViewModel.getPostById(GlobalEntries.idAnnounce, GlobalEntries.idCompany)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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

                IconButton(
                    onClick = {
                        if (isFromNotification) {
                            navController.popBackStack(Screen.HomeScreen.route, false)
                            isFromNotification = false
                        } else navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterStart)
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp)
        ) {

            val name = userCompany.companyName ?: "Test Test"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.company_information_text),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Initials Avatar
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = WhatsAppGreen
                        ) {
                            val nickname = name.trim().ifEmpty { "Test Test" }.split(" ")
                                .map { it.first() }.joinToString("")
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = nickname,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            userCompany.companyActivitySector?.let {
                                if (it.isNotEmpty()) {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Info chips
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        userCompany.email?.let {
                            if (it.isNotEmpty()) {
                                InfoChip(
                                    icon = Icons.Default.Business,
                                    text = it
                                )
                            }
                        }

                        userCompany.country?.let {
                            if (it.isNotEmpty()) {
                                InfoChip(
                                    icon = Icons.Default.Work,
                                    text = it
                                )
                            }
                        }

                        Column {
                            userCompany.companyAddress?.let {
                                if (it.isNotEmpty()) {
                                    InfoChip(
                                        icon = Icons.Default.LocationOn,
                                        text = it
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            userCompany.companySecondAddress?.let {
                                if (it.isNotEmpty()) {
                                    InfoChip(
                                        icon = Icons.Default.LocationOn,
                                        text = it
                                    )
                                }
                            }

                        }

                        Column {
                            userCompany.phoneCompany?.let {
                                if (it.isNotEmpty()) {
                                    InfoChip(
                                        icon = Icons.Default.Phone,
                                        text = it
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            userCompany.secondPhoneCompany?.let {
                                if (it.isNotEmpty()) {
                                    InfoChip(
                                        icon = Icons.Default.Phone,
                                        text = it
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Invitation Content Card
            /*Box(modifier = Modifier.padding(top = 15.dp)) {
                InvitationContentCard(
                    status = invitation.status ?: "",
                    subject = invitation.message,
                    message = invitation.description,
                    onUpdate = {
                        GlobalEntries.candidateUser = userCandidate
                        navController.navigate(Screen.SendInvitationScreen.route)
                    }
                )
            }*/

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}