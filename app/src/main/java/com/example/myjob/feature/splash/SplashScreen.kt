package com.example.myjob.feature.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(bottomEnd = 70.dp))
                .background(Color.Gray) // Just for visibility
        ) {
            Image(
                painter = painterResource(id = R.drawable.filtercandidates),
                contentDescription = null,
                contentScale = ContentScale.FillHeight, // Or ContentScale.Fit, depending on your need
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f))
            )
        }



        /*Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.87f),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                modifier = Modifier
                    .padding(start = 20.dp),
                contentDescription = ""
            )

        }*/

        Box(
            modifier = Modifier.wrapContentSize()
                .padding(top = 20.dp, start = 20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                    val isFinished = splashViewModel.isOnBoardingFinished()
                    GlobalEntries.isFromLogin = true

                    val token = splashViewModel.getToken()
                    Log.i("token", "SplashScreen: $token")

                    if (isFinished) {
                        if (token.isNotEmpty()) navController.navigate(Screen.HomeScreen.route)
                        else navController.navigate(Screen.LoginScreen.route)

                    } else navController.navigate(Screen.OnBoardingScreen.route)
                }
                .background(
                    color = colorResource(id = R.color.whatsapp),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.start_app),
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                style = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}