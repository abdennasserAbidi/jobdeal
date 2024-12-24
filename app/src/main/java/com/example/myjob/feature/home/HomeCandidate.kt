package com.example.myjob.feature.home

import android.util.Log
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.domain.entities.HOME_ENTITY
import com.example.myjob.domain.entities.HomeEntity
import com.example.myjob.feature.favorites.Button3D
import com.example.myjob.feature.favorites.DuolingoButtonStyle
import com.example.myjob.feature.favorites.New3D
import com.example.myjob.feature.navigation.Screen

@Composable
fun HomeCandidate(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val interactionSource = remember { MutableInteractionSource() }

    HomeCandidatePreview(navController, interactionSource, homeViewModel)

    /*HomeCandidate2 {
        navController.navigate(Screen.FavoritesScreen.route)
    }*/
    //HomeCandidate1()
}

@Composable
fun HomeCandidatePreview(
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

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
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
fun HomeCandidate2(navigation: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        var show by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*ComposedThreeD()
            Spacer(modifier = Modifier.height(30.dp))
            ComposedThreeD()
            Spacer(modifier = Modifier.height(30.dp))
            ComposedThreeD()*/
            New3D(true, R.drawable.math) {
                Log.i("azdazaaaqqqqqq", "New3D: $it")
                show = it
            }
            Spacer(modifier = Modifier.height(30.dp))
            New3D(false, R.drawable.physique) {
                Log.i("azdazaaaqqqqqq", "New3D: $it")
            }

        }

        AnimatedVisibility(
            visible = show,
            enter =
            fadeIn(
                // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                initialAlpha = 0.4f
            ),
            exit =
            fadeOut(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 250)
            )
        ) {

            Column(modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.4f)
                .border(1.dp, Color.Black, RoundedCornerShape(2.dp))
                .background(Color.Gray)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Math",
                        color = Color.White,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                            .size(20.dp)
                            .clickable { show = false }
                            .background(Color.Red, shape = RoundedCornerShape(5.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp),
                            contentDescription = ""
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Module 1 to 6",
                        style = MaterialTheme.typography.h6,
                    )

                    Text(
                        text = "2 videos",
                        style = MaterialTheme.typography.h6,
                    )

                    Text(
                        text = "prices",
                        style = MaterialTheme.typography.h6,
                    )

                    val style = DuolingoButtonStyle(
                        normalColor = Color(0xff00B3FD),
                        pressedColor = Color(0xff009CDC),
                        textColor = Color.White,
                        fontSize = 16.sp,
                        cornerRadius = 16.dp,
                        offset = 6.dp,
                        horizontalPadding = 40.dp,
                        verticalPadding = 16.dp,
                        customIcon = {
                            /*Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_heart),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )*/
                        }
                    )

                    Button3D(
                        modifier = Modifier.wrapContentSize(),
                        style = style,
                        content = { isPressed ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(
                                        top = style.verticalPadding,
                                        bottom = style.verticalPadding,
                                        start = style.horizontalPadding,
                                        end = style.horizontalPadding
                                    )
                                    .offset(
                                        x = 0.dp,
                                        y = if (isPressed) 0.dp else (-style.offset / 2)
                                    )
                            ) {
                                // if customIcon is set, then show it
                                style.customIcon?.let { icon ->
                                    icon()
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                Text(
                                    text = "Start",
                                    fontSize = style.fontSize,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Bold,
                                    color = style.textColor
                                )
                            }
                        },
                        onClick = {
                            print("clicked")
                            show = false
                            navigation()
                        }
                    )

                }


            }

        }

    }
}

@Composable
@Preview
fun Previews() {
    HomeCandidate2 {

    }
}