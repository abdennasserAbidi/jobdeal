package com.example.myjob.domain.entities

import android.view.View

data class SearchHistory(
    val id: Int = View.generateViewId(),
    val idUser: Int? = -1,
    val gender: String? = "",
    val fullName: String? = "",
    val experience: String? = ""
)