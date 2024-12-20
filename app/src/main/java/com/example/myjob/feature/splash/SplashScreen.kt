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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.myjob.feature.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.BottomCenter),
                    contentDescription = ""
                )

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 10.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp)
                        .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                            val isFinished = splashViewModel.isOnBoardingFinished()

                            val token = splashViewModel.getToken()
                            Log.i("token", "SplashScreen: $token")

                            if (isFinished) {
                                if (token.isNotEmpty()) navController.navigate(Screen.HomeScreen.route)
                                else navController.navigate(Screen.LoginScreen.route)

                            } else navController.navigate(Screen.OnBoardingScreen.route)
                        }
                        .background(
                            color = colorResource(id = R.color.whatsapp),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.start_app),
                        modifier = Modifier.padding(vertical = 20.dp),
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
    }
}