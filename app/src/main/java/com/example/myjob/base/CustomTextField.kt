package com.example.myjob.base

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    text: String,
    value: String,
    onValueChange: (txt: String) -> Unit
) {
    Text(
        text = text,
        modifier = Modifier.padding(
            top = 10.dp,
            start = 20.dp
        ),
        style = TextStyle(
            color = colorResource(id = R.color.whatsapp),
            fontFamily = FontFamily.Default,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    )

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp)
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.whatsapp),
                shape = RoundedCornerShape(30.dp)
            )
            .clip(shape = RoundedCornerShape(30.dp)),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
    )
}