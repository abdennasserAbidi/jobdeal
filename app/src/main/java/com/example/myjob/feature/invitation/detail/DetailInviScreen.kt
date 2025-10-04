package com.example.myjob.feature.invitation.detail

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.domain.entities.Experience

@Composable
fun DetailInviScreen(
    navController: NavController,
    invitationDetailViewModel: InvitationDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {

    val scrollState = rememberScrollState()
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val c by invitationDetailViewModel.candidate.collectAsStateWithLifecycle()
    val invitationModel by invitationDetailViewModel.invitations.collectAsState()
    val invitation = invitationModel.invitationModel

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {

            Box(modifier = Modifier.padding(top = 5.dp)) {
                // Status Card
                InvitationStatusCard(
                    status = invitation.status ?: "",
                    sentDate = invitation.date ?: "",
                    responseDate = invitation.dateEnd,
                    invitationModel = invitation
                )
            }


            val userName = c.fullName ?: "Test Test"

            val experiences = c.experience ?: mutableListOf()
            var lastExp = Experience()
            if (experiences.isNotEmpty()) {
                lastExp = experiences[experiences.lastIndex]
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Candidate Information",
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
                            modifier = Modifier.size(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = WhatsAppGreen
                        ) {

                            val nickname = userName.trim().ifEmpty { "Test Test" }.split(" ")
                                .map { it.first() }.joinToString("")
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = nickname,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            lastExp.title?.let {
                                if (it.isNotEmpty()) {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Info chips
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                lastExp.companyName?.let {
                                    if (it.isNotEmpty()) {
                                        InfoChip(
                                            icon = Icons.Default.Business,
                                            text = it
                                        )
                                    }
                                }

                                InfoChip(
                                    icon = Icons.Default.Work,
                                    text = "5+ years"
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            lastExp.place?.let {
                                if (it.isNotEmpty()) {
                                    InfoChip(
                                        icon = Icons.Default.LocationOn,
                                        text = it
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Invitation Content Card
            Box(modifier = Modifier.padding(top = 5.dp)) {
                InvitationContentCard(
                    subject = invitation.message,
                    message = invitation.description
                )
            }

            // Contract & Fee Details Card
            Box(modifier = Modifier.padding(top = 5.dp)) {
                ContractDetailsCard(invitationModel = invitation)

            }

            // Notes Card (if available)
            invitation.reason?.let { notes ->
                NotesCard(notes = notes)
            }

            // Action Buttons
            /*ActionButtonsSection(
                status = invitation.status,
                invitationModel = invitation
            )*/

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}