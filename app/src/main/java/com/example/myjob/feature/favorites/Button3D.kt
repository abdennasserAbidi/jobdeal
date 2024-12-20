package com.example.myjob.feature.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Button3D(
    content: @Composable (isPressed: Boolean) -> Unit,
    onClick: () -> Unit,
    style: DuolingoButtonStyle = DuolingoButtonStyle(),
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(

        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            )
            .drawWithCache {
                onDrawBehind {
                    drawRoundRect(
                        color = style.pressedColor,
                        cornerRadius = CornerRadius(
                            style.cornerRadius.toPx(),
                            style.cornerRadius.toPx()
                        ),
                        size = Size(
                            size.width,
                            size.height
                        )
                    )
                    if (!isPressed) {
                        drawRoundRect(
                            color = style.normalColor,
                            cornerRadius = CornerRadius(
                                style.cornerRadius.toPx(),
                                style.cornerRadius.toPx()
                            ),
                            size = Size(
                                size.width,
                                size.height - style.offset.toPx()
                            )
                        )
                    }
                }
            },

        ) {

        content(isPressed)
    }
}