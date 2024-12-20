package com.example.myjob.feature.favorites

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DuolingoProgressBar(
    progress: Float, // Progress value between 0.0 and 1.0
    modifier: Modifier = Modifier
) {
    // Validate progress to stay between 0 and 1
    val validatedProgress = progress.coerceIn(0f, 1f)

    // Animate the progress smoothly
    val animatedProgress by animateFloatAsState(targetValue = validatedProgress, label = "")

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .height(16.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xFFEDEDED), // Background gray color
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress) // Animate width to represent progress
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF58CC02), // Start vibrant green
                            Color(0xFF46A602)  // End darker green
                        )
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
                )
        )
    }
}