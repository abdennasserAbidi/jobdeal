package com.example.myjob.feature.favorites

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun Realistic3DButton(
    topColors: List<Color> = listOf(Color(0xFFFFC107), Color(0xFFFFA000)),
    edgeColor: Color = Color(0xFF795548),
    shadowColor: Color = Color.Gray
) {
    Box(
        modifier = Modifier
            .size(120.dp, 80.dp)
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cornerRadius = 24.dp.toPx()

            // Bottom Shadow Layer
            drawRoundRect(
                color = shadowColor.copy(alpha = 0.5f),
                size = size.copy(width = size.width - 12.dp.toPx(), height = size.height - 12.dp.toPx()),
                cornerRadius = CornerRadius(cornerRadius),
                topLeft = Offset(12.dp.toPx(), 12.dp.toPx())
            )

            // Edge Layer
            drawRoundRect(
                color = edgeColor,
                size = size.copy(width = size.width - 6.dp.toPx(), height = size.height - 6.dp.toPx()),
                cornerRadius = CornerRadius(cornerRadius),
                topLeft = Offset(6.dp.toPx(), 6.dp.toPx())
            )

            // Top Layer
            drawRoundRect(
                brush = Brush.linearGradient(colors = topColors),
                cornerRadius = CornerRadius(cornerRadius),
                size = size
            )
        }

        // Center Content
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            StarIconContent()
        }
    }
}

@Composable
fun StarIconContent() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            val midX = size.width / 2
            val midY = size.height / 2
            moveTo(midX, 0f)
            lineTo(midX * 1.2f, midY * 0.7f)
            lineTo(size.width, midY)
            lineTo(midX * 1.4f, midY * 1.3f)
            lineTo(midX * 1.6f, size.height)
            lineTo(midX, midY * 1.6f)
            lineTo(midX * 0.4f, size.height)
            lineTo(midX * 0.6f, midY * 1.3f)
            lineTo(0f, midY)
            lineTo(midX * 0.8f, midY * 0.7f)
            close()
        }

        // Star Shape
        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(Color.Yellow, Color(0xFFFFD700))
            )
        )
    }
}
