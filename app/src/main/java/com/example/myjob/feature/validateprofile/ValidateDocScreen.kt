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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.feature.profile.test.FormTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateDocScreen(
    navController: NavController,
    selectImage: ActivityResultLauncher<String>,
    viewModel: InterviewValidationViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val user by viewModel.user.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    var docs by remember { mutableStateOf(mutableListOf("")) }
    var indexSelected by remember { mutableStateOf(0) }
    var docsVerify by remember { mutableStateOf(mutableListOf(false)) }
    var activatedCheck by remember { mutableStateOf(false) }

    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri.value = uri
            val listImageUri = GlobalEntries.listImageUri.toMutableList()
            listImageUri.add(uri)

            GlobalEntries.listImageUri = listImageUri

            val fileName = viewModel.imageInfo(context, uri)
            val list = docs
            list[indexSelected] = fileName
            docs = list
            Log.i("gjzgklehgklz", "ValidateDocScreen: $indexSelected")
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        docs = (docs + "").toMutableList()
                        docsVerify = (docsVerify + false).toMutableList()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "More",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        //docs
        Log.i("jfeakhgealgk", "ValidateDocScreen: ${imageUri.value}")
        docs.mapIndexed { index, doc ->
            FormTextField(
                value = doc,
                borderColor = if (activatedCheck && doc.isEmpty()) Color.Red else colorResource(
                    id = R.color.whatsapp
                ),
                onValueChange = {
                    //docs[index] = it
                },
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

            if (index > 0) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp, end = 20.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                docs = (docs - doc).toMutableList()
                                /*GlobalEntries.listImageUri =
                                    (GlobalEntries.listImageUri - uri).toMutableList()*/
                                docsVerify = (docsVerify - docsVerify[index]).toMutableList()

                            },
                        color = Color.Red,
                        text = stringResource(id = R.string.remove_document_text),
                    )
                }
            }
        }


        Button(
            onClick = {
                docs.mapIndexed { index, document ->
                    if (document.isEmpty()) {
                        docsVerify[index] = true
                    }
                }
                val isNoError = docsVerify.none { it }
                if (!isNoError) {
                    activatedCheck = true
                }

                if (isNoError) {
                    Log.i("lkehagkelga", "ValidateDocScreen: ${GlobalEntries.listImageUri}")
                    GlobalEntries.listImageUri.mapIndexed { index, document ->
                        document?.let {
                            viewModel.uploadDoc(context = context, it, index)
                        }
                    }
                    viewModel.changeDocs(docs)
                    GlobalEntries.stepShared = 0

                    val validationProfileStatus = ValidationProfileStatus()
                    validationProfileStatus.typeValidation = "doc"
                    validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name
                    viewModel.validate(validationProfileStatus)

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

/*docs.mapIndexed { index, doc ->
            FormTextField(
                value = doc,
                borderColor = if (activatedCheck && doc.isEmpty()) Color.Red else colorResource(
                    id = R.color.whatsapp
                ),
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
                onClick = {
                    selectImage.launch("image/*")
                }
            )

            if (index > 0) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp, end = 20.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                docs = (docs - doc).toMutableList()
                                docsVerify = (docsVerify - docsVerify[index]).toMutableList()

                            },
                        color = Color.Red,
                        text = stringResource(id = R.string.remove_document_text),
                    )
                }
            }
        }*/