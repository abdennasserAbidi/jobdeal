package com.example.myjob.feature.invitation.detail

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TypeSpecimen
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.ContractType
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus

@Composable
fun InvitationStatusCard(
    status: String,
    sentDate: String,
    responseDate: String?,
    invitationModel: InvitationModel,
    userCandidate: User = User(),
    userCompany: User = User(),
    viewProfile: (User) -> Unit = {},
    onTerminateInvitation: (InvitationModel) -> Unit = {},
    onRejectInvitation: (InvitationModel) -> Unit = {},
    onAcceptInvitation: (InvitationModel) -> Unit = {}
) {

    val (statusColor, statusIcon) = when (status) {
        InvitationStatus.ON_HOLD.name -> Pair(Color(0xFFFF9800), Icons.Default.Send)
        InvitationStatus.HIRED.name -> Pair(Color(0xFF2196F3), Icons.Default.Visibility)
        InvitationStatus.IN_PROCESS.name -> Pair(WhatsAppGreen, Icons.Default.CheckCircle)
        InvitationStatus.REJECTED.name -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
        InvitationStatus.NOT_INTERESTED.name -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
        else -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
    }

    val statusText = if (GlobalEntries.role == "Company" || GlobalEntries.role == "Entreprise") {
        when (status) {
            InvitationStatus.ON_HOLD.name -> stringResource(id = R.string.title_status_pending_invitation_text, userCandidate.fullName ?: "")
            InvitationStatus.HIRED.name -> stringResource(id = R.string.title_status_hired_invitation_text, userCandidate.fullName ?: "")
            InvitationStatus.IN_PROCESS.name -> stringResource(id = R.string.title_status_process_invitation_text, userCandidate.fullName ?: "")
            InvitationStatus.REJECTED.name -> stringResource(id = R.string.title_status_rejecting_invitation_text, userCandidate.fullName ?: "")
            InvitationStatus.NOT_INTERESTED.name -> stringResource(id = R.string.title_status_not_interested_invitation_text, userCandidate.fullName ?: "")
            else -> stringResource(id = R.string.title_status_pending_invitation_text, userCandidate.fullName ?: "")
        }
    } else {
        when (status) {
            InvitationStatus.ON_HOLD.name -> stringResource(id = R.string.title_status_pending_invitation_candidate_text, userCompany.companyName ?: "")
            InvitationStatus.HIRED.name -> stringResource(id = R.string.title_status_hired_invitation_candidate_text, userCompany.companyName ?: "")
            InvitationStatus.IN_PROCESS.name -> stringResource(id = R.string.title_status_process_invitation_candidate_text, userCompany.companyName ?: "")
            InvitationStatus.REJECTED.name -> stringResource(id = R.string.title_status_rejecting_invitation_candidate_text, userCompany.companyName ?: "")
            InvitationStatus.NOT_INTERESTED.name -> stringResource(id = R.string.title_status_not_interested_invitation_candidate_text, userCompany.companyName ?: "")
            else -> stringResource(id = R.string.title_status_pending_invitation_candidate_text, userCompany.companyName ?: "")
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = statusColor
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                statusIcon,
                                contentDescription = status,
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                        Text(
                            text = stringResource(id = R.string.title_date_sent_text, sentDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (responseDate != null) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_date_response_text),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = responseDate,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (GlobalEntries.role == "Company" || GlobalEntries.role == "Entreprise") {

                when (status) {
                    InvitationStatus.HIRED.name -> {
                        OutlinedButton(
                            onClick = { viewProfile(userCandidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "View Profile",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.view_profile_text))
                        }
                    }

                    InvitationStatus.REJECTED.name -> {
                        OutlinedButton(
                            onClick = { viewProfile(userCandidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "View Profile",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.view_profile_text))
                        }
                    }

                    InvitationStatus.NOT_INTERESTED.name -> {
                        OutlinedButton(
                            onClick = { viewProfile(userCandidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "View Profile",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.view_profile_text))
                        }
                    }

                    InvitationStatus.ON_HOLD.name -> {
                        OutlinedButton(
                            onClick = { viewProfile(userCandidate) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(id = R.string.view_profile_text))
                        }
                    }

                    InvitationStatus.IN_PROCESS.name -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedButton(
                                onClick = { viewProfile(userCandidate) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text))
                            }

                            Button(
                                onClick = { onTerminateInvitation(invitationModel) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
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
                        }
                    }
                }
            } else {
                when (status) {
                    InvitationStatus.NOT_INTERESTED.name -> {
                        Text(
                            text = stringResource(id = R.string.rejected_offer_text),
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )
                    }

                    InvitationStatus.IN_PROCESS.name -> {
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("${stringResource(id = R.string.in_process_text)}...")
                        }
                    }

                    InvitationStatus.HIRED.name -> {

                        Text(
                            text = stringResource(id = R.string.hire_text),
                            color = colorResource(id = R.color.whatsapp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )
                    }

                    InvitationStatus.REJECTED.name -> {

                        Text(
                            text = "${stringResource(id = R.string.refuse_text)} \n ${invitationModel.reason}",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onRejectInvitation(invitationModel) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.reject_text))
                            }

                            Button(
                                onClick = { onAcceptInvitation(invitationModel) },
                                modifier = Modifier.weight(1f),
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
                                Text(stringResource(id = R.string.accept_text))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InvitationContentCard(
    status: String,
    subject: String,
    message: String,
    onUpdate: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.invitation_content_text),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                val role = GlobalEntries.role
                val isCompany = role == "Company" || role == "Entreprise"
                val isNotHired = status != InvitationStatus.HIRED.name
                if (isCompany && isNotHired) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onUpdate()
                            },
                        contentDescription = "edit"
                    )
                }
            }

            // Subject
            DetailRow(
                icon = Icons.Default.Subject,
                label = "Subject",
                value = subject
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Message
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Message,
                        contentDescription = "Message",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Message",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ContractDetailsCard(
    invitationModel: InvitationModel
) {

    val typeContract by remember { mutableStateOf(invitationModel.typeContract) }
    val descriptionContract by remember { mutableStateOf(invitationModel.descriptionContract) }
    val duration by remember { mutableStateOf(invitationModel.duration ?: "") }
    val nameContract by remember { mutableStateOf(invitationModel.nameContract) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhatsAppGreenSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.contract_detail_text),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Contract Type
            DetailRow(
                icon = if (typeContract == ContractType.CONTRACT.name) Icons.Default.Work else Icons.Default.Person,
                label = stringResource(id = R.string.work_type_text),
                value = if (typeContract == ContractType.CONTRACT.name) "Contract" else "Freelance"
            )

            Spacer(modifier = Modifier.height(12.dp))


            if (typeContract == ContractType.CONTRACT.name) {
                DetailRow(
                    icon = Icons.Default.TypeSpecimen,
                    label = stringResource(id = R.string.contract_type_text),
                    value = nameContract
                )
            } else if (typeContract == ContractType.FREELANCE.name) {
                DetailRow(
                    icon = Icons.Default.Timer,
                    label = stringResource(id = R.string.duration_text),
                    value = duration
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // description
            DetailRow(
                icon = Icons.Default.Description,
                label = stringResource(id = R.string.contract_description_text),
                value = descriptionContract
            )
        }
    }
}

@Composable
fun NotesCard(notes: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun ActionButtonsSection(
    status: String?,
    invitationModel: InvitationModel,
    onDeleteInvitation: (InvitationModel) -> Unit = {},
    viewProfile: (InvitationModel) -> Unit = {},
    onTerminateInvitation: (InvitationModel) -> Unit = {},
    onRejectInvitation: (InvitationModel) -> Unit = {},
    onAcceptInvitation: (InvitationModel) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            if (GlobalEntries.role == "Company" || GlobalEntries.role == "Entreprise") {
                when (status) {
                    InvitationStatus.HIRED.name -> {
                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Delete Invitation",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.delete_invitation_text))
                        }
                    }

                    InvitationStatus.REJECTED.name -> {
                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Delete Invitation",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.delete_invitation_text))
                        }
                    }

                    InvitationStatus.NOT_INTERESTED.name -> {
                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Send Invitation",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.delete_invitation_text))
                        }
                    }

                    InvitationStatus.ON_HOLD.name -> {
                        OutlinedButton(
                            onClick = { viewProfile(invitationModel) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(id = R.string.view_profile_text))
                        }
                    }

                    InvitationStatus.IN_PROCESS.name -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedButton(
                                onClick = { viewProfile(invitationModel) },
                                modifier = Modifier.fillMaxWidth(0.7f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.view_profile_text))
                            }

                            Button(
                                onClick = { onTerminateInvitation(invitationModel) },
                                modifier = Modifier.fillMaxWidth(0.7f),
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
                        }
                    }
                }
            } else {
                when (status) {
                    InvitationStatus.NOT_INTERESTED.name -> {
                        Text(
                            text = "You rejected this offer",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    InvitationStatus.IN_PROCESS.name -> {
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("${stringResource(id = R.string.in_process_text)}...")
                        }
                    }

                    InvitationStatus.HIRED.name -> {

                        Text(
                            text = stringResource(id = R.string.hire_text),
                            color = colorResource(id = R.color.whatsapp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { /*onDeleteInvitation(invitationModel)*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Delete Invitation",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.delete_invitation_text))
                        }
                    }

                    InvitationStatus.REJECTED.name -> {

                        Text(
                            text = "${stringResource(id = R.string.refuse_text)} \n ${invitationModel.reason}",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { /*onDeleteInvitation(invitationModel)*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Delete Invitation",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.delete_invitation_text))
                        }
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onRejectInvitation(invitationModel) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "View Profile",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(id = R.string.reject_text))
                            }

                            Button(
                                onClick = { onAcceptInvitation(invitationModel) },
                                modifier = Modifier.weight(1f),
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
                                Text(stringResource(id = R.string.accept_text))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun InfoChip(
    icon: ImageVector,
    text: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
