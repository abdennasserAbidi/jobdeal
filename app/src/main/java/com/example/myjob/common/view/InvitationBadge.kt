package com.example.myjob.common.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.myjob.domain.entities.invitation.InvitationStatus

@Composable
fun InvitationBadge(status: InvitationStatus) {
    val (color, text) = when (status) {
        InvitationStatus.ON_HOLD -> Color(0xFF9E9E9E) to "On hold"
        InvitationStatus.IN_PROCESS -> Color(0xFF2196F3) to "In process..."
        InvitationStatus.HIRED -> Color(0xFF4CAF50) to "Hired"
        InvitationStatus.REJECTED -> Color.Red to "Rejected"
        InvitationStatus.NOT_INTERESTED -> Color.Red to "Not Interested"
    }

    Badge(color, text)
}