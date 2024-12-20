package com.example.myjob.domain.entities

import android.view.View

data class EmploymentType(
    var id: Int = View.generateViewId(),
    var type: String = "Please select",
    var isSelected: Boolean = false
)

val DEFAULT_TYPE = listOf(
    EmploymentType(type = "Full-time", isSelected = false),
    EmploymentType(type = "Part-time", isSelected = false),
    EmploymentType(type = "Self-employed", isSelected = false),
    EmploymentType(type = "Freelance", isSelected = false),
    EmploymentType(type = "Contract", isSelected = false),
    EmploymentType(type = "Internship", isSelected = false),
    EmploymentType(type = "Apprenticeship", isSelected = false),
    EmploymentType(type = "Seasonal", isSelected = false),
)