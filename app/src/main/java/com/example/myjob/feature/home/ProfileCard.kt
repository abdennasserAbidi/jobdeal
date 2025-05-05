package com.example.myjob.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myjob.domain.entities.User

@Composable
fun ProfileCard(
    modifier: Modifier,
    matchProfile: User,
    lang: String,
    onSave: () -> Unit,
    onSkip: () -> Unit,
    onMatch: () -> Unit,
    openProfile: (profile: User) -> Unit
) {
    JobSwipeCard(
        profile = matchProfile,
        openProfile = {
            openProfile(it)
        },
        lang = lang,
        modifier = modifier,
        onSave = { onSave() },
        onSkip = { onSkip() },
        onMatch = { onMatch() },
    )
}