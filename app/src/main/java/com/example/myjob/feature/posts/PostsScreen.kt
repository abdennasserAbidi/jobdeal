package com.example.myjob.feature.posts


// PostsScreen.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Post(
    val id: String,
    val author: Author,
    val content: String,
    val timestamp: String,
    val likes: Int = 0,
    val comments: List<Comment> = emptyList(),
    val isLiked: Boolean = false
)

data class Author(
    val name: String,
    val title: String,
    val avatarUrl: String? = null
)

data class Comment(
    val id: String,
    val author: String,
    val content: String,
    val timestamp: String
)

private val _postsTest = MutableStateFlow<List<Post>>(emptyList())
val postsTest: StateFlow<List<Post>> = _postsTest.asStateFlow()

fun toggleLike(postId: String) {
    _postsTest.value = _postsTest.value.map { post ->
        if (post.id == postId) {
            post.copy(
                isLiked = !post.isLiked,
                likes = if (post.isLiked) post.likes - 1 else post.likes + 1
            )
        } else post
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PostsScreen() {
    val posts by postsTest.collectAsState()
    var showCreatePost by remember { mutableStateOf(false) }

    _postsTest.value = listOf(
        Post(
            id = "1",
            author = Author(
                name = "Sarah Johnson",
                title = "Senior Software Engineer at TechCorp"
            ),
            content = "Excited to share that I'm hiring for a Full Stack Developer position on my team! We're looking for someone passionate about React and Node.js. DM me if interested! 🚀",
            timestamp = "2 hours ago",
            likes = 24,
            comments = listOf(
                Comment("1", "Mike Chen", "Congrats! What's the tech stack?", "1 hour ago"),
                Comment("2", "Sarah Johnson", "React, Node.js, PostgreSQL, AWS", "45 min ago")
            )
        ),
        Post(
            id = "2",
            author = Author(
                name = "David Martinez",
                title = "Product Manager at StartupXYZ"
            ),
            content = "Just wrapped up an amazing interview process. Remember: being nervous is normal, but preparation builds confidence. Here are my top 3 tips:\n\n1. Research the company thoroughly\n2. Prepare questions for the interviewer\n3. Practice your answers out loud\n\nGood luck to everyone job hunting! 💪",
            timestamp = "5 hours ago",
            likes = 156
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Deal Feed") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreatePost = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Post")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(posts) { post ->
                PostItem(
                    post = post,
                    onLikeClick = { toggleLike(post.id) },
                    onCommentSubmit = { comment ->
                        addComment(post.id, comment, "Current User")
                    }
                )
            }
        }

        if (showCreatePost) {
            CreatePostDialog(
                onDismiss = { showCreatePost = false },
                onPostCreated = { content ->
                    createPost(content, "Current User", "Job Seeker")
                    showCreatePost = false
                }
            )
        }
    }
}

fun addComment(postId: String, content: String, authorName: String) {
    _postsTest.value = _postsTest.value.map { post ->
        if (post.id == postId) {
            val newComment = Comment(
                id = System.currentTimeMillis().toString(),
                author = authorName,
                content = content,
                timestamp = "Just now"
            )
            post.copy(comments = post.comments + newComment)
        } else post
    }
}

fun createPost(content: String, authorName: String, authorTitle: String) {
    val newPost = Post(
        id = System.currentTimeMillis().toString(),
        author = Author(name = authorName, title = authorTitle),
        content = content,
        timestamp = "Just now"
    )
    _postsTest.value = listOf(newPost) + _postsTest.value
}

@Composable
fun PostItem(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentSubmit: (String) -> Unit
) {
    var showComments by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Author Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.author.name.first().toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.author.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = post.author.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = post.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                IconButton(onClick = { /* More options */ }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.Gray
                    )
                }
            }

            // Post Content
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium
            )

            // Action Buttons
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Like Button
                Row(
                    modifier = Modifier
                        .clickable { onLikeClick() }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.likes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                // Comment Button
                Row(
                    modifier = Modifier
                        .clickable { showComments = !showComments }
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
                        text = "${post.comments.size}",
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

            // Comments Section
            if (showComments) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Existing Comments
                post.comments.forEach { comment ->
                    CommentItem(comment)
                }

                // Add Comment Input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add a comment...") },
                        shape = RoundedCornerShape(24.dp),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                onCommentSubmit(commentText)
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
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = comment.author.first().toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = comment.author,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = comment.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit,
    onPostCreated: (String) -> Unit
) {
    var postContent by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Post") },
        text = {
            OutlinedTextField(
                value = postContent,
                onValueChange = { postContent = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = { Text("What do you want to talk about?") },
                maxLines = 6
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (postContent.isNotBlank()) {
                        onPostCreated(postContent)
                    }
                }
            ) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}