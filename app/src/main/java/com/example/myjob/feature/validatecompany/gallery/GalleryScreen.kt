package com.example.myjob.feature.validatecompany.gallery

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.validatecompany.HomeViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun GalleryScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    selectImage: ActivityResultLauncher<String>,
    imageUri: MutableState<Uri?>,
    textChanged: MutableState<String>
) {

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(3f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    modifier = Modifier.align(Alignment.BottomStart),
                    onClick = {
                        selectImage.launch("image/*")
                    }) {
                    Icon(
                        Icons.Filled.Add,
                        "add",
                        tint = Color.Blue
                    )
                }

                if (imageUri.value != null) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberImagePainter(
                            data = imageUri.value
                        ),
                        contentDescription = "image"
                    )

                    IconButton(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onClick = {
                            val image = InputImage.fromFilePath(context, imageUri.value!!)
                            recognizer.process(image)
                                .addOnSuccessListener {
                                    textChanged.value = it.text
                                    val list = it.text.split("\n").toList()
                                    Log.i("qfqfdsvr", "NavGraph: $list")
                                }
                                .addOnFailureListener {
                                    Log.e("TEXT_REC", it.message.toString())
                                }
                        }) {
                        Icon(
                            Icons.Filled.Search,
                            "scan",
                            tint = Color.Blue
                        )
                    }
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    text = textChanged.value
                )
                IconButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {
                        val list = textChanged.value.split("\n").toList()
                        Log.i("qfqfdsvr", "NavGraph: $list")

                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, textChanged.value)
                        sendIntent.type = "text/plain"
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(context, shareIntent, null)

                    }) {
                    Icon(
                        Icons.Filled.Share,
                        "share",
                        tint = Color.Blue
                    )
                }
            }
        }
    }

}