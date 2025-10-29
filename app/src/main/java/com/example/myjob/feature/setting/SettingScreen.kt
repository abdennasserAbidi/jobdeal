package com.example.myjob.feature.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.draw.shadow
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
import com.example.myjob.base.CustomTextField
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.isFromSettings
import com.example.myjob.common.LanguageHelper
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.common.tablayout.CustomTab
import com.example.myjob.domain.entities.SettingsParams
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.validateprofile.VerificationStatus
import kotlinx.coroutines.flow.update

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
    val verificationSteps by settingViewModel.verificationSteps.collectAsState()

    val userName = if (role == "Candidate" || role == "Candidat") GlobalEntries.user.fullName
    else GlobalEntries.user.companyName

    val interactionSource = remember { MutableInteractionSource() }

    var lc by remember { mutableStateOf(if (language == "English") "en" else "fr") }

    var expanded by remember { mutableStateOf(false) }
    var openTest by remember { mutableStateOf(false) }

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

    if (openTest) {
        ModernSettingScreen(navController)
    } else {
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
                    .height(if (expanded) (screenHeightDp - 20.dp) else 200.dp)
                    .offset { offset }
                    .clickable {
                        openTest = true
                    }
                ) {
                    val shapeInit = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

                    Box(
                        modifier = Modifier
                            .animateContentSize()
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                color = colorResource(id = R.color.whatsapp),
                                shape = shapeInit
                            )
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
                                            settingViewModel.saveCompanyInfo()
                                        }
                                )

                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 75.dp, bottom = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Surface(
                            elevation = 2.dp,
                            color = MaterialTheme.colors.surface,
                            shape = RoundedCornerShape(40.dp)
                        ) {

                            Card(
                                shape = RoundedCornerShape(40.dp),
                                modifier = Modifier
                                    .animateContentSize(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = LinearOutSlowInEasing
                                        )
                                    )
                                    .fillMaxWidth(if (expanded) 0.85f else 0.65f)
                                    .wrapContentHeight()
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(40.dp)
                                    )
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
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            tint = Color.Black,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.CenterStart)
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    navController.popBackStack()
                                                },
                                            contentDescription = ""
                                        )


                                        Text(
                                            modifier = Modifier.align(Alignment.Center),
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
                                        if (verificationSteps.status == VerificationStatus.VERIFIED.name) {
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
                                    } else {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .verticalScroll(rememberScrollState())
                                        ) {

                                            var companyName by remember {
                                                mutableStateOf(
                                                    user.companyName ?: ""
                                                )
                                            }

                                            CustomTextField(
                                                text = "${stringResource(id = R.string.company_name_text)}*",
                                                value = companyName,
                                            ) {
                                                companyName = it
                                                settingViewModel.changeCompanyName(it)
                                            }

                                            var companyActivitySector by remember {
                                                mutableStateOf(
                                                    user.companyActivitySector ?: ""
                                                )
                                            }
                                            CustomTextField(
                                                text = "${stringResource(id = R.string.activity_text)}*",
                                                value = companyActivitySector,
                                            ) {
                                                companyActivitySector = it
                                                settingViewModel.changeCompanyActivitySector(it)
                                            }

                                            var companyDescription by remember {
                                                mutableStateOf(
                                                    user.companyDescription ?: ""
                                                )
                                            }

                                            CustomTextField(
                                                text = "Description*",
                                                value = companyDescription,
                                            ) {
                                                companyDescription = it
                                                settingViewModel.changeCompanyDescription(it)
                                            }

                                            var companyPhone by remember {
                                                mutableStateOf(
                                                    user.phoneCompany ?: ""
                                                )
                                            }

                                            CustomTextField(
                                                text = "Phone*",
                                                value = companyPhone,
                                            ) {
                                                companyPhone = it
                                                settingViewModel.changeCompanyPhone(it)
                                            }

                                            var secondPhone by remember {
                                                mutableStateOf(
                                                    user.secondPhoneCompany ?: ""
                                                )
                                            }

                                            CustomTextField(
                                                text = "Extra phone",
                                                value = secondPhone,
                                            ) {
                                                secondPhone = it
                                                settingViewModel.changeCompanySecondPhone(it)
                                            }

                                            var companyAddress by remember {
                                                mutableStateOf(
                                                    user.companyAddress ?: ""
                                                )
                                            }

                                            CustomTextField(
                                                text = "Address*",
                                                value = companyAddress,
                                            ) {
                                                companyAddress = it
                                                settingViewModel.changeCompanyAddress(it)
                                            }

                                            var secondAddress by remember {
                                                mutableStateOf(
                                                    user.companySecondAddress ?: ""
                                                )
                                            }

                                            CustomTextField(
                                                text = "Extra address",
                                                value = secondAddress,
                                            ) {
                                                secondAddress = it
                                                settingViewModel.changeCompanySecondAddress(it)
                                            }

                                            Spacer(modifier = Modifier.height(50.dp))

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (!expanded) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        var selected by remember(language) {
                            mutableStateOf(
                                if (language == "" || language == "English" || language == "Anglais") 0 else 1
                            )
                        }

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

                        Spacer(modifier = Modifier.height(10.dp))

                        val list = mutableListOf(
                            SettingsParams(
                                icon = R.drawable.ic_settings_notifications,
                                title = stringResource(id = R.string.notification_text)
                            ),
                            SettingsParams(
                                icon = R.drawable.ic_settings_privacy,
                                title = stringResource(id = R.string.validate_profile_text)
                            ),
                            SettingsParams(
                                icon = R.drawable.ic_settings_terms,
                                title = stringResource(id = R.string.terms_text)
                            ),
                            SettingsParams(
                                icon = R.drawable.ic_settings_privacy,
                                title = stringResource(id = R.string.confidentiality_text)
                            ),
                            SettingsParams(
                                icon = R.drawable.ic_settings_account,
                                title = stringResource(id = R.string.my_account_text)
                            )
                        )

                        var isRejected by remember { mutableStateOf(false) }
                        var showDialog by remember { mutableStateOf(false) }
                        var textInfo by remember { mutableStateOf("") }
                        val textVerified = stringResource(id = R.string.verification_validate_info_text)
                        val textPending = stringResource(id = R.string.verification_info_text)

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Verification") },
                                text = { Text(textInfo) },
                                confirmButton = {
                                    TextButton(onClick = { showDialog = false }) {
                                        Text("OK")
                                    }
                                }
                            )
                        }

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
                                            when (index) {
                                                0 -> navController.navigate(Screen.NotificationCompanyScreen.route)
                                                1 -> {

                                                    if (role == "Candidate" || role == "Candidat")
                                                        navController.navigate(Screen.ValidateProfileCandidateScreen.route)
                                                    else {

                                                        when (verificationSteps.status) {
                                                            VerificationStatus.PENDING_REVIEW.name -> {
                                                                isRejected = false
                                                                showDialog = true
                                                                textInfo = textPending
                                                            }

                                                            VerificationStatus.VERIFIED.name -> {
                                                                isRejected = false
                                                                showDialog = true
                                                                textInfo = textVerified

                                                            }

                                                            else -> {
                                                                isRejected = false
                                                                navController.navigate(Screen.ValidateProfileCompanyScreen.route)
                                                            }
                                                        }
                                                    }

                                                }

                                                4 -> {
                                                    if (role == "Candidate" || role == "Candidat") {
                                                        isFromSettings = true
                                                        navController.navigate(Screen.ProfileScreen.route)
                                                    } else expanded = true
                                                    GlobalEntries.isVisibleNav.update { false }
                                                }
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
        }
    }
}
