package com.example.myjob.domain.entities

import com.example.myjob.R

data class HomeEntity(
    val title: Int,
    val subTitle: Int,
    val icon: Int? = null,
    val withNotification: Boolean = true
)

val HOME_ENTITY = listOf(
    HomeEntity(
        R.string.invitations_text,
        R.string.navigate_invitations_text,
        icon = R.drawable.invitationproject
    ),

    HomeEntity(
        R.string.validate_text,
        R.string.validate_profile_text,
        icon = R.drawable.ic_settings_privacy
    ),

    HomeEntity(
        R.string.setting_text,
        R.string.navigate_settings_text,
        icon = R.drawable.settings,
        withNotification = false
    ),

    HomeEntity(
        R.string.profile_text,
        R.string.navigate_profile_text,
        icon = R.drawable.user,
        withNotification = false
    ),

    HomeEntity(
        R.string.posts_text,
        R.string.posts_desc_text,
        icon = R.drawable.ic_settings_privacy
    ),

    HomeEntity(
        R.string.discussion_text,
        R.string.discussion_desc_text,
        icon = R.drawable.ic_settings_privacy
    ),
)