package com.example.myjob.feature.home

import android.view.View
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
fun HomeCandidate(
    navController: NavController,
    clearData: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }

    scheduleFileDownload(LocalContext.current, homeViewModel.getPDFName())

    HomeCandidatePreview(navController, clearData = {
        clearData()
    }, interactionSource, homeViewModel)
}

@Composable
fun HomeCandidatePreview(
    navController: NavController,
    clearData: () -> Unit = {},
    interactionSource: MutableInteractionSource,
    homeViewModel: HomeViewModel
) {
    val listHomeEntity by homeViewModel.listHomeEntity.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lighter_gray))
    ) {
        // Draw custom arc with gradient
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // adjust height as needed
                .align(Alignment.TopEnd)
        ) {
            val radius = size.width * 1.5f
            val arcSize = Size(radius - 200, radius)

            val topLeft = Offset(
                x = size.width - radius,
                y = -radius / 2f
            )

            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF76C94D), // light green
                        Color(0xFF049344)  // dark green
                    ),
                    start = Offset.Zero,
                    end = Offset(size.width, size.height)
                ),
                startAngle = 0f,
                sweepAngle = 280f,
                useCenter = true,
                topLeft = topLeft,
                size = arcSize
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "Hello, ${GlobalEntries.user.fullName}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp),
                columns = GridCells.Fixed(2)
            ) {
                itemsIndexed(
                    items = listHomeEntity,
                    key = { i, _ ->
                        View.generateViewId()
                    }
                ) { index, item ->

                    //if (index == listHomeEntity.lastIndex && index == listHomeEntity.lastIndex -1)


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = 10.dp)
                            .padding(horizontal = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                val action = when (index) {
                                    0 -> Screen.InvitationScreen.route
                                    1 -> Screen.InvitationScreen.route
                                    2 -> Screen.SettingScreen.route
                                    3 -> Screen.ProfileScreen.route
                                    else -> Screen.InvitationScreen.route
                                }
                                navController.navigate(action)
                            },
                        shape = RoundedCornerShape(20.dp),
                        elevation = 5.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color.White,
                                                Color.White,
                                                Color.White.copy(alpha = 0.8f),
                                                Color.Transparent
                                            ),
                                            startX = 0f,
                                            endX = 1000f
                                        )
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    item.icon?.let { img ->
                                        if (index == 3) {
                                            Image(
                                                painter = painterResource(id = img),
                                                modifier = Modifier.size(50.dp),
                                                contentDescription = ""
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(id = img),
                                                modifier = Modifier.size(50.dp),
                                                tint = colorResource(id = R.color.whatsapp),
                                                contentDescription = ""
                                            )
                                        }
                                    }

                                    Text(
                                        text = stringResource(id = item.title),
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
                                        text = stringResource(id = item.subTitle).uppercase(),
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 30.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        homeViewModel.logout()
                        clearData()
                        navController.navigate(Screen.LoginScreen.route)
                    },
                shape = RoundedCornerShape(20.dp),
                elevation = 5.dp
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings_account),
                        modifier = Modifier.size(30.dp),
                        tint = colorResource(id = R.color.whatsapp),
                        contentDescription = ""
                    )

                    Text(
                        text = stringResource(id = R.string.logout_text),
                        color = colorResource(id = R.color.whatsapp),
                        modifier = Modifier.padding(start = 10.dp),
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
                }
            }
        }
    }
}