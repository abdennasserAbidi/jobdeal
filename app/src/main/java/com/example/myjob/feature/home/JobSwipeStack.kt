package com.example.myjob.feature.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
    lang: String,
    openProfile: (profile: User) -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .border(
                1.dp,
                Color.Black,
                RoundedCornerShape(20.dp)
            )
            .background(color = Color.Transparent, shape = RoundedCornerShape(20.dp))
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                val fullName = (profile.fullName ?: "").trimEnd().trimStart()
                var l = ""
                if (fullName.isNotEmpty() && fullName != " ") {
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
                            .background(colorResource(id = R.color.whatsapp), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = l,
                            color = Color.White,
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

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = profile.resumeUser(),
                    style = MaterialTheme.typography.body2
                )
            }

            Text(
                text = "View profile",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        openProfile(profile)
                    },
                color = colorResource(id = R.color.whatsapp)
            )
        }

    }
}