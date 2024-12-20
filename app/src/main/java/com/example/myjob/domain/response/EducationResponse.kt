package com.example.myjob.domain.response

import com.example.myjob.domain.entities.Educations

data class EducationResponse(
    val content: List<Educations> = emptyList(),
    val totalPages: Int = 0,
    val number: Int = 1,
    val size: Int = 10,
    val numberOfElements: Int = 10,
    val first: Boolean = false,
    val empty: Boolean = false,
)