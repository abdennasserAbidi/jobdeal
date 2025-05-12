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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.scheduleFileDownload
import com.example.myjob.feature.navigation.Screen

@Composable
fun HomeCandidate(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val interactionSource = remember { MutableInteractionSource() }

    scheduleFileDownload(LocalContext.current, homeViewModel.getPDFName())

    HomeCandidatePreview(navController, interactionSource, homeViewModel)
}

@Composable
fun HomeCandidatePreview(
    navController: NavController,
    interactionSource: MutableInteractionSource,
    homeViewModel: HomeViewModel
) {
    val listHomeEntity by homeViewModel.listHomeEntity.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.whatsapp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Hello, ${GlobalEntries.user.fullName}",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterStart),
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.Settings,
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
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

        val shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp)
                .background(color = Color.White, shape = shape)
        ) {

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                columns = GridCells.Fixed(2)
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
                            .padding(horizontal = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Screen.InvitationScreen.route)
                            },
                        shape = RoundedCornerShape(20.dp),
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


            /*LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
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
                            .padding(horizontal = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Screen.InvitationScreen.route)
                            }
                        ,
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
            }*/

        }

        /*Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .align(Alignment.TopCenter)
        ) {
            val shapeInit = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

            Box(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = colorResource(id = R.color.whatsapp), shape = shapeInit)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 75.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth(0.5f)
                        .height(50.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            GlobalEntries.isVisibleNav.update { true }
                        },
                    elevation = 5.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 15.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = GlobalEntries.user.fullName ?: "",
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

                        Icon(
                            imageVector = Icons.Default.Settings,
                            modifier = Modifier
                                .size(20.dp)
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

        }*/
    }
}