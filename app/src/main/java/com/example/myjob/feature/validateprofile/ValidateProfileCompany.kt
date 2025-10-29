package com.example.myjob.feature.validateprofile

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.feature.profile.test.FormTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateProfileCompany(
    navController: NavController,
    selectImage: ActivityResultLauncher<String>,
    addCompanyToList: () -> Unit = {},
    validateProfileViewModel: ValidateProfileViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.validate_profile_text),
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.whatsapp),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        val user by validateProfileViewModel.user.collectAsState()
        val isEmailValid by validateProfileViewModel.isEmailValid.collectAsState()

        val interactionSource = remember { MutableInteractionSource() }

        var numSecuritySocial by remember { mutableStateOf("") }
        var docs by remember { mutableStateOf(mutableListOf("")) }

        var activatedCheck by remember { mutableStateOf(false) }

        var email by remember { mutableStateOf("") }
        val emailVerified by remember { derivedStateOf { isEmailValid } }

        val verificationSteps by validateProfileViewModel.verificationSteps.collectAsState()
        var isRejected by remember { mutableStateOf(false) }

        when(verificationSteps.status) {
            VerificationStatus.PENDING_REVIEW.name -> {
                isRejected = false
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = stringResource(id = R.string.verification_info_text),
                    color = Color.Cyan
                )
            }
            VerificationStatus.VERIFIED.name -> {
                isRejected = false
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = stringResource(id = R.string.verification_validate_info_text),
                    color = colorResource(id = R.color.whatsapp)
                )

            }
            VerificationStatus.REJECTED.name -> {
                isRejected = true
                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 40.dp),
                        text = stringResource(id = R.string.verification_rejected_info_text),
                        color = Color.Red
                    )

                    FormTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (activatedCheck) validateProfileViewModel.validateEmail(it)
                            validateProfileViewModel.changeUserEmail(it)
                        },
                        label = "Email",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 40.dp),
                        isRequired = true
                    )

                    if (activatedCheck) {
                        if (email.isEmpty() || !emailVerified) {
                            Log.i("submitEnabled", "SignUpScreen: $emailVerified")

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (emailVerified) "Valid First name" else stringResource(id = R.string.error_email),
                                color = if (emailVerified) colorResource(id = R.color.whatsapp) else Color.Red
                            )
                        }
                    }

                    FormTextField(
                        value = numSecuritySocial,
                        onValueChange = {
                            numSecuritySocial = it
                        },
                        label = stringResource(id = R.string.registration_number_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        isRequired = true
                    )

                    docs.mapIndexed { index, doc ->

                        if (index > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                verticalAlignment = CenterVertically
                            ) {

                                FormTextField(
                                    value = doc,
                                    onValueChange = {
                                        docs[index] = it
                                    },
                                    label = stringResource(id = R.string.add_document_text),
                                    modifier = Modifier
                                        .weight(0.85f)
                                        .padding(start = 20.dp),
                                    isRequired = true,
                                    readOnly = true,
                                    focusChange = { isFocused ->
                                        if (isFocused) {
                                            selectImage.launch("*/*")
                                        }
                                    }
                                )

                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .weight(0.15f)
                                        .height(20.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            if (docs.size > 1)
                                                docs = (docs - docs[index]).toMutableList()
                                        },
                                    contentDescription = ""
                                )
                            }
                        } else {
                            FormTextField(
                                value = doc,
                                onValueChange = {
                                    docs[index] = it
                                },
                                label = stringResource(id = R.string.add_document_text),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 20.dp),
                                isRequired = true,
                                readOnly = true,
                                focusChange = { isFocused ->
                                    if (isFocused) {
                                        selectImage.launch("*/*")
                                    }
                                }
                            )
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp, end = 20.dp)
                                .align(Alignment.CenterEnd)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    docs = (docs + "").toMutableList()
                                },
                            color = colorResource(id = R.color.whatsapp),
                            text = stringResource(id = R.string.add_document_text),
                        )
                    }

                    Button(
                        onClick = {
                            val s = validateProfileViewModel.validateEmail(email)
                            if (!emailVerified) activatedCheck = true

                            if (s && numSecuritySocial.isNotEmpty()) {
                                val validationProfileStatus = ValidationProfileStatus()
                                validationProfileStatus.email = email
                                validationProfileStatus.typeValidation = "company"
                                validationProfileStatus.registrationNumber = numSecuritySocial
                                validationProfileStatus.docs = docs
                                validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name

                                GlobalEntries.listImageUri.mapIndexed { index, uri ->
                                    uri?.let { validateProfileViewModel.uploadDoc(context, it, index) }
                                }
                                validateProfileViewModel.validate(validationProfileStatus)

                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.whatsapp)
                        )
                    ) {
                        Text(
                            stringResource(id = R.string.save_text),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                }
            }
            else -> {
                isRejected = false
                Column(modifier = Modifier.fillMaxWidth()) {
                    FormTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (activatedCheck) validateProfileViewModel.validateEmail(it)
                            validateProfileViewModel.changeUserEmail(it)
                        },
                        label = "Email",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 40.dp),
                        isRequired = true
                    )

                    if (activatedCheck) {
                        if (email.isEmpty() || !emailVerified) {
                            Log.i("submitEnabled", "SignUpScreen: $emailVerified")

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (emailVerified) "Valid First name" else stringResource(id = R.string.error_email),
                                color = if (emailVerified) colorResource(id = R.color.whatsapp) else Color.Red
                            )
                        }
                    }

                    FormTextField(
                        value = numSecuritySocial,
                        onValueChange = {
                            numSecuritySocial = it
                        },
                        label = stringResource(id = R.string.registration_number_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        isRequired = true
                    )

                    docs.mapIndexed { index, doc ->

                        if (index > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                verticalAlignment = CenterVertically
                            ) {

                                FormTextField(
                                    value = doc,
                                    onValueChange = {
                                        docs[index] = it
                                    },
                                    label = stringResource(id = R.string.add_document_text),
                                    modifier = Modifier
                                        .weight(0.85f)
                                        .padding(start = 20.dp),
                                    isRequired = true,
                                    readOnly = true,
                                    focusChange = { isFocused ->
                                        if (isFocused) {
                                            selectImage.launch("*/*")
                                        }
                                    }
                                )

                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .weight(0.15f)
                                        .height(20.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            if (docs.size > 1)
                                                docs = (docs - docs[index]).toMutableList()
                                        },
                                    contentDescription = ""
                                )
                            }
                        } else {
                            FormTextField(
                                value = doc,
                                onValueChange = {
                                    docs[index] = it
                                },
                                label = stringResource(id = R.string.add_document_text),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 20.dp),
                                isRequired = true,
                                readOnly = true,
                                focusChange = { isFocused ->
                                    if (isFocused) {
                                        selectImage.launch("*/*")
                                    }
                                }
                            )
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp, end = 20.dp)
                                .align(Alignment.CenterEnd)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    docs = (docs + "").toMutableList()
                                },
                            color = colorResource(id = R.color.whatsapp),
                            text = stringResource(id = R.string.add_document_text),
                        )
                    }

                    Button(
                        onClick = {
                            val s = validateProfileViewModel.validateEmail(email)
                            if (!emailVerified) activatedCheck = true

                            if (s && numSecuritySocial.isNotEmpty()) {
                                val validationProfileStatus = ValidationProfileStatus()
                                validationProfileStatus.email = email
                                validationProfileStatus.typeValidation = "company"
                                validationProfileStatus.registrationNumber = numSecuritySocial
                                validationProfileStatus.docs = docs
                                validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name

                                GlobalEntries.listImageUri.mapIndexed { index, uri ->
                                    uri?.let { validateProfileViewModel.uploadDoc(context, it, index) }
                                }
                                validateProfileViewModel.validate(validationProfileStatus)

                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.whatsapp)
                        )
                    ) {
                        Text(
                            stringResource(id = R.string.save_text),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}