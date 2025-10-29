package com.example.myjob.feature.profile.test

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.profile.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CandidateProfileFormExec(
    navController: NavController,
    list: List<NewCountry>,
    clearData: () -> Unit = {},
    allSubjects: MutableList<Subject>,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    // Sample initial data (could be empty for new profile or pre-filled for editing)
    val initialFormData = CandidateFormData(
        firstName = "Sarah",
        lastName = "Johnson",
        email = "sarah.johnson@email.com",
        phone = "+1 (555) 123-4567",
        position = "Senior Android Developer",
        currentCompany = "Google",
        location = "San Francisco, CA",
        bio = "Passionate Android developer with expertise in modern development practices...",
        experience = "Senior (5-8 years)",
        expectedSalary = "$140k - $170k",
        currentSalary = "$120k - $150k",
        availability = "Available in 2 weeks",
        noticePeriod = "2 weeks",
        skills = mutableListOf("Kotlin", "Jetpack Compose", "MVVM", "Coroutines"),
        certifications = mutableListOf("Google Associate Android Developer"),
        languages = mutableListOf(
            LanguageForm("English", "Native"),
            LanguageForm("Spanish", "Conversational")
        ),
        remoteWork = true,
        hybridWork = true,
        onSiteWork = false,
        travelWillingness = "Occasional (10-25%)",
        preferredWorkingHours = "9 AM - 6 PM PST",
        linkedIn = "linkedin.com/in/sarahjohnson",
        github = "github.com/sarahjohnson",
        portfolio = "sarahjohnson.dev",
        workExperience = mutableListOf(
            WorkExperienceForm(
                company = "Google",
                position = "Senior Android Developer",
                startDate = "01/2022",
                endDate = "",
                current = true,
                description = "Lead Android development for Google Pay...",
                technologies = mutableListOf("Kotlin", "Jetpack Compose", "MVVM")
            )
        ),
        projects = mutableListOf(
            ProjectForm(
                name = "TaskMaster Pro",
                description = "A comprehensive task management app...",
                technologies = mutableListOf("Kotlin", "Firebase", "Room"),
                link = "github.com/sarahjohnson/taskmaster"
            )
        ),
        education = mutableListOf(
            EducationForm(
                institution = "Stanford University",
                degree = "Master of Science",
                field = "Computer Science",
                year = "2019",
                gpa = "3.8"
            )
        ),
        preferredRoles = mutableListOf("Senior Android Developer", "Lead Mobile Developer"),
        preferredIndustries = mutableListOf("Technology", "Fintech", "Healthcare"),
        companySizePreference = "Medium (201-1000 employees)",
        nationality = "American",
        visaStatus = "US Citizen",
        address = "San Francisco Bay Area, CA"
    )

    var showToast by remember { mutableStateOf("") }

    CandidateProfileFormScreen(
        initialData = initialFormData,
        navController = navController,
        list = list,
        clearData = clearData,
        allSubjects = allSubjects,
        onBackClick = {
            navController.popBackStack()
        },
        onSaveProfile = { formData ->
            showToast = "Profile completed successfully!"
            // Here you would typically save to database or API
            println("Saving profile: ${formData.firstName} ${formData.lastName}")
        },
        onSaveDraft = { formData ->
            showToast = "Draft saved"
            // Here you would typically save draft to local storage
            println("Saving draft: ${formData.firstName} ${formData.lastName}")
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

/*@Preview(showBackground = true)
@Composable
fun CandidateProfileFormPreview() {
    CandidateProfileFormExec()
}*/
