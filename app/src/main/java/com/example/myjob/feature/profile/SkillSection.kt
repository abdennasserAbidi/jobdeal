package com.example.myjob.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

@Composable
fun SkillSection() {
    val interactionSource = remember { MutableInteractionSource() }

    Column {
        Text(
            text = stringResource(id = R.string.skills_title_text),
            style = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(id = R.string.skills_desc_text),
            modifier = Modifier.padding(top = 5.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(top = 20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                }
                .background(
                    color = colorResource(id = R.color.whatsapp),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.skills_add_text),
                modifier = Modifier.padding(vertical = 20.dp),
                style = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}