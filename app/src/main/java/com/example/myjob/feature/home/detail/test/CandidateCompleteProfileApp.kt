package com.example.myjob.feature.home.detail.test

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.CandidateStatus
import com.example.myjob.feature.home.detail.Education
import com.example.myjob.feature.home.detail.Language
import com.example.myjob.feature.home.detail.Project
import com.example.myjob.feature.home.detail.WorkExperience

@Composable
fun CandidateCompleteProfileApp() {
    // Sample candidate data
    val sampleCandidate = Candidate(
        id = 1,
        name = "Sarah Johnson",
        position = "Senior Android Developer",
        company = "Google",
        experience = "5+ years",
        location = "San Francisco, CA",
        skills = listOf("Kotlin", "Jetpack Céompose", "MVVM", "Coroutines", "Room", "Retrofit", "Dagger Hilt", "Firebase", "Git", "CI/CD", "Unit Testing", "Material Design"),
        salary = "$120k - $150k",
        status = CandidateStatus.AVAILABLE
    )

    val sampleProfile = CompleteCandidateProfile(
        candidate = sampleCandidate,
        email = "sarah.johnson@email.com",
        phone = "+1 (555) 123-4567",
        linkedIn = "linkedin.com/in/sarahjohnson",
        github = "github.com/sarahjohnson",
        portfolio = "sarahjohnson.dev",
        bio = "Passionate Android developer with 5+ years of experience building scalable mobile applications. Expertise in modern Android development using Kotlin, Jetpack Compose, and clean architecture patterns. Led development teams and mentored junior developers. Strong advocate for clean code and test-driven development. Experienced in agile methodologies and cross-functional collaboration.",
        experience = listOf(
            WorkExperience(
                company = "Google",
                position = "Senior Android Developer",
                duration = "2022 - Present",
                description = "Lead Android development for Google Pay, implementing new features and improving app performance. Collaborated with cross-functional teams to deliver high-quality user experiences. Mentored junior developers and established coding standards.",
                technologies = listOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines", "Room", "Hilt")
            ),
            WorkExperience(
                company = "Meta",
                position = "Android Developer",
                duration = "2020 - 2022",
                description = "Developed and maintained Facebook mobile app features. Implemented new UI components and optimized app performance for millions of users. Worked on accessibility improvements and internationalization.",
                technologies = listOf("Java", "Kotlin", "RxJava", "Retrofit", "Dagger", "Espresso")
            ),
            WorkExperience(
                company = "Spotify",
                position = "Junior Android Developer",
                duration = "2019 - 2020",
                description = "Built music streaming features and worked on playlist management functionality. Gained experience in media playback and audio processing. Contributed to offline music capabilities.",
                technologies = listOf("Java", "ExoPlayer", "MediaSession", "SQLite", "Glide")
            )
        ),
        education = listOf(
            Education(
                institution = "Stanford University",
                degree = "Master of Science",
                field = "Computer Science",
                year = "2019",
                gpa = "3.8"
            ),
            Education(
                institution = "UC Berkeley",
                degree = "Bachelor of Science",
                field = "Computer Engineering",
                year = "2017",
                gpa = "3.6"
            )
        ),
        projects = listOf(
            Project(
                name = "TaskMaster Pro",
                description = "A comprehensive task management app with team collaboration features, real-time synchronization, and advanced analytics. Built using modern Android architecture components.",
                technologies = listOf("Kotlin", "Jetpack Compose", "Firebase", "Room", "WorkManager"),
                link = "github.com/sarahjohnson/taskmaster"
            ),
            Project(
                name = "Weather Forecast App",
                description = "Beautiful weather app with location-based forecasts, interactive maps integration, and weather alerts. Features offline support and widget functionality.",
                technologies = listOf("Kotlin", "MVVM", "Retrofit", "Google Maps", "Notifications"),
                link = "github.com/sarahjohnson/weather-app"
            ),
            Project(
                name = "Expense Tracker",
                description = "Personal finance app with budget tracking, expense categorization, financial insights, and data visualization. Includes biometric authentication and data export features.",
                technologies = listOf("Kotlin", "Room", "Charts", "Material Design", "Biometric"),
                link = null
            )
        ),
        certifications = listOf(
            "Google Associate Android Developer",
            "AWS Certified Developer - Associate",
            "Scrum Master Certified (PSM I)",
            "Kotlin Certified Developer"
        ),
        languages = listOf(
            Language("English", "Native"),
            Language("Spanish", "Conversational"),
            Language("French", "Basic"),
            Language("Mandarin", "Basic")
        ),
        availability = "Available immediately",
        expectedSalary = "$140k - $170k",
        noticePeriod = "2 weeks",
        workPreference = WorkPreference(
            remoteWork = true,
            hybridWork = true,
            onSiteWork = false,
            travelWillingness = "Occasional travel (10-20%)",
            preferredWorkingHours = "9 AM - 6 PM PST"
        ),
        achievements = listOf(
            Achievement(
                title = "Best Mobile App Award",
                description = "Won the company-wide innovation award for developing the most user-friendly mobile application of the year.",
                year = "2023",
                organization = "Google"
            ),
            Achievement(
                title = "Tech Lead of the Year",
                description = "Recognized for outstanding leadership in guiding the Android team through successful product launches.",
                year = "2022",
                organization = "Meta"
            ),
            Achievement(
                title = "Open Source Contributor",
                description = "Active contributor to Android open source projects with over 500 GitHub contributions.",
                year = "2021",
                organization = "GitHub"
            )
        ),
        references = listOf(
            Reference(
                name = "John Smith",
                position = "Engineering Manager",
                company = "Google",
                email = "john.smith@google.com",
                phone = "+1 (555) 987-6543",
                relationship = "Direct Manager"
            ),
            Reference(
                name = "Emily Chen",
                position = "Senior Product Manager",
                company = "Meta",
                email = "emily.chen@meta.com",
                phone = "+1 (555) 456-7890",
                relationship = "Cross-functional Partner"
            ),
            Reference(
                name = "Michael Rodriguez",
                position = "Tech Lead",
                company = "Spotify",
                email = "michael.rodriguez@spotify.com",
                phone = null,
                relationship = "Former Mentor"
            )
        ),
        socialProfiles = SocialProfiles(
            twitter = "@sarahjohnsondev",
            instagram = null,
            behance = null,
            dribbble = null,
            medium = "@sarahjohnson"
        ),
        jobPreferences = JobPreferences(
            preferredRoles = listOf("Senior Android Developer", "Lead Mobile Developer", "Android Architect", "Mobile Engineering Manager"),
            preferredIndustries = listOf("Technology", "Fintech", "Healthcare", "E-commerce", "Social Media"),
            companySizePreference = "Medium to Large (100-5000 employees)",
            culturePreferences = listOf("Innovation-focused", "Work-life balance", "Remote-friendly", "Diversity & Inclusion")
        ),
        personalInfo = PersonalInfo(
            dateOfBirth = null, // Privacy
            nationality = "American",
            visaStatus = "US Citizen",
            address = "San Francisco Bay Area, CA",
            emergencyContact = null // Privacy
        ),
        assessments = listOf(
            Assessment(
                name = "Android Development",
                score = "95/100",
                completedDate = "Dec 2023",
                validUntil = "Dec 2025"
            ),
            Assessment(
                name = "Kotlin Programming",
                score = "92/100",
                completedDate = "Nov 2023",
                validUntil = "Nov 2025"
            ),
            Assessment(
                name = "System Design",
                score = "88/100",
                completedDate = "Oct 2023",
                validUntil = "Oct 2025"
            )
        )
    )

    var showToast by remember { mutableStateOf("") }

    CandidateCompleteProfileScreen(
        candidateProfile = sampleProfile,
        onBackClick = {
            showToast = "Back clicked"
        },
        onSendInvitation = {
            showToast = "Invitation sent to ${sampleProfile.candidate.name}"
        },
        onSaveCandidate = {
            showToast = "Candidate saved"
        },
        onContactCandidate = {
            showToast = "Contacting ${sampleProfile.candidate.name}"
        },
        onScheduleInterview = {
            showToast = "Interview scheduled with ${sampleProfile.candidate.name}"
        },
        onViewResume = {
            showToast = "Opening resume for ${sampleProfile.candidate.name}"
        },
        onShareProfile = {
            showToast = "Sharing profile of ${sampleProfile.candidate.name}"
        }
    )

    // Show toast messages
    if (showToast.isNotEmpty()) {
        LaunchedEffect(showToast) {
            kotlinx.coroutines.delay(2000)
            showToast = ""
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CandidateCompleteProfilePreview() {
    CandidateCompleteProfileApp()
}
