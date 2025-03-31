package com.example.myjob.domain.entities

import android.view.View
import com.example.myjob.R

data class Availabilities(
    val id: Int? = View.generateViewId(),
    var title: Int = R.string.disponibility1_text,
    var isSelected: Boolean = false
)