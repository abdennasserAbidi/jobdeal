package com.example.myjob.feature.messagerie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myjob.base.ConnectionState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionScreen(
    navController: NavController,
    conversationId: String = "",
    conversationName: String = "Chat",
    viewModel: DiscussionViewModel = hiltViewModel(),
    hideNavigation: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val messages by viewModel.messages.collectAsState()
    val typingUsers by viewModel.typingUsers.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        viewModel.connectSocket()
        viewModel.selectConversation(conversationId)
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(conversationName)
                        Text(
                            text = when {
                                connectionState != ConnectionState.CONNECTED -> "connecting..."
                                typingUsers.isNotEmpty() -> "${typingUsers.first()} is typing..."
                                else -> "online"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (connectionState == ConnectionState.CONNECTED)
                                Color.White.copy(alpha = 0.7f)
                            else
                                Color.Yellow
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Connection indicator
            if (connectionState != ConnectionState.CONNECTED) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Yellow
                ) {
                    Text(
                        text = when (connectionState) {
                            ConnectionState.CONNECTING -> "Connecting..."
                            ConnectionState.ERROR -> "Connection error. Retrying..."
                            else -> "Disconnected"
                        },
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(
                        message = message,
                        isOwnMessage = message.senderId == "user123"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Input Field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = {
                        messageText = it
                        if (it.isNotEmpty()) {
                            viewModel.sendTypingIndicator(true)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    shape = RoundedCornerShape(24.dp),
                    enabled = connectionState == ConnectionState.CONNECTED
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText)
                            viewModel.sendTypingIndicator(false)
                            messageText = ""
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    containerColor = MaterialTheme.colorScheme.primary
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

@Composable
fun MessageBubble(
    message: ChatMessage,
    isOwnMessage: Boolean
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        if (!isOwnMessage) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.first().toString().uppercase(),
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
                    text = message.senderName,
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
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    color = if (isOwnMessage) Color.White else Color.Black
                )
            }

            Text(
                text = timeFormat.format(Date(message.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

// ConversationsListScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsListScreen(
    viewModel: DiscussionViewModel = viewModel(),
    onConversationClick: (String, String) -> Unit
) {
    val conversations by viewModel.conversations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Messages") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Open new conversation dialog */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Chat",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(conversations) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    onClick = {
                        onConversationClick(conversation.id, "conversation.name")
                    }
                )
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ala".first().toString().uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "conversation.name",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                conversation.lastMessage?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            conversation.lastMessageTime?.let { time ->
                Text(
                    text = timeFormat.format(Date(time)),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}