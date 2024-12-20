package com.example.myjob.domain.entities

import com.example.myjob.R

data class Currency(
    var name: String? = "",
    var flag: Int? = R.drawable.ic_launcher_foreground,
    var rate: Double? = null,
    var isBase: Boolean? = false
)
