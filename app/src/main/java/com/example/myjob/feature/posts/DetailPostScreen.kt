package com.example.myjob.feature.posts

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.common.GlobalEntries.isFromNotification
import com.example.myjob.common.GlobalEntries.otherUserId
import com.example.myjob.common.GlobalEntries.otherUserName
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.feature.invitation.detail.DetailRow
import com.example.myjob.feature.invitation.detail.InfoChip
import com.example.myjob.feature.invitation.detail.WhatsAppGreen
import com.example.myjob.feature.invitation.detail.WhatsAppGreenSurface
import com.example.myjob.feature.navigation.Screen

@Composable
fun DetailPostScreen(
    navController: NavController,
    postsViewModel: PostsViewModel = hiltViewModel()
) {

    val whatsappGreen = colorResource(id = R.color.whatsapp)
    val scrollState = rememberScrollState()
    var showComments by remember { mutableStateOf(false) }

    val userConnectedId by postsViewModel.userConnectedId.collectAsState()
    val userCompany by postsViewModel.companyDetail.collectAsStateWithLifecycle()
    val announcement by postsViewModel.post.collectAsStateWithLifecycle()
    val commentsCompany by postsViewModel.commentsCompany.collectAsStateWithLifecycle()

    var selectedPost by remember(commentsCompany) { mutableStateOf(commentsCompany) }

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            postsViewModel.getCompanyDetail(GlobalEntries.idCompany)
            postsViewModel.getPostById(GlobalEntries.idAnnounce, GlobalEntries.idCompany)
            postsViewModel.getPostCommentsCompany(GlobalEntries.idAnnounce)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.whatsapp)),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 15.dp)
                ) {

                    IconButton(
                        onClick = {
                            if (isFromNotification) {
                                navController.popBackStack(Screen.HomeScreen.route, false)
                                isFromNotification = false
                            } else navController.popBackStack()
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterStart)
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
                        text = stringResource(id = R.string.announce_text),
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp)
            ) {

                val name = userCompany.companyName ?: "Test Test"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.company_information_text),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Initials Avatar
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = WhatsAppGreen
                            ) {
                                val nickname = name.trim().ifEmpty { "Test Test" }.split(" ")
                                    .map { it.first() }.joinToString("")
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = nickname,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                userCompany.companyActivitySector?.let {
                                    if (it.isNotEmpty()) {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        // Info chips
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            userCompany.email?.let {
                                if (it.isNotEmpty()) {
                                    InfoChip(
                                        icon = Icons.Default.Business,
                                        text = it
                                    )
                                }
                            }

                            userCompany.country?.let {
                                if (it.isNotEmpty()) {
                                    InfoChip(
                                        icon = Icons.Default.Work,
                                        text = it
                                    )
                                }
                            }

                            Column {
                                InfoChip(
                                    icon = Icons.Default.LocationOn,
                                    text = userCompany.addressList?.joinToString { "\n" }?:""
                                )
                            }

                            Column {
                                InfoChip(
                                    icon = Icons.Default.Phone,
                                    text = userCompany.phoneList?.joinToString { "\n" }?:""
                                )
                            }
                        }

                        //Contact button
                        OutlinedButton(
                            onClick = {
                                candidateUser = GlobalEntries.user
                                otherUserId = userCompany.id ?: -1
                                otherUserName = userCompany.companyName ?: ""

                                navController.navigate(Screen.SendMessageScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, whatsappGreen)
                        ) {
                            androidx.compose.material3.Icon(
                                Icons.Default.Message,
                                contentDescription = "Contact",
                                tint = whatsappGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.contact_text), color = whatsappGreen)
                        }
                    }
                }

                val eventText = stringResource(id = R.string.event_candidate_text)
                val internshipText = stringResource(id = R.string.internship_text)
                val workshopText = stringResource(id = R.string.apprenticeship_text)

                val (typeColor, typeIcon, typeText) = when (announcement.postType) {
                    PostType.EVENT.name -> Triple(Color(0xFFFF9800), Icons.Default.Event, eventText)
                    PostType.WORKSHOP.name -> Triple(
                        Color(0xFF2196F3),
                        Icons.Default.Work,
                        workshopText
                    )

                    PostType.INTERNSHIP.name -> Triple(
                        WhatsAppGreen,
                        Icons.Default.Person,
                        internshipText
                    )

                    else -> Triple(Color(0xFFF44336), Icons.Default.Cancel, "")
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = stringResource(id = R.string.post_content_text),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.CenterEnd),
                                    onClick = {
                                        showComments = true
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        tint = whatsappGreen,
                                        contentDescription = ""
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    color = typeColor
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        androidx.compose.material3.Icon(
                                            typeIcon,
                                            contentDescription = "",
                                            modifier = Modifier.size(20.dp),
                                            tint = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = typeText,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = typeColor
                                    )
                                    Text(
                                        text = stringResource(
                                            id = R.string.title_date_posted_text,
                                            announcement.date ?: ""
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Subject
                        DetailRow(
                            icon = Icons.Default.Subject,
                            label = "Titre",
                            value = announcement.title
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Message
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.material3.Icon(
                                    Icons.Default.Message,
                                    contentDescription = "Message",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = announcement.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp),
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.padding(top = 15.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = WhatsAppGreenSurface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.other_detail_text),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Like
                            DetailRow(
                                icon = Icons.Default.ThumbUp,
                                label = stringResource(id = R.string.like_text),
                                value = announcement.likes?.size.toString()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            //comment
                            DetailRow(
                                icon = Icons.Default.CommentBank,
                                label = stringResource(id = R.string.comments_text),
                                value = announcement.comments?.size.toString()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        AnimatedVisibility(
            visible = showComments,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            androidx.compose.material.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                HorizontalDivider(
                                    thickness = 5.dp,
                                    modifier = Modifier.width(100.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = stringResource(id = R.string.comments_text),
                                        modifier = Modifier.align(Alignment.Center),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )

                                    IconButton(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(end = 10.dp)
                                            .align(Alignment.CenterEnd),
                                        onClick = {
                                            showComments = false
                                        }) {
                                        androidx.compose.material3.Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "close"
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Log.i("klzhgkzgz", "getPostCommentsCompany: $selectedPost")

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {

                            itemsIndexed(
                                items = selectedPost
                            ) { index, comment ->
                                CommentItem(comment)
                            }
                        }
                    }

                    var commentText by remember { mutableStateOf("") }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .border(
                                1.dp,
                                whatsappGreen,
                                RectangleShape
                            )
                            .align(Alignment.BottomCenter),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text(stringResource(id = R.string.add_comment_hint_text)) },
                            shape = RoundedCornerShape(24.dp),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.whatsapp),
                                focusedLabelColor = colorResource(id = R.color.whatsapp)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (commentText.isNotBlank()) {

                                    postsViewModel.addComment(
                                        announcement.idAnnounce,
                                        commentText,
                                        userCompany.companyName ?: ""
                                    )

                                    val c = CommentsPost(
                                        idCandidate = userConnectedId,
                                        text = commentText,
                                        userName = userCompany.companyName ?: ""
                                    )

                                    selectedPost = (selectedPost + c).toMutableList()

                                    commentText = ""
                                }
                            }
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send comment",
                                tint = colorResource(id = R.color.whatsapp)
                            )
                        }
                    }
                }
            }
        }
    }

}