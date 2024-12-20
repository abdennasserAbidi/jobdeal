package com.example.myjob.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CandidateFavorites(
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
    FavoritesScreen()
}

@Composable
fun FavoritesScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //3states: not started, encour, finished
        val style = DuolingoButtonStyle(
            normalColor = Color.LightGray,
            pressedColor = Color.Gray,
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
            content = { isPressed ->
                ContentContinue(
                    modifier = Modifier
                        .offset(
                            x = 0.dp,
                            y = if (isPressed) 0.dp else (-style.offset / 2)
                        ),
                    style
                )

            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            style = style,
            onClick = {
                print("clicked")
            }
        )

        Button3D(
            content = { isPressed ->
                ContentStart(
                    modifier = Modifier
                        .offset(
                            x = 0.dp,
                            y = if (isPressed) 0.dp else (-style.offset / 2)
                        ),
                    style
                )

            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(top = 10.dp),
            style = style,
            onClick = {
                print("clicked")
            }
        )

        /*LazyColumn() {
            itemsIndexed(
                items = listOf("1"),
                key = { i, _ ->
                    View.generateViewId()
                }
            ) { index, item ->

                val style = DuolingoButtonStyle(
                    normalColor = Color.LightGray,
                    pressedColor = Color.Gray,
                    textColor = Color.White,
                    fontSize = 16.sp,
                    cornerRadius = 16.dp,
                    offset = 6.dp,
                    horizontalPadding = 40.dp,
                    verticalPadding = 16.dp,
                    customIcon = {
                        *//*Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_heart),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )*//*
                    }
                )

                Button3D(
                    content = { isPressed ->
                        Content(
                            modifier = Modifier
                                .offset(
                                    x = 0.dp,
                                    y = if (isPressed) 0.dp else (-style.offset / 2)
                                ),
                            style
                        )

                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    style = style,
                    onClick = {
                        print("clicked")
                    }
                )
            }
        }*/

    }
}

@Composable
fun ContentContinue(modifier: Modifier, style: DuolingoButtonStyle) {
    Column(
        modifier = modifier
            .padding(
                top = style.verticalPadding,
                bottom = style.verticalPadding,
                start = style.horizontalPadding,
                end = style.horizontalPadding
            )
    ) {

        Text(
            text = "Chapter 1",
            fontSize = style.fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )

        Text(
            text = "description",
            fontSize = style.fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )


        var progress by remember { mutableStateOf(0.5f) } // Initial progress (50%)
        DuolingoProgressBar(
            progress = progress,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        Text(
            text = "Continue",
            fontSize = style.fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                progress = (progress + 0.1f).coerceIn(0f, 1f)
            }
        )

    }
}


@Composable
fun ContentStart(modifier: Modifier, style: DuolingoButtonStyle) {
    Column(
        modifier = modifier
            .padding(
                top = style.verticalPadding,
                bottom = style.verticalPadding,
                start = style.horizontalPadding,
                end = style.horizontalPadding
            )
    ) {

        Text(
            text = "name of module",
            fontSize = style.fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )

        Text(
            text = "description",
            fontSize = style.fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )

        Text(
            text = "price",
            fontSize = style.fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )

        Text(
            text = "Start",
            fontSize = style.fontSize,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
            }
        )

    }
}

@Composable
@Preview
fun Previews() {
    FavoritesScreen()
}