package com.example.myjob.feature.invitation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.ContractType
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus

@Composable
fun InvitationDetailsScreen(
    invitation: InvitationModel,
    status: String,
    userCandidate: User = User(),
    userCompany: User = User(),
    onBack: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    viewProfile: (User) -> Unit = {},
    onTerminateInvitation: (InvitationModel) -> Unit = {},
    onRejectInvitation: (InvitationModel) -> Unit = {},
    onAcceptInvitation: (InvitationModel) -> Unit = {}
) {
    var showRejectDialog by rememberSaveable { mutableStateOf(false) }
//        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (GlobalEntries.role == "Company" || GlobalEntries.role == "Entreprise") {
                when (status) {
                    InvitationStatus.HIRED.name -> {

                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = true,
                            isRejected = false,
                            isEnabled = true,
                            text = stringResource(id = R.string.view_profile_text),
                            other = { viewProfile(userCandidate) },
                            onAccept = {},
                            onReject = {}
                        )
                    }

                    InvitationStatus.REJECTED.name -> {
                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = false,
                            isRejected = true,
                            isEnabled = true,
                            text = stringResource(id = R.string.view_profile_text),
                            other = { viewProfile(userCandidate) },
                            onAccept = {},
                            onReject = {}
                        )
                    }

                    InvitationStatus.NOT_INTERESTED.name -> {
                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = false,
                            isRejected = true,
                            isEnabled = true,
                            text = stringResource(id = R.string.view_profile_text),
                            other = { viewProfile(userCandidate) },
                            onAccept = {},
                            onReject = {}
                        )
                    }

                    InvitationStatus.ON_HOLD.name -> {

                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = false,
                            isRejected = true,
                            isEnabled = true,
                            text = stringResource(id = R.string.pending_text),
                            other = { viewProfile(userCandidate) },
                            onAccept = {},
                            onReject = {}
                        )
                    }

                    InvitationStatus.IN_PROCESS.name -> {

                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = false,
                            isRejected = false,
                            isEnabled = true,
                            text = stringResource(id = R.string.view_profile_text),
                            textTwo = stringResource(id = R.string.terminate_text),
                            other = {},
                            onAccept = {viewProfile(userCandidate)},
                            onAcceptTwo = {onTerminateInvitation(invitation)},
                            onReject = {}
                        )
                    }
                }
            } else {
                when (status) {
                    InvitationStatus.NOT_INTERESTED.name -> {

                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = false,
                            isRejected = true,
                            isEnabled = false,
                            text = stringResource(id = R.string.rejected_offer_text)
                        )
                    }

                    InvitationStatus.IN_PROCESS.name -> {
                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = false,
                            isRejected = true,
                            isEnabled = false,
                            text = stringResource(id = R.string.in_process_text)
                        )
                    }

                    InvitationStatus.HIRED.name -> {

                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = true,
                            isRejected = false,
                            isEnabled = false,
                            text = stringResource(id = R.string.hire_text)
                        )
                    }

                    InvitationStatus.REJECTED.name -> {

                        ActionBar(
                            isLoading = isLoading,
                            isAccepted = false,
                            isRejected = true,
                            isEnabled = false,
                            text = "${stringResource(id = R.string.refuse_text)} \n ${invitation.reason}",
                        )
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onRejectInvitation(invitation) },
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
                                onClick = { onAcceptInvitation(invitation) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp),
                                    contentColor = White
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
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState()),
        ) {
            HeroHeader(invitation = invitation, onBack = {
                onBack()
            })

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp),
            ) {
                OfferSection(subject = invitation.message, message = invitation.description)

                ContractSection(
                    workType = invitation.nameContract,
                    contractType = invitation.typeContract,
                    description = invitation.descriptionContract,
                )

                CompanySection(
                    email = userCompany.email ?: "",
                    country = userCompany.country ?: "",
                )
            }
        }
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            shape = RoundedCornerShape(24.dp),
            title = { Text("Decline this invitation?") },
            text = {
                Text("${invitation.companyName} will be notified that you declined. You can't undo this.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRejectDialog = false
                        onReject()
                    },
                ) {
                    Text("Decline", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) { Text("Keep it") }
            },
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  Hero                                                                       */
/* -------------------------------------------------------------------------- */

