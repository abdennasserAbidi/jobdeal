package com.example.myjob.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myjob.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    isSuccess: Boolean,
    message: String,
    setShowDialog: (Boolean) -> Unit
) {

    val color =
        if (isSuccess) Color.Blue else colorResource(android.R.color.holo_red_dark)
    val title = if (isSuccess) "Success" else "Error"
    val icon = if (isSuccess) Icons.Default.Check else Icons.Default.Info
    val interactionSource = remember { MutableInteractionSource() }

    Dialog(
        properties = DialogProperties(
        dismissOnClickOutside = true,
        dismissOnBackPress = false,
        ), onDismissRequest = {
        setShowDialog(false)
    }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        tint = color,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { setShowDialog(false) }
                    )

                    Text(
                        text = title,
                        Modifier.padding(top = 15.dp),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = message,
                        Modifier.padding(top = 7.dp),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Box(modifier = Modifier.padding(40.dp, 10.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = color),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(50.dp)
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}