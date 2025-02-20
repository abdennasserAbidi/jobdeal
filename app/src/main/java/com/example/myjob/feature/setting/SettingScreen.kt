package com.example.myjob.feature.setting

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.base.LanguageHelper
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.common.tablayout.CustomTab
import com.example.myjob.domain.entities.SettingsParams
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    clearData: () -> Unit = {},
    onResumed: (index: Int) -> Unit = {},
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val allLanguages by settingViewModel.allLanguages.collectAsState()
    val language by settingViewModel.language.collectAsState()
    val role by settingViewModel.role.collectAsState()
    val user by settingViewModel.user.collectAsState()

    val userName = if (role == "Candidate" || role == "Candidat") GlobalEntries.user.fullName
    else GlobalEntries.user.companyName

    val interactionSource = remember { MutableInteractionSource() }

    var lc by remember { mutableStateOf(if (language == "English") "en" else "fr") }

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
            IntOffset(0, 0)
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

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(if (expanded) (screenHeightDp - 50.dp) else 200.dp)
                .offset { offset }
            ) {
                val shapeInit = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = colorResource(id = R.color.whatsapp), shape = shapeInit)
                ) {

                    this@Column.AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .padding(top = 15.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        expanded = false
                                        GlobalEntries.isVisibleNav.update { true }
                                    },
                                tint = Color.White,
                                contentDescription = ""
                            )

                            Text(
                                text = "Edit profile",
                                color = Color.White,
                                modifier = Modifier.align(Alignment.TopCenter)
                            )

                            Text(
                                text = stringResource(id = R.string.save_text),
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        //TODO("save updated company")
                                    }
                            )

                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 75.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier
                            .animateContentSize()
                            .fillMaxWidth(if (expanded) 0.85f else 0.5f)
                            .height(if (expanded) (screenHeightDp - 50.dp) else 50.dp)
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
                            if (!expanded) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = userName ?: "",
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
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    var isLinkOpened by remember { mutableStateOf(false) }

                                    AnimatedVisibility(visible = !isLinkOpened) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            Text(
                                                text = "Name",
                                                modifier = Modifier.padding(
                                                    top = 20.dp,
                                                    start = 20.dp
                                                ),
                                                style = TextStyle(
                                                    color = colorResource(id = R.color.whatsapp),
                                                    fontFamily = FontFamily.Default,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            TextField(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 20.dp)
                                                    .padding(top = 10.dp)
                                                    .border(
                                                        width = 1.dp,
                                                        color = colorResource(id = R.color.whatsapp),
                                                        shape = RoundedCornerShape(30.dp)
                                                    )
                                                    .clip(shape = RoundedCornerShape(30.dp)),
                                                colors = TextFieldDefaults.textFieldColors(
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent
                                                ),
                                                value = user.companyName ?: "",
                                                onValueChange = {
                                                    settingViewModel.changeCompanyName(it)
                                                },
                                                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.activity_text),
                                                modifier = Modifier.padding(
                                                    top = 10.dp,
                                                    start = 20.dp
                                                ),
                                                style = TextStyle(
                                                    color = colorResource(id = R.color.whatsapp),
                                                    fontFamily = FontFamily.Default,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            TextField(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 20.dp)
                                                    .padding(top = 10.dp)
                                                    .border(
                                                        width = 1.dp,
                                                        color = colorResource(id = R.color.whatsapp),
                                                        shape = RoundedCornerShape(30.dp)
                                                    )
                                                    .clip(shape = RoundedCornerShape(30.dp)),
                                                colors = TextFieldDefaults.textFieldColors(
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent
                                                ),
                                                value = user.companyActivitySector ?: "",
                                                onValueChange = {
                                                    settingViewModel.changeCompanyActivitySector(it)
                                                },
                                                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                            )

                                            Text(
                                                text = "A propos",
                                                modifier = Modifier.padding(
                                                    top = 10.dp,
                                                    start = 20.dp
                                                ),
                                                style = TextStyle(
                                                    color = colorResource(id = R.color.whatsapp),
                                                    fontFamily = FontFamily.Default,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            TextField(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 20.dp)
                                                    .padding(top = 10.dp)
                                                    .border(
                                                        width = 1.dp,
                                                        color = colorResource(id = R.color.whatsapp),
                                                        shape = RoundedCornerShape(30.dp)
                                                    )
                                                    .clip(shape = RoundedCornerShape(30.dp)),
                                                colors = TextFieldDefaults.textFieldColors(
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent
                                                ),
                                                value = user.companyDescription ?: "",
                                                onValueChange = {
                                                    settingViewModel.changeCompanyDescription(it)
                                                },
                                                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                            )
                                        }
                                    }

                                    AnimatedVisibility(visible = isLinkOpened) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            Text(
                                                text = "Website",
                                                modifier = Modifier.padding(
                                                    top = 20.dp,
                                                    start = 20.dp
                                                ),
                                                style = TextStyle(
                                                    color = colorResource(id = R.color.whatsapp),
                                                    fontFamily = FontFamily.Default,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            TextField(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 20.dp)
                                                    .padding(top = 10.dp)
                                                    .border(
                                                        width = 1.dp,
                                                        color = colorResource(id = R.color.whatsapp),
                                                        shape = RoundedCornerShape(30.dp)
                                                    )
                                                    .clip(shape = RoundedCornerShape(30.dp)),
                                                colors = TextFieldDefaults.textFieldColors(
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent
                                                ),
                                                value = user.linkWebsite ?: "",
                                                onValueChange = {
                                                    settingViewModel.changeCompanyWebsite(it)
                                                },
                                                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                            )

                                            Text(
                                                text = "LinkedIn",
                                                modifier = Modifier.padding(
                                                    top = 10.dp,
                                                    start = 20.dp
                                                ),
                                                style = TextStyle(
                                                    color = colorResource(id = R.color.whatsapp),
                                                    fontFamily = FontFamily.Default,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )

                                            TextField(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 20.dp)
                                                    .padding(top = 10.dp)
                                                    .border(
                                                        width = 1.dp,
                                                        color = colorResource(id = R.color.whatsapp),
                                                        shape = RoundedCornerShape(30.dp)
                                                    )
                                                    .clip(shape = RoundedCornerShape(30.dp)),
                                                colors = TextFieldDefaults.textFieldColors(
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent
                                                ),
                                                value = user.linkLinkedIn ?: "",
                                                onValueChange = {
                                                    settingViewModel.changeCompanyLinkedIn(it)
                                                },
                                                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                                            )

                                            if (user.listNum?.isNotEmpty() == true) {
                                                user.listNum?.mapIndexed { index, numSocial ->
                                                    Text(
                                                        text = "Numéro social",
                                                        modifier = Modifier.padding(
                                                            top = 20.dp,
                                                            start = 20.dp
                                                        ),
                                                        style = TextStyle(
                                                            color = colorResource(id = R.color.whatsapp),
                                                            fontFamily = FontFamily.Default,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )

                                                    TextField(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp)
                                                            .padding(top = 10.dp)
                                                            .border(
                                                                width = 1.dp,
                                                                color = colorResource(id = R.color.whatsapp),
                                                                shape = RoundedCornerShape(30.dp)
                                                            )
                                                            .clip(shape = RoundedCornerShape(30.dp)),
                                                        colors = TextFieldDefaults.textFieldColors(
                                                            focusedIndicatorColor = Color.Transparent,
                                                            unfocusedIndicatorColor = Color.Transparent
                                                        ),
                                                        value = numSocial,
                                                        onValueChange = {
                                                            settingViewModel.changeCompanyNum(
                                                                it,
                                                                index
                                                            )
                                                        },
                                                        textStyle = TextStyle(
                                                            Color.Black,
                                                            fontSize = 14.sp
                                                        )
                                                    )
                                                }
                                            } else {
                                                var companyNum by remember { mutableStateOf("") }

                                                Text(
                                                    text = "Numéro social",
                                                    modifier = Modifier.padding(
                                                        top = 20.dp,
                                                        start = 20.dp
                                                    ),
                                                    style = TextStyle(
                                                        color = colorResource(id = R.color.whatsapp),
                                                        fontFamily = FontFamily.Default,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )

                                                TextField(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 20.dp)
                                                        .padding(top = 10.dp)
                                                        .border(
                                                            width = 1.dp,
                                                            color = colorResource(id = R.color.whatsapp),
                                                            shape = RoundedCornerShape(30.dp)
                                                        )
                                                        .clip(shape = RoundedCornerShape(30.dp)),
                                                    colors = TextFieldDefaults.textFieldColors(
                                                        focusedIndicatorColor = Color.Transparent,
                                                        unfocusedIndicatorColor = Color.Transparent
                                                    ),
                                                    value = companyNum,
                                                    onValueChange = {
                                                        companyNum = it
                                                        settingViewModel.addCompanyNum(it)
                                                    },
                                                    textStyle = TextStyle(
                                                        Color.Black,
                                                        fontSize = 14.sp
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    val text =
                                        if (isLinkOpened) "Change other info" else "Change links as well?"

                                    Text(
                                        text = text,
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                isLinkOpened = !isLinkOpened
                                            },
                                        color = Color.Blue,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )

                                }
                            }
                        }
                    }
                }

            }

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

                        /*if (isExisting) {
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

                        }
                        else {
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
                        }*/
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

                                        if (role == "Candidate" || role == "Candidat")
                                            navController.navigate(Screen.ProfileScreen.route)
                                        else expanded = true
                                        GlobalEntries.isVisibleNav.update { false }
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
    }
}
