package com.example.myjob.common.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.myjob.R
import com.example.myjob.domain.entities.CandidateStatus
import com.example.myjob.domain.entities.invitation.InvitationStatus

@Composable
fun StatusBadge(status: CandidateStatus) {
    val (color, text) = when (status) {
        CandidateStatus.AVAILABLE -> Color(0xFF4CAF50) to "Available"
        CandidateStatus.INTERVIEWING -> Color(0xFFFF9800) to "In process..."
        CandidateStatus.HIRED -> Color(0xFF2196F3) to "Hired"
        CandidateStatus.NOT_INTERESTED -> Color(0xFF9E9E9E) to "Not Interested"
    }

    Badge(color, text)
}

@Composable
fun StatusBadge(status: String) {
    val (color, text) = when (status) {
        stringResource(id = R.string.on_hold_text) -> Color(0xFFFF9800) to stringResource(id = R.string.pending_text)
        stringResource(id = R.string.holding) -> Color(0xFFFF9800) to stringResource(id = R.string.pending_text)
        "Holding" -> Color(0xFFFF9800) to stringResource(id = R.string.pending_text)
        InvitationStatus.IN_PROCESS.name-> Color(0xFF2196F3) to stringResource(id = R.string.in_process_long_text)
        InvitationStatus.HIRED.name -> Color(0xFF9E9E9E) to stringResource(id = R.string.hired_text)
        InvitationStatus.REJECTED.name -> Color(0xFF9E9E9E) to stringResource(id = R.string.Rejected)
        InvitationStatus.NOT_INTERESTED.name -> Color(0xFF9E9E9E) to stringResource(id = R.string.not_interested_text)
        else -> Color(0xFF4CAF50) to stringResource(id = R.string.available_text)
    }

    Badge(color, text)
}
