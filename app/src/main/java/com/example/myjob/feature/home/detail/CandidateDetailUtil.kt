package com.example.myjob.feature.home.detail

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.feature.profile.test.LanguageForm
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CandidateDetailsScreen(
    candidate: User,
    onBackClick: () -> Unit = {},
    onContactClick: (User) -> Unit = {},
    onHireClick: (User) -> Unit = {},
    onTerminateInvitation: (InvitationModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.profile_candidate_type_text)) },
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
                candidate = candidate,
                onContactClick = {
                    onContactClick(candidate)
                },
                onHireClick = {
                    onHireClick(candidate)
                },
                onTerminateInvitation = {
                    onTerminateInvitation(it)
                }
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
                    text = candidate.bio ?: "",
                    color = Color(0xFF4B5563),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            // Skills Section
            SectionCard(title = "Compétences") {
                val skills = candidate.candidateSkills?.listSkills ?: emptyList()
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    skills.forEach { skill ->
                        SkillChip(skill = skill)
                    }
                }
            }

            // Languages Section
            SectionCard(title = "Langues") {
                val languages = candidate.candidateSkills?.listLanguages ?: emptyList()

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    languages.forEach { language ->
                        LanguageItem(language)
                    }
                }
            }

            // Experience Section
            SectionCard(title = "Expérience professionnelle") {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    candidate.experience?.forEach { exp ->
                        ExperienceCard(experience = exp)
                    }
                }
            }

            // Education Section
            SectionCard(title = "Formation") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    candidate.education?.forEach { edu ->
                        EducationItem(education = edu)
                    }
                }
            }
            val certifications = candidate.candidateSkills?.listCertification ?: emptyList()

            // Certifications Section
            if (certifications.isNotEmpty()) {
                SectionCard(title = "Certifications") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        certifications.forEach { cert ->
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
fun HeaderSection(candidate: User) {
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
                    text = candidate.fullName?.ifEmpty { "Test Test" }?.split(" ")?.map { it.first() }?.take(2)?.joinToString("") ?: "",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = candidate.fullName ?: "",
                color = Color(0xFF1F2937),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Position
            Text(
                text = candidate.preferredActivitySector ?: "",
                color = Color(0xFF049344),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Badge
            val workType = candidate.preferredWorkType?.let { preferredWorkType ->
                if (preferredWorkType.isEmpty()) ""
                else if (preferredWorkType.size == 1) preferredWorkType[0]
                else "Contrat et Freelance"
            } ?: "Contrat"


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
                        text = workType,
                        color = Color(0xFF049344),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
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
                    text = candidate.email ?: ""
                )
                ContactInfoItem(
                    icon = Icons.Filled.Phone,
                    text = candidate.phone ?: ""
                )
            }
        }
    }
}

@Composable
fun QuickInfoSection(candidate: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var diffYear by remember { mutableStateOf(0) }

        val experiences = candidate.experience ?: emptyList()
        if (experiences.isNotEmpty()) {
            val firstExp = experiences[0]
            val start = firstExp.dateStart ?: ""

            if (start.isNotEmpty()) {
                val startArray = start.split(" ")
                val yearStart = startArray[1].toInt()

                val calendar: Calendar = Calendar.getInstance()
                val currentYear: Int = calendar.get(Calendar.YEAR)

                diffYear = currentYear - yearStart
            }
        }


        val yearsText = stringResource(id = R.string.years_text)
        val textExp = if (diffYear <= 5) "$diffYear $yearsText exp"
        else "+$diffYear $yearsText exp"

        InfoCard(
            icon = Icons.Filled.Work,
            label = "Expérience",
            value = textExp,
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            icon = Icons.Filled.LocationOn,
            label = "Localisation",
            value = candidate.address ?: "",
            modifier = Modifier.weight(1f)
        )
        InfoCard(
            icon = Icons.Filled.AttachMoney,
            label = "Salaire",
            value = candidate.professionalStatus?.preferredSalary ?: "",
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
fun LanguageItem(language: LanguageForm) {
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
            text = language.proficiency,
            color = Color(0xFF6B7280),
            fontSize = 13.sp
        )
    }
}

@Composable
fun ExperienceCard(experience: Experience) {
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
                text = experience.title ?: "",
                color = Color(0xFF1F2937),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = experience.companyName ?: "",
                color = Color(0xFF049344),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            var startYear by remember { mutableStateOf(0) }
            var endYear by remember { mutableStateOf(0) }
            var diffYear by remember { mutableStateOf(0) }

            val start = experience.dateStart ?: ""

            if (start.isNotEmpty()) {
                val startArray = start.split(" ")
                startYear = startArray[1].toInt()
            }

            val end = experience.dateEnd ?: ""
            endYear = if (end.isNotEmpty()) {
                val endArray = end.split(" ")
                endArray[1].toInt()
            } else -1


            val difference = if (endYear != -1) {
                diffYear = endYear - startYear
                val yearsText = stringResource(id = R.string.years_text)
                if (diffYear <= 5) "$diffYear $yearsText"
                else "+$diffYear $yearsText"
            } else "Jusqu'a présent"

            Text(
                text = difference,
                color = Color(0xFF6B7280),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = experience.description ?: "",
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
                text = education.institution,
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
    candidate: User,
    onContactClick: () -> Unit,
    onHireClick: () -> Unit,
    onTerminateInvitation: (InvitationModel) -> Unit
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

            val invitations = GlobalEntries.user.invitations ?: mutableListOf()
            if (invitations.isNotEmpty()) {
                val invitation = invitations.filter { it.idTo == candidate.id }
                if (invitation.isNotEmpty()) {
                    val isFriend = invitation[0].status == InvitationStatus.HIRED.name
                    val isInProcess = invitation[0].status == InvitationStatus.IN_PROCESS.name
                    val isPending = invitation[0].status == InvitationStatus.ON_HOLD.name
                    val isRejecting =
                        invitation[0].status == InvitationStatus.REJECTED.name || invitation[0].status == InvitationStatus.NOT_INTERESTED.name

                    val whatsappGreen = colorResource(id = R.color.whatsapp)
                    Log.i("faklekahklbhga", "BottomActionBar: $isFriend")
                    if (isFriend) {
                        OutlinedButton(
                            onClick = onContactClick,
                            modifier = Modifier.fillMaxWidth(),
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
                    } else if (isInProcess) {
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
                            onClick = {
                                onTerminateInvitation(invitation[0])
                            },
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
                                text = stringResource(id = R.string.terminate_text),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else if (isPending) {

                        OutlinedButton(
                            onClick = {},
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
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(id = R.string.pending_text),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                    } else if (isRejecting) {
                        Column(modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                color = Color.Red,
                                text = stringResource(id = R.string.refuse_candidate_text)
                            )

                            Spacer(Modifier.height(10.dp))

                            Button(
                                onClick = onHireClick,
                                modifier = Modifier.fillMaxWidth(),
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
                } else {
                    Button(
                        onClick = onHireClick,
                        modifier = Modifier.fillMaxWidth(),
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
            } else {
                Button(
                    onClick = onHireClick,
                    modifier = Modifier.fillMaxWidth(),
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
}