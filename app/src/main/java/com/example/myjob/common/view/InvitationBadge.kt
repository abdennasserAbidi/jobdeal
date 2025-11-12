package com.example.myjob.common.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.myjob.R
import com.example.myjob.domain.entities.invitation.InvitationStatus

@Composable
fun InvitationBadge(status: InvitationStatus) {
    val (color, text) = when (status) {
        InvitationStatus.ON_HOLD -> Color(0xFF9E9E9E) to stringResource(id = R.string.on_hold_text)
        InvitationStatus.IN_PROCESS -> Color(0xFF2196F3) to stringResource(id = R.string.in_process_long_text)
        InvitationStatus.HIRED -> Color(0xFF4CAF50) to stringResource(id = R.string.hired_text)
        InvitationStatus.REJECTED -> Color.Red to stringResource(id = R.string.Rejected)
        InvitationStatus.NOT_INTERESTED -> Color.Red to stringResource(id = R.string.not_interested_text)
    }

    Badge(color, text)
}