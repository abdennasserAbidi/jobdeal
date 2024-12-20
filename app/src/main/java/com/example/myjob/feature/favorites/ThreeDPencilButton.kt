package com.example.myjob.feature.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

sealed class Perspective() {
    data class Left(
        val bottomEdgeColor: Color, val rightEdgeColor: Color
    ) : Perspective()

    data class Right(
        val topEdgeColor: Color, val leftEdgeColor: Color
    ) : Perspective()

    data class Top(
        val bottomEdgeColor: Color
    ) : Perspective()
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreeDimensionalLayout(
    perspective: Perspective,
    edgeOffset: Dp = 16.dp,
    onClick: (isPressed: Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val offsetInPx = with(LocalDensity.current) {
        edgeOffset.toPx()
    }

    val elevationOffset by remember {
        derivedStateOf {
            if (isPressed) {
                when (perspective) {
                    is Perspective.Left -> {
                        IntOffset(offsetInPx.toInt(), offsetInPx.toInt())
                    }

                    is Perspective.Right -> {
                        IntOffset(-offsetInPx.toInt(), -offsetInPx.toInt())
                    }

                    is Perspective.Top -> {
                        IntOffset(0, offsetInPx.toInt())
                    }
                }
            } else {
                IntOffset.Zero
            }
        }
    }

    val hapticFeedBack = LocalHapticFeedback.current

    Box(modifier = Modifier
        .combinedClickable(interactionSource = interactionSource, indication = null, onClick = {
            hapticFeedBack.performHapticFeedback(HapticFeedbackType.LongPress)
        })
        .graphicsLayer {
            rotationX = when (perspective) {
                is Perspective.Top -> {
                    16f
                }

                else -> {
                    0f
                }
            }
        }
        .drawBehind {
            onClick(isPressed)
            if (isPressed.not()) {
                when (perspective) {
                    is Perspective.Left -> {
                        // right edge
                        val rightEdge = Path().apply {
                            moveTo(size.width, 0f)
                            lineTo(size.width + offsetInPx, offsetInPx)
                            lineTo(size.width + offsetInPx, size.height + offsetInPx)
                            lineTo(size.width, size.height)
                            close()
                        }
                        // bottom edge
                        val bottomEdge = Path().apply {
                            moveTo(size.width, size.height)
                            lineTo(size.width + offsetInPx, size.height + offsetInPx)
                            lineTo(offsetInPx, size.height + offsetInPx)
                            lineTo(0f, size.height)
                            close()
                        }
                        drawPath(
                            path = rightEdge, color = perspective.rightEdgeColor, style = Fill
                        )
                        drawPath(
                            path = bottomEdge, color = perspective.bottomEdgeColor, style = Fill
                        )
                    }

                    is Perspective.Top -> {
                        // bottom edge
                        val bottomEdge = Path().apply {
                            moveTo(0f, size.height)
                            lineTo(size.width, size.height)
                            lineTo(size.width - offsetInPx, size.height + offsetInPx)
                            lineTo(offsetInPx, size.height + offsetInPx)
                            close()
                        }
                        drawPath(
                            path = bottomEdge, color = perspective.bottomEdgeColor, style = Fill
                        )
                    }

                    is Perspective.Right -> {
                        val topEdge = Path().apply {
                            moveTo(-offsetInPx, -offsetInPx)
                            lineTo(size.width - offsetInPx, -offsetInPx)
                            lineTo(size.width, 0f)
                            lineTo(0f, 0f)
                            close()
                        }
                        val leftEdge = Path().apply {
                            moveTo(-offsetInPx, -offsetInPx)
                            lineTo(0f, 0f)
                            lineTo(0f, size.height)
                            lineTo(-offsetInPx, size.height - offsetInPx)
                            close()
                        }
                        drawPath(path = topEdge, color = perspective.topEdgeColor, style = Fill)
                        drawPath(
                            path = leftEdge, color = perspective.leftEdgeColor, style = Fill
                        )
                    }
                }
            }
        }, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.offset {
                elevationOffset
            }, contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}