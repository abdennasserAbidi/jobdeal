package com.example.myjob.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class ProfessionalStatus(
    var id: Int = 0,
    var preferredSalary: String? = "",
    var availability: String? = "",
    var userExperience: String? = "",
    var workType: String? = "",
    var userGithub: String? = "",
    var userMedium: String? = "",
    var userPortfolio: String? = "",
    var language: String? = "en",
    var onSitePreference: Boolean? = false,
    var hybridPreference: Boolean? = false,
    var remotePreference: Boolean? = false
)