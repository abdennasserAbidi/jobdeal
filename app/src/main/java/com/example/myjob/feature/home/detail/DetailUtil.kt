package com.example.myjob.feature.home.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.feature.profile.test.LanguageForm

@Composable
fun CandidateHeaderCard(
    candidateProfile: User,
    experienceYears: Int,
    sendInvitation: (User) -> Unit,
    sendMessage: (User) -> Unit,
    onTerminateInvitation: (InvitationModel) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large Avatar
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = colorResource(id = R.color.whatsapp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        val userName = candidateProfile.fullName ?: "Test Test"
                        val nickname = userName.trim().ifEmpty { "Test Test" }.split(" ")
                            .map { it.first() }.joinToString("")
                        Text(
                            text = nickname,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = candidateProfile.fullName ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = candidateProfile.preferredActivitySector ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val experiences = candidateProfile.experience ?: mutableListOf()
            var lastExp = Experience()
            if (experiences.isNotEmpty()) {
                lastExp = experiences[experiences.lastIndex]
            }

            val lastContractExp = experiences.findLast {
                it.type == "Contract" || it.type == "Contrat"
            }

            lastContractExp?.let { lastContractExperience ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickInfoItem(
                        icon = Icons.Default.Business,
                        label = stringResource(id = R.string.choose_companies_text),
                        value = lastContractExperience.companyName ?: ""
                    )

                    if (experienceYears != 0) {
                        val yearsText = stringResource(id = R.string.years_text)
                        val textExp = if (experienceYears <= 5) "$experienceYears $yearsText"
                        else "+$experienceYears $yearsText"
                        QuickInfoItem(
                            icon = Icons.Default.Work,
                            label = stringResource(id = R.string.experience_text),
                            value = textExp
                        )
                    }

                    QuickInfoItem(
                        icon = Icons.Default.LocationOn,
                        label = stringResource(id = R.string.location_text),
                        value = lastContractExperience.place ?: ""
                    )
                }
            }

            val invitations = GlobalEntries.user.invitations ?: mutableListOf()
            if (invitations.isNotEmpty()) {
                val invitation = invitations.filter { it.idTo == candidateProfile.id }
                if (invitation.isNotEmpty()) {
                    val isFriend = invitation[0].status == InvitationStatus.HIRED.name
                    val isInProcess = invitation[0].status == InvitationStatus.IN_PROCESS.name
                    val isPending = invitation[0].status == InvitationStatus.ON_HOLD.name
                    val isRejecting = invitation[0].status == InvitationStatus.REJECTED.name || invitation[0].status == InvitationStatus.NOT_INTERESTED.name

                    val whatsappGreen = colorResource(id = R.color.whatsapp)
                    if (isFriend) {
                        OutlinedButton(
                            onClick = {
                                sendMessage(candidateProfile)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, whatsappGreen)
                        ) {
                            Icon(
                                Icons.Default.Message,
                                contentDescription = "Contact",
                                tint = whatsappGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.contact_text), color = whatsappGreen)
                        }
                    } else if (isInProcess) {
                        OutlinedButton(
                            onClick = {
                                sendMessage(candidateProfile)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, whatsappGreen)
                        ) {
                            Icon(
                                Icons.Default.Message,
                                contentDescription = "Contact",
                                tint = whatsappGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.contact_text), color = whatsappGreen)
                        }

                        Button(
                            onClick = { onTerminateInvitation(invitation[0]) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.whatsapp),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.terminate_text))
                        }
                    } else if (isPending) {
                        OutlinedButton(
                            onClick = {

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = "timer",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(id = R.string.pending_text))
                        }

                    } else if (isRejecting) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            color = Color.Red,
                            text = stringResource(id = R.string.refuse_candidate_text)
                        )
                    }
                }
                else {
                    OutlinedButton(
                        onClick = {
                            sendInvitation(candidateProfile)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send Invitation",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.send_invitation_text))
                    }
                }
            } else {
                OutlinedButton(
                    onClick = {
                        sendInvitation(candidateProfile)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send Invitation",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(id = R.string.send_invitation_text))
                }
            }
        }
    }
}

@Composable
fun ContactInformationCard(candidateProfile: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.contact_information_text),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val email = candidateProfile.email ?: ""
            if (email.isNotEmpty()) {
                ContactDetailRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = candidateProfile.email ?: "",
                    isClickable = true
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            val phone = candidateProfile.phone ?: ""
            if (phone.isNotEmpty()) {
                ContactDetailRow(
                    icon = Icons.Default.Phone,
                    label = stringResource(id = R.string.phone_text),
                    value = phone,
                    isClickable = true
                )
            }

            candidateProfile.professionalStatus?.userMedium?.let { medium ->
                if (medium.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ContactDetailRow(
                        icon = Icons.Default.Link,
                        label = "LinkedIn",
                        value = medium,
                        isClickable = true
                    )
                }
            }

            candidateProfile.professionalStatus?.userGithub?.let { github ->
                if (github.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ContactDetailRow(
                        icon = Icons.Default.Code,
                        label = "GitHub",
                        value = github,
                        isClickable = true
                    )
                }
            }

            candidateProfile.professionalStatus?.userPortfolio?.let { portfolio ->
                if (portfolio.isNotEmpty()) {
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
}

@Composable
fun BioCard(bio: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.about_text),
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

@Composable
fun SkillsCard(skills: List<String>) {
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
                text = "Skills & Technologies",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(skills) { skill ->
                    SkillChip(skill = skill, isHighlighted = true)
                }
            }
        }
    }
}

@Composable
fun ExperienceCard(experience: List<Experience>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.work_experience_text),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            experience.forEachIndexed { index, exp ->
                ExperienceItem(experience = exp)
                if (index < experience.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ProjectsCard(projects: List<Project>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Featured Projects",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            projects.forEachIndexed { index, project ->
                ProjectItem(project = project)
                if (index < projects.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun EducationCard(educations: MutableList<Educations>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.education_title_text),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            educations.forEach { education ->
                EducationItem(education = education)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AdditionalInfoCard(
    certifications: List<String>,
    languages: List<LanguageForm>,
    availability: String,
    expectedSalary: String,
    noticePeriod: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Availability
            DetailRow(
                icon = Icons.Default.Schedule,
                label = "Availability",
                value = availability
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Expected Salary
            DetailRow(
                icon = Icons.Default.AttachMoney,
                label = "Expected Salary",
                value = expectedSalary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Notice Period
            DetailRow(
                icon = Icons.Default.CalendarToday,
                label = "Notice Period",
                value = noticePeriod
            )

            if (languages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Languages",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                languages.forEach { language ->
                    Text(
                        text = "${language.name} - ${language.proficiency}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (certifications.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Certifications",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                certifications.forEach { cert ->
                    Text(
                        text = "• $cert",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButtonsCard(
    onSendInvitation: () -> Unit,
    onContactCandidate: () -> Unit,
    onScheduleInterview: () -> Unit
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

            // Send Invitation Button
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Contact Button
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

                // Schedule Interview Button
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
        }
    }
}

// Helper Composables
@Composable
fun QuickInfoItem(
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
            modifier = Modifier.size(20.dp),
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
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
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
fun ExperienceItem(experience: Experience) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = experience.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = experience.companyName ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WhatsAppGreen,
                    fontWeight = FontWeight.Medium
                )
            }

            val start = experience.dateStart ?: ""
            val end = experience.dateEnd ?: ""

            var duration = ""

            if (start.isNotEmpty()) {
                duration = start

                if (experience.current == false) {
                    if (end.isNotEmpty()) {
                        duration += " - $end"
                    }
                } else duration += " to present"
            }

            Text(
                text = duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = experience.description ?: "",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
        )

        if (experience.listSkills?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val listTech = experience.listSkills?.toMutableList() ?: mutableListOf()
                items(listTech) { tech ->
                    SkillChip(skill = tech, isHighlighted = false)
                }
            }
        }
    }
}

@Composable
fun ProjectItem(project: Project) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            project.link?.let {
                Icon(
                    Icons.Default.OpenInNew,
                    contentDescription = "Open Project",
                    modifier = Modifier.size(16.dp),
                    tint = WhatsAppGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = project.description,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
        )

        if (project.technologies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(project.technologies) { tech ->
                    SkillChip(skill = tech, isHighlighted = false)
                }
            }
        }
    }
}

@Composable
fun EducationItem(education: Educations) {
    Column {
        Text(
            text = education.degree ?: "",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${education.fieldStudy} • ${education.schoolName}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val duration = "${education.dateStart} - ${education.dateEnd}"
            Text(
                text = duration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SkillChip(skill: String, isHighlighted: Boolean) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isHighlighted) WhatsAppLightGreen else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = skill,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isHighlighted) WhatsAppDarkGreen else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isHighlighted) FontWeight.Medium else FontWeight.Normal
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