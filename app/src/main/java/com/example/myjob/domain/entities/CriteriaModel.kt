package com.example.myjob.domain.entities

import android.view.View

data class CriteriaModel(
    var id: Int = View.generateViewId(),
    var disponibility: MutableList<String> = mutableListOf(),
    var situation: MutableList<String> = mutableListOf(),
    var sex: MutableList<String> = mutableListOf(),
    var experiences: MutableList<String> = mutableListOf(),
    var location: MutableList<String> = mutableListOf(),
    var typeContract: MutableList<String> = mutableListOf(),
    var institutions: MutableList<String> = mutableListOf(),
    var preferredActivitySector: MutableList<String> = mutableListOf(),
    var companies: MutableList<String> = mutableListOf()
)