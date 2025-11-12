package com.example.myjob.common.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myjob.R
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import kotlinx.coroutines.flow.update
import java.util.Calendar

@Composable
fun CandidateCard(
    user: User,
    candidate: Candidate,
    onClick: () -> Unit,
    onSendInvitation: (Candidate) -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }
    var diffYear by remember { mutableStateOf(0) }
    var lastContractExperience by remember { mutableStateOf<Experience?>(null) }

    val experiences = user.experience ?: mutableListOf()

    val lastContractExp = experiences.findLast {
        it.type == "Contract" || it.type == "Contrat"
    }

    LaunchedEffect(lastContractExp) {
        lastContractExperience = lastContractExp
    }

    if (experiences.isNotEmpty()) {
        val firstExp = experiences[0]
        val start = firstExp.dateStart ?: ""

        if (start.isNotEmpty()) {
            val startArray = start.split(", ")
            val yearStart = startArray[2].toInt()

            val calendar: Calendar = Calendar.getInstance()
            val currentYear: Int = calendar.get(Calendar.YEAR)

            diffYear = currentYear - yearStart
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row - Name, Position and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Name and Position
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user.fullName ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        color = Color.Black,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = user.preferredActivitySector ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status Badge
                StatusBadge(status = candidate.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Work,
                        contentDescription = "Experience",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    val userExperience = user.professionalStatus.userExperience ?: ""

                    Text(
                        text = userExperience,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user.address ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Company and Experience
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Work,
                        contentDescription = "Experience",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    val availability = user.professionalStatus.availability ?: ""

                    Text(
                        text = availability,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }

                lastContractExp?.let { lastContractExperience ->
                    val companyName = lastContractExperience.companyName ?: ""
                    if (companyName.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Business,
                                contentDescription = "work",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = companyName,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Skills
            /*LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(user.candidateSkills.listSkills.take(5)) { skill ->
                    SkillChip(skill = skill)
                }
                if (user.candidateSkills.listSkills.size > 5) {
                    item {
                        Text(
                            text = "+${user.candidateSkills.listSkills.size - 5} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }*/

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onClick() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "View Profile",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View Profile")
                }

                Button(
                    onClick = { onSendInvitation(candidate) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send Invitation",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Invite")
                }
            }
        }
    }
}