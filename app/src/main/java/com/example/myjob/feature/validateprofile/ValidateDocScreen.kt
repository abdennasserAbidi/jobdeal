package com.example.myjob.feature.validateprofile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myjob.R
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.profile.test.FormTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateDocScreen(
    navController: NavController,
    viewModel: InterviewValidationViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val user by viewModel.user.collectAsState()
    val filesList by viewModel.filesList.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    var docs by remember { mutableStateOf(mutableListOf("")) }
    var documentList by remember { mutableStateOf(mutableListOf(Documents())) }

    var showDialog by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            //viewModel.getFiles()
        }
    }

    val message by viewModel.message.collectAsState()

    if (showDialog) {
        CustomDialog(isSuccess = isSuccess, message = message) {
            showDialog = false
            navController.popBackStack()
        }
    }

    LaunchedEffect(message) {
        if (message == "your docs has uploaded") {
            //viewModel.changeDocs(docs)
            GlobalEntries.listImageUri.mapIndexed { index, document ->
                if (index < documentList.size) {
                    viewModel.uploadDoc(context = context, document.second, index, documentList[index])
                }
            }
        }
    }

    val uploadMessage by viewModel.uploadMessage.collectAsState()
    LaunchedEffect(uploadMessage) {
        if (uploadMessage.contains("http")) {
            GlobalEntries.stepShared = 0
            navController.popBackStack()
        }
    }

    LaunchedEffect(filesList) {
        if (filesList.isNotEmpty()) {
            GlobalEntries.listImageUri.clear()
            filesList.map {
                val document = Documents(
                    name = viewModel.getNameDocFromLink(it),
                    url = it,
                    type = viewModel.getTypeDocFromLink(it)
                )
                //documentList = (documentList + document).toMutableList()
                val uri: Uri = Uri.parse(it)
                GlobalEntries.listImageUri = (GlobalEntries.listImageUri + Pair(it, uri)).toMutableList()
            }
        }
    }

    LaunchedEffect(GlobalEntries.listImageUri) {
        //documentList.clear()
        Log.i("tgrklhrhrhlrt", "documentList: $documentList")
        Log.i("tgrklhrhrhlrt", "listImageUri: ${GlobalEntries.listImageUri}")
        GlobalEntries.listImageUri.map {
            val fileName = viewModel.imageInfo(context, it.second)
            val document = Documents(
                url = it.first,
                name = fileName,
                type = viewModel.getTypeDoc(fileName)
            )
            documentList.add(document)
        }
    }

    var indexSelected by remember { mutableStateOf(0) }
    var docsVerify by remember { mutableStateOf(mutableListOf(false)) }
    var activatedCheck by remember { mutableStateOf(false) }

    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri.value = uri
            GlobalEntries.listImageUri = (GlobalEntries.listImageUri + Pair(uri.path ?: "", uri)).toMutableList()

            val fileName = viewModel.imageInfo(context, uri)
            /*docs = (docs - docs[indexSelected]).toMutableList()
            docs.add(indexSelected, fileName)*/



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
                        //docs = (docs + "").toMutableList()
                        val document = Documents()
                        //documentList = (documentList + document).toMutableList()
                        GlobalEntries.listImageUri = (GlobalEntries.listImageUri + Pair("", Uri.parse(""))).toMutableList()
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
        documentList.mapIndexed { index, doc ->
            FormTextField(
                value = doc.name,
                borderColor = if (activatedCheck && doc.name.isEmpty()) Color.Red else colorResource(
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

                                //docs = (docs - doc.name).toMutableList()
                                documentList = (documentList - documentList[index]).toMutableList()
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
                documentList.mapIndexed { index, document ->
                    if (document.name.isEmpty()) {
                        docsVerify[index] = true
                    }
                }
                val isNoError = docsVerify.none { it }
                if (!isNoError) {
                    activatedCheck = true
                }

                if (isNoError) {

                    val validationProfileStatus = ValidationProfileStatus()
                    validationProfileStatus.typeValidation = "doc"
                    validationProfileStatus.docs = docs
                    validationProfileStatus.documents = documentList
                    validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name
                    viewModel.validate(validationProfileStatus)
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

        Log.e("IMAGE_ERROR", "documentList : $documentList")

        LazyRow {
            itemsIndexed(
                items = documentList
            ) { index, doc ->
                Log.e("IMAGE_ERROR", "Image failed to load: ${doc.url}")
                doc.url
                if (doc.url.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://res.cloudinary.com/dds7p6ltm/image/upload/v1767527341/document74.jpg")
                            .crossfade(true)
                            .build(),
                        //model = doc.url,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop,
                        onError = { error ->
                            Log.e("IMAGE_ERROR", "Image failed to load: ${error.result.throwable.message ?: "Unknown error"}")
                        }
                    )
                }
            }
        }

    }
}