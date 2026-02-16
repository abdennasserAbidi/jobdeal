package com.example.myjob.feature.splash

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
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
    val listCompanies by splashViewModel.listCompanies.collectAsState()

    val context = LocalContext.current
    val app = context.applicationContext as MyApp

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            LanguageHelper.changeLanguage(context, "fr")
            LanguageHelper.updateLanguage(context, "fr")
        }
    }

    LaunchedEffect(listCompanies) {
        app.listCompanies.removeAt(app.listCompanies.lastIndex)
        if (!app.listCompanies.containsAll(listCompanies)) app.listCompanies.addAll(listCompanies)
    }

    val user by splashViewModel.user.collectAsState()

    LaunchedEffect(Unit) {
        delay(  3115L)
        val isFinished = splashViewModel.isOnBoardingFinished()
        GlobalEntries.isFromLogin = true

        val token = splashViewModel.getToken()
        val type = splashViewModel.getType()
        if (isFinished) {
            if (token.isNotEmpty()) {
                if (user.firstTimeUse == true) {
                    when (user.role) {
                        "Candidate", "Candidat" -> navController.navigate(Screen.SearchWordScreen.route)
                        "Services" -> navController.navigate(Screen.ServiceProfileForm.route)
                        else -> navController.navigate(Screen.CompanyProfileForm.route)
                    }
                } else {
                    if (type == "service") navController.navigate(Screen.DemandServiceScreen.route)
                    else navController.navigate(Screen.HomeScreen.route)
                }

            } else navController.navigate(Screen.LoginScreen.route)

        } else navController.navigate(Screen.OnBoardingScreen.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        GifImage(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
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
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.loading_page).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}