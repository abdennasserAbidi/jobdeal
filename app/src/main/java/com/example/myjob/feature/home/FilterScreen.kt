package com.example.myjob.feature.home

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
    val listHomeEntity by homeViewModel.listHomeEntity.collectAsState()
    Scaffold(
        topBar = {
            Card(
                elevation = 5.dp,
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(70.dp)
                ) {

                    Text(
                        text = "JobDeal",
                        modifier = Modifier.align(Alignment.CenterStart),
                        style = MaterialTheme.typography.h6,
                        color = colorResource(id = R.color.whatsapp)
                    )

                    Icon(
                        imageVector = Icons.Default.Settings,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Screen.SettingScreen.route)
                            },
                        contentDescription = "settings"
                    )
                }
            }
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            itemsIndexed(
                items = listHomeEntity,
                key = { i, _ ->
                    View.generateViewId()
                }
            ) { index, item ->

                //My invitation contract
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 10.dp)
                        .padding(horizontal = 10.dp),
                    elevation = 5.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Image(
                            painter = painterResource(id = item.img),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            contentDescription = ""
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.White,       // Start color
                                            Color.White,      // Keep the first half white
                                            Color.White.copy(alpha = 0.8f),     // Keep the first half white
                                            Color.Transparent // End transparent
                                        ),
                                        startX = 0f,         // Start at the left
                                        endX = 1000f         // End at the right (adjust as needed)
                                    )
                                )
                        ) {

                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 20.dp)
                            ) {

                                Text(
                                    text = item.title,
                                    color = colorResource(id = R.color.whatsapp),
                                    modifier = Modifier.padding(top = 10.dp),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.rubikbold,
                                                weight = FontWeight.Bold
                                            )
                                        )
                                    )
                                )

                                Text(
                                    text = item.subTitle,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }

                        }
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