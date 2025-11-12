package com.example.myjob.common.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.myjob.domain.entities.CandidateStatus

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
