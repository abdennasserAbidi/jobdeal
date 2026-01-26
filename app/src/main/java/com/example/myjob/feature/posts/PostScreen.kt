package com.example.myjob.feature.posts

import android.view.View
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.feature.profile.test.FormTextField
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavController,
    postsViewModel: PostsViewModel = hiltViewModel()
) {

    val posts by postsViewModel.posts.collectAsState()
    var allPost by remember { mutableStateOf(emptyList<AnnouncementModel>()) }
    var selectedItemPost by remember { mutableStateOf(AnnouncementModel()) }
    var openOptions by remember { mutableStateOf(false) }

    val announcementModel by postsViewModel.announcementModel.collectAsState()
    val userConnectedId by postsViewModel.userConnectedId.collectAsState()
    val annoucementStatus by postsViewModel.annoucementStatus.collectAsState()
    val announcementUpdateStatus by postsViewModel.announcementUpdateStatus.collectAsState()

    var openAnnounceForm by remember { mutableStateOf(false) }
    var showAddButton by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }
    var activatedCheck by remember { mutableStateOf(false) }
    var showComments by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf(mutableListOf<CommentsPost>()) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSearchSheet by remember { mutableStateOf(false) }
    var targetIndex by remember { mutableStateOf(-1) }
    var selectedFilter by remember { mutableStateOf(PostType.ALL) }
    var selectedSearch by remember { mutableStateOf(PostType.ALL) }

    var postName by remember { mutableStateOf(announcementModel.title) }
    var descriptions by remember { mutableStateOf(announcementModel.description) }
    var postType by remember { mutableStateOf(announcementModel.postType) }

    var isUpdating by remember { mutableStateOf(false) }
    var isFirstTime by remember { mutableStateOf(false) }

    LaunchedEffect(posts) {
        allPost = posts
    }

    val isPostLiked by postsViewModel.isPostLiked.collectAsState()
    var isPostLikedUser by remember { mutableStateOf(false) }

    LaunchedEffect(isPostLiked) {
        isPostLikedUser = isPostLiked
    }


    val isAllPostLiked by postsViewModel.isAllPostLikedCompany.collectAsState()
    var isAllPostLikedUser by remember(isAllPostLiked) { mutableStateOf(isAllPostLiked) }
    var isItemLiked by remember { mutableStateOf(false) }

    val numberLikes by postsViewModel.numberLikesCompany.collectAsState()
    var numberLikesUser by remember(numberLikes) { mutableStateOf(numberLikes) }
    var itemNumberLike by remember { mutableStateOf(0) }

    val numberCommentCompany by postsViewModel.numberCommentCompany.collectAsState()
    var numberCommentUser by remember(numberCommentCompany) { mutableStateOf(numberCommentCompany) }
    var itemNumberComment by remember { mutableStateOf(0) }

    val commentsCompany by postsViewModel.commentsCompany.collectAsState()
    val deleteStatus by postsViewModel.deleteStatus.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            isFirstTime = true
            postsViewModel.isAllPostLikedCompany()
            postsViewModel.getPostNumberLikesCompany()
            postsViewModel.getPostNumberCommentCompany()
        }
    }

    LaunchedEffect(annoucementStatus) {
        if (annoucementStatus == "saved successfully") {
            announcementModel.companyName = GlobalEntries.user.companyName ?: ""
            allPost = (allPost + announcementModel).toMutableList()
        }
    }

    LaunchedEffect(announcementUpdateStatus) {
        if (announcementUpdateStatus == "saved successfully") {
            announcementModel.companyName = GlobalEntries.user.companyName ?: ""

            allPost = allPost
                .mapIndexed { index, value ->
                    if (index == targetIndex) announcementModel else value
                }
                .toMutableList()

        }
    }

    if (showDialog) {
        CustomDialog(isSuccess = false, message = deleteStatus) {
            showDialog = false
        }
    }

    LaunchedEffect(deleteStatus) {
        if (deleteStatus == "deleted successfully") {
            allPost = (allPost - selectedItemPost).toMutableList()
        } else if (deleteStatus.isNotEmpty()) {
            showDialog = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                showComments = !showComments
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
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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

                    IconButton(
                        modifier = Modifier.size(40.dp),
                        onClick = {
                            showAddButton = false
                            openAnnounceForm = true
                            GlobalEntries.isVisibleNav.update {
                                false
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                }
            }

            // Filter Row
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

            if (allPost.isNotEmpty()) {

                val lazyListState = rememberLazyListState()
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                ) {
                    itemsIndexed(
                        items = allPost,
                        key = { _, item ->
                            View.generateViewId()
                        }
                    ) { index, item ->
                        postsViewModel.getPostCommentsCompany(item.idAnnounce)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
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
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Name and Position
                                    Column(
                                        modifier = Modifier.align(Alignment.CenterStart)
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

                                    IconButton(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.CenterEnd),
                                        onClick = {
                                            selectedItemPost = item
                                            targetIndex = index
                                            showComments = false
                                            openAnnounceForm = false
                                            openOptions = true
                                        }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            tint = Color.Red,
                                            modifier = Modifier.size(20.dp),
                                            contentDescription = "delete"
                                        )
                                    }

                                    /*IconButton(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.CenterEnd),
                                        onClick = {
                                            selectedItemPost = item
                                            postsViewModel.deleteCompanyAnnouncement(item.idAnnounce)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            tint = Color.Red,
                                            modifier = Modifier.size(20.dp),
                                            contentDescription = "delete"
                                        )
                                    }*/
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
                                ) {

                                    Text(
                                        text = item.date ?: "",
                                        modifier = Modifier.align(Alignment.CenterStart),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Row(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .align(Alignment.CenterEnd)
                                    ) {
                                        postsViewModel.isNotLikedCandidate(
                                            item.likes ?: mutableListOf()
                                        )

                                        if (isFirstTime) {
                                            isItemLiked =
                                                if (isAllPostLikedUser.isNotEmpty() && index < isAllPostLikedUser.size) isAllPostLikedUser[index] else false
                                            itemNumberLike =
                                                if (numberLikesUser.isNotEmpty() && index < numberLikesUser.size) numberLikesUser[index] else 0
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
                                            itemNumberComment =
                                                if (numberCommentUser.isNotEmpty() && index < numberCommentUser.size) numberCommentUser[index] else 0
                                        }

                                        // Comment Button
                                        Row(
                                            modifier = Modifier
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) {
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

                                                postsViewModel.addComment(
                                                    item.idAnnounce,
                                                    commentText,
                                                    GlobalEntries.user.companyName ?: ""
                                                )

                                                isFirstTime = false
                                                itemNumberComment += 1

                                                selectedPost.add(
                                                    CommentsPost(
                                                        idCandidate = userConnectedId,
                                                        userName = GlobalEntries.user.companyName
                                                            ?: ""
                                                    )
                                                )
                                                commentText = ""
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = "Send comment",
                                            tint = colorResource(id = R.color.whatsapp)
                                        )
                                    }
                                }

                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "nothing to show",
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

                            Text(
                                text = stringResource(id = R.string.comments_text),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {

                        itemsIndexed(
                            items = commentsCompany
                        ) { index, comment ->
                            CommentItem(comment)
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = openAnnounceForm,
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

            androidx.compose.material.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .padding(top = 20.dp)
                    ) {

                        androidx.compose.material.Icon(
                            imageVector = Icons.Filled.Close,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    GlobalEntries.isVisibleNav.update {
                                        true
                                    }
                                    openAnnounceForm = false
                                    showAddButton = true
                                },
                            contentDescription = ""
                        )

                        Text(
                            text = stringResource(id = R.string.posts_text),
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Filter Row
                        //val allPostsText = stringResource(id = R.string.all_posts_text)
                        val allPostsText = stringResource(id = R.string.select_posts_text)
                        val internshipText = stringResource(id = R.string.internship_text)
                        val eventText = stringResource(id = R.string.type_event_text)
                        val formationText = stringResource(id = R.string.type_formation_text)

                        FilterChip(
                            onClick = { showFilterSheet = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(top = 20.dp)
                                .padding(horizontal = 20.dp),
                            label = {
                                Text(
                                    text = when (selectedFilter) {
                                        PostType.ALL -> stringResource(id = R.string.all_posts_text)
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

                        val validator =
                            postType.isEmpty() || postType == PostType.ALL.name || postType == allPostsText
                        if (activatedCheck && validator) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = stringResource(id = R.string.type_annonce_error),
                                color = Color.Red
                            )

                        }

                        FormTextField(
                            value = postName,
                            borderColor = if (activatedCheck && postName.isEmpty()) Color.Red else colorResource(
                                id = R.color.whatsapp
                            ),
                            onValueChange = {
                                postName = it
                                if (activatedCheck) postName.isNotEmpty()
                                postsViewModel.changePostName(it)
                            },
                            label = stringResource(id = R.string.post_title_text),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 40.dp),
                            isRequired = true
                        )

                        FormTextField(
                            value = descriptions,
                            borderColor = if (activatedCheck && descriptions.isEmpty()) Color.Red else colorResource(
                                id = R.color.whatsapp
                            ),
                            onValueChange = {
                                descriptions = it
                                if (activatedCheck) descriptions.isNotEmpty()
                                postsViewModel.changeDescriptions(it)
                            },
                            label = stringResource(id = R.string.post_description_text),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 40.dp),
                            isRequired = true
                        )

                        Button(
                            onClick = {
                                val postTypeValidator =
                                    postType.isNotEmpty() && postType != PostType.ALL.name && postType != allPostsText
                                val postTitleValidator = postName.isNotEmpty()
                                val postDescriptionValidator = descriptions.isNotEmpty()
                                if (!postTitleValidator || !postDescriptionValidator || !postTypeValidator) {
                                    activatedCheck = true
                                }

                                if (postTitleValidator && postDescriptionValidator && postTypeValidator) {
                                    if (!isUpdating) postsViewModel.saveCompanyAnnouncement()
                                    else postsViewModel.updateCompanyAnnouncement()
                                    openAnnounceForm = false
                                    showAddButton = true
                                    GlobalEntries.isVisibleNav.update { true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 40.dp),
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

    // Filter Bottom Sheet
    if (openOptions) {
        ModalBottomSheet(
            onDismissRequest = { openOptions = false }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    verticalAlignment = CenterVertically,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        postsViewModel.deleteCompanyAnnouncement(selectedItemPost.idAnnounce)
                        openOptions = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp),
                        contentDescription = "delete"
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(stringResource(R.string.delete_text))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = CenterVertically,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        isUpdating = true

                        postsViewModel.changePostId(selectedItemPost.idAnnounce)
                        postsViewModel.changePostName(selectedItemPost.title)
                        postsViewModel.changeDescriptions(selectedItemPost.description)
                        postsViewModel.changePostType(selectedItemPost.postType)

                        descriptions = selectedItemPost.description
                        postName = selectedItemPost.title
                        postType = selectedItemPost.postType

                        openAnnounceForm = true
                        openOptions = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = colorResource(R.color.whatsapp),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "edit"
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(stringResource(R.string.update_text))
                }

            }
        }
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false }
        ) {
            FilterPostBottomSheet(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    selectedFilter = filter
                    postType = filter.name
                    postsViewModel.changePostType(postType)
                    showFilterSheet = false
                }
            )
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
                    postsViewModel.getFilteredAnnounceCompany(filter.name)
                    showSearchSheet = false
                }
            )
        }
    }
}

@Composable
fun FilterPostBottomSheet(
    selectedFilter: PostType,
    onFilterSelected: (PostType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.type_change_text),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val allPostsText = stringResource(id = R.string.all_posts_text)
        val internshipText = stringResource(id = R.string.internship_text)
        val eventText = stringResource(id = R.string.type_event_text)
        val formationText = stringResource(id = R.string.type_formation_text)
        PostType.values().forEach { filter ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onFilterSelected(filter)
                    }
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedFilter == filter,
                    onClick = {
                        onFilterSelected(filter)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.whatsapp),
                        unselectedColor = colorResource(id = R.color.whatsapp)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (filter) {
                        PostType.ALL -> allPostsText
                        PostType.INTERNSHIP -> internshipText
                        PostType.EVENT -> eventText
                        PostType.WORKSHOP -> formationText
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CommentItem(comment: CommentsPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = comment.userName?.first().toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = comment.userName ?: "",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = comment.text ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
            /*Text(
                text = comment.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )*/
        }
    }
}