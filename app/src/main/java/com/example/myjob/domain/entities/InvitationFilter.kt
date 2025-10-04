package com.example.myjob.domain.entities

import android.view.View

data class InvitationFilter(
    var id: Int = View.generateViewId(),
    var idUser: Int ?= -1,
    var type: String? = "All Candidates",
    var listTypeContract: List<String>? = listOf()
)