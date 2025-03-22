package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.feature.navigation.Screen

@Composable
fun FilterScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val interactionSource = remember { MutableInteractionSource() }

    Filter(navController, interactionSource, homeViewModel)

}

@Composable
fun FilterPreview(

) {

}

@Composable
fun Filter(
    navController: NavController,
    interactionSource: MutableInteractionSource,
    homeViewModel: HomeViewModel
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = colorResource(id = R.color.whatsapp), shape = shape)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 75.dp),
                    elevation = 5.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 15.dp)
                    ) {

                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterStart)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    navController.popBackStack()
                                },
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.width(50.dp))

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Filter",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.rubik_medium,
                                        weight = FontWeight.Medium
                                    )
                                )
                            )
                        )
                    }
                }
            }

        }


        
    }
}


@Composable
@Preview
fun PreviewFilter() {
    FilterPreview()
}