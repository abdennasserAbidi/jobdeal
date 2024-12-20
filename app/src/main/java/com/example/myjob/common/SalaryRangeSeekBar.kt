package com.example.myjob.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun SalaryRangeSeekBar(
    salaryRange: ClosedFloatingPointRange<Float> = 0f..100f, // Example salary range
    initialMin: Float = 20f, // Initial min value
    initialMax: Float = 80f, // Initial max value
    onRangeChanged: (Float, Float) -> Unit
) {
    var minValue by remember { mutableStateOf(initialMin) }
    var maxValue by remember { mutableStateOf(initialMax) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Display the current range
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "${stringResource(id = R.string.salary_range_text)}: ",
                style = TextStyle(
                    color = colorResource(id = R.color.whatsapp),
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "${minValue.toInt()}k - ${maxValue.toInt()}k")
        }

        Spacer(modifier = Modifier.height(10.dp))

        val whatsappColor = colorResource(id = R.color.whatsapp)

        // Dual slider for min and max values
        RangeSlider(
            value = minValue..maxValue,
            colors = SliderColors(
                thumbColor = whatsappColor,
                activeTrackColor = whatsappColor,
                inactiveTrackColor = Color.Gray, // Default or custom inactive track color
                activeTickColor = Color.Transparent, // Optional customization
                inactiveTickColor = Color.Transparent, // Optional customization
                disabledThumbColor = Color.Transparent, // Optional customization
                disabledActiveTrackColor = Color.Gray, // Optional customization
                disabledActiveTickColor = Color.Gray, // Optional customization
                disabledInactiveTrackColor = Color.Transparent, // Optional customization
                disabledInactiveTickColor = Color.Transparent, // Optional customization
            ),
            onValueChange = { values ->
                minValue = values.start
                maxValue = values.endInclusive
            },
            valueRange = salaryRange,
            onValueChangeFinished = {
                onRangeChanged(minValue, maxValue)
            },
            steps = (salaryRange.endInclusive - salaryRange.start).toInt() // Optional for discrete steps
        )
    }
}