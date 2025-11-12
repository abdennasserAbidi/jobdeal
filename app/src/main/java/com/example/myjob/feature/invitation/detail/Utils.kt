package com.example.myjob.feature.invitation.detail

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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Subject
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
    onDeleteInvitation: (InvitationModel) -> Unit = {},
    viewProfile: (InvitationModel) -> Unit = {},
    onTerminateInvitation: (InvitationModel) -> Unit = {},
    onRejectInvitation: (InvitationModel) -> Unit = {},
    onAcceptInvitation: (InvitationModel) -> Unit = {}
) {

    val (statusColor, statusIcon) = when (status) {
        stringResource(id = R.string.on_hold_text) -> Pair(Color(0xFFFF9800), Icons.Default.Send)
        stringResource(id = R.string.hired_text) -> Pair(Color(0xFF2196F3), Icons.Default.Visibility)
        stringResource(id = R.string.in_process_text) -> Pair(WhatsAppGreen, Icons.Default.CheckCircle)
        stringResource(id = R.string.Rejected) -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
        stringResource(id = R.string.not_interested_text) -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
        else -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
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
                            text = status,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                        Text(
                            text = "Sent on $sentDate",
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
                            text = "Response",
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
                    stringResource(id = R.string.hired_text) -> {
                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
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

                    stringResource(id = R.string.Rejected) -> {
                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
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

                    stringResource(id = R.string.not_interested_text) -> {
                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
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

                    stringResource(id = R.string.on_hold_text) -> {
                        OutlinedButton(
                            onClick = { viewProfile(invitationModel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(id = R.string.view_profile_text))
                        }
                    }

                    stringResource(id = R.string.in_process_text) -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedButton(
                                onClick = { viewProfile(invitationModel) },
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
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
                                    .fillMaxWidth(0.7f)
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
                    stringResource(id = R.string.not_interested_text) -> {
                        Text(
                            text = stringResource(id = R.string.rejected_offer_text),
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )
                    }

                    stringResource(id = R.string.in_process_text) -> {
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

                    stringResource(id = R.string.hired_text) -> {

                        Text(
                            text = stringResource(id = R.string.hire_text),
                            color = colorResource(id = R.color.whatsapp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )

                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
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

                    stringResource(id = R.string.Rejected) -> {

                        Text(
                            text = "${stringResource(id = R.string.refuse_text)} \n ${invitationModel.reason}",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { onDeleteInvitation(invitationModel) },
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
fun InvitationContentCard(
    subject: String,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Invitation Content",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

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
                text = "Contract & Fee Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Contract Type
            DetailRow(
                icon = if (invitationModel.typeContract == ContractType.CONTRACT.name) Icons.Default.Work else Icons.Default.Person,
                label = "Contract Type",
                value = if (invitationModel.typeContract == ContractType.CONTRACT.name) "Contract" else "Freelance"
            )

            Spacer(modifier = Modifier.height(12.dp))



            // Fee Information
            if (invitationModel.typeContract == ContractType.FREELANCE.name) {
                DetailRow(
                    icon = Icons.Default.AttachMoney,
                    label = "TGM",
                    value = invitationModel.tgm
                )
            } else if (invitationModel.typeContract == ContractType.CONTRACT.name) {
                DetailRow(
                    icon = Icons.Default.AttachMoney,
                    label = stringResource(id = R.string.salary_text),
                    value = invitationModel.salary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payment Terms
            DetailRow(
                icon = Icons.Default.Schedule,
                label = "Contract descriiption",
                value = invitationModel.descriptionContract
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
        color = MaterialTheme.colorScheme.surfaceVariant
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
