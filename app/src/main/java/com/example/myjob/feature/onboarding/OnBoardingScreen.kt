package com.example.myjob.feature.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myjob.R
import com.example.myjob.feature.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }

    val animations = listOf(
        R.raw.intro1,
        R.raw.intro2,
        R.raw.intro3
    )
    val titles = listOf(
        stringResource(id = R.string.onboarding_title_one),
        stringResource(id = R.string.onboarding_title_two),
        stringResource(id = R.string.onboarding_title_three)
    )

    val descriptions = listOf(
        "Join a community of experts, share your knowledge, and expand your professional network",
        "Discover new opportunities to propel your career forward",
        "Showcase your skills and attract top industry professionals"
    )
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {
        animations.size
    })

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f)
                .align(Alignment.TopCenter)
        ) {
            HorizontalPager(
                state = pagerState,
                Modifier.fillMaxSize()
            ) { currentPage ->
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(
                            animations[currentPage]
                        )
                    )
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .height(400.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = titles[currentPage],
                        modifier = Modifier.padding(top = 10.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = descriptions[currentPage],
                        modifier = Modifier.padding(top = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .align(Alignment.BottomCenter)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                PageIndicator(
                    pageCount = animations.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .height(50.dp)
                )

                Column(
                    modifier = Modifier.fillMaxSize(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    if (pagerState.currentPage == 2) {
                        Box(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                                    onBoardingViewModel.finishOnBoarding()
                                    navController.navigate(Screen.SignupScreen.route)
                                }
                                .background(
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(30.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Join Jobdealer",
                                modifier = Modifier.padding(vertical = 20.dp),
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = FontFamily.Default,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Text(
                            text = "Log in to an existing account",
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                                    onBoardingViewModel.finishOnBoarding()
                                    val token = onBoardingViewModel.getToken()

                                    val destination =
                                        if (token.isNotEmpty()) Screen.HomeScreen.route
                                        else Screen.LoginScreen.route

                                    navController.navigate(destination)
                                },
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage)
        }
    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean) {

    /*val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp, label = "")
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(15.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(if (isSelected) colorResource(id = R.color.whatsapp) else colorResource(id = R.color.light_whatsapp))
    )*/

    val height = animateDpAsState(targetValue = if (isSelected) 7.dp else 5.dp, label = "")
    val width = animateDpAsState(targetValue = if (isSelected) 30.dp else 15.dp, label = "")
    Box(
        modifier = Modifier
            .padding(end = 2.dp)
            .height(height.value)
            .width(width.value)
            .clip(RectangleShape)
            .background(if (isSelected) colorResource(id = R.color.whatsapp) else colorResource(id = R.color.light_whatsapp))
    )
}