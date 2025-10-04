package com.example.myjob.domain.entities

import android.view.View

enum class ContractType {
    CONTRACT, FREELANCE
}

enum class ContractTypeRole {
    TRAINER, INTERN, EVENT
}

data class Candidate(
    val id: Int = View.generateViewId(),
    val name: String = "",
    val position: String = "",
    val company: String = "",
    val experience: String = "",
    val location: String = "",
    val skills: List<String> = emptyList(),
    val salary: String = "",
    val status: CandidateStatus = CandidateStatus.AVAILABLE
)

// Sample data
val candidates = listOf(
    Candidate(
        name = "Sarah Johnson",
        position = "Senior Android Developer",
        company = "Google",
        experience = "5+ years",
        location = "San Francisco, CA",
        skills = listOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines"),
        salary = "$120k - $150k",
        status = CandidateStatus.AVAILABLE
    ),
    Candidate(
        name = "Michael Chen",
        position = "Mobile App Developer",
        company = "Meta",
        experience = "3+ years",
        location = "Seattle, WA",
        skills = listOf("Flutter", "React Native", "Firebase", "GraphQL"),
        salary = "$95k - $120k",
        status = CandidateStatus.INTERVIEWING
    ),
    Candidate(
        name = "Emily Rodriguez",
        position = "iOS Developer",
        company = "Apple",
        experience = "4+ years",
        location = "Austin, TX",
        skills = listOf("Swift", "SwiftUI", "Core Data", "Combine"),
        salary = "$110k - $140k",
        status = CandidateStatus.AVAILABLE
    ),
    Candidate(
        name = "David Kim",
        position = "Full Stack Developer",
        company = "Netflix",
        experience = "6+ years",
        location = "Los Angeles, CA",
        skills = listOf("React", "Node.js", "Python", "AWS"),
        salary = "$130k - $160k",
        status = CandidateStatus.HIRED
    ),
    Candidate(
        name = "Jessica Wang",
        position = "UI/UX Designer",
        company = "Airbnb",
        experience = "4+ years",
        location = "New York, NY",
        skills = listOf("Figma", "Sketch", "Prototyping", "User Research"),
        salary = "$85k - $110k",
        status = CandidateStatus.NOT_INTERESTED
    )
)

enum class CandidateStatus {
    AVAILABLE, INTERVIEWING, HIRED, NOT_INTERESTED
}

enum class FilterType {
    ALL, ON_HOLD, INTERVIEWING, HIRED, NOT_INTERESTED, REJECTED
}
