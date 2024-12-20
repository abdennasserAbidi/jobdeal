package com.example.myjob.feature.favorites

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myjob.R

@Composable
fun New3D(
    isEnable: Boolean,
    icon: Int,
    onClick: (isPressed: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val darkGreen = Color(0xFF006400)  // Forest Green
        // Apply opacity to the green color to make it closer to black
        val darkGreenWithOpacity =
            darkGreen.copy(alpha = 0.5f) // Low alpha for close-to-black opacity

        val p2 = if (isEnable) {
            Perspective.Left(
                bottomEdgeColor = darkGreenWithOpacity, rightEdgeColor = darkGreenWithOpacity
            )
        } else {
            Perspective.Left(
                bottomEdgeColor = Color.LightGray, rightEdgeColor = Color.LightGray
            )
        }


        //val color = if (isEnable) colorResource(id = R.color.lighter_gray) else Color.Gray
        var isPressed by remember { mutableStateOf(false) }

        LaunchedEffect(isPressed) {
            if (isPressed) onClick(isPressed)
        }

        ThreeDimensionalLayout(
            p2,
            onClick = {
                isPressed = it
            }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(80.dp)
                    .background(if (isEnable) Color.Green else colorResource(id = R.color.lighter_gray))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        modifier = Modifier.size(50.dp),
                        contentDescription = ""
                    )
                }

            }
        }
    }
}