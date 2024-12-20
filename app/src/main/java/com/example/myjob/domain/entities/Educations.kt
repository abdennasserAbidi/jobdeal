package com.example.myjob.domain.entities

import android.view.View

data class Educations(
    var id: Int = View.generateViewId(),
    val title: String? = "",
    val schoolName: String? = "",
    val degree: String? = "",
    val fieldStudy: String? = "",
    val dateStart: String? = "",
    val dateEnd: String? = "",
    val place: String? = "",
    val grade: String? = "",
    val description: String? = "",
    val stillStudying: Boolean? = true,
    var idUser: Int? = 0
)