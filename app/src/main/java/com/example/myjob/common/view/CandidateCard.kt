package com.example.myjob.common.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.feature.invitation.detail.InfoChip
import com.example.myjob.feature.invitation.detail.WhatsAppGreen
import com.example.myjob.feature.navigation.Screen
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
    var diffYear by remember { mutableStateOf(0) }
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
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header Row - Name, Position and Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Name and Position
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = user.fullName ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            color = Color.Black,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = user.preferredActivitySector ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Status Badge
                    val invitations = GlobalEntries.user.invitations ?: mutableListOf()
                    if (invitations.isNotEmpty()) {
                        val invitation = invitations.filter { it.idTo == user.id }
                        if (invitation.isNotEmpty()) {
                            StatusBadge(status = invitation[0].status ?: "")
                        }
                    } else StatusBadge(status = "")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Work,
                            contentDescription = "Experience",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        val userExperience = user.professionalStatus?.userExperience ?: ""

                        Text(
                            text = userExperience,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.address ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Company and Experience
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Work,
                            contentDescription = "Experience",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        val availability = user.professionalStatus?.availability ?: ""

                        Text(
                            text = availability,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }

                    lastContractExp?.let { lastContractExperience ->
                        val companyName = lastContractExperience.companyName ?: ""
                        if (companyName.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Business,
                                    contentDescription = "work",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = companyName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )
                            }
                        }
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
                        val notInterested = invitation[0].status == InvitationStatus.NOT_INTERESTED.name

                        if (isFriend) {
                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            OutlinedButton(
                                onClick = {
                                    onSendMessage(user)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
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
                                Text(stringResource(id = R.string.contact_text), color = whatsappGreen)
                            }
                        } else if (isInProcess) {
                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            Button(
                                onClick = { onTerminateInvitation(invitation[0]) },
                                modifier = Modifier.fillMaxWidth(),
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
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
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
                                    .padding(top = 10.dp),
                                color = Color.Red,
                                text = stringResource(id = R.string.refuse_candidate_text)
                            )

                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
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
                        else if (isRejecting) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                color = Color.Red,
                                text = "${stringResource(id = R.string.refuse_text)}  ${invitation[0].reason}"
                            )

                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
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
                    else {
                        OutlinedButton(
                            onClick = { onClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, whatsappGreen)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "View Profile",
                                tint = whatsappGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                        }

                        Button(
                            onClick = { onSendInvitation(candidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
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
                else {
                    OutlinedButton(
                        onClick = { onClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, whatsappGreen)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "View Profile",
                            tint = whatsappGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                    }

                    Button(
                        onClick = { onSendInvitation(candidate) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
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
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        user.companyActivitySector?.let {
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
                    user.email?.let {
                        if (it.isNotEmpty()) {
                            InfoChip(
                                icon = Icons.Default.Business,
                                text = it
                            )
                        }
                    }

                    user.country?.let {
                        if (it.isNotEmpty()) {
                            InfoChip(
                                icon = Icons.Default.Work,
                                text = it
                            )
                        }
                    }

                    Column {
                        user.companyAddress?.let {
                            if (it.isNotEmpty()) {
                                InfoChip(
                                    icon = Icons.Default.LocationOn,
                                    text = it
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        user.companySecondAddress?.let {
                            if (it.isNotEmpty()) {
                                InfoChip(
                                    icon = Icons.Default.LocationOn,
                                    text = it
                                )
                            }
                        }

                    }

                    Column {
                        user.phoneCompany?.let {
                            if (it.isNotEmpty()) {
                                InfoChip(
                                    icon = Icons.Default.Phone,
                                    text = it
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        user.secondPhoneCompany?.let {
                            if (it.isNotEmpty()) {
                                InfoChip(
                                    icon = Icons.Default.Phone,
                                    text = it
                                )
                            }
                        }
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
                        val notInterested = invitation[0].status == InvitationStatus.NOT_INTERESTED.name

                        if (isFriend) {
                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            OutlinedButton(
                                onClick = {
                                    onSendMessage(user)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
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
                                Text(stringResource(id = R.string.contact_text), color = whatsappGreen)
                            }
                        } else if (isInProcess) {
                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            Button(
                                onClick = { onTerminateInvitation(invitation[0]) },
                                modifier = Modifier.fillMaxWidth(),
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
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
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
                                    .padding(top = 10.dp),
                                color = Color.Red,
                                text = stringResource(id = R.string.refuse_candidate_text)
                            )

                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
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
                        else if (isRejecting) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                color = Color.Red,
                                text = "${stringResource(id = R.string.refuse_text)}  ${invitation[0].reason}"
                            )

                            OutlinedButton(
                                onClick = { onClick() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, whatsappGreen)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    tint = whatsappGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                            }

                            Button(
                                onClick = { onSendInvitation(candidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
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
                    else {
                        OutlinedButton(
                            onClick = { onClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, whatsappGreen)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "View Profile",
                                tint = whatsappGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                        }

                        Button(
                            onClick = { onSendInvitation(candidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp),
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
                else {
                    OutlinedButton(
                        onClick = { onClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, whatsappGreen)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "View Profile",
                            tint = whatsappGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.view_profile_text), color = whatsappGreen)
                    }

                    Button(
                        onClick = { onSendInvitation(candidate) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
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