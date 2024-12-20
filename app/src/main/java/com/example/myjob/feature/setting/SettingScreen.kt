package com.example.myjob.feature.setting

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.base.LanguageHelper
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.navigation.Screen

@Composable
fun SettingScreen(
    navController: NavController,
    clearData: () -> Unit,
    onResumed: (index: Int) -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val allLanguages by settingViewModel.allLanguages.collectAsState()
    val language by settingViewModel.language.collectAsState()
    val username by settingViewModel.username.collectAsState()
    val userFullName by settingViewModel.userFullName.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    var lc by remember { mutableStateOf(if (language == "English") "en" else "fr") }

    var expend by remember { mutableStateOf(false) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        Log.i("lifecycleExp", "login: $lifecycleEvent")

        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            onResumed(3)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(colorResource(id = R.color.whatsapp), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username,
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

                Text(
                    text = userFullName,
                    modifier = Modifier.padding(top = 10.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.rubikbold,
                                weight = FontWeight.Bold
                            )
                        )
                    )
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowBack,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .align(Alignment.TopStart)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                      navController.popBackStack()
                    },
                contentDescription = "")

            Text(
                text = "Edit profile",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.TopEnd)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        navController.navigate(Screen.ProfileScreen.route)
                    },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.rubik_medium,
                            weight = FontWeight.Medium
                        )
                    )
                )
            )
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 20.dp)
                .padding(horizontal = 10.dp),
            elevation = 5.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.cvtable),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,       // Start color
                                    Color.White,      // Keep the first half white
                                    Color.White.copy(alpha = 0.8f),     // Keep the first half white
                                    Color.Transparent // End transparent
                                ),
                                startX = 0f,         // Start at the left
                                endX = 1000f         // End at the right (adjust as needed)
                            )
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 20.dp)
                    ) {

                        Text(
                            text = stringResource(id = R.string.my_resume_text),
                            color = colorResource(id = R.color.whatsapp),
                            modifier = Modifier.padding(top = 10.dp),
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

                        Text(
                            text = stringResource(id = R.string.upload_resume_text),
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }

                }
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(text = "Notifications", modifier = Modifier.align(Alignment.CenterStart))
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(text = "Valider votre profile", modifier = Modifier.align(Alignment.CenterStart))
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(text = "Partager", modifier = Modifier.align(Alignment.CenterStart))
                Icon(
                    imageVector = Icons.Default.Share,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(text = "Terms of use", modifier = Modifier.align(Alignment.CenterStart))
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(text = "Privacy", modifier = Modifier.align(Alignment.CenterStart))
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(text = "Licences", modifier = Modifier.align(Alignment.CenterStart))
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = "Email",
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.password_text),
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = ""
                )
            }
        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .padding(horizontal = 10.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    expend = !expend
                }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.White)
            ) {

                Card(
                    elevation = 3.dp,
                    shape = RectangleShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 10.dp)
                    ) {

                        val text = if (expend) stringResource(id = R.string.choose_language_text)
                        else language ?: stringResource(id = R.string.language_text)

                        Text(text = text, modifier = Modifier.align(Alignment.CenterStart))
                        if (expend) Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                        else Text(
                            text = stringResource(id = R.string.change_language_text),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }

                if (expend) {
                    allLanguages.mapIndexed { index, item ->
                        val paddingTop = if (index == 0) 20.dp else 10.dp
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 20.dp)
                            .padding(start = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                settingViewModel.changeLanguage(allLanguages[index])

                                lc =
                                    if (allLanguages[index] == "English" || allLanguages[index] == "Anglais") "en" else "fr"

                                LanguageHelper.changeLanguage(context, lc)

                                LanguageHelper.updateLanguage(context, lc)

                                expend = false
                            }) {
                            androidx.compose.material.Text(text = item, color = Color.Black)
                        }

                        if (index < allLanguages.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }

        }

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 10.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        settingViewModel.logout()
                        clearData()
                        navController.navigate(Screen.LoginScreen.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.logout_text))
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }


}