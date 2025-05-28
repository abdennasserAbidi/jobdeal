package com.example.myjob.domain.entities

import com.example.myjob.R

data class HomeEntity(
    val title: String,
    val subTitle: String,
    val icon: Int?= null,
    val withNotification: Boolean = true
)

val HOME_ENTITY = listOf(
    HomeEntity("Mes invitations", "Voir vos invitations", icon = R.drawable.invitationproject),
    HomeEntity("Carrière", "Booseter mon carrière", icon = R.drawable.invitationproject),
    HomeEntity("Settings", "SELECT YOUR SETTINGS", icon = R.drawable.settings, withNotification = false),
    HomeEntity("Profile", "EDIT YOUR PROFILE", icon = R.drawable.user, withNotification = false),
)