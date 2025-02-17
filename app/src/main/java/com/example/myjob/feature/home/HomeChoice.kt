package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
import com.example.myjob.feature.navigation.Screen

@Composable
fun HomeChoice(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val color = colorResource(id = R.color.whatsapp)

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {
            val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = colorResource(id = R.color.whatsapp), shape = shape))

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 75.dp),
                    elevation = 5.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = GlobalEntries.user.companyName ?: "",
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

        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.Center)
                .background(color, RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(top = 30.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        navController.navigate(Screen.HomeCompanyScreen.route)
                    }
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Find Candidate",
                    modifier = Modifier.padding(vertical = 20.dp),
                    style = TextStyle(
                        color = color,
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(top = 10.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {

                    }
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.start_app),
                    modifier = Modifier.padding(vertical = 20.dp),
                    style = TextStyle(
                        color = color,
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(top = 10.dp, bottom = 30.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {

                    }
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.start_app),
                    modifier = Modifier.padding(vertical = 20.dp),
                    style = TextStyle(
                        color = color,
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

        }
    }


}