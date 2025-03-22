package com.example.myjob.feature.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.domain.entities.User

@Composable
fun ActionButtons(
    onSave: () -> Unit,
    onSkip: () -> Unit,
    onMatch: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //skip
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onSkip()
                    }
                    .background(
                        color = colorResource(id = R.color.whatsapp),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                    contentDescription = "",
                    tint = Color.White
                )
            }

            Text(
                    text = "Skip",
                    modifier = Modifier.padding(top = 10.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //save
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onSave()
                    }
                    .background(
                        color = colorResource(id = R.color.whatsapp),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Filled.Favorite,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                    contentDescription = "",
                    tint = Color.White
                )
            }

            Text(
                    text = "Save",
                    modifier = Modifier.padding(top = 10.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //connect
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onMatch()
                    }
                    .background(
                        color = colorResource(id = R.color.whatsapp),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Filled.Home,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                    contentDescription = "",
                    tint = Color.White
                )
            }

            Text(
                text = "Connect",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun JobSwipeCard(
    profile: User,
    lang: String,
    openProfile: (profile: User) -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
    ) {

        Card(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.TopCenter)
                .padding(top = 25.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(20.dp)),
            elevation = 5.dp
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        val fullName = (profile.fullName ?: "").trimEnd().trimStart()
                        var l = ""
                        if (fullName.isNotEmpty() && fullName != " ") {
                            val s = fullName.split(" ")
                            l = "${s[0][0].uppercaseChar()}${s[1][0].uppercaseChar()}"
                        }

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(
                                    colorResource(id = R.color.whatsapp),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = l,
                                color = Color.White,
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

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = profile.fullName ?: "",
                                style = MaterialTheme.typography.h6
                            )

                            Text(
                                text = "${profile.activitySector ?: ""} - ${profile.address ?: ""}",
                                color = Color.Gray
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                        text = profile.resumeUser(),
                        style = MaterialTheme.typography.body2
                    )
                }

                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            openProfile(profile)
                        },
                    elevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "View profile",
                            modifier = Modifier.padding(vertical = 20.dp),
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.whatsapp)
                        )
                    }
                }
            }

        }

        /*Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.TopCenter)
                .padding(top = 25.dp)
                .border(
                    1.dp,
                    Color.Black,
                    RoundedCornerShape(20.dp)
                )
                .background(color = Color.Transparent, shape = RoundedCornerShape(20.dp))
        ) {

            Column(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = profile.fullName ?: "",
                        style = MaterialTheme.typography.h6
                    )

                    Text(
                        text = "${profile.activitySector ?: ""} - ${profile.address ?: ""}",
                        color = Color.Gray
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                    text = profile.resumeUser(),
                    style = MaterialTheme.typography.body2
                )
            }

            val shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .border(
                    1.dp,
                    Color.Black,
                    shape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    openProfile(profile)
                }
                .background(color = colorResource(id = R.color.whatsapp), shape = shape),
                contentAlignment = Alignment.Center) {

                Text(
                    text = "View profile",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }*/


    }
}