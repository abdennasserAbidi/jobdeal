import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

// Data class for Candidate
data class Candidate(
    val id: String,
    val name: String,
    val position: String,
    val experience: String,
    val location: String,
    val workType: String,
    val skills: List<String>,
    val available: Boolean = true
)

@Composable
fun CandidateCard(
    candidate: Candidate,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    )
    {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF049344)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = candidate.name.split(" ").map { it.first() }.take(2).joinToString(""),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Name and Status Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = candidate.name,
                            color = Color(0xFF1F2937),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (candidate.available) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = candidate.experience,
                                    color = Color(0xFF10B981),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Position
                    Text(
                        text = candidate.position,
                        color = Color(0xFF049344),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = candidate.location,
                        color = Color(0xFF6B7280),
                        fontSize = 13.sp
                    )
                }
            }

            /*Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "egjeahgklehglkeajgahlkgheaghelhgllaehgalgal",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF6B7280),
                fontSize = 13.sp
            )*/

            // Skills
            val s = candidate.skills.joinToString("  .  ")
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = s,
                color = Color(0xFF6B7280),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )
            /*Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                candidate.skills.forEach { skill ->
                    Text(
                        text = skill,
                        color = Color(0xFF6B7280),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (skill != candidate.skills.last()) {
                        Text(
                            text = "·",
                            color = Color(0xFFD1D5DB),
                            fontSize = 12.sp
                        )
                    }
                }
            }*/

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(10.dp),
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
        }

    }
}

@Preview
@Composable
fun CandidateListExample() {
    val sampleCandidates = listOf(
        Candidate(
            id = "1",
            name = "Ahmed B.",
            position = "Android Developer",
            experience = "5+ ans exp",
            location = "Tunis",
            workType = "Remote",
            skills = listOf("Kotlin", "Compose", "Firebase"),
            available = true
        ),
        Candidate(
            id = "2",
            name = "Sarah M.",
            position = "Senior Mobile Developer",
            experience = "7+ ans exp",
            location = "Sfax",
            workType = "Hybride",
            skills = listOf("Flutter", "React Native", "Node.js","Android", "Java", "Spring Boot"),
            available = true
        ),
        Candidate(
            id = "3",
            name = "Mohamed K.",
            position = "Full Stack Developer",
            experience = "3 ans exp",
            location = "Sousse",
            workType = "Sur site",
            skills = listOf("Android", "Java", "Spring Boot"),
            available = false
        ),
        Candidate(
            id = "4",
            name = "Amira L.",
            position = "Lead Android Engineer",
            experience = "8+ ans exp",
            location = "Ariana",
            workType = "Remote",
            skills = listOf("Kotlin", "MVVM", "Clean Architecture"),
            available = true
        ),
        Candidate(
            id = "5",
            name = "Youssef T.",
            position = "Mobile Developer",
            experience = "4 ans exp",
            location = "Nabeul",
            workType = "Hybride",
            skills = listOf("Android", "Jetpack", "Coroutines"),
            available = true
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF3F4F6)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(sampleCandidates) { candidate ->
                CandidateCard(
                    candidate = candidate,
                    onClick = { /* Handle click */ }
                )
            }
        }
    }
}