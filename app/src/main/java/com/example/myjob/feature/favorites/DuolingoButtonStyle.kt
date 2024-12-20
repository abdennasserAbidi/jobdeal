package com.example.myjob.feature.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DuolingoButtonStyle(
    val normalColor: Color = Color(0xff00B3FD),
    val pressedColor: Color = Color(0xff009CDC),
    val textColor: Color = Color.White,
    val fontSize: TextUnit = 18.sp,
    val cornerRadius: Dp = 8.dp,
    val offset: Dp = 4.dp,
    val horizontalPadding: Dp = 40.dp,
    val verticalPadding: Dp = 16.dp,
    val customIcon: @Composable (() -> Unit)? = null
)