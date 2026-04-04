package com.example.myjob.common.view

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import java.util.Calendar

@Composable
fun CandidateCard(
    user: User,
    candidate: Candidate,
    onClick: () -> Unit,
    onSendInvitation: (Candidate) -> Unit = {},
    onSendMessage: (User) -> Unit = {},
    onTerminateInvitation: (InvitationModel) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var diffYear by remember { mutableIntStateOf(0) }
    var lastContractExperience by remember { mutableStateOf<Experience?>(null) }

    val experiences = user.experience ?: mutableListOf()

    val lastContractExp = experiences.findLast {
        it.type == "Contract" || it.type == "Contrat"
    }

    LaunchedEffect(lastContractExp) {
        lastContractExperience = lastContractExp
    }

    if (experiences.isNotEmpty()) {
        val firstExp = experiences[0]
        val start = firstExp.dateStart ?: ""

        if (start.isNotEmpty()) {
            val startArray = start.split(" ")
            val yearStart = startArray[1].toInt()

            val calendar: Calendar = Calendar.getInstance()
            val currentYear: Int = calendar.get(Calendar.YEAR)

            diffYear = currentYear - yearStart
        }
    }

    if (user.role == "Candidat" || user.role == "Candidate") {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onClick()
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar with User Type Badge
                    Box {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF049344)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = candidate.name.split(" ").map { it.first() }.take(2)
                                    .joinToString(""),
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // User Type Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF3B82F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "👤",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }


                    // Content
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Name and Status Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = user.fullName ?: "",
                                color = Color(0xFF1F2937),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val yearsText = stringResource(id = R.string.years_text)
                                val textExp = if (diffYear == 0) "No experience"
                                else if (diffYear <= 5) "$diffYear $yearsText exp"
                                else "+$diffYear $yearsText exp"

                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = textExp,
                                    color = Color(0xFF10B981),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Position
                        Text(
                            text = user.preferredActivitySector ?: "",
                            color = Color(0xFF049344),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = user.country ?: "",
                            color = Color(0xFF6B7280),
                            fontSize = 13.sp
                        )
                    }
                }

                // Skills
                val list = user.candidateSkills?.listSkills ?: emptyList()

                val skills = list.joinToString("  .  ")
                if (skills.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = skills,
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(3.dp))
                }

                // Certifications
                val listCertifications = user.candidateSkills?.listCertification ?: emptyList()

                val certifications = listCertifications.joinToString("  .  ")
                if (certifications.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = certifications,
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(10.dp))
                }

                val whatsappGreen = colorResource(id = R.color.whatsapp)
                val invitations = GlobalEntries.user.invitations ?: mutableListOf()
                if (invitations.isNotEmpty()) {
                    val invitation = invitations.filter { it.idTo == user.id }
                    if (invitation.isNotEmpty()) {
                        val isFriend = invitation[invitation.lastIndex].status == InvitationStatus.HIRED.name
                        val isInProcess = invitation[invitation.lastIndex].status == InvitationStatus.IN_PROCESS.name
                        val isPending = invitation[invitation.lastIndex].status == InvitationStatus.ON_HOLD.name
                        val isRejecting = invitation[invitation.lastIndex].status == InvitationStatus.REJECTED.name
                        val notInterested =
                            invitation[invitation.lastIndex].status == InvitationStatus.NOT_INTERESTED.name

                        if (isFriend) {
                            OutlinedButton(
                                onClick = {
                                    onSendMessage(user)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Message,
                                    contentDescription = "Contact",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    stringResource(id = R.string.contact_text),
                                    color = whatsappGreen
                                )
                            }
                        } else if (isInProcess) {
                            Button(
                                onClick = { onTerminateInvitation(invitation[0]) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.terminate_text))
                            }
                        } else if (isPending) {

                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = "timer",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.pending_text))
                            }

                        } else if (notInterested) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                color = Color.Red,
                                text = stringResource(id = R.string.refuse_candidate_text)
                            )

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send Invitation",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.send_invitation_text))
                            }
                        } else if (isRejecting) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                color = Color.Red,
                                text = "${stringResource(id = R.string.refuse_text)}  ${invitation[0].reason}"
                            )

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send Invitation",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.send_invitation_text))
                            }
                        }
                    } else {
                        Button(
                            onClick = { onSendInvitation(candidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.whatsapp),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send Invitation",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.send_invitation_text))
                        }
                    }
                } else {
                    Button(
                        onClick = { onSendInvitation(candidate) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.whatsapp),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send Invitation",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.send_invitation_text))
                    }
                }
            }

        }
    } else {
        val name = user.companyName ?: "Test Test"

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onClick()
                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar
                    /*Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF049344)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.split(" ").map { it.first() }.take(2).joinToString(""),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }*/

                    Box {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF049344)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.split(" ").map { it.first() }.take(2).joinToString(""),
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // User Type Badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFFF59E0B)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🏢",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Content
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Name and Status Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                color = Color(0xFF1F2937),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (user.isVerified == true) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {

                                    Icon(
                                        modifier = Modifier.padding(horizontal = 10.dp),
                                        tint = colorResource(R.color.whatsapp),
                                        painter = painterResource(R.drawable.ic_settings_privacy),
                                        contentDescription = ""
                                    )
                                }
                            }
                        }

                        // Position
                        Text(
                            text = user.companyActivitySector ?: "",
                            color = Color(0xFF049344),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = user.country ?: "",
                            color = Color(0xFF6B7280),
                            fontSize = 13.sp
                        )
                    }
                }

                user.companyDescription?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val whatsappGreen = colorResource(id = R.color.whatsapp)
                val invitations = GlobalEntries.user.invitations ?: mutableListOf()
                if (invitations.isNotEmpty()) {
                    val invitation = invitations.filter { it.idTo == user.id }
                    if (invitation.isNotEmpty()) {
                        val isFriend = invitation[0].status == InvitationStatus.HIRED.name
                        val isInProcess = invitation[0].status == InvitationStatus.IN_PROCESS.name
                        val isPending = invitation[0].status == InvitationStatus.ON_HOLD.name
                        val isRejecting = invitation[0].status == InvitationStatus.REJECTED.name
                        val notInterested =
                            invitation[0].status == InvitationStatus.NOT_INTERESTED.name

                        if (isFriend) {
                            OutlinedButton(
                                onClick = {
                                    onSendMessage(user)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Message,
                                    contentDescription = "Contact",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    stringResource(id = R.string.contact_text),
                                    color = whatsappGreen
                                )
                            }
                        } else if (isInProcess) {
                            Button(
                                onClick = { onTerminateInvitation(invitation[0]) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.terminate_text))
                            }
                        } else if (isPending) {

                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = "timer",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.pending_text))
                            }

                        } else if (notInterested) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                color = Color.Red,
                                text = stringResource(id = R.string.refuse_candidate_text)
                            )

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send Invitation",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.send_invitation_text))
                            }
                        } else if (isRejecting) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                color = Color.Red,
                                text = "${stringResource(id = R.string.refuse_text)}  ${invitation[0].reason}"
                            )

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send Invitation",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.send_invitation_text))
                            }
                        }
                    } else {
                        Button(
                            onClick = { onSendInvitation(candidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.whatsapp),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send Invitation",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.send_invitation_text))
                        }
                    }
                } else {
                    Button(
                        onClick = { onSendInvitation(candidate) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.whatsapp),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send Invitation",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.send_invitation_text))
                    }
                }
            }
        }
    }
}