@Composable
private fun HeroHeader(
    invitation: InvitationModel,
    userCandidate: User = User(),
    userCompany: User = User(),
    onBack: () -> Unit,
) {
    val primary = WhatsAppGreen
    val gradient = Brush.verticalGradient(listOf(primary, lerp(primary, Color.Black, 0.28f)))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
            .background(gradient)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top row: back button + title
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.16f)),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = White,
                )
            }
            Text(
                text = "Invitation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = White,
            )
        }

        Spacer(Modifier.height(20.dp))

        // Company avatar
        Box(
            modifier = Modifier
                .size(84.dp)
                .border(4.dp, White.copy(alpha = 0.35f), CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(White),
            contentAlignment = Center,
        ) {
            Text(
                text = invitation.companyName.take(1).uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = primary,
            )
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = invitation.companyName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = userCompany.companyActivitySector ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = White.copy(alpha = 0.85f),
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusPill(invitation.status ?: "")
            HeroPill {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Sent ${invitation.date}",
                    style = MaterialTheme.typography.labelLarge,
                    color = White,
                )
            }
        }
    }
}

@Composable
private fun HeroPill(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.18f))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
private fun StatusPill(
    status: String,
    userCompany: User = User(),
    userCandidate: User = User()
) {

    val statusText = if (GlobalEntries.role == "Company" || GlobalEntries.role == "Entreprise") {
        when (status) {
            InvitationStatus.ON_HOLD.name -> stringResource(
                id = R.string.title_status_pending_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.HIRED.name -> stringResource(
                id = R.string.title_status_hired_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.IN_PROCESS.name -> stringResource(
                id = R.string.title_status_process_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.REJECTED.name -> stringResource(
                id = R.string.title_status_rejecting_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.NOT_INTERESTED.name -> stringResource(
                id = R.string.title_status_not_interested_invitation_text,
                userCandidate.fullName ?: ""
            )

            else -> stringResource(
                id = R.string.title_status_pending_invitation_text,
                userCandidate.fullName ?: ""
            )
        }
    } else {
        when (status) {
            InvitationStatus.ON_HOLD.name -> stringResource(
                id = R.string.title_status_pending_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.HIRED.name -> stringResource(
                id = R.string.title_status_hired_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.IN_PROCESS.name -> stringResource(
                id = R.string.title_status_process_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.REJECTED.name -> stringResource(
                id = R.string.title_status_rejecting_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.NOT_INTERESTED.name -> stringResource(
                id = R.string.title_status_not_interested_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            else -> stringResource(
                id = R.string.title_status_pending_invitation_candidate_text,
                userCompany.companyName ?: ""
            )
        }
    }

    val dotColor = when (status) {
        InvitationStatus.ON_HOLD.name -> Color(0xFFFFC145)
        InvitationStatus.HIRED.name -> Color(0xFF8CFFB4)
        InvitationStatus.IN_PROCESS.name -> WhatsAppGreen
        InvitationStatus.REJECTED.name -> Color(0xFFF44336)
        InvitationStatus.NOT_INTERESTED.name -> Color(0xFFFF9C94)
        else -> Color(0xFFF44336)
    }
    HeroPill {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelLarge,
            color = White,
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  Sections                                                                   */
/* -------------------------------------------------------------------------- */

@Composable
private fun OfferSection(subject: String, message: String) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionTitle("Invitation")

        Text(
            text = subject,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        // Quote-style message: accent bar + soft tint
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(14.dp))
                .background(WhatsAppGreen.copy(alpha = 0.07f)),
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(WhatsAppGreen),
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            )
        }
    }
}

@Composable
private fun ContractSection(
    workType: String,
    contractType: String,
    description: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Contract details")
        Panel {
            InfoRow(Icons.Rounded.Work, "Preferred work type", workType)
            PanelDivider()
            InfoRow(Icons.Rounded.Badge, "Contract type", contractType)
            PanelDivider()
            InfoRow(Icons.Rounded.Description, "Description", description)
        }
    }
}

@Composable
private fun CompanySection(email: String, country: String) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Company contact")
        Panel {
            InfoRow(Icons.Rounded.Email, "Email", email)
            PanelDivider()
            InfoRow(Icons.Rounded.LocationOn, "Location", country)
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  Building blocks                                                            */
/* -------------------------------------------------------------------------- */

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun Panel(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) { content() }
    }
}

@Composable
private fun PanelDivider() {
    // Indented so it lines up with the text, not the icon
    HorizontalDivider(
        modifier = Modifier.padding(start = 70.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f),
    )
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(WhatsAppGreen.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = WhatsAppGreen,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  Sticky bottom actions                                                      */
/* -------------------------------------------------------------------------- */

@Composable
private fun ActionBar(
    isLoading: Boolean,
    isAccepted: Boolean,
    isRejected: Boolean,
    text: String,
    textTwo: String = "",
    isEnabled: Boolean,
    onAccept: () -> Unit = {},
    onAcceptTwo: () -> Unit = {},
    onReject: () -> Unit = {},
    onRejectTwo: () -> Unit = {},
    other: () -> Unit = {},
) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding()
                .background(Cyan),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            if (isAccepted || isRejected) {
                OutlinedButton(
                    onClick = { other() },
                    enabled = isEnabled,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(text, fontWeight = FontWeight.SemiBold)
                }
            } else {
                OutlinedButton(
                    onClick = onReject,
                    enabled = !isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(text, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onAccept,
                    enabled = !isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(textTwo, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  Preview (replace InvitationTheme with your app theme)                      */
/* -------------------------------------------------------------------------- */

@Composable
fun InvitationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF05943F),
            onPrimary = White,
            background = Color(0xFFF5F8F6),
            surface = White,
            onSurface = Color(0xFF13201A),
            onSurfaceVariant = Color(0xFF55645B),
            outlineVariant = Color(0xFFDCE5DF),
            error = Color(0xFFC62828),
        ),
        content = content,
    )
}

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
            InvitationStatus.ON_HOLD.name -> stringResource(
                id = R.string.title_status_pending_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.HIRED.name -> stringResource(
                id = R.string.title_status_hired_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.IN_PROCESS.name -> stringResource(
                id = R.string.title_status_process_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.REJECTED.name -> stringResource(
                id = R.string.title_status_rejecting_invitation_text,
                userCandidate.fullName ?: ""
            )

            InvitationStatus.NOT_INTERESTED.name -> stringResource(
                id = R.string.title_status_not_interested_invitation_text,
                userCandidate.fullName ?: ""
            )

            else -> stringResource(
                id = R.string.title_status_pending_invitation_text,
                userCandidate.fullName ?: ""
            )
        }
    } else {
        when (status) {
            InvitationStatus.ON_HOLD.name -> stringResource(
                id = R.string.title_status_pending_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.HIRED.name -> stringResource(
                id = R.string.title_status_hired_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.IN_PROCESS.name -> stringResource(
                id = R.string.title_status_process_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.REJECTED.name -> stringResource(
                id = R.string.title_status_rejecting_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            InvitationStatus.NOT_INTERESTED.name -> stringResource(
                id = R.string.title_status_not_interested_invitation_candidate_text,
                userCompany.companyName ?: ""
            )

            else -> stringResource(
                id = R.string.title_status_pending_invitation_candidate_text,
                userCompany.companyName ?: ""
            )
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
    invitationModel: InvitationModel,
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
                val isNotHired = status == InvitationStatus.ON_HOLD.name

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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, WhatsAppGreen, RoundedCornerShape(8.dp)),
                    contentAlignment = Center
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(Modifier.padding(top = 16.dp))

            val typeContract by remember { mutableStateOf(invitationModel.typeContract) }
            val descriptionContract by remember { mutableStateOf(invitationModel.descriptionContract) }
            val duration by remember { mutableStateOf(invitationModel.duration ?: "") }
            val nameContract by remember { mutableStateOf(invitationModel.nameContract) }

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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
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
