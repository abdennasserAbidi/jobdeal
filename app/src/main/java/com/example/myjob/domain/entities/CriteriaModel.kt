package com.example.myjob.domain.entities

import android.view.View

data class CriteriaModel(
    var id: Int = View.generateViewId(),
    var experiences: String = "",
    var location: String = "",
    var typeContract: MutableList<String> = mutableListOf(),
    var institutions: MutableList<String> = mutableListOf(),
    var preferredActivitySector: MutableList<String> = mutableListOf(),
    var companies: MutableList<String> = mutableListOf()
)