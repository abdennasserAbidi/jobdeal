package com.example.myjob.feature.posts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidatePostScreen(
    navController: NavController,
    postsViewModel: PostsViewModel = hiltViewModel()
) {

    val announcementModel = postsViewModel.announcementForCandidate.collectAsLazyPagingItems()

    val countList by postsViewModel.countList.collectAsState()
    val userConnectedId by postsViewModel.userConnectedId.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    var showComments by remember { mutableStateOf(false) }

    val commentsCompany by postsViewModel.commentsCompany.collectAsStateWithLifecycle()
    var selectedPost by remember(commentsCompany) { mutableStateOf(commentsCompany) }

    var isFirstTime by remember { mutableStateOf(false) }

    val isAllPostLiked by postsViewModel.isAllPostLiked.collectAsState()
    var isAllPostLikedUser by remember(isAllPostLiked) { mutableStateOf(isAllPostLiked) }
    var isItemLiked by remember { mutableStateOf(false) }

    val numberLikes by postsViewModel.numberLikes.collectAsState()
    var numberLikesUser by remember(numberLikes) { mutableStateOf(numberLikes) }
    var itemNumberLike by remember { mutableStateOf(0) }

    val numberComment by postsViewModel.numberComment.collectAsState()
    var numberCommentUser by remember(numberComment) { mutableStateOf(numberComment) }
    var itemNumberComment by remember { mutableStateOf(0) }

    var idAnnounceComment by remember { mutableStateOf(0) }

    var showSearchSheet by remember { mutableStateOf(false) }
    var selectedSearch by remember { mutableStateOf(PostType.ALL) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            isFirstTime = true
            postsViewModel.isAllPostLiked()
            postsViewModel.getPostNumberLikes()
            postsViewModel.getNumberCommentAllPosts()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            showComments = false
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                        text = stringResource(id = R.string.posts_text),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 20.dp),
                        color = Color.White
                    )
                }
            }

            val allPostsText = stringResource(id = R.string.all_posts_text)
            val internshipText = stringResource(id = R.string.internship_text)
            val eventText = stringResource(id = R.string.type_event_text)
            val formationText = stringResource(id = R.string.type_formation_text)

            FilterChip(
                onClick = { showSearchSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                label = {
                    Text(
                        text = when (selectedSearch) {
                            PostType.ALL -> allPostsText
                            PostType.INTERNSHIP -> internshipText
                            PostType.EVENT -> eventText
                            PostType.WORKSHOP -> formationText
                        }
                    )
                },
                selected = false,
                trailingIcon = {
                    androidx.compose.material.Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Filter",
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WhatsAppLightGreen,
                    selectedLabelColor = WhatsAppDarkGreen
                )
            )

            if (announcementModel.itemCount != 0) {
                val lazyListState = rememberLazyListState()
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                ) {

                    items(announcementModel.itemCount) { index ->
                        val item = announcementModel[index] ?: AnnouncementModel()
                        idAnnounceComment = item.idAnnounce
                        postsViewModel.getPostCommentsCompany(item.idAnnounce)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    GlobalEntries.isFromNotification = false
                                    GlobalEntries.idAnnounce = item.idAnnounce
                                    GlobalEntries.idCompany = item.idCompany
                                    navController.navigate(Screen.DetailPostScreen.route)
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                // Header Row - Name, Position and Status
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Name and Position
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            color = Color.Black,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            text = "${stringResource(id = R.string.posted_by_text)} ${item.companyName}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Black,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Text(
                                        text = item.date ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = item.description,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Row(
                                        modifier = Modifier.wrapContentWidth()
                                    ) {
                                        postsViewModel.isNotLikedCandidate(
                                            item.likes ?: mutableListOf()
                                        )

                                        if (isFirstTime) {
                                            isItemLiked =
                                                if (isAllPostLikedUser.isNotEmpty()) isAllPostLikedUser[index] else false
                                            itemNumberLike =
                                                if (numberLikesUser.isNotEmpty()) numberLikesUser[index] else 0
                                        }
                                        Row(
                                            modifier = Modifier
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    isFirstTime = false
                                                    if (isItemLiked) {
                                                        postsViewModel.disLikePost(item.idAnnounce)
                                                        if (itemNumberLike > 0) itemNumberLike -= 1
                                                        isItemLiked = false
                                                    } else {
                                                        postsViewModel.likePost(item.idAnnounce)
                                                        itemNumberLike += 1
                                                        isItemLiked = true
                                                    }
                                                }
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (isItemLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                contentDescription = "Like",
                                                tint = if (isItemLiked) Color.Red else Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "$itemNumberLike",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }

                                        if (isFirstTime) {
                                            itemNumberComment = if (numberCommentUser.isNotEmpty() && index < numberCommentUser.size) numberCommentUser[index] else 0
                                        }

                                        // Comment Button
                                        Row(
                                            modifier = Modifier
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
                                                    //postsViewModel.getPostCommentsCompany(item.idAnnounce)
                                                    selectedPost = item.comments ?: mutableListOf()
                                                    showComments = !showComments
                                                    /*if (itemNumberComment > 0)
                                                        showComments = !showComments*/
                                                }
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ChatBubbleOutline,
                                                contentDescription = "Comment",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "$itemNumberComment",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }

                                        // Share Button
                                        Row(
                                            modifier = Modifier
                                                .clickable { /* Share action */ }
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = "Share",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp
                                )

                                var commentText by remember { mutableStateOf("") }

                                // Add Comment Input
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
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

                                                postsViewModel.addComment(item.idAnnounce, commentText, GlobalEntries.user.fullName ?: "")

                                                itemNumberComment += 1
                                                isFirstTime = false

                                                val c = CommentsPost(
                                                    idCandidate = userConnectedId,
                                                    text = commentText,
                                                    userName = GlobalEntries.user.fullName ?: ""
                                                )
                                                selectedPost = (selectedPost + c).toMutableList()

                                                commentText = ""

                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Send,
                                            contentDescription = "Send comment",
                                            tint = colorResource(id = R.color.whatsapp)
                                        )
                                    }
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    announcementModel.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = announcementModel.loadState.refresh as LoadState.Error
                                item {
                                    ErrorMessage(
                                        modifier = Modifier.fillParentMaxSize(),
                                        message = error.error.localizedMessage ?: "",
                                        onClickRetry = { retry() })
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item { LoadingNextPageItem(modifier = Modifier) }
                            }

                            loadState.append is LoadState.Error -> {
                                val error = announcementModel.loadState.append as LoadState.Error
                                item {
                                    ErrorMessage(
                                        modifier = Modifier,
                                        message = error.error.localizedMessage!!,
                                        onClickRetry = { retry() })
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Il n'y a pas de publications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = colorResource(id = R.color.whatsapp)
                    )
                }
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
                            HorizontalDivider(
                                thickness = 5.dp,
                                modifier = Modifier.width(100.dp)
                            )
                        }

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
                                    .size(50.dp)
                                    .padding(end = 20.dp)
                                    .align(Alignment.CenterEnd),
                                onClick = {
                                    showComments = false
                                }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
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
                            .align(Alignment.BottomCenter)
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text(stringResource(id = R.string.add_comment_hint_text)) },
                            shape = RoundedCornerShape(24.dp),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (commentText.isNotBlank()) {

                                    val username =
                                        if (GlobalEntries.user.role == "Candidate" || GlobalEntries.user.role == "Candidat")
                                            GlobalEntries.user.fullName else GlobalEntries.user.companyName

                                    postsViewModel.addComment(idAnnounceComment, commentText, username ?: "")

                                    val c = CommentsPost(
                                        idCandidate = userConnectedId,
                                        text = commentText,
                                        userName = username
                                    )
                                    selectedPost = (selectedPost + c).toMutableList()
                                    commentText = ""
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send comment",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        if (showSearchSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSearchSheet = false }
            ) {
                FilterPostBottomSheet(
                    selectedFilter = selectedSearch,
                    onFilterSelected = { filter ->
                        selectedSearch = filter
                        postsViewModel.getFilteredAnnounceCandidate(filter.name)
                        showSearchSheet = false
                    }
                )
            }
        }
    }
}