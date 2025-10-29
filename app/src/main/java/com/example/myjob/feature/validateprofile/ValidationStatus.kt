package com.example.myjob.feature.validateprofile

data class ValidationProfileStatus(
    var id: Int = -1,
    var email: String = "",
    var typeValidation: String = "",
    var linkedin: String = "",
    var status: String = VerificationStatus.NOT_STARTED.name,
    var registrationNumber: String = "",
    var docs: List<String> = emptyList()
)