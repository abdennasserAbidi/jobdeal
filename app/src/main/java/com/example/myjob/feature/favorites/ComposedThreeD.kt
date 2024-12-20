package com.example.myjob.feature.favorites

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myjob.R

@Composable
fun ComposedThreeD() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val p1 = Perspective.Left(
            bottomEdgeColor = Color.Gray.copy(alpha = 0.8f), rightEdgeColor = Color.Gray
        )

        ThreeDimensionalLayout(p1, onClick = {
            Log.i("azdazaaaqqqqqq", "New3D: $it")
        }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(120.dp)
                    .background(colorResource(id = R.color.lighter_gray))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cvtable),
                    modifier = Modifier.fillMaxSize().alpha(0.5f),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
            }
        }

        val darkGreen = Color(0xFF006400)  // Forest Green
        // Apply opacity to the green color to make it closer to black
        val darkGreenWithOpacity =
            darkGreen.copy(alpha = 0.5f) // Low alpha for close-to-black opacity


        val p2 = Perspective.Left(
            bottomEdgeColor = darkGreenWithOpacity, rightEdgeColor = Color.Gray
        )

        ThreeDimensionalLayout(p2,onClick = {
            Log.i("azdazaaaqqqqqq", "New3D: $it")
        }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(55.dp)
                    .background(Color.Green)
            ) {
                Text(text = "Start", modifier = Modifier.padding(10.dp))
            }
        }
    }


}