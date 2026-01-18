package com.example.myjob.feature.validateprofile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
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
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.profile.test.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateProfileCompany(
    navController: NavController,
    validateProfileViewModel: ValidateProfileViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val verificationSteps by validateProfileViewModel.verificationSteps.collectAsState()
    val message by validateProfileViewModel.message.collectAsState()
    val isEmailValid by validateProfileViewModel.isEmailValid.collectAsState()
    val filesLists by validateProfileViewModel.filesList.collectAsState()
    var filesList by remember(filesLists) { mutableStateOf(
        filesLists.ifEmpty { listOf("") }
    ) }

    val interactionSource = remember { MutableInteractionSource() }
    var activatedCheck by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    val emailVerified by remember { derivedStateOf { isEmailValid } }
    var numSecuritySocial by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    var isProgressing by remember { mutableStateOf(false) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            validateProfileViewModel.getFiles()
        }
    }

    if (showDialog) {
        isProgressing = false
        CustomDialog(isSuccess = isSuccess, message = message) {
            showDialog = false
            isProgressing = false
            navController.popBackStack()
        }
    }

    LaunchedEffect(message) {
        if (message == "your docs has uploaded") {
            filesList.mapIndexed { index, path ->
                if (path.isNotEmpty()) {
                    val fileName = validateProfileViewModel.imageInfo(context, validateProfileViewModel.fromPathToUri(path))
                    val document = Documents(
                        url = path,
                        name = fileName,
                        type = validateProfileViewModel.getTypeDoc(fileName)
                    )
                    validateProfileViewModel.uploadDoc(context = context, validateProfileViewModel.fromPathToUri(path), index, document)
                }
            }
        }
    }

    val uploadMessage by validateProfileViewModel.uploadMessage.collectAsState()
    Log.i("grthtrhrhrhttr", "ValidateProfileCompany: $uploadMessage")

    LaunchedEffect(uploadMessage) {
        if (uploadMessage.contains("http")) {
            GlobalEntries.stepShared = 0
            navController.popBackStack()
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

            filesList = if (filesList.contains("")) {
                filesList.mapIndexed { index, value ->
                    if (index == 0) uri.path ?: value else value
                }
            } else {
                (filesList + (uri.path ?: "")).toMutableList()
            }

        }
    }

    Box(modifier = Modifier
        .fillMaxSize()) {

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
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        text = stringResource(id = R.string.verification_info_text),
                        color = Color.Cyan
                    )
                }
                VerificationStatus.VERIFIED.name -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        text = stringResource(id = R.string.verification_validate_info_text),
                        color = colorResource(id = R.color.whatsapp)
                    )

                }
                VerificationStatus.REJECTED.name -> {
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

                        filesList.mapIndexed { index, doc ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                verticalAlignment = CenterVertically
                            ) {

                                FormTextField(
                                    value = doc,
                                    borderColor = if (activatedCheck && doc.isEmpty()) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {},
                                    label = stringResource(id = R.string.add_document_text),
                                    modifier = Modifier
                                        .weight(0.85f)
                                        .padding(start = 20.dp),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                    }
                                )

                                if (index > 0) {
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
                                                filesList =
                                                    (filesList - filesList[index]).toMutableList()
                                            },
                                        contentDescription = ""
                                    )
                                }
                            }
                            Log.i("fjzkgrhzrjgzg", "activatedCheck: $activatedCheck")

                            if (activatedCheck) {
                                Log.i("fjzkgrhzrjgzg", "ValidateProfileCompany: $doc")
                                if (doc.isEmpty()) {
                                    Text(
                                        modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                        text = stringResource(id = R.string.upload_doc_empty),
                                        color = Color.Red
                                    )
                                }
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
                                        filesList = (filesList + "").toMutableList()
                                    },
                                color = colorResource(id = R.color.whatsapp),
                                text = stringResource(id = R.string.add_document_text),
                            )
                        }

                        Button(
                            onClick = {
                                val s = validateProfileViewModel.validateEmail(email)

                                val isNoError = filesList.none { it.isEmpty() }
                                if (!isNoError || !emailVerified) {
                                    activatedCheck = true
                                }

                                if (isNoError && s && numSecuritySocial.isNotEmpty()) {

                                    val listDoc = mutableListOf<String>()
                                    val listDocuments = mutableListOf<Documents>()
                                    filesList.mapIndexed { _, path ->
                                        val fileName = validateProfileViewModel.imageInfo(context, validateProfileViewModel.fromPathToUri(path))
                                        val type = validateProfileViewModel.getTypeDoc(fileName)

                                        val document = Documents(
                                            name = fileName,
                                            type = type
                                        )

                                        listDoc.add(fileName)
                                        listDocuments.add(document)
                                    }

                                    val validationProfileStatus = ValidationProfileStatus()
                                    validationProfileStatus.email = email
                                    validationProfileStatus.typeValidation = "company"
                                    validationProfileStatus.registrationNumber = numSecuritySocial
                                    validationProfileStatus.docs = listDoc
                                    validationProfileStatus.documents = listDocuments
                                    validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name
                                    validateProfileViewModel.validate(validationProfileStatus)
                                    isProgressing = true
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

                        filesList.mapIndexed { index, doc ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                verticalAlignment = CenterVertically
                            ) {

                                FormTextField(
                                    value = doc,
                                    borderColor = if (activatedCheck && doc.isEmpty()) Color.Red else colorResource(
                                        id = R.color.whatsapp
                                    ),
                                    onValueChange = {
                                    },
                                    label = stringResource(id = R.string.add_document_text),
                                    modifier = Modifier
                                        .then(
                                            if (index != 0) Modifier.weight(0.85f)
                                            else Modifier
                                                .weight(1f)
                                                .padding(end = 20.dp)
                                        )
                                        .padding(start = 20.dp),
                                    isRequired = true,
                                    readOnly = true,
                                    onClick = {
                                        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                    }
                                )

                                if (index > 0) {
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
                                                filesList =
                                                    (filesList - filesList[index]).toMutableList()
                                            },
                                        contentDescription = ""
                                    )
                                }
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
                                        filesList = (filesList + "").toMutableList()
                                    },
                                color = colorResource(id = R.color.whatsapp),
                                text = stringResource(id = R.string.add_document_text),
                            )
                        }

                        Button(
                            onClick = {
                                val s = validateProfileViewModel.validateEmail(email)

                                val isNoError = filesList.none { it.isEmpty() }
                                if (!isNoError || !emailVerified) {
                                    activatedCheck = true
                                }

                                if (isNoError && s && numSecuritySocial.isNotEmpty()) {

                                    val listDoc = mutableListOf<String>()
                                    val listDocuments = mutableListOf<Documents>()
                                    filesList.mapIndexed { _, path ->
                                        val fileName = validateProfileViewModel.imageInfo(context, validateProfileViewModel.fromPathToUri(path))
                                        val type = validateProfileViewModel.getTypeDoc(fileName)

                                        val document = Documents(
                                            name = fileName,
                                            type = type
                                        )

                                        listDoc.add(fileName)
                                        listDocuments.add(document)
                                    }

                                    val validationProfileStatus = ValidationProfileStatus()
                                    validationProfileStatus.email = email
                                    validationProfileStatus.typeValidation = "company"
                                    validationProfileStatus.registrationNumber = numSecuritySocial
                                    validationProfileStatus.docs = listDoc
                                    validationProfileStatus.documents = listDocuments
                                    validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name
                                    validateProfileViewModel.validate(validationProfileStatus)
                                    isProgressing = true
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

        if (isProgressing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center)
            {

                Card(
                    modifier = Modifier
                        .size(150.dp)
                        .background(shape = RoundedCornerShape(30.dp), color = Color.White),
                    elevation = 15.dp,
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(20.dp),
                            color = colorResource(id = R.color.whatsapp),
                            strokeWidth = 8.dp,
                            trackColor = Color.LightGray,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}