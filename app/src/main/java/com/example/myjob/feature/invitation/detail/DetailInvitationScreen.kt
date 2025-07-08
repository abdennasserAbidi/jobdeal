package com.example.myjob.feature.invitation.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries.role
import com.example.myjob.domain.entities.ContractType
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus

// WhatsApp Green Theme Colors
val WhatsAppGreen = Color(0xFF25D366)
val WhatsAppGreenSurface = Color(0xFFF0F9F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInvitationScreen(
    navController: NavController,
    idInvitation: String = "85",
    invitationDetailViewModel: InvitationDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    val invitation by invitationDetailViewModel.invitations.collectAsState()
    val candidate by invitationDetailViewModel.candidate.collectAsState()
    //idInvitation.toInt()
    invitationDetailViewModel.getInvitationDetail(581)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Invitation Details",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Handle share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = WhatsAppGreen,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            InvitationStatusCard(
                status = invitation.status ?: "",
                sentDate = invitation.date ?: "",
                responseDate = invitation.dateEnd
            )

            // Candidate Info Card
            CandidateDetailCard(candidate = candidate)

            // Invitation Content Card
            InvitationContentCard(
                subject = invitation.message,
                message = invitation.description
            )

            // Contract & Fee Details Card
            ContractDetailsCard(
                invitation
            )
            // Notes Card (if available)
            invitation.reason?.let { notes ->
                NotesCard(notes = notes)
            }

            // Action Buttons
            ActionButtonsSection(
                status = invitation.status,
                invitationModel = invitation,
                onDeleteInvitation = {

                },
                viewProfile = {},
                onTerminateInvitation = {},
                onRejectInvitation = {},
                onAcceptInvitation = {}
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InvitationStatusCard(
    status: String,
    sentDate: String,
    responseDate: String?
) {

    val (statusColor, statusIcon) = when (status) {
        InvitationStatus.ON_HOLD.name -> Pair(Color(0xFFFF9800), Icons.Default.Send)
        InvitationStatus.HIRED.name -> Pair(Color(0xFF2196F3), Icons.Default.Visibility)
        InvitationStatus.IN_PROCESS.name -> Pair(WhatsAppGreen, Icons.Default.CheckCircle)
        InvitationStatus.REJECTED.name -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
        InvitationStatus.NOT_INTERESTED.name -> Pair(Color(0xFFF44336), Icons.Default.Cancel)
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
        }
    }
}

@Composable
fun CandidateDetailCard(candidate: User) {
    val userName = candidate.fullName ?: "Test Test"
    val experiences = candidate.experience ?: mutableListOf()
    var lastExp = Experience()
    if (experiences.isNotEmpty()) {
        lastExp = experiences[experiences.lastIndex]
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.split(" ").map { it.first() }.joinToString(""),
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

            if (role == "Company" || role == "Entreprise") {
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
