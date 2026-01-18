package com.example.myjob.feature.home.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Extended Candidate data class
data class CandidateDetail(
    val id: String,
    val name: String,
    val position: String,
    val experience: String,
    val location: String,
    val workType: String,
    val salary: String,
    val available: Boolean,
    val email: String,
    val phone: String,
    val bio: String,
    val skills: List<String>,
    val languages: List<Language>,
    val education: List<Education>,
    val experience_details: List<ExperienceItem>,
    val certifications: List<String>
)

data class Language(val name: String, val level: String)
data class Education(val degree: String, val school: String, val year: String)
data class ExperienceItem(val title: String, val company: String, val duration: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CandidateDetailsScreen(
    candidate: CandidateDetail,
    onBackClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    onHireClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil du candidat") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color(0xFF049344)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomActionBar(
                onContactClick = onContactClick,
                onHireClick = onHireClick
            )
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            HeaderSection(candidate)

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Info Cards
            QuickInfoSection(candidate)

            Spacer(modifier = Modifier.height(16.dp))

            // About Section
            SectionCard(title = "À propos") {
                Text(
                    text = candidate.bio,
                    color = Color(0xFF4B5563),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            // Skills Section
            SectionCard(title = "Compétences") {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    candidate.skills.forEach { skill ->
                        SkillChip(skill = skill)
                    }
                }
            }

            // Languages Section
            SectionCard(title = "Langues") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    candidate.languages.forEach { language ->
                        LanguageItem(language)
                    }
                }
            }

            // Experience Section
            SectionCard(title = "Expérience professionnelle") {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    candidate.experience_details.forEach { exp ->
                        ExperienceCard(experience = exp)
                    }
                }
            }

            // Education Section
            SectionCard(title = "Formation") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    candidate.education.forEach { edu ->
                        EducationItem(education = edu)
                    }
                }
            }

            // Certifications Section
            if (candidate.certifications.isNotEmpty()) {
                SectionCard(title = "Certifications") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        candidate.certifications.forEach { cert ->
                            CertificationItem(certification = cert)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HeaderSection(candidate: CandidateDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF049344)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = candidate.name.split(" ").map { it.first() }.take(2).joinToString(""),
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = candidate.name,
                color = Color(0xFF1F2937),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Position
            Text(
                text = candidate.position,
                color = Color(0xFF049344),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Badge
            if (candidate.available) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF049344).copy(alpha = 0.1f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "⭐", fontSize = 14.sp)
                        Text(
                            text = "Disponible immédiatement",
                            color = Color(0xFF049344),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ContactInfoItem(
                    icon = Icons.Filled.Email,
                    text = candidate.email
                )
                ContactInfoItem(
                    icon = Icons.Filled.Phone,
                    text = candidate.phone
                )
            }
        }
    }
}

@Composable
fun QuickInfoSection(candidate: CandidateDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard(
            icon = Icons.Filled.Work,
            label = "Expérience",
            value = candidate.experience,
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            icon = Icons.Filled.LocationOn,
            label = "Localisation",
            value = candidate.location,
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            icon = Icons.Filled.AttachMoney,
            label = "Salaire",
            value = candidate.salary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF049344),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = Color(0xFF1F2937),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Color(0xFF6B7280),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFF1F2937),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun SkillChip(skill: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF049344).copy(alpha = 0.1f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = skill,
            color = Color(0xFF049344),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun LanguageItem(language: Language) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = null,
                tint = Color(0xFF049344),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = language.name,
                color = Color(0xFF1F2937),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = language.level,
            color = Color(0xFF6B7280),
            fontSize = 13.sp
        )
    }
}

@Composable
fun ExperienceCard(experience: ExperienceItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF049344).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Work,
                contentDescription = null,
                tint = Color(0xFF049344),
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = experience.title,
                color = Color(0xFF1F2937),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = experience.company,
                color = Color(0xFF049344),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = experience.duration,
                color = Color(0xFF6B7280),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = experience.description,
                color = Color(0xFF4B5563),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun EducationItem(education: Education) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.School,
            contentDescription = null,
            tint = Color(0xFF049344),
            modifier = Modifier.size(20.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = education.degree,
                color = Color(0xFF1F2937),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = education.school,
                color = Color(0xFF6B7280),
                fontSize = 13.sp
            )
            Text(
                text = education.year,
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun CertificationItem(certification: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.VerifiedUser,
            contentDescription = null,
            tint = Color(0xFF049344),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = certification,
            color = Color(0xFF1F2937),
            fontSize = 14.sp
        )
    }
}

@Composable
fun ContactInfoItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            color = Color(0xFF6B7280),
            fontSize = 12.sp
        )
    }
}

@Composable
fun BottomActionBar(
    onContactClick: () -> Unit,
    onHireClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onContactClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF049344))
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF049344)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Message,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Contacter",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onHireClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF049344)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recruter",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Sample data for preview
@Preview
@Composable
fun CandidateDetailPreview() {
    val sampleCandidate = CandidateDetail(
        id = "1",
        name = "Ahmed Bouzid",
        position = "Senior Android Developer",
        experience = "5+ ans",
        location = "Tunis",
        workType = "Remote",
        salary = "3000 DT",
        available = true,
        email = "ahmed.b@email.com",
        phone = "+216 20 123 456",
        bio = "Développeur Android passionné avec plus de 5 ans d'expérience dans la création d'applications mobiles innovantes. Expert en Kotlin et Jetpack Compose, j'aime créer des interfaces utilisateur fluides et performantes.",
        skills = listOf("Kotlin", "Jetpack Compose", "Firebase", "MVVM", "Clean Architecture", "Coroutines", "Room", "Retrofit"),
        languages = listOf(
            Language("Français", "Courant"),
            Language("Arabe", "Langue maternelle"),
            Language("Anglais", "Professionnel")
        ),
        education = listOf(
            Education("Master en Informatique", "ESPRIT", "2018"),
            Education("Licence en Informatique", "FST", "2016")
        ),
        experience_details = listOf(
            ExperienceItem(
                title = "Senior Android Developer",
                company = "TechCorp Tunisia",
                duration = "2021 - Présent",
                description = "Développement d'applications Android natives avec Kotlin et Jetpack Compose. Lead technique sur plusieurs projets."
            ),
            ExperienceItem(
                title = "Android Developer",
                company = "StartupHub",
                duration = "2018 - 2021",
                description = "Création d'applications mobiles pour startups locales et internationales."
            )
        ),
        certifications = listOf(
            "Google Associate Android Developer",
            "Kotlin Certified Developer",
            "Firebase Certified"
        )
    )

    CandidateDetailsScreen(candidate = sampleCandidate)
}