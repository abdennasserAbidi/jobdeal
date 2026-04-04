package com.example.myjob.feature.splash

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.request.onAnimationEnd
import coil.request.repeatCount
import coil.size.Size
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myjob.R
import com.example.myjob.base.MyApp
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.LanguageHelper
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isCompleted by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val app = context.applicationContext as MyApp

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            LanguageHelper.changeLanguage(context, "fr")
            LanguageHelper.updateLanguage(context, "fr")
        }
    }

    val user by splashViewModel.user.collectAsState()
    val isFinished = splashViewModel.isOnBoardingFinished()
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            val token = splashViewModel.getToken()
            val type = splashViewModel.getType()
            GlobalEntries.isFromLogin = true

            if (isFinished) {
                if (token.isNotEmpty()) {
                    if (user.firstTimeUse == true) {
                        when (user.role) {
                            "Candidate", "Candidat" -> navController.navigate(Screen.SearchWordScreen.route)
                            "Services" -> navController.navigate(Screen.ServiceProfileForm.route)
                            else -> navController.navigate(Screen.CompanyProfileForm.route)
                        }
                    } else {
                        if (user.role == "Services" || type == "service") navController.navigate(Screen.DemandServiceScreen.route)
                        else navController.navigate(Screen.HomeScreen.route)
                    }

                } else navController.navigate(Screen.LoginScreen.route)

            } else {

                navController.navigate(Screen.OnBoardingScreen.route)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        GifImage(modifier = Modifier.fillMaxSize()) {
            isCompleted = true
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(R.drawable.loading_page)
            .size(Size.ORIGINAL)
            // 0 means it plays once and stops. Default is -1 (infinite)
            .repeatCount(0)
            .onAnimationEnd {
                // This triggers when the animation reaches the final frame
                println("Animation finished!")
                onFinish()
            }
            .build(),
        imageLoader = imageLoader
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun LottieAnimationScreen() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.test)
    )

    val state = animateLottieCompositionAsState(composition)

    // 2. Control the animation playback
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        clipSpec = LottieClipSpec.Frame(max = 119), // Replace 119 with your actual last frame minus 1
        restartOnPlay = false // Prevents resetting if the composable recomposes
    )

    LaunchedEffect(Unit) {
        delay(2000) // wait before restart
    }

    val animatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        composition?.let {
            animatable.animate(it, iterations = 1)
            // Explicitly stay at the end
            animatable.snapTo(progress = 1f)
        }
    }
    // Use LaunchedEffect to react to the progress reaching 1.0
    LaunchedEffect(state.progress) {
        if (state.progress >= 1f || state.isAtEnd) {
            println("Animation finished!")
        }
    }

    // 3. Display the animation
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxSize(),
        renderMode = RenderMode.SOFTWARE
    )
}