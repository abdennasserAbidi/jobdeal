package com.example.myjob.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myjob.domain.entities.User

@Composable
fun ProfileCard(
    modifier: Modifier,
    matchProfile: User,
) {
    JobSwipeCard(
        profile = matchProfile,
        modifier = modifier
    )
}