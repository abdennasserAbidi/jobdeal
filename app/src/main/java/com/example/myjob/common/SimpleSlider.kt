package com.example.myjob.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.myjob.R

@Composable
fun SimpleSlider(modifier: Modifier, defaultSalary: Float?, updateSalary: (salary: Int) -> Unit) {
    val salary = defaultSalary?.toInt()
    var sliderFloatPosition by remember { mutableFloatStateOf(defaultSalary ?: 1000f) }
    var sliderPosition by remember { mutableIntStateOf(1000) }
    Column(modifier = modifier) {
        Slider(
            value = sliderFloatPosition,
            onValueChange = {
                sliderFloatPosition = it
                sliderPosition = it.toInt()
                updateSalary(sliderPosition)
            },
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.whatsapp),
                activeTrackColor = colorResource(id = R.color.whatsapp),
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            valueRange = 10000f..50000f
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "10000dt", modifier = Modifier.align(Alignment.CenterStart))
            Text(text = "50000dt", modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}
