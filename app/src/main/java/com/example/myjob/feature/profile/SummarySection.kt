package com.example.myjob.feature.profile

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummarySection(showUser: Map<String, String> = mapOf()) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        showUser.map {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp)
            ) {

                Text(
                    text = "${it.key} :",
                    modifier = Modifier.weight(0.3f),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = it.value,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(0.5f),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}