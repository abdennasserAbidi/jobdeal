package com.example.myjob.feature.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.LanguageHelper
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.test.FormTextField
import com.example.myjob.feature.validateprofile.VerificationStatus

@Composable
fun ModernSettingScreen(
    navController: NavController,
    clearData: () -> Unit = {},
    onResumed: (index: Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onValidateProfileClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onMyAccountClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    settingViewModel: SettingViewModel = hiltViewModel()
) {

    val role by settingViewModel.role.collectAsState()
    val user by settingViewModel.user.collectAsState()
    val verificationSteps by settingViewModel.verificationSteps.collectAsState()

    val userName = if (role == "Candidate" || role == "Candidat") GlobalEntries.user.fullName?.trimStart()
    else GlobalEntries.user.companyName

    val interactionSource = remember { MutableInteractionSource() }

    var showEditProfile by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val textVerified = stringResource(id = R.string.verification_validate_info_text)
    val textPending = stringResource(id = R.string.verification_info_text)

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {

        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            settingViewModel.getRole()
            onResumed(3)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Verification",
                    fontWeight = FontWeight.Bold
                )
            },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK", color = Color(0xFF25D366))
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFF),
                            Color.White
                        )
                    )
                ),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                ProfileHeaderSection(
                    userName = userName ?: "",
                    isVerified = verificationSteps.status == VerificationStatus.VERIFIED.name,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        if (role == "Candidate" || role == "Candidat")
                            navController.navigate(Screen.SearchWordScreen.route)
                        else showEditProfile = true
                    }
                )
            }

            // Language Selection
            item {
                LanguageSection(
                    interactionSource = interactionSource,
                    settingViewModel = settingViewModel
                )
            }

            // Settings Options
            item {
                SettingsOptionsSection(
                    role = role,
                    interactionSource = interactionSource,
                    isVerified = verificationSteps.status == VerificationStatus.VERIFIED.name,
                    onNotificationsClick = onNotificationsClick,
                    onValidateProfileClick = {
                        when (verificationSteps.status) {
                            VerificationStatus.PENDING_REVIEW.name -> {
                                showDialog = true
                                dialogMessage = textPending
                            }

                            VerificationStatus.VERIFIED.name -> {
                                showDialog = true
                                dialogMessage = textVerified
                            }

                            else -> {
                                //navController.navigate(Screen.ValidateProfileCompanyScreen.route)
                            }
                        }
                    },
                    onTermsClick = onTermsClick,
                    onPrivacyClick = onPrivacyClick,
                    onMyAccountClick = {
                        if (role == "Company" || role == "Entreprise") {
                            showEditProfile = true
                        } else {
                            onMyAccountClick()
                        }
                    }
                )
            }

            // Logout Section
            item {
                LogoutSection(onLogout = {
                    settingViewModel.logout()
                    clearData()
                    navController.navigate(Screen.LoginScreen.route)
                })
            }
        }

        // Edit Profile Bottom Sheet
        AnimatedVisibility(
            visible = showEditProfile,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            EditProfileBottomSheet(
                onDismiss = {
                    showEditProfile = false
                },
                onSave = {
                    showEditProfile = false
                },
                settingViewModel = settingViewModel
            )
        }
    }
}

@Composable
fun ProfileHeaderSection(
    userName: String,
    isVerified: Boolean,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1F2937)
                    )
                }

                Text(
                    text = stringResource(id = R.string.setting_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = colorResource(id = R.color.whatsapp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF25D366),
                                        Color(0xFF25D366).copy(alpha = 0.7f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.firstOrNull()?.toString()?.uppercase() ?: "U",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (isVerified) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = Color(0xFF25D366),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    if (isVerified) {
                        Text(
                            text = stringResource(id = R.string.verification_done_text),
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.whatsapp),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageSection(
    interactionSource: MutableInteractionSource,
    settingViewModel: SettingViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = Color(0xFF25D366),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(id = R.string.choose_language_text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            val context = LocalContext.current
            val allLanguages by settingViewModel.allLanguages.collectAsState()
            val language by settingViewModel.language.collectAsState()
            var lc by remember { mutableStateOf(if (language == "English") "en" else "fr") }

            var selected by remember(language) {
                mutableStateOf(
                    if (language == "" || language == "English" || language == "Anglais") 0 else 1
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                allLanguages.forEachIndexed { index, lang ->
                    val isSelected = index == selected
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected)
                                    Color(0xFF25D366).copy(alpha = 0.1f)
                                else
                                    Color(0xFFF3F4F6)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selected = index
                                settingViewModel.changeLanguage(allLanguages[index])
                                lc = "fr"
                                    //if (allLanguages[index] == "English" || allLanguages[index] == "Anglais") "en" else "fr"

                                LanguageHelper.changeLanguage(context, lc)
                                LanguageHelper.updateLanguage(context, lc)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected)
                                Color(0xFF25D366)
                            else
                                Color(0xFF6B7280)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsOptionsSection(
    role: String,
    interactionSource: MutableInteractionSource,
    isVerified: Boolean,
    onNotificationsClick: () -> Unit,
    onValidateProfileClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onMyAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.manage_profiles_text),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                SettingsOption(
                    interactionSource = interactionSource,
                    iconRes = R.drawable.ic_settings_notifications,
                    title = stringResource(id = R.string.notification_text),
                    onClick = onNotificationsClick
                )

                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFE5E7EB)
                )

                SettingsOption(
                    interactionSource = interactionSource,
                    iconRes = R.drawable.ic_settings_privacy,
                    title = stringResource(id = R.string.validate_profile_text),
                    showBadge = isVerified,
                    onClick = onValidateProfileClick
                )

                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFE5E7EB)
                )

                SettingsOption(
                    interactionSource = interactionSource,
                    iconRes = R.drawable.ic_settings_terms,
                    title = stringResource(id = R.string.terms_text),
                    onClick = onTermsClick
                )

                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFE5E7EB)
                )

                SettingsOption(
                    interactionSource = interactionSource,
                    iconRes = R.drawable.ic_settings_privacy,
                    title = stringResource(id = R.string.confidentiality_text),
                    onClick = onPrivacyClick
                )

                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color(0xFFE5E7EB)
                )

                SettingsOption(
                    interactionSource = interactionSource,
                    iconRes = R.drawable.ic_settings_account,
                    title = stringResource(id = R.string.my_account_text),
                    onClick = onMyAccountClick
                )
            }
        }
    }
}

@Composable
fun SettingsOption(
    interactionSource: MutableInteractionSource,
    iconRes: Int,
    title: String,
    showBadge: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color(0xFF25D366),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )
            if (showBadge) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = Color(0xFF25D366),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        if (!showBadge) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LogoutSection(onLogout: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onLogout() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = R.string.logout_text),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        }
    }
}

@Composable
fun EditProfileBottomSheet(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    settingViewModel: SettingViewModel
) {
    val user by settingViewModel.user.collectAsState()

    var companyName by remember { mutableStateOf(user.companyName ?: "") }
    var activitySector by remember { mutableStateOf(user.companyActivitySector ?: "") }
    var description by remember { mutableStateOf(user.companyDescription ?: "") }
    var phone by remember { mutableStateOf("+1 234 567 8900") }
    var secondPhone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf(user.companyAddress ?: "") }
    var secondAddress by remember { mutableStateOf(user.companySecondAddress ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .align(Alignment.BottomCenter)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF1F2937)
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.edit_profile_text),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    TextButton(onClick = {
                        settingViewModel.saveCompanyInfo()
                    }) {
                        Text(
                            text = "Save",
                            color = colorResource(id = R.color.whatsapp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Divider(color = Color(0xFFE5E7EB))

                // Form Fields
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {

                        /*var activatedCheck by remember { mutableStateOf(false) }
                        val isCompanyNameValid by settingViewModel.isCompanyNameValid.collectAsState()
                        val companyNameVerified by remember { derivedStateOf { isCompanyNameValid } }*/

                        FormTextField(
                            value = companyName,
                            onValueChange = {
                                companyName = it
                                //if (activatedCheck) settingViewModel.validateCompanyName(it)
                                settingViewModel.changeCompanyName(it)
                            },
                            label = stringResource(id = R.string.company_name_text),
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )

                        /*if (activatedCheck) {
                            if (companyName.isEmpty() || !companyNameVerified) {
                                Text(
                                    modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                    text = if (!companyNameVerified) stringResource(id = R.string.error_email) else "",
                                    color = if (companyNameVerified) colorResource(id = R.color.whatsapp) else Color.Red
                                )
                            }
                        }*/


                    }

                    item {

                        FormTextField(
                            value = activitySector,
                            onValueChange = {
                                activitySector = it
                                settingViewModel.changeCompanyActivitySector(it)
                            },
                            label = stringResource(id = R.string.activity_text),
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }

                    item {

                        FormTextField(
                            value = description,
                            onValueChange = {
                                description = it
                                settingViewModel.changeCompanyDescription(it)
                            },
                            label = "Description",
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone*") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = secondPhone,
                            onValueChange = { secondPhone = it },
                            label = { Text("Extra Phone") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {

                        FormTextField(
                            value = address,
                            onValueChange = {
                                address = it
                                settingViewModel.changeCompanyAddress(it)
                            },
                            label = "Address",
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }

                    item {
                        FormTextField(
                            value = secondAddress,
                            onValueChange = {
                                secondAddress = it
                                settingViewModel.changeCompanySecondAddress(it)
                            },
                            label = "Extra Address",
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }
                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewModernSettingScreen() {
    ModernSettingScreen(
        userName = "John Doe",
        isVerified = true,
        currentLanguage = "English",
        role = "Candidate"
    )
}*/
