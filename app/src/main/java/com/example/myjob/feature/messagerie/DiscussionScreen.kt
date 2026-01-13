package com.example.myjob.feature.messagerie

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.validateprofile.Documents
import java.util.*

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionScreen(
    navController: NavController,
    conversationName: String = "Chat",
    viewModel: DiscussionViewModel = hiltViewModel(),
    hideNavigation: () -> Unit = {}
) {
    val listMessages by viewModel.listMessages.collectAsState()
    val messages = viewModel.messages

    var isFirstTime by remember { mutableStateOf(true) }
    var listUri by remember { mutableStateOf(mutableListOf<Uri>()) }

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isFirstTime = false
            listUri.add(it)
            val text = viewModel.readTextFromUri(context, it)
            //onFilePicked(text)
        }
    }


    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            isFirstTime = true
            hideNavigation()
            viewModel.connect()
            viewModel.findConversations(GlobalEntries.otherUserId)
        }
    }

    LaunchedEffect(listMessages.size) {
        if (listMessages.isNotEmpty()) {
            listState.animateScrollToItem(listMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(conversationName)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.whatsapp),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            // Messages List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(listMessages) { message ->
                    val userId = GlobalEntries.otherUserId
                    MessageBubble(
                        isFirstTime = isFirstTime,
                        listUri = listUri,
                        message = message,
                        isOwnMessage = viewModel.isOwnMessage(message.userConnectedId)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(listUri) { _, item ->

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .height(120.dp)
                                .width(60.dp),
                            contentScale = ContentScale.Crop,
                            onError = { error ->
                                Log.e("IMAGE_ERROR", "Image failed to load: ${error.result.throwable.message ?: "Unknown error"}")
                            }
                        )

                        /*Image(
                            painter = rememberAsyncImagePainter(model = item),
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .height(120.dp)
                                .width(60.dp)
                        )*/

                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = {
                                messageText = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Type a message...") },
                            shape = RoundedCornerShape(24.dp)
                        )

                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 10.dp),
                            onClick = {
                                launcher.launch("*/*")
                            }) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                tint = colorResource(id = R.color.whatsapp),
                                contentDescription = ""
                            )
                        }

                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    val uploadMessage by viewModel.uploadMessage.collectAsState()
                    LaunchedEffect(uploadMessage) {
                        if (uploadMessage.contains("http")) {
                            listUri = mutableListOf()
                        }
                    }

                    val listMessages by viewModel.listMessages.collectAsState()
                    LaunchedEffect(listMessages) {
                        listUri.map { uri ->
                            val document = Documents(
                                url = uri.path ?: "",
                                name = "",
                                type = ""
                            )
                            viewModel.uploadDoc(context = context, uri, -1, document)
                        }
                    }

                    FloatingActionButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                viewModel.sendMessage(messageText)
                                messageText = ""
                            } else {
                                viewModel.uploadListDoc(context = context, listUri)
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        containerColor = colorResource(id = R.color.whatsapp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    isFirstTime: Boolean,
    listUri: List<Uri>,
    message: ChatMessage,
    isOwnMessage: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        if (!isOwnMessage) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.whatsapp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = GlobalEntries.otherUserName.ifEmpty { "Test Test" }.first().toString()
                        .uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start
        ) {
            if (!isOwnMessage) {
                Text(
                    text = GlobalEntries.otherUserName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isOwnMessage) 16.dp else 4.dp,
                    bottomEnd = if (isOwnMessage) 4.dp else 16.dp
                ),
                color = if (isOwnMessage)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            ) {

                Column {
                    Box {
                        if (!isFirstTime) {
                            listUri.map {
                                Image(
                                    painter = rememberAsyncImagePainter(model = it),
                                    contentDescription = "Selected image",
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(100.dp)
                                        .padding(10.dp)
                                )
                            }
                        } else {
                            message.documents.map { item ->
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(100.dp)
                                        .padding(10.dp),
                                    contentScale = ContentScale.Crop,
                                    onError = { error ->
                                        Log.e("IMAGE_ERROR", "Image failed to load: ${error.result.throwable.message ?: "Unknown error"}")
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = message.content,
                        modifier = Modifier.padding(12.dp),
                        color = if (isOwnMessage) Color.White else Color.Black
                    )
                }
            }
        }
    }
}