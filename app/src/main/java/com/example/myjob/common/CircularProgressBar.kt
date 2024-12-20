package com.example.myjob.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(modifier: Modifier) {

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        contentColor = Color.Blue,
        elevation = 5.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                color = Color.Blue,
                strokeWidth = 8.dp,
                trackColor = Color.LightGray,
                strokeCap = StrokeCap.Round
            )

            Text(text = "Loading", modifier = Modifier.padding(top = 15.dp))
        }
    }

}