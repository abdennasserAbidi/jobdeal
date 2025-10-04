package com.example.myjob.feature.home.detail.test

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.feature.home.detail.Education
import com.example.myjob.feature.home.detail.Language
import com.example.myjob.feature.home.detail.Project
import com.example.myjob.feature.home.detail.WorkExperience

// WhatsApp Green Theme Colors
val WhatsAppGreen = Color(0xFF25D366)
val WhatsAppDarkGreen = Color(0xFF128C7E)
val WhatsAppLightGreen = Color(0xFFDCF8C6)
val WhatsAppGreenSurface = Color(0xFFF0F9F0)

data class CompleteCandidateProfile(
    val candidate: Candidate,
    val email: String,
    val phone: String,
    val linkedIn: String?,
    val github: String?,
    val portfolio: String?,
    val bio: String,
    val experience: List<WorkExperience>,
    val education: List<Education>,
    val projects: List<Project>,
    val certifications: List<String>,
    val languages: List<Language>,
    val availability: String,
    val expectedSalary: String,
    val noticePeriod: String,
    val workPreference: WorkPreference,
    val achievements: List<Achievement>,
    val references: List<Reference>,
    val socialProfiles: SocialProfiles,
    val jobPreferences: JobPreferences,
    val personalInfo: PersonalInfo,
    val assessments: List<Assessment>
)

data class WorkPreference(
    val remoteWork: Boolean,
    val hybridWork: Boolean,
    val onSiteWork: Boolean,
    val travelWillingness: String,
    val preferredWorkingHours: String
)

data class Achievement(
    val title: String,
    val description: String,
    val year: String,
    val organization: String?
)

data class Reference(
    val name: String,
    val position: String,
    val company: String,
    val email: String,
    val phone: String?,
    val relationship: String
)

data class SocialProfiles(
    val twitter: String?,
    val instagram: String?,
    val behance: String?,
    val dribbble: String?,
    val medium: String?
)

data class JobPreferences(
    val preferredRoles: List<String>,
    val preferredIndustries: List<String>,
    val companySizePreference: String,
    val culturePreferences: List<String>
)

data class PersonalInfo(
    val dateOfBirth: String?,
    val nationality: String?,
    val visaStatus: String?,
    val address: String?,
    val emergencyContact: String?
)

data class Assessment(
    val name: String,
    val score: String,
    val completedDate: String,
    val validUntil: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateCompleteProfileScreen(
    candidateProfile: CompleteCandidateProfile,
    onBackClick: () -> Unit = {},
    onSendInvitation: () -> Unit = {},
    onSaveCandidate: () -> Unit = {},
    onContactCandidate: () -> Unit = {},
    onScheduleInterview: () -> Unit = {},
    onViewResume: () -> Unit = {},
    onShareProfile: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var isSaved by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Overview", "Experience", "Skills", "Projects", "Education", "More")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Complete Profile",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        isSaved = !isSaved
                        onSaveCandidate()
                    }
                ) {
                    Icon(
                        if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) WhatsAppGreen else Color.White
                    )
                }
                IconButton(onClick = onShareProfile) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = WhatsAppGreen,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )

        // Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = WhatsAppGreenSurface,
            contentColor = WhatsAppDarkGreen,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = WhatsAppGreen
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = WhatsAppGreen,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Content based on selected tab
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> OverviewTab(candidateProfile)
                1 -> ExperienceTab(candidateProfile.experience)
                2 -> SkillsTab(candidateProfile)
                3 -> ProjectsTab(candidateProfile.projects)
                4 -> EducationTab(candidateProfile.education)
                5 -> MoreTab(candidateProfile)
            }

            // Action Buttons (always visible)
            ActionButtonsSection(
                onSendInvitation = onSendInvitation,
                onContactCandidate = onContactCandidate,
                onScheduleInterview = onScheduleInterview,
                onViewResume = onViewResume
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OverviewTab(candidateProfile: CompleteCandidateProfile) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        ProfileHeaderCard(candidateProfile = candidateProfile)

        // Quick Stats Card
        QuickStatsCard(candidateProfile = candidateProfile)

        // Bio Card
        BioCard(bio = candidateProfile.bio)

        // Contact Information Card
        ContactInformationCard(candidateProfile = candidateProfile)

        // Work Preferences Card
        WorkPreferencesCard(workPreference = candidateProfile.workPreference)

        // Job Preferences Card
        JobPreferencesCard(jobPreferences = candidateProfile.jobPreferences)
    }
}

@Composable
fun ExperienceTab(experience: List<WorkExperience>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Professional Experience",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        experience.forEach { exp ->
            ExperienceDetailCard(experience = exp)
        }
    }
}

@Composable
fun SkillsTab(candidateProfile: CompleteCandidateProfile) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Technical Skills
        SkillsDetailCard(
            title = "Technical Skills",
            skills = candidateProfile.candidate.skills,
            isHighlighted = true
        )

        // Languages
        LanguagesCard(languages = candidateProfile.languages)

        // Certifications
        CertificationsCard(certifications = candidateProfile.certifications)

        // Assessments
        AssessmentsCard(assessments = candidateProfile.assessments)
    }
}

@Composable
fun ProjectsTab(projects: List<Project>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Featured Projects",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        projects.forEach { project ->
            ProjectDetailCard(project = project)
        }
    }
}

@Composable
fun EducationTab(education: List<Education>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Educational Background",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        education.forEach { edu ->
            EducationDetailCard(education = edu)
        }
    }
}

@Composable
fun MoreTab(candidateProfile: CompleteCandidateProfile) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Achievements
        AchievementsCard(achievements = candidateProfile.achievements)

        // References
        ReferencesCard(references = candidateProfile.references)

        // Social Profiles
        SocialProfilesCard(socialProfiles = candidateProfile.socialProfiles)

        // Personal Information
        PersonalInfoCard(personalInfo = candidateProfile.personalInfo)
    }
}

@Composable
fun ProfileHeaderCard(candidateProfile: CompleteCandidateProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhatsAppLightGreen
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large Avatar
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(25.dp),
                    color = WhatsAppGreen
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = candidateProfile.candidate.name.split(" ").map { it.first() }.joinToString(""),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = candidateProfile.candidate.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = candidateProfile.candidate.position,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status and Availability
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusChip(
                            text = candidateProfile.candidate.status.name.lowercase().replaceFirstChar { it.uppercase() },
                            color = WhatsAppGreen
                        )
                        StatusChip(
                            text = candidateProfile.availability,
                            color = Color(0xFF2196F3)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Key Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricItem(
                    icon = Icons.Default.Work,
                    label = "Experience",
                    value = candidateProfile.candidate.experience
                )
                MetricItem(
                    icon = Icons.Default.LocationOn,
                    label = "Location",
                    value = candidateProfile.candidate.location
                )
                MetricItem(
                    icon = Icons.Default.AttachMoney,
                    label = "Expected",
                    value = candidateProfile.expectedSalary
                )
            }
        }
    }
}

@Composable
fun QuickStatsCard(candidateProfile: CompleteCandidateProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = "${candidateProfile.experience.size}",
                    label = "Companies"
                )
                StatItem(
                    value = "${candidateProfile.projects.size}",
                    label = "Projects"
                )
                StatItem(
                    value = "${candidateProfile.candidate.skills.size}",
                    label = "Skills"
                )
                StatItem(
                    value = "${candidateProfile.certifications.size}",
                    label = "Certificates"
                )
            }
        }
    }
}

@Composable
fun WorkPreferencesCard(workPreference: WorkPreference) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhatsAppGreenSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Work Preferences",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Work Type Preferences
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (workPreference.remoteWork) {
                    PreferenceChip(text = "Remote", icon = Icons.Default.Home)
                }
                if (workPreference.hybridWork) {
                    PreferenceChip(text = "Hybrid", icon = Icons.Default.Business)
                }
                if (workPreference.onSiteWork) {
                    PreferenceChip(text = "On-site", icon = Icons.Default.LocationOn)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Additional Preferences
            DetailRow(
                icon = Icons.Default.FlightTakeoff,
                label = "Travel Willingness",
                value = workPreference.travelWillingness
            )

            Spacer(modifier = Modifier.height(8.dp))

            DetailRow(
                icon = Icons.Default.Schedule,
                label = "Preferred Hours",
                value = workPreference.preferredWorkingHours
            )
        }
    }
}

@Composable
fun JobPreferencesCard(jobPreferences: JobPreferences) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Job Preferences",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Preferred Roles
            Text(
                text = "Preferred Roles",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(jobPreferences.preferredRoles) { role ->
                    SkillChip(skill = role, isHighlighted = false)
                }
            }

            // Preferred Industries
            Text(
                text = "Preferred Industries",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(jobPreferences.preferredIndustries) { industry ->
                    SkillChip(skill = industry, isHighlighted = false)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            DetailRow(
                icon = Icons.Default.Business,
                label = "Company Size",
                value = jobPreferences.companySizePreference
            )
        }
    }
}

@Composable
fun ExperienceDetailCard(experience: WorkExperience) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = experience.position,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = experience.company,
                        style = MaterialTheme.typography.titleSmall,
                        color = WhatsAppGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = experience.duration,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = experience.description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )

            if (experience.technologies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Technologies Used",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(experience.technologies) { tech ->
                        SkillChip(skill = tech, isHighlighted = true)
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectDetailCard(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                project.link?.let {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = WhatsAppGreen.copy(alpha = 0.1f),
                        modifier = Modifier.clickable { /* Handle link click */ }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.OpenInNew,
                                contentDescription = "Open Project",
                                modifier = Modifier.size(16.dp),
                                tint = WhatsAppGreen
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "View",
                                style = MaterialTheme.typography.bodySmall,
                                color = WhatsAppGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 18.sp
            )

            if (project.technologies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(project.technologies) { tech ->
                        SkillChip(skill = tech, isHighlighted = true)
                    }
                }
            }
        }
    }
}

@Composable
fun EducationDetailCard(education: Education) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = education.degree,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = education.field,
                        style = MaterialTheme.typography.titleSmall,
                        color = WhatsAppGreen,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = education.institution,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = education.year,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    education.gpa?.let { gpa ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "GPA: $gpa",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SkillsDetailCard(
    title: String,
    skills: List<String>,
    isHighlighted: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) WhatsAppGreenSurface else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.height((skills.size / 3 + 1) * 40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(skills.chunked(3)) { skillRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        skillRow.forEach { skill ->
                            SkillChip(
                                skill = skill,
                                isHighlighted = isHighlighted,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill remaining space if row is not complete
                        repeat(3 - skillRow.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LanguagesCard(languages: List<Language>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Languages",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            languages.forEach { language ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = language.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = WhatsAppLightGreen
                    ) {
                        Text(
                            text = language.proficiency,
                            style = MaterialTheme.typography.bodySmall,
                            color = WhatsAppDarkGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CertificationsCard(certifications: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Certifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            certifications.forEach { cert ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = "Certified",
                        modifier = Modifier.size(16.dp),
                        tint = WhatsAppGreen
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = cert,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun AssessmentsCard(assessments: List<Assessment>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Skill Assessments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            assessments.forEach { assessment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = assessment.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WhatsAppGreen
                            ) {
                                Text(
                                    text = assessment.score,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Text(
                            text = "Completed: ${assessment.completedDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementsCard(achievements: List<Achievement>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Achievements & Awards",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            achievements.forEach { achievement ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = WhatsAppGreenSurface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = achievement.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                achievement.organization?.let { org ->
                                    Text(
                                        text = org,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = WhatsAppGreen,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Text(
                                text = achievement.year,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = achievement.description,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReferencesCard(references: List<Reference>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Professional References",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            references.forEach { reference ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = reference.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${reference.position} at ${reference.company}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Relationship: ${reference.relationship}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = reference.email,
                                style = MaterialTheme.typography.bodySmall,
                                color = WhatsAppGreen
                            )
                            reference.phone?.let { phone ->
                                Text(
                                    text = phone,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = WhatsAppGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SocialProfilesCard(socialProfiles: SocialProfiles) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Social Profiles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            socialProfiles.twitter?.let { twitter ->
                SocialProfileRow(
                    icon = Icons.Default.Share,
                    platform = "Twitter",
                    handle = twitter
                )
            }

            socialProfiles.medium?.let { medium ->
                SocialProfileRow(
                    icon = Icons.Default.Article,
                    platform = "Medium",
                    handle = medium
                )
            }

            socialProfiles.behance?.let { behance ->
                SocialProfileRow(
                    icon = Icons.Default.Palette,
                    platform = "Behance",
                    handle = behance
                )
            }
        }
    }
}

@Composable
fun PersonalInfoCard(personalInfo: PersonalInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            personalInfo.nationality?.let { nationality ->
                DetailRow(
                    icon = Icons.Default.Public,
                    label = "Nationality",
                    value = nationality
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            personalInfo.visaStatus?.let { visaStatus ->
                DetailRow(
                    icon = Icons.Default.CardMembership,
                    label = "Visa Status",
                    value = visaStatus
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            personalInfo.address?.let { address ->
                DetailRow(
                    icon = Icons.Default.Home,
                    label = "Address",
                    value = address
                )
            }
        }
    }
}

@Composable
fun ActionButtonsSection(
    onSendInvitation: () -> Unit,
    onContactCandidate: () -> Unit,
    onScheduleInterview: () -> Unit,
    onViewResume: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Primary Action - Send Invitation
            Button(
                onClick = onSendInvitation,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WhatsAppGreen,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send Invitation",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send Invitation")
            }

            // Secondary Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onContactCandidate,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Message,
                        contentDescription = "Contact",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Contact")
                }

                OutlinedButton(
                    onClick = onScheduleInterview,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Schedule",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Schedule")
                }
            }

            // View Resume Button
            OutlinedButton(
                onClick = onViewResume,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = "View Resume",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Resume")
            }
        }
    }
}

// Helper Composables
@Composable
fun StatusChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MetricItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = WhatsAppDarkGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StatItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = WhatsAppGreen
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PreferenceChip(text: String, icon: ImageVector) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = WhatsAppLightGreen
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier.size(16.dp),
                tint = WhatsAppDarkGreen
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = WhatsAppDarkGreen,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SocialProfileRow(
    icon: ImageVector,
    platform: String,
    handle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Handle social profile click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = platform,
            modifier = Modifier.size(20.dp),
            tint = WhatsAppGreen
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = platform,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = handle,
                style = MaterialTheme.typography.bodyMedium,
                color = WhatsAppGreen,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Default.OpenInNew,
            contentDescription = "Open",
            modifier = Modifier.size(16.dp),
            tint = WhatsAppGreen
        )
    }
}

@Composable
fun SkillChip(
    skill: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (isHighlighted) WhatsAppLightGreen else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = skill,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isHighlighted) WhatsAppDarkGreen else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isHighlighted) FontWeight.Medium else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ContactInformationCard(candidateProfile: CompleteCandidateProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ContactDetailRow(
                icon = Icons.Default.Email,
                label = "Email",
                value = candidateProfile.email,
                isClickable = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactDetailRow(
                icon = Icons.Default.Phone,
                label = "Phone",
                value = candidateProfile.phone,
                isClickable = true
            )

            candidateProfile.linkedIn?.let { linkedIn ->
                Spacer(modifier = Modifier.height(8.dp))
                ContactDetailRow(
                    icon = Icons.Default.Link,
                    label = "LinkedIn",
                    value = linkedIn,
                    isClickable = true
                )
            }

            candidateProfile.github?.let { github ->
                Spacer(modifier = Modifier.height(8.dp))
                ContactDetailRow(
                    icon = Icons.Default.Code,
                    label = "GitHub",
                    value = github,
                    isClickable = true
                )
            }

            candidateProfile.portfolio?.let { portfolio ->
                Spacer(modifier = Modifier.height(8.dp))
                ContactDetailRow(
                    icon = Icons.Default.Web,
                    label = "Portfolio",
                    value = portfolio,
                    isClickable = true
                )
            }
        }
    }
}

@Composable
fun ContactDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    isClickable: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = if (isClickable) WhatsAppGreen else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isClickable) WhatsAppGreen else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
        if (isClickable) {
            Icon(
                Icons.Default.OpenInNew,
                contentDescription = "Open",
                modifier = Modifier.size(16.dp),
                tint = WhatsAppGreen
            )
        }
    }
}

@Composable
fun BioCard(bio: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = bio,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }
    }
}
