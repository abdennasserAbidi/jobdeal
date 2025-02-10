package com.example.myjob.feature.setting

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.base.LanguageHelper
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.getFileNameFromUri
import com.example.myjob.common.pdf.PdfViewer
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.common.tablayout.CustomTab
import com.example.myjob.feature.navigation.Screen
import java.io.File
import kotlin.math.roundToInt

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
    val role by settingViewModel.role.collectAsState()
    val isExisting by settingViewModel.isExisting.collectAsState()
    val uploadMessage by settingViewModel.uploadMessage.collectAsState()
    //val userFullName = GlobalEntries.user.fullName ?: ""

    val interactionSource = remember { MutableInteractionSource() }

    var lc by remember { mutableStateOf(if (language == "English") "en" else "fr") }

    var isUploaded by remember { mutableStateOf(false) }
    var expend by remember { mutableStateOf(false) }
    var showPdf by remember { mutableStateOf(false) }

    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var pdfName by remember { mutableStateOf<String?>(null) }
    var expectedName by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val animatedPadding by animateDpAsState(
        if (expanded) {
            10.dp
        } else {
            0.dp
        },
        label = "padding"
    )

    val pxToMove = with(LocalDensity.current) {
        50.dp.toPx().roundToInt()
    }
    val offset by animateIntOffsetAsState(
        targetValue = if (expanded) {
            IntOffset(0, pxToMove)
        } else {
            IntOffset.Zero
        },
        label = "offset"
    )

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        Log.i("lifecycleExp", "login: $lifecycleEvent")

        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            settingViewModel.getRole()
            pdfName = settingViewModel.getPDFName()
            onResumed(3)
        }
    }

    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx().toInt()
    }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    Log.i("saveUserRes", "screenHeightDp: ${GlobalEntries.user}")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current

            Log.i("screenHeight", "SettingScreen: $screenHeight")
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(if (expanded) (screenHeightDp - 50.dp) else 200.dp)
                .offset {
                    offset
                }
            ) {
                val shapeInit = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                val shapeAfter = RoundedCornerShape(10.dp)
                val shape = if (expanded) shapeAfter else shapeInit

                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth()
                        .height(if (expanded) (screenHeightDp - 150.dp) else 100.dp)
                        .padding(horizontal = animatedPadding)
                        .background(color = colorResource(id = R.color.whatsapp), shape = shape)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, end = 20.dp)
                ) {

                    if (role == "Candidate" || role == "Candidat") {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp)
                                .align(Alignment.TopStart)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    navController.popBackStack()
                                },
                            contentDescription = ""
                        )
                    }
                }


                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier
                            .fillMaxWidth(if (expanded) 0.5f else 0.8f)
                            .padding(top = if (expanded) (screenHeightDp - 175.dp) else 75.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                //navController.navigate(Screen.ProfileScreen.route)
                                expanded = !expanded
                            },
                        elevation = 5.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 15.dp)
                        ) {

                            val align = if (expanded) Alignment.Center else Alignment.CenterStart
                            val text =
                                if (expanded) "Validate" else GlobalEntries.user.companyName ?: ""

                            Text(
                                modifier = Modifier.align(align),
                                text = text,
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

                            if (!expanded) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    modifier = Modifier
                                        .padding(top = 10.dp, start = 10.dp)
                                        .align(Alignment.CenterEnd)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            if (role.isNotEmpty()) {
                                                val screen =
                                                    if (role == "Company" || role == "Entreprise") Screen.CompanyProfileScreen.route
                                                    else Screen.ProfileScreen.route
                                                navController.navigate(screen)
                                            } else navController.navigate(Screen.ProfileScreen.route)
                                        },
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }

            }

            // Launcher for opening a file picker
            val pdfPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = { uri ->
                    // Handle the selected PDF URI
                    selectedPdfUri = uri
                    pdfName = uri?.let { context.getFileNameFromUri(it) } // Get file name

                    expectedName = if (pdfName?.isNotEmpty() == true) {
                        if (pdfName?.contains("(") == true) "${
                            pdfName?.split(" ")?.get(0)
                        }.pdf" else (pdfName ?: "")
                    } else ""
                    Log.i("fileUri", "createPdf: $expectedName")

                    uri?.let {
                        uploadFile(settingViewModel, context, it, expectedName ?: "")
                    }
                }
            )

            if (!expanded) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (role == "Candidate" || role == "Candidat") {
                        Log.i("pdfName", "SettingScreen: $isExisting")

                        if (isExisting) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .padding(top = 20.dp)
                                    .padding(horizontal = 10.dp)
                                    .border(
                                        1.dp,
                                        shape = RoundedCornerShape(3.dp),
                                        color = colorResource(id = R.color.whatsapp)
                                    )
                                    .background(Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {

                                Column(
                                    modifier = Modifier.clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        showPdf = true
                                    },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pdf),
                                        tint = Color.Red,
                                        modifier = Modifier.size(30.dp),
                                        contentDescription = ""
                                    )
                                    Log.i("pdfName", "SettingScreen: $uploadMessage")
                                    //val s = uploadMessage.split(":")[1].trim()
                                    Text(
                                        text = uploadMessage,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(top = 5.dp)
                                    )
                                }
                            }

                        } else {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .padding(top = 20.dp)
                                    .padding(horizontal = 10.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isUploaded = true
                                        pdfPickerLauncher.launch(arrayOf("application/pdf"))
                                    },
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
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    val list = if (role == "Candidate" || role == "Candidat") mutableListOf(
                        "Notifications",
                        "Valider votre profile",
                        "Partager",
                        "Terms of use",
                        "Privacy",
                        "Licences",
                        "Email",
                        stringResource(id = R.string.password_text)
                    ) else mutableListOf(
                        "Valider votre profile",
                        "Partager",
                        "Terms of use",
                        "Privacy",
                        "Licences",
                        "Email",
                        stringResource(id = R.string.password_text)
                    )


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        list.map { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_icon_back),
                                    tint = colorResource(id = R.color.whatsapp),
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterEnd),
                                    contentDescription = ""
                                )
                            }
                        }
                    }

                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 20.dp),
                        text = stringResource(id = R.string.choose_language_text),
                        color = colorResource(id = R.color.dark_blue),
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

                    var selected by remember { mutableStateOf(0) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomTab(
                            items = listOf("Français", "Anglais"),
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                            selectedItemIndex = selected,
                            onClick = {
                                selected = it
                            }
                        )
                    }


                    /*if (role == "Candidate" || role == "Candidat") {
                        Card(
                            elevation = 5.dp,
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp, horizontal = 10.dp)
                            ) {
                                Text(
                                    text = "Notifications",
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
                                settingViewModel.validateAccount()
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 10.dp)
                        ) {
                            Text(
                                text = "Valider votre profile",
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
                    }*/

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

                                    val text =
                                        if (expend) stringResource(id = R.string.choose_language_text)
                                        else language ?: stringResource(id = R.string.language_text)

                                    Text(
                                        text = text,
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
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
                                        androidx.compose.material.Text(
                                            text = item,
                                            color = Color.Black
                                        )
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
        }

        if (showPdf) {

            var isLoading by remember { mutableStateOf(false) }
            var currentLoadingPage by remember { mutableStateOf<Int?>(null) }
            var pageCount by remember { mutableStateOf<Int?>(null) }
            // Save the PDF to a file
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                pdfName
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                PdfViewer(
                    modifier = Modifier.fillMaxSize(),
                    pdfResId = file,
                    loadingListener = { loading, currentPage, maxPage ->
                        isLoading = loading
                        if (currentPage != null) currentLoadingPage = currentPage
                        if (maxPage != null) pageCount = maxPage
                    }
                )
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            progress = if (currentLoadingPage == null || pageCount == null) 0f
                            else currentLoadingPage!!.toFloat() / pageCount!!.toFloat()
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 5.dp)
                                .padding(horizontal = 30.dp),
                            text = "${currentLoadingPage ?: "-"} pages loaded/${pageCount ?: "-"} total pages"
                        )
                    }
                }
            }

        }
    }
}

fun uploadFile(
    settingViewModel: SettingViewModel,
    context: Context,
    fileUri: Uri,
    pdfName: String
) {
    settingViewModel.uploadCV(context, fileUri, pdfName)
}
