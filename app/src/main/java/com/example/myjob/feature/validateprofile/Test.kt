package com.example.myjob.feature.validateprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

// Data Classes
data class VerificationData(
    val profilePhotos: List<String> = emptyList(),
    val selfiePhoto: String? = null,
    val phoneNumber: String = "",
    val isPhoneVerified: Boolean = false,
    val idDocument: String? = null,
    val linkedInProfile: String? = null,
    val workEmail: String? = null,
    val isWorkEmailVerified: Boolean = false,
    val videoIntroduction: String? = null
)

enum class VerificationStep {
    PROFILE_PHOTOS, SELFIE_VERIFICATION, PHONE_VERIFICATION,
    ID_DOCUMENT, LINKEDIN_VERIFICATION, WORK_EMAIL, VIDEO_INTRO
}

enum class VerificationStatus {
    NOT_STARTED, IN_PROGRESS, PENDING_REVIEW, VERIFIED, REJECTED
}

data class StepStatus(
    var step: VerificationStep,
    var status: VerificationStatus,
    var title: String,
    var description: String,
    var icon: ImageVector,
    var isRequired: Boolean = true
)

@Preview
@Composable
fun Test() {
    var verificationData by remember { mutableStateOf(VerificationData()) }
    var currentStep by remember { mutableStateOf<VerificationStep?>(null) }

    val verificationSteps = remember {
        listOf(
            StepStatus(
                VerificationStep.PROFILE_PHOTOS,
                VerificationStatus.VERIFIED,
                "Profile Photos",
                "Add at least 3 clear photos of yourself",
                Icons.Default.PhotoCamera
            ),
            StepStatus(
                VerificationStep.SELFIE_VERIFICATION,
                VerificationStatus.NOT_STARTED,
                "Selfie Verification",
                "Take a live selfie to verify your identity",
                Icons.Default.CameraAlt
            ),
            StepStatus(
                VerificationStep.PHONE_VERIFICATION,
                VerificationStatus.IN_PROGRESS,
                "Phone Verification",
                "Verify your phone number with SMS code",
                Icons.Default.Phone
            ),
            StepStatus(
                VerificationStep.ID_DOCUMENT,
                VerificationStatus.PENDING_REVIEW,
                "ID Document",
                "Upload a government-issued ID (optional)",
                Icons.Default.Badge,
                isRequired = false
            ),
            StepStatus(
                VerificationStep.LINKEDIN_VERIFICATION,
                VerificationStatus.NOT_STARTED,
                "LinkedIn Profile",
                "Connect your LinkedIn for professional verification",
                Icons.Default.Link,
                isRequired = false
            ),
            StepStatus(
                VerificationStep.WORK_EMAIL,
                VerificationStatus.NOT_STARTED,
                "Work Email",
                "Verify with your company email address",
                Icons.Default.Work,
                isRequired = false
            ),
            StepStatus(
                VerificationStep.VIDEO_INTRO,
                VerificationStatus.NOT_STARTED,
                "Video Introduction",
                "Record a 30-second video introduction",
                Icons.Default.VideoCall,
                isRequired = false
            )
        )
    }

    val completedSteps = verificationSteps.count { it.status == VerificationStatus.VERIFIED }
    val requiredSteps = verificationSteps.count { it.isRequired }
    val requiredCompleted = verificationSteps.count { it.isRequired && it.status == VerificationStatus.VERIFIED }
    val isFullyVerified = requiredCompleted == requiredSteps

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFF),
                        Color.White,
                        Color(0xFFF3F4F6)
                    )
                )
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Header
        item {
            VerificationHeader(
                completedSteps = completedSteps,
                totalSteps = verificationSteps.size,
                isFullyVerified = isFullyVerified
            )
        }

        // Verification Badge Preview
        item {
            VerificationBadgePreview(isFullyVerified = isFullyVerified)
        }

        // Required Steps Section
        item {
            Text(
                text = "Required Verification",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Required Steps
        verificationSteps.filter { it.isRequired }.forEach { step ->
            item {
                VerificationStepCard(
                    stepStatus = step,
                    onClick = { currentStep = step.step }
                )
            }
        }

        // Optional Steps Section
        item {
            Text(
                text = "Optional Verification",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Complete these for higher credibility",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Optional Steps
        verificationSteps.filter { !it.isRequired }.forEach { step ->
            item {
                VerificationStepCard(
                    stepStatus = step,
                    onClick = { currentStep = step.step }
                )
            }
        }

        // Verification Benefits
        item {
            VerificationBenefitsCard()
        }

        // Action Button
        item {
            if (isFullyVerified) {
                Button(
                    onClick = { /* Navigate back or complete */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Profile Verified!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { /* Show next required step */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Continue Verification Process",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4F46E5)
                    )
                }
            }
        }
    }
}

@Composable
fun VerificationHeader(
    completedSteps: Int,
    totalSteps: Int,
    isFullyVerified: Boolean
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (isFullyVerified) Icons.Default.Verified else Icons.Default.Security,
                contentDescription = null,
                tint = if (isFullyVerified) Color(0xFF10B981) else Color(0xFF4F46E5),
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = if (isFullyVerified) "Profile Verified" else "Verify Your Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = if (isFullyVerified)
                        "Your profile has been verified!"
                    else
                        "Build trust with employers and stand out",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        if (!isFullyVerified) {
            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = completedSteps.toFloat() / totalSteps.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF4F46E5),
                trackColor = Color(0xFFE5E7EB)
            )

            Text(
                text = "$completedSteps of $totalSteps verification steps completed",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun VerificationBadgePreview(isFullyVerified: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFullyVerified) Color(0xFFECFDF5) else Color(0xFFF9FAFB)
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF9CA3AF)
                )

                if (isFullyVerified) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Verified",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "John Doe",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    if (isFullyVerified) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Badge",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = if (isFullyVerified) "Verified Professional" else "Verification in progress",
                    fontSize = 14.sp,
                    color = if (isFullyVerified) Color(0xFF10B981) else Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VerificationStepCard(
    stepStatus: StepStatus,
    onClick: () -> Unit
) {
    val backgroundColor = when (stepStatus.status) {
        VerificationStatus.VERIFIED -> Color(0xFFECFDF5)
        VerificationStatus.PENDING_REVIEW -> Color(0xFFFFFBEB)
        VerificationStatus.IN_PROGRESS -> Color(0xFFEFF6FF)
        VerificationStatus.REJECTED -> Color(0xFFFEF2F2)
        VerificationStatus.NOT_STARTED -> Color.White
    }

    val borderColor = when (stepStatus.status) {
        VerificationStatus.VERIFIED -> Color(0xFF10B981)
        VerificationStatus.PENDING_REVIEW -> Color(0xFFF59E0B)
        VerificationStatus.IN_PROGRESS -> Color(0xFF3B82F6)
        VerificationStatus.REJECTED -> Color(0xFFEF4444)
        VerificationStatus.NOT_STARTED -> Color(0xFFE5E7EB)
    }

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(borderColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = stepStatus.icon,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stepStatus.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )

                    /*if (!stepStatus.isRequired) {
                        Chip(
                            onClick = { },
                            content = {
                                Text(
                                    color = Color(0xFF6B7280),
                                    text = "Optional",
                                    fontSize = 10.sp
                                )
                            },
                            modifier = Modifier.height(24.dp),
                            colors = ChipDefaults.chipColors(
                                contentColor = Color(0xFFF3F4F6)
                            )
                        )
                    }*/
                }

                Text(
                    text = stepStatus.description,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = getStatusText(stepStatus.status),
                    fontSize = 12.sp,
                    color = borderColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = when (stepStatus.status) {
                    VerificationStatus.VERIFIED -> Icons.Default.CheckCircle
                    VerificationStatus.PENDING_REVIEW -> Icons.Default.Schedule
                    VerificationStatus.IN_PROGRESS -> Icons.Default.Refresh
                    VerificationStatus.REJECTED -> Icons.Default.Cancel
                    VerificationStatus.NOT_STARTED -> Icons.Default.ChevronRight
                },
                contentDescription = null,
                tint = borderColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun VerificationBenefitsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F9FF)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colorResource(id = R.color.whatsapp),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.benefits_text),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val benefits = listOf(
                "✓ ${stringResource(id = R.string.badge_text)}",
                "✓ ${stringResource(id = R.string.visibility_search_text)}",
                "✓ ${stringResource(id = R.string.build_trust_text)}",
                "✓ ${stringResource(id = R.string.approvals_text)}"
            )

            benefits.forEach { benefit ->
                Text(
                    text = benefit,
                    fontSize = 14.sp,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

fun getStatusText(status: VerificationStatus): String {
    return when (status) {
        VerificationStatus.VERIFIED -> "✓ Verified"
        VerificationStatus.PENDING_REVIEW -> "⏳ Pending Review"
        VerificationStatus.IN_PROGRESS -> "🔄 In Progress"
        VerificationStatus.REJECTED -> "❌ Rejected - Try Again"
        VerificationStatus.NOT_STARTED -> "Tap to Start"
    }
}