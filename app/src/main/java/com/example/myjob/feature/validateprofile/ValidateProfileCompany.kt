package com.example.myjob.feature.validateprofile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.profile.test.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateProfileCompany(
    navController: NavController,
    addCompanyToList: () -> Unit = {},
    validateProfileViewModel: ValidateProfileViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val user by validateProfileViewModel.user.collectAsState()
    val isEmailValid by validateProfileViewModel.isEmailValid.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    var numSecuritySocial by remember { mutableStateOf("") }
    var docs by remember { mutableStateOf(mutableListOf("")) }
    var documentList by remember { mutableStateOf(mutableListOf(Documents())) }

    var activatedCheck by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    val emailVerified by remember { derivedStateOf { isEmailValid } }

    val verificationSteps by validateProfileViewModel.verificationSteps.collectAsState()
    var isRejected by remember { mutableStateOf(false) }

    var indexSelected by remember { mutableStateOf(0) }
    var docsVerify by remember { mutableStateOf(mutableListOf(false)) }

    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    val filesList by validateProfileViewModel.filesList.collectAsState()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            validateProfileViewModel.getFiles()
        }
    }

    LaunchedEffect(filesList) {
        if (filesList.isNotEmpty()) {
            documentList.clear()
            filesList.map {
                val document = Documents(
                    name = validateProfileViewModel.getNameDocFromLink(it),
                    url = it,
                    type = validateProfileViewModel.getTypeDocFromLink(it)
                )
                documentList = (documentList + document).toMutableList()
            }
        }
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri.value = uri
            val listImageUri = GlobalEntries.listCompanyImageUri.toMutableList()
            listImageUri.add(uri)

            GlobalEntries.listCompanyImageUri = listImageUri

            val fileName = validateProfileViewModel.imageInfo(context, uri)
            val list = docs
            list[indexSelected] = fileName
            docs = list

            documentList = (documentList - documentList[indexSelected]).toMutableList()
            val document = Documents(
                url = uri.path ?: "",
                name = fileName,
                type = validateProfileViewModel.getTypeDoc(fileName)
            )
            documentList.add(indexSelected, document)

        } else {
            // Handle the case where no media was selected
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.whatsapp)
                )
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.validate_profile_text),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 20.dp),
                    color = Color.White
                )
            }
        }

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
                        borderColor = if (activatedCheck && email.isEmpty()) Color.Red else colorResource(
                            id = R.color.whatsapp
                        ),
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
                                text = stringResource(id = R.string.error_email),
                                color = Color.Red
                            )
                        }
                    }

                    FormTextField(
                        value = numSecuritySocial,
                        borderColor = if (activatedCheck && numSecuritySocial.isEmpty()) Color.Red else colorResource(
                            id = R.color.whatsapp
                        ),
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

                    documentList.mapIndexed { index, doc ->
                        if (index > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                verticalAlignment = CenterVertically
                            ) {

                                FormTextField(
                                    value = doc.name,
                                    borderColor = if (activatedCheck && doc.name.isEmpty()) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {
                                    },
                                    label = stringResource(id = R.string.add_document_text),
                                    modifier = Modifier
                                        .weight(0.85f)
                                        .padding(start = 20.dp),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        indexSelected = index
                                        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                                            if (docs.size > 1) {
                                                docs = (docs - docs[index]).toMutableList()
                                                documentList = (documentList - documentList[index]).toMutableList()
                                                docsVerify =
                                                    (docsVerify - docsVerify[index]).toMutableList()
                                            }
                                        },
                                    contentDescription = ""
                                )
                            }
                        } else {
                            FormTextField(
                                value = doc.name,
                                borderColor = if (activatedCheck && doc.name.isEmpty()) Color.Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                onValueChange = {},
                                label = stringResource(id = R.string.add_document_text),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 20.dp),
                                isRequired = true,
                                readOnly = true,
                                onClick = {
                                    indexSelected = index
                                    pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                                    val document = Documents()
                                    documentList = (documentList + document).toMutableList()
                                    docsVerify = (docsVerify + false).toMutableList()
                                },
                            color = colorResource(id = R.color.whatsapp),
                            text = stringResource(id = R.string.add_document_text),
                        )
                    }

                    Button(
                        onClick = {
                            val s = validateProfileViewModel.validateEmail(email)
                            docs.mapIndexed { index, document ->
                                if (document.isEmpty()) {
                                    docsVerify[index] = true
                                }
                            }
                            val isNoError = docsVerify.none { it }
                            if (!isNoError || !emailVerified) {
                                activatedCheck = true
                            }

                            if (isNoError && s && numSecuritySocial.isNotEmpty()) {
                                val validationProfileStatus = ValidationProfileStatus()
                                validationProfileStatus.email = email
                                validationProfileStatus.typeValidation = "company"
                                validationProfileStatus.registrationNumber = numSecuritySocial
                                validationProfileStatus.docs = docs
                                validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name

                                GlobalEntries.listCompanyImageUri.mapIndexed { index, uri ->
                                    uri?.let { validateProfileViewModel.uploadDoc(context = context, it, index, documentList[index]) }
                                }
                                validateProfileViewModel.validate(validationProfileStatus)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
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
                        borderColor = if (activatedCheck && email.isEmpty()) Color.Red else colorResource(
                            id = R.color.whatsapp
                        ),
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
                                text = stringResource(id = R.string.error_email),
                                color = Color.Red
                            )
                        }
                    }

                    FormTextField(
                        value = numSecuritySocial,
                        borderColor = if (activatedCheck && numSecuritySocial.isEmpty()) Color.Red else colorResource(
                            id = R.color.whatsapp
                        ),
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

                    documentList.mapIndexed { index, doc ->
                        if (index > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                verticalAlignment = CenterVertically
                            ) {

                                FormTextField(
                                    value = doc.name,
                                    borderColor = if (activatedCheck && doc.name.isEmpty()) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {
                                    },
                                    label = stringResource(id = R.string.add_document_text),
                                    modifier = Modifier
                                        .weight(0.85f)
                                        .padding(start = 20.dp),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        indexSelected = index
                                        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                                            if (docs.size > 1) {
                                                docs = (docs - docs[index]).toMutableList()
                                                documentList = (documentList - documentList[index]).toMutableList()
                                                docsVerify =
                                                    (docsVerify - docsVerify[index]).toMutableList()
                                            }
                                        },
                                    contentDescription = ""
                                )
                            }
                        } else {
                            FormTextField(
                                value = doc.name,
                                borderColor = if (activatedCheck && doc.name.isEmpty()) Color.Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                onValueChange = {},
                                label = stringResource(id = R.string.add_document_text),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 20.dp),
                                isRequired = true,
                                readOnly = true,
                                onClick = {
                                    indexSelected = index
                                    pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
                                    val document = Documents()
                                    documentList = (documentList + document).toMutableList()
                                    docsVerify = (docsVerify + false).toMutableList()
                                },
                            color = colorResource(id = R.color.whatsapp),
                            text = stringResource(id = R.string.add_document_text),
                        )
                    }

                    Button(
                        onClick = {
                            val s = validateProfileViewModel.validateEmail(email)
                            docs.mapIndexed { index, document ->
                                if (document.isEmpty()) {
                                    docsVerify[index] = true
                                }
                            }
                            val isNoError = docsVerify.none { it }
                            if (!isNoError || !emailVerified) {
                                activatedCheck = true
                            }

                            if (isNoError && s && numSecuritySocial.isNotEmpty()) {
                                val validationProfileStatus = ValidationProfileStatus()
                                validationProfileStatus.email = email
                                validationProfileStatus.typeValidation = "company"
                                validationProfileStatus.registrationNumber = numSecuritySocial
                                validationProfileStatus.docs = docs
                                validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name

                                GlobalEntries.listCompanyImageUri.mapIndexed { index, uri ->
                                    uri?.let { validateProfileViewModel.uploadDoc(context = context, it, index, documentList[index]) }
                                }
                                validateProfileViewModel.validate(validationProfileStatus)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
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