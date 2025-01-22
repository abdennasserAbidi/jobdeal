package com.example.myjob.feature.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.domain.entities.Experience

@Composable
fun CareerSection(
    experiences: List<Experience>
) {
    val interactionSource = remember { MutableInteractionSource() }



   /* LazyColumn(
        Modifier.verticalScroll(scrollState).padding(paddingValues)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.career_title_text),
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        itemsIndexed(
            items = experiences,
            key = { _, item ->
                item.id
            }
        ) { index, experience ->

        }
    }*/
}