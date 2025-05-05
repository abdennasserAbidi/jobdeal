package com.example.myjob.common

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class TopInwardCurveShape(
    private val cornerRadius: Float = 70f,
    private val curveWidthFraction: Float = 0.6f,
    private val curveDepth: Float = 90f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height

        val curveWidth = width * curveWidthFraction
        val curveStart = (width - curveWidth) / 2f
        val curveEnd = curveStart + curveWidth

        /*val path = Path().apply {
            moveTo(0f, 0f)

            // Top left to curve start
            lineTo(curveStart, 0f)

            // Inward curve in top center
            quadraticBezierTo(
                width / 2, curveDepth,
                curveEnd, 0f
            )

            // Top right
            lineTo(width, 0f)

            // Down to bottom-right
            lineTo(width, height)

            // Bottom to curve start
            lineTo(curveEnd, height)
            // Bottom center curve
            quadraticBezierTo(
                width / 2, height - curveDepth,
                curveStart, height
            )
            // Bottom-left
            lineTo(0f, height)

            // Close
            close()
        }*/

        val path = Path().apply {
            // Start from top-left corner radius
            moveTo(0f, cornerRadius)

            // Top-left rounded corner
            quadraticBezierTo(0f, 0f, cornerRadius, 0f)

            // Top line to start of curve
            lineTo(curveStart, 0f)

            // Top center dip
            quadraticBezierTo(width / 2f, curveDepth, curveEnd, 0f)

            // Top line to top-right corner
            lineTo(width - cornerRadius, 0f)

            // Top-right rounded corner
            quadraticBezierTo(width, 0f, width, cornerRadius)

            // Right side
            lineTo(width, height - cornerRadius)

            // Bottom-right rounded corner
            quadraticBezierTo(width, height, width - cornerRadius, height)

            // Bottom to start of curve
            lineTo(curveEnd, height)

            // Bottom center dip
            quadraticBezierTo(width / 2f, height - curveDepth, curveStart, height)

            // Bottom to bottom-left corner
            lineTo(cornerRadius, height)

            // Bottom-left rounded corner
            quadraticBezierTo(0f, height, 0f, height - cornerRadius)

            // Close the path
            close()
        }


        return Outline.Generic(path)
    }
}