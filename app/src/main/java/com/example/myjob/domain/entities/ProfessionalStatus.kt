package com.example.myjob.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class ProfessionalStatus(
    var id: Int = 0,
    var preferredSalary: String? = "Unspecified",
    var availability: String? = "Unspecified",
    var userExperience: String? = "Unspecified",
    var userGithub: String? = "Unspecified",
    var userMedium: String? = "Unspecified",
    var userPortfolio: String? = "Unspecified",
    var onSitePreference: Boolean? = false,
    var hybridPreference: Boolean? = false,
    var remotePreference: Boolean? = false
)