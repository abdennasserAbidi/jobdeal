package com.example.myjob.feature.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

@Composable
fun Test() {
    HomeCandidate1()
}

@Composable
fun HomeCandidate1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.youtube),
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable {

                },
            contentDescription = ""
        )



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(120.dp)
        ) {
            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(horizontal = 10.dp)
                    .background(colorResource(id = R.color.lighter_gray))
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(text = "Question 1")

                }

            }

            Row(modifier = Modifier.align(Alignment.BottomCenter)) {
                Card(
                    elevation = 5.dp,
                    shape = CircleShape
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(vertical = 5.dp, horizontal = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = ""
                        )
                    }
                }

                Card(
                    elevation = 5.dp,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(start = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(vertical = 5.dp, horizontal = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = ""
                        )
                    }
                }

                Card(
                    elevation = 5.dp,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(start = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(vertical = 5.dp, horizontal = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = ""
                        )
                    }
                }
            }

        }

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
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(top = 30.dp),
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
                        text = "A",
                        fontSize = style.fontSize,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        color = style.textColor
                    )
                }
            },
            onClick = {

            }
        )

        Button3D(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(top = 10.dp),
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
                        text = "A",
                        fontSize = style.fontSize,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        color = style.textColor
                    )
                }
            },
            onClick = {

            }
        )

        Button3D(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(top = 10.dp),
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
                        text = "A",
                        fontSize = style.fontSize,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        color = style.textColor
                    )
                }
            },
            onClick = {

            }
        )
    }
}


@Composable
@Preview
fun Greeting() {
    HomeCandidate1()
}