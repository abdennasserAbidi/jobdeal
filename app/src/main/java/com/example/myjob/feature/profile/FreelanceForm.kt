package com.example.myjob.feature.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

@Composable
fun FreelanceForm(
    defaultValue: String,
    text: String,
    isLast: Boolean,
    onValueChange: (value: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 20.dp, end = 20.dp)
    ) {

        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterStart),
            style = TextStyle(
                color = colorResource(id = R.color.whatsapp),
                fontFamily = FontFamily.Default,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )

        var value by remember { mutableStateOf(defaultValue) }
        var isFocused by remember { mutableStateOf(false) }
        var isLabelVisible by remember { mutableStateOf(true) }

        val localFocusManager = LocalFocusManager.current

        val keyboardActions = if (isLast) {
            KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                }
            )
        } else {
            KeyboardActions(
                onNext = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                }
            )
        }

        val imeActions = if (isLast) ImeAction.Done else ImeAction.Next

        LaunchedEffect(defaultValue != "0") {
            value = defaultValue
        }

        OutlinedTextField(
            value = value,
            onValueChange = {
                value = it
                if (value.isNotEmpty()) onValueChange(value.toInt())
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeActions
            ),
            keyboardActions = keyboardActions,
            modifier = Modifier
                .size(width = 150.dp, height = 55.dp)
                .align(Alignment.CenterEnd)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        isLabelVisible = false
                    } else if (value.isEmpty()) {
                        isLabelVisible = true
                    }
                },
            shape = RoundedCornerShape(30.dp),
            textStyle = TextStyle(fontSize = 12.sp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                unfocusedBorderColor = colorResource(id = R.color.whatsapp),
                focusedBorderColor = colorResource(id = R.color.whatsapp),
                cursorColor = Color.Black
            ),
            label = {
                /*if (isLabelVisible) {
                    Text(text = "Hint")
                }*/
            }
        )
    }
}