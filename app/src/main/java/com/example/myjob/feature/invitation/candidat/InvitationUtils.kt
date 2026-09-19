package com.example.myjob.feature.invitation.candidat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus

@Composable
fun InvitationsPreviewTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF05943F),
            onPrimary = Color.White,
            background = Color(0xFFF5F8F6),
            surface = Color.White,
            onSurface = Color(0xFF13201A),
            onSurfaceVariant = Color(0xFF55645B),
            outlineVariant = Color(0xFFDCE5DF),
            error = Color(0xFFC62828),
        ),
        content = content,
    )
}

enum class InvitationFilter(val label: String) {
    ALL("All"),
    ON_HOLD("Pending"),
    INTERVIEWING("Interviewing"),
    NOT_INTERESTED("NotInterested"),
    ACCEPTED("Accepted"),
    REJECTED("Declined");

    fun matches(status: String): Boolean = when (this) {
        ALL -> true
        ON_HOLD -> status == FilterType.ON_HOLD.name
        ACCEPTED -> status == FilterType.HIRED.name
        REJECTED -> status == FilterType.REJECTED.name
        INTERVIEWING -> status == FilterType.INTERVIEWING.name
        else -> status == FilterType.NOT_INTERESTED.name
    }
}
@Composable
fun ListHeader(pendingCount: Int, onBack: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    val gradient = Brush.verticalGradient(listOf(primary, lerp(primary, Color.Black, 0.28f)))

    val subtitle = when (pendingCount) {
        0 -> "You're all caught up"
        1 -> "1 invitation waiting for your reply"
        else -> "$pendingCount invitations waiting for your reply"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(gradient)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.16f)),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                text = "My invitations",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
            )
        }
    }
}

@Composable
fun FilterRow(
    invitations: List<InvitationModel>,
    selected: InvitationFilter,
    onSelect: (InvitationFilter) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(InvitationFilter.entries) { filter ->
            val isSelected = filter == selected
            val count = invitations.count { filter.matches(it.status ?: "") }
            val contentColor =
                if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant

            Surface(
                selected = isSelected,
                onClick = { onSelect(filter) },
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface,
                contentColor = contentColor,
                border = if (isSelected) null
                else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = filter.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  Card                                                                       */
/* -------------------------------------------------------------------------- */

@Composable
fun InvitationCard(
    item: InvitationModel,
    onClick: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                CompanyAvatar(name = item.companyName)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.message,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Business,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = item.companyName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                StatusChip(item.status ?: "")
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.CalendarToday,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Sent ${item.date}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (item.status == InvitationStatus.ON_HOLD.name) {
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        onClick = onReject,
                        modifier = Modifier.height(40.dp),
                    ) {
                        Text(
                            text = "Decline",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Accept", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun CompanyAvatar(name: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.take(1).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun StatusChip(status: String) {
    val (label, container, content) = when (status) {
        InvitationStatus.ON_HOLD.name -> Triple("Pending", Color(0xFFFFF1D0), Color(0xFF8A5A00))
        InvitationStatus.HIRED.name -> Triple(
            "Accepted",
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            MaterialTheme.colorScheme.primary,
        )
        InvitationStatus.REJECTED.name -> Triple(
            "Declined",
            MaterialTheme.colorScheme.error.copy(alpha = 0.10f),
            MaterialTheme.colorScheme.error,
        )
        else -> Triple("Pending", Color(0xFFFFF1D0), Color(0xFF8A5A00))
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = content,
        modifier = Modifier
            .clip(CircleShape)
            .background(container)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

/* -------------------------------------------------------------------------- */
/*  Empty state                                                                */
/* -------------------------------------------------------------------------- */

@Composable
fun EmptyState(filter: InvitationFilter, modifier: Modifier = Modifier) {
    val body = if (filter == InvitationFilter.ALL) {
        "When a company invites you to work with them, it will show up here."
    } else {
        "You have no ${filter.label.lowercase()} invitations right now."
    }

    Column(
        modifier = modifier.padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Inbox,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp),
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No invitations here",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
fun InvitationsListScreen(
    invitations: LazyPagingItems<InvitationModel>,
    onBack: () -> Unit,
    onInvitationClick: (id: Int) -> Unit,
    onAccept: (item: InvitationModel) -> Unit,
    onReject: (item: InvitationModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedFilter by rememberSaveable { mutableStateOf(InvitationFilter.ALL) }
    var pendingReject by remember { mutableStateOf<InvitationModel?>(null) }

    val visible = invitations.itemSnapshotList.filter { selectedFilter.matches(it?.status ?: "") }
    //val pendingCount = invitations.itemCount { it.status == InvitationStatus.ON_HOLD }
    val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ListHeader(pendingCount = 0, onBack = onBack)

        FilterRow(
            invitations = invitations.itemSnapshotList.items,
            selected = selectedFilter,
            onSelect = { selectedFilter = it },
        )

        if (visible.isEmpty()) {
            EmptyState(
                filter = selectedFilter,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = 24.dp + navBottom,
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                items(invitations.itemCount) { index ->
                    val item = invitations[index] ?: InvitationModel()

                    InvitationCard(
                        item = item,
                        onClick = { onInvitationClick(item.idInvitation) },
                        onAccept = { onAccept(item) },
                        onReject = { pendingReject = item },
                    )
                }
            }
        }
    }

    pendingReject?.let { item ->
        AlertDialog(
            onDismissRequest = { pendingReject = null },
            shape = RoundedCornerShape(24.dp),
            title = { Text("Decline this invitation?") },
            text = { Text("${item.companyName} will be notified that you declined. You can't undo this.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReject(item)
                        pendingReject = null
                    },
                ) { Text("Decline", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { pendingReject = null }) { Text("Keep it") }
            },
        )
    }
}
