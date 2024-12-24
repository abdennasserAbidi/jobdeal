package com.example.myjob.feature.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.phonekit.getFlagResource
import com.example.myjob.domain.entities.NewCountry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPhoneKit(
    modifier: Modifier,
    selectedCountry: NewCountry,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    var phone by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(contentAlignment = Alignment.Center) {
            Row(modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(
                    1.dp,
                    colorResource(id = R.color.whatsapp),
                    RoundedCornerShape(30.dp)
                )
                .align(Alignment.TopCenter)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                }
                .background(color = Color.White, shape = RoundedCornerShape(30.dp)),
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Row(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .padding(start = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onClick()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = context.getFlagResource(selectedCountry.iso2)),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 10.dp)
                    )

                    Text(
                        text = "+${selectedCountry.code}",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Text(
                        text = "",
                        modifier = Modifier
                            .width(2.dp)
                            .padding(start = 10.dp)
                            .padding(vertical = 5.dp)
                            .background(colorResource(id = R.color.whatsapp))
                    )
                }

                TextField(
                    value = phone,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            phone = input
                        }
                    },
                    placeholder = { Text(text = "") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number // Numeric keyboard
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent, // Background color
                        focusedIndicatorColor = Color.Transparent, // No border when focused
                        unfocusedIndicatorColor = Color.Transparent, // No border when unfocused
                        disabledIndicatorColor = Color.Transparent // No border when disabled
                    )
                )

            }
        }
    }
}