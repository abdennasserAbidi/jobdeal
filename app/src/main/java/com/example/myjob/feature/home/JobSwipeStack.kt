package com.example.myjob.feature.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.domain.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun JobSwipeStack(
    profiles: List<User>,
    onSwipe: (User, String) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {
        profiles.forEachIndexed { index, profile ->
            if (index == 0) {
                SwipeableCard(
                    profile = profile,
                    onSwiped = onSwipe,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                JobSwipeCard(
                    profile = profile,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = 1f - (0.05f * index),
                            scaleY = 1f - (0.05f * index),
                            translationY = (20 * index).toFloat()
                        )
                )
            }
        }
    }
}

@Composable
fun SwipeableCard(
    profile: User, // The data model for the card
    onSwiped: (User, String) -> Unit, // Callback with swipe direction ("Left" or "Right")
    modifier: Modifier = Modifier
) {
    // Animatable values for X and Y offsets
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .graphicsLayer(
                translationX = offsetX.value, // Move the card horizontally
                translationY = offsetY.value, // Move the card vertically
                rotationZ = offsetX.value / 20f // Rotate slightly based on horizontal drag
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        // Determine swipe direction
                        val swipeThreshold = 300f
                        when {
                            offsetX.value > swipeThreshold -> { // Swiped Right
                                onSwiped(profile, "Right")
                            }

                            offsetX.value < -swipeThreshold -> { // Swiped Left
                                onSwiped(profile, "Left")
                            }

                            else -> { // Reset if not swiped past threshold
                                CoroutineScope(Dispatchers.Main).launch {
                                    offsetX.animateTo(0f, animationSpec = tween(300))
                                    offsetY.animateTo(0f, animationSpec = tween(300))
                                }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        // Update the offsets during drag
                        change.consume() // Consume the gesture to prevent propagation
                        CoroutineScope(Dispatchers.Main).launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                )
            }
    ) {
        // Card content goes here
        JobSwipeCard(profile = profile)
    }
}

@Composable
fun ActionButtons(
    onSave: () -> Unit,
    onSkip: () -> Unit,
    onMatch: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(onClick = onSkip) {
            Text("Skip")
        }
        Button(onClick = onSave) {
            Text("Save")
        }
        Button(onClick = onMatch) {
            Text("Connect")
        }
    }
}

@Composable
fun JobSwipeCard(
    profile: User,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = 8.dp,
        shape = RoundedCornerShape(20.dp)
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                val fullName = profile.fullName ?: ""
                var l = ""
                if (fullName.isNotEmpty()) {
                    val s = fullName.split(" ")
                    l = "${s[0][0].uppercaseChar()}${s[1][0].uppercaseChar()}"
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.whatsapp),
                                CircleShape
                            )
                            .background(Color.Transparent, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = l,
                            color = colorResource(id = R.color.whatsapp),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.rubikbold,
                                        weight = FontWeight.Bold
                                    )
                                )
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = profile.fullName ?: "",
                            style = MaterialTheme.typography.h6
                        )

                        Text(
                            text = profile.activitySector ?: "",
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Job or Candidate Details

                Text(profile.address ?: "", style = MaterialTheme.typography.subtitle1)
                Text(profile.activitySector ?: "", style = MaterialTheme.typography.body2)
                Spacer(modifier = Modifier.height(8.dp))
                Text(profile.email ?: "", style = MaterialTheme.typography.body2, maxLines = 3)
            }

            Text(
                text = "View profile",
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp),
                color = colorResource(id = R.color.whatsapp)
            )
        }


    }
}