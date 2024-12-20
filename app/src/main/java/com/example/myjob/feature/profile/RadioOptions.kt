package com.example.myjob.feature.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.myjob.R

@Composable
fun RadioOptions(listOptions: List<String>, defaultSelection: String?,modifier: Modifier, onSelect: (item: String) -> Unit) {

    var selectedOption by remember { mutableStateOf(defaultSelection ?: listOptions[0]) }

    Row(modifier = modifier) {
        listOptions.forEach { clock ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    modifier = Modifier
                        .padding(vertical = 0.dp)
                        .size(20.dp),
                    colors = RadioButtonColors(
                        colorResource(id = R.color.whatsapp),
                        colorResource(id = R.color.whatsapp),
                        colorResource(id = R.color.whatsapp),
                        colorResource(id = R.color.whatsapp)
                    ),
                    selected = (clock == selectedOption),
                    onClick = {
                        selectedOption = clock
                        onSelect(selectedOption)
                    }
                )

                Text(text = clock, modifier = Modifier.padding(horizontal = 10.dp))
            }
        }

    }

}