package com.example.myjob.common

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun RangeSliders(modifier: Modifier, updateRange: () -> Unit) {
    var sliderPosition by remember { mutableStateOf(0f..100f) }
    Column(modifier = modifier) {
        RangeSlider(
            value = sliderPosition,
            steps = 5,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                //Log.i(TAG, "RangeSliders: ")
            },
        )
        Text(text = sliderPosition.toString())
    }
}