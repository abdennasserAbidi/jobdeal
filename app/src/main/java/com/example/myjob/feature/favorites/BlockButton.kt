package com.example.myjob.feature.favorites

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun BlockButton(
    onClick: () -> Unit,
    label: String = "A"
) {
    Box(
        modifier = Modifier
            .size(120.dp, 80.dp)
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cornerRadius = 24.dp.toPx()

            // Back Layer (Shadow for 3D Effect)
            drawRoundRect(
                color = Color.Gray.copy(alpha = 0.6f),
                cornerRadius = CornerRadius(cornerRadius),
                size = size.copy(width = size.width - 8.dp.toPx(), height = size.height - 8.dp.toPx()),
                topLeft = Offset(8.dp.toPx(), 8.dp.toPx())
            )

            // Main Button Layer
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFFC107), Color(0xFFFFA000))
                ),
                cornerRadius = CornerRadius(cornerRadius),
                size = size
            )
        }

        // Add Center Content
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            PlayfulIconContent()
        }
    }
}

@Composable
fun PlayfulIconContent() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo(size.width * 0.5f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(Color.Cyan, Color.Blue)
            )
        )
    }
}