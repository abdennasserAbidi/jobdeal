package com.example.myjob.domain.entities

import android.view.View

data class Degrees(
    var id: Int = View.generateViewId(),
    var type: String = "Please select",
    var isSelected: Boolean = false
)

val DEFAULT_DEGREE = listOf(
    Degrees(type = "Pas de bac", isSelected = false),
    Degrees(type = "Bac", isSelected = false),
    Degrees(type = "Bac + 1", isSelected = false),
    Degrees(type = "Bac + 2", isSelected = false),
    Degrees(type = "Bac + 3", isSelected = false),
    Degrees(type = "Bac + 5", isSelected = false),
    Degrees(type = "Doctorant", isSelected = false),
    Degrees(type = "Master", isSelected = false)
)