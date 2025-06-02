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
        R.string.career_text,
        R.string.navigate_career_text,
        icon = R.drawable.invitationproject
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
    )
)