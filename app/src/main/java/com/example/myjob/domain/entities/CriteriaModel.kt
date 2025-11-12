package com.example.myjob.domain.entities

import android.view.View

data class CriteriaModel(
    var id: Int = View.generateViewId(),
    var status: MutableList<String> = mutableListOf(),
    var disponibility: MutableList<String> = mutableListOf(),
    var situation: MutableList<String> = mutableListOf(),
    var sex: MutableList<String> = mutableListOf(),
    var experiences: MutableList<String> = mutableListOf(),
    var categories: MutableList<String> = mutableListOf(),
    var location: MutableList<String> = mutableListOf(),
    var typeContract: MutableList<String> = mutableListOf(),
    var institutions: MutableList<String> = mutableListOf(),
    var preferredActivitySector: MutableList<String> = mutableListOf(),
    var companies: MutableList<String> = mutableListOf()
) {
    fun checkEmpty(): Boolean = disponibility.isNotEmpty() && situation.isNotEmpty()
            && sex.isNotEmpty() && experiences.isNotEmpty()
            && location.isNotEmpty() && typeContract.isNotEmpty()
            && institutions.isNotEmpty() && preferredActivitySector.isNotEmpty()
            && companies.isNotEmpty()
}