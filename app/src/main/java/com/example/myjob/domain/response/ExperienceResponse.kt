package com.example.myjob.domain.response

import com.example.myjob.domain.entities.Experience

data class ExperienceResponse(
    val content: List<Experience> = emptyList(),
    val totalPages: Int = 0,
    val number: Int = 1,
    val size: Int = 10,
    val numberOfElements: Int = 10,
    val first: Boolean = false,
    val empty: Boolean = false,
)