package com.example.myjob.domain.entities

import android.view.View

data class FavoriteModel(
    var id: Int = View.generateViewId(),
    val name: String? = "",
    val email: String? = "",
    val phone: String? = ""
) {
}