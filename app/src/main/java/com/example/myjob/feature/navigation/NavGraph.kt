package com.example.myjob.feature.navigation

import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myjob.common.VoiceToTextParser
import com.example.myjob.feature.validatecompany.camera.CameraScreen
import com.example.myjob.feature.validatecompany.camera.NoPermissionScreen
import com.example.myjob.feature.validatecompany.gallery.GalleryScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NavGraph(
    voiceToTextParser: VoiceToTextParser,
    selectImage: ActivityResultLauncher<String>,
    imageUri: MutableState<Uri?>,
    textChanged: MutableState<String>
) {

    val cameraPermissionState: PermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            //CallScreen(navController = navController)
        }

        composable(route = Screen.recordScreen.route) {
            //RecordScreen(navController = navController, voiceToTextParser)
        }

        composable(route = Screen.textRecognitionScreen.route) {
            if (cameraPermissionState.status.isGranted) CameraScreen(navController = navController)
            else NoPermissionScreen(cameraPermissionState::launchPermissionRequest)
        }

        composable(route = Screen.galleryScreen.route) {
            GalleryScreen(
                navController = navController,
                selectImage = selectImage,
                imageUri = imageUri,
                textChanged = textChanged
            )
        }
    }
}
