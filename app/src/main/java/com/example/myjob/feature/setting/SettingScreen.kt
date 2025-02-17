package com.example.myjob.feature.setting

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.myjob.domain.entities.SettingsParams
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update
import java.io.File
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    clearData: () -> Unit,
    onResumed: (index: Int) -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val allLanguages by settingViewModel.allLanguages.collectAsState()
    val language by settingViewModel.language.collectAsState()
    val role by settingViewModel.role.collectAsState()
    val isExisting by settingViewModel.isExisting.collectAsState()
    val uploadMessage by settingViewModel.uploadMessage.collectAsState()

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
        10.dp.toPx().roundToInt()
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

            if (expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(top = 10.dp)
                ) {

                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        modifier = Modifier.align(Alignment.TopStart),
                        contentDescription = ""
                    )

                    Text(text = "Edit profile", modifier = Modifier.align(Alignment.TopCenter))

                }
            }

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
                ) {
                    if (expanded) {
                        var numSocial by remember { mutableStateOf("") }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(horizontal = 20.dp)
                                        .padding(top = 10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = /*if (activatedCheck && !submitEnabled) Color.Red else*/ Color.Transparent,
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(30.dp)),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    label = { Text(text = "Numéro social" ) },
                                    value = numSocial,
                                    onValueChange = {
                                        numSocial = it
                                        /*if (activatedCheck) viewModel.validateFirstName(it)
                                        viewModel.changeUserFirstName(it)*/
                                    },
                                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                )

                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    modifier = Modifier.padding(start = 20.dp),
                                    contentDescription = ""
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(horizontal = 20.dp)
                                        .padding(top = 10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = /*if (activatedCheck && !submitEnabled) Color.Red else*/ Color.Transparent,
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(30.dp)),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    label = { Text(text = "Address" ) },
                                    value = numSocial,
                                    onValueChange = {
                                        numSocial = it
                                        /*if (activatedCheck) viewModel.validateFirstName(it)
                                        viewModel.changeUserFirstName(it)*/
                                    },
                                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                )

                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    modifier = Modifier.padding(start = 20.dp),
                                    contentDescription = ""
                                )
                            }


                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(horizontal = 20.dp)
                                        .padding(top = 10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = /*if (activatedCheck && !submitEnabled) Color.Red else*/ Color.Transparent,
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(30.dp)),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    label = { Text(text = "Email" ) },
                                    value = numSocial,
                                    onValueChange = {
                                        numSocial = it
                                        /*if (activatedCheck) viewModel.validateFirstName(it)
                                        viewModel.changeUserFirstName(it)*/
                                    },
                                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                )

                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    modifier = Modifier.padding(start = 20.dp),
                                    contentDescription = ""
                                )
                            }


                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(horizontal = 20.dp)
                                        .padding(top = 10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = /*if (activatedCheck && !submitEnabled) Color.Red else*/ Color.Transparent,
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(30.dp)),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    label = { Text(text = "Phone" ) },
                                    value = numSocial,
                                    onValueChange = {
                                        numSocial = it
                                        /*if (activatedCheck) viewModel.validateFirstName(it)
                                        viewModel.changeUserFirstName(it)*/
                                    },
                                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                )

                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    modifier = Modifier.padding(start = 20.dp),
                                    contentDescription = ""
                                )
                            }


                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(horizontal = 20.dp)
                                        .padding(top = 10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = /*if (activatedCheck && !submitEnabled) Color.Red else*/ Color.Transparent,
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(30.dp)),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    label = { Text(text = "Name" ) },
                                    value = numSocial,
                                    onValueChange = {
                                        numSocial = it
                                        /*if (activatedCheck) viewModel.validateFirstName(it)
                                        viewModel.changeUserFirstName(it)*/
                                    },
                                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                )

                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    modifier = Modifier.padding(start = 20.dp),
                                    contentDescription = ""
                                )
                            }

                        }
                    }
                }

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
                            .fillMaxWidth(if (expanded) 0.5f else 0.5f)
                            .padding(top = if (expanded) (screenHeightDp - 175.dp) else 75.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                expanded = false
                                GlobalEntries.isVisibleNav.update { true }
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
                                val color = colorResource(id = R.color.whatsapp)
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterEnd)
                                        .background(color = color, shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_settings_privacy),
                                        tint = Color.White,
                                        contentDescription = ""
                                    )

                                }
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
                Column(modifier = Modifier.fillMaxSize()) {

                    var selected by remember { mutableStateOf(0) }

                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                        text = stringResource(id = R.string.choose_language_text),
                        color = colorResource(id = R.color.dark_blue),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.rubikbold,
                                    weight = FontWeight.Medium
                                )
                            )
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomTab(
                            items = allLanguages,
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                            selectedItemIndex = selected,
                            onClick = {
                                selected = it

                                settingViewModel.changeLanguage(allLanguages[it])

                                lc =
                                    if (allLanguages[it] == "English" || allLanguages[it] == "Anglais") "en" else "fr"

                                LanguageHelper.changeLanguage(context, lc)

                                LanguageHelper.updateLanguage(context, lc)
                            }
                        )
                    }

                    Text(
                        modifier = Modifier.padding(start = 10.dp, top = 50.dp),
                        text = stringResource(id = R.string.manage_profiles_text),
                        color = colorResource(id = R.color.dark_blue),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.rubikbold,
                                    weight = FontWeight.Medium
                                )
                            )
                        )
                    )

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

                    Spacer(modifier = Modifier.height(10.dp))

                    val list = mutableListOf(
                        SettingsParams(
                            icon = R.drawable.ic_settings_notifications,
                            title = "Notifications"
                        ),
                        SettingsParams(
                            icon = R.drawable.ic_settings_privacy,
                            title = "Valider votre profile"
                        ),
                        SettingsParams(
                            icon = R.drawable.ic_settings_terms,
                            title = "Terms & Conditions"
                        ),
                        SettingsParams(
                            icon = R.drawable.ic_settings_privacy,
                            title = "Politique de Confidentialité"
                        ),
                        SettingsParams(icon = R.drawable.ic_settings_account, title = "Mon Compte")
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        list.mapIndexed { index, item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        if (index == list.lastIndex) {
                                            expanded = true
                                            GlobalEntries.isVisibleNav.update { false }
                                        }
                                    }
                            ) {

                                Row(
                                    modifier = Modifier.align(Alignment.CenterStart)
                                ) {

                                    Icon(
                                        painter = painterResource(
                                            id = item.icon ?: R.drawable.ic_icon_back
                                        ),
                                        tint = colorResource(id = R.color.whatsapp),
                                        modifier = Modifier.size(20.dp),
                                        contentDescription = ""
                                    )

                                    Text(
                                        text = item.title ?: "",
                                        modifier = Modifier.padding(start = 10.dp),
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
                                }


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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp, start = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                settingViewModel.logout()
                                clearData()
                                navController.navigate(Screen.LoginScreen.route)
                            }
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings_account),
                            tint = colorResource(id = R.color.whatsapp),
                            modifier = Modifier.size(20.dp),
                            contentDescription = ""
                        )

                        Text(
                            text = stringResource(id = R.string.logout_text),
                            modifier = Modifier.padding(start = 10.dp),
                            color = colorResource(id = R.color.dark_blue),
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
                pdfName ?: ""
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
