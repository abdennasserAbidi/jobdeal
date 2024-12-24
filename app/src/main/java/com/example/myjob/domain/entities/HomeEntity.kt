package com.example.myjob.domain.entities

import com.example.myjob.R

data class HomeEntity(
    val title: String,
    val subTitle: String,
    val img: Int,
)

val HOME_ENTITY = listOf(
    HomeEntity("Mes invitations", "Voir vos invitations", R.drawable.cvtable),
    HomeEntity("Freelances", "Voir vos invitations freelances", R.drawable.cvtable),
    HomeEntity("Formations", "Voir vos formations", R.drawable.cvtable),
    HomeEntity("Evenements", "Voir vos évennements", R.drawable.cvtable),
    HomeEntity("Stages", "Voir vos invitations stages", R.drawable.cvtable)
)