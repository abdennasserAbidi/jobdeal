package com.example.myjob.base

data class GenericResponse<out T> constructor(
    val content: List<T> = emptyList(),
    val totalPages: Int = 0,
    val number: Int = 1,
    val size: Int = 10,
    val numberOfElements: Int = 10,
    val first: Boolean = false,
    val empty: Boolean = false,
)