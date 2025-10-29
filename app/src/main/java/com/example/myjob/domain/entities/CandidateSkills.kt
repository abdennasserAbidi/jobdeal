package com.example.myjob.domain.entities

import com.example.myjob.feature.profile.test.LanguageForm
import kotlinx.serialization.Serializable

@Serializable
data class CandidateSkills(
    var id: Int = 0,
    var listSkills: MutableList<String> = mutableListOf(),
    var listCertification: MutableList<String> = mutableListOf(),
    var listLanguages: MutableList<LanguageForm> = mutableListOf()
)