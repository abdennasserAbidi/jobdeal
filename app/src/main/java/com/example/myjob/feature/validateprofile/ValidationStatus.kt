package com.example.myjob.feature.validateprofile

import android.view.View
import kotlinx.serialization.Serializable

@Serializable
data class ValidationProfileStatus(
    var id: Int = -1,
    var email: String = "",
    var typeValidation: String = "",
    var linkedin: String = "",
    var status: String = VerificationStatus.NOT_STARTED.name,
    var registrationNumber: String = "",
    var docs: List<String> = emptyList(),
    var documents: List<Documents> = emptyList()
)

@Serializable
data class Documents(
    var id: Int = View.generateViewId(),
    var url: String = "",
    var name: String = "",
    var type: String = ""
)