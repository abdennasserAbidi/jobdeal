package com.example.myjob.domain.entities

import android.view.View

data class Rate(
    var id: Int = View.generateViewId(),
    var idCandidate: Int = -1,
    var idUserDemand: Int = -1,
    var userDemandName: String = "",
    var candidateName: String = "",
    var like: String = "",
    var note: String = "",
    var percentRate: Float = 0f
)