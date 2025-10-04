package com.example.myjob.feature.validateprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Data Classes
data class ProfileData(
    val fullName: String = "John Doe",
    val email: String = "john.doe@email.com",
    val phone: String = "+1 (555) 123-4567",
    val location: String = "New York, NY",
    val jobTitle: String = "Senior Software Engineer",
    val experience: String = "5+ years",
    val education: String = "Computer Science, MIT",
    val skills: List<String> = listOf("React", "Node.js", "Python", "AWS"),
    val resume: String = "resume_john_doe.pdf",
    val profilePhotoUrl: String? = null
)

enum class ValidationStatus {
    VALID, INVALID, PENDING, MISSING
}

data class ValidationState(
    val fullName: ValidationStatus = ValidationStatus.VALID,
    val email: ValidationStatus = ValidationStatus.VALID,
    val phone: ValidationStatus = ValidationStatus.VALID,
    val location: ValidationStatus = ValidationStatus.VALID,
    val jobTitle: ValidationStatus = ValidationStatus.PENDING,
    val experience: ValidationStatus = ValidationStatus.VALID,
    val education: ValidationStatus = ValidationStatus.INVALID,
    val skills: ValidationStatus = ValidationStatus.VALID,
    val resume: ValidationStatus = ValidationStatus.VALID,
    val profilePhoto: ValidationStatus = ValidationStatus.MISSING
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileValidationScreenTest() {
    var profileData by remember { mutableStateOf(ProfileData()) }
    var validationState by remember { mutableStateOf(ValidationState()) }
    var editingField by remember { mutableStateOf<String?>(null) }
    var tempValue by remember { mutableStateOf("") }

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
            Column {
                Text(
                    text = "Profile Validation",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "Complete and verify your profile information",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Progress Card
        item {
            ProgressCard(validationState = validationState)
        }

        // Profile Photo
        item {
            ProfilePhotoCard(
                profileData = profileData,
                validationStatus = validationState.profilePhoto,
                onPhotoClick = { /* Handle photo selection */ }
            )
        }

        // Profile Fields
        item {
            ProfileField(
                icon = Icons.Default.Person,
                label = "Full Name",
                value = profileData.fullName,
                validationStatus = validationState.fullName,
                isEditing = editingField == "fullName",
                tempValue = tempValue,
                onEditClick = {
                    editingField = "fullName"
                    tempValue = profileData.fullName
                },
                onValueChange = { tempValue = it },
                onSave = {
                    profileData = profileData.copy(fullName = tempValue)
                    editingField = null
                    // Simulate validation
                    validationState = validationState.copy(fullName = ValidationStatus.PENDING)
                    /*LaunchedEffect(Unit) {
                        delay(1000)
                        validationState = validationState.copy(fullName = ValidationStatus.VALID)
                    }*/
                    validationState = validationState.copy(fullName = ValidationStatus.VALID)

                },
                onCancel = { editingField = null }
            )
        }

        item {
            ProfileField(
                icon = Icons.Default.Email,
                label = "Email Address",
                value = profileData.email,
                validationStatus = validationState.email,
                isEditing = editingField == "email",
                tempValue = tempValue,
                keyboardType = KeyboardType.Email,
                onEditClick = {
                    editingField = "email"
                    tempValue = profileData.email
                },
                onValueChange = { tempValue = it },
                onSave = {
                    profileData = profileData.copy(email = tempValue)
                    editingField = null
                },
                onCancel = { editingField = null }
            )
        }

        item {
            ProfileField(
                icon = Icons.Default.Phone,
                label = "Phone Number",
                value = profileData.phone,
                validationStatus = validationState.phone,
                isEditing = editingField == "phone",
                tempValue = tempValue,
                keyboardType = KeyboardType.Phone,
                onEditClick = {
                    editingField = "phone"
                    tempValue = profileData.phone
                },
                onValueChange = { tempValue = it },
                onSave = {
                    profileData = profileData.copy(phone = tempValue)
                    editingField = null
                },
                onCancel = { editingField = null }
            )
        }

        item {
            ProfileField(
                icon = Icons.Default.LocationOn,
                label = "Location",
                value = profileData.location,
                validationStatus = validationState.location,
                isEditing = editingField == "location",
                tempValue = tempValue,
                onEditClick = {
                    editingField = "location"
                    tempValue = profileData.location
                },
                onValueChange = { tempValue = it },
                onSave = {
                    profileData = profileData.copy(location = tempValue)
                    editingField = null
                },
                onCancel = { editingField = null }
            )
        }

        item {
            ProfileField(
                icon = Icons.Default.Work,
                label = "Job Title",
                value = profileData.jobTitle,
                validationStatus = validationState.jobTitle,
                isEditing = editingField == "jobTitle",
                tempValue = tempValue,
                onEditClick = {
                    editingField = "jobTitle"
                    tempValue = profileData.jobTitle
                },
                onValueChange = { tempValue = it },
                onSave = {
                    profileData = profileData.copy(jobTitle = tempValue)
                    editingField = null
                },
                onCancel = { editingField = null }
            )
        }

        item {
            ProfileField(
                icon = Icons.Default.WorkHistory,
                label = "Experience Level",
                value = profileData.experience,
                validationStatus = validationState.experience,
                isEditing = editingField == "experience",
                tempValue = tempValue,
                onEditClick = {
                    editingField = "experience"
                    tempValue = profileData.experience
                },
                onValueChange = { tempValue = it },
                onSave = {
                    profileData = profileData.copy(experience = tempValue)
                    editingField = null
                },
                onCancel = { editingField = null }
            )
        }

        item {
            ProfileField(
                icon = Icons.Default.School,
                label = "Education",
                value = profileData.education,
                validationStatus = validationState.education,
                isEditing = editingField == "education",
                tempValue = tempValue,
                onEditClick = {
                    editingField = "education"
                    tempValue = profileData.education
                },
                onValueChange = { tempValue = it },
                onSave = {
                    profileData = profileData.copy(education = tempValue)
                    editingField = null
                },
                onCancel = { editingField = null }
            )
        }

        // Skills Card
        item {
            SkillsCard(
                skills = profileData.skills,
                validationStatus = validationState.skills
            )
        }

        // Resume Card
        item {
            ResumeCard(
                resumeName = profileData.resume,
                validationStatus = validationState.resume,
                onReplaceClick = { /* Handle resume replacement */ }
            )
        }

        // Action Buttons
        item {
            ActionButtons(
                onSubmitClick = { /* Handle submit */ },
                onSaveDraftClick = { /* Handle save draft */ }
            )
        }
    }
}

@Composable
fun ProgressCard(validationState: ValidationState) {
    val validCount = listOf(
        validationState.fullName,
        validationState.email,
        validationState.phone,
        validationState.location,
        validationState.jobTitle,
        validationState.experience,
        validationState.education,
        validationState.skills,
        validationState.resume,
        validationState.profilePhoto
    ).count { it == ValidationStatus.VALID }

    val totalFields = 10
    val completionPercentage = (validCount * 100) / totalFields

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Completion Progress",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "$completionPercentage%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = completionPercentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF4F46E5),
                trackColor = Color(0xFFE5E7EB)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$validCount of $totalFields fields completed",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
fun ProfilePhotoCard(
    profileData: ProfileData,
    validationStatus: ValidationStatus,
    onPhotoClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(80.dp)
            ) {
                if (profileData.profilePhotoUrl != null) {
                    AsyncImage(
                        model = profileData.profilePhotoUrl,
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Avatar",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF9CA3AF)
                        )
                    }
                }

                FloatingActionButton(
                    onClick = onPhotoClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp),
                    containerColor = Color(0xFF4F46E5)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Profile Photo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "Add a professional headshot",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ValidationIcon(validationStatus)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (validationStatus == ValidationStatus.MISSING) "Photo required for verification" else "Photo uploaded",
                        fontSize = 12.sp,
                        color = getValidationColor(validationStatus)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    validationStatus: ValidationStatus,
    isEditing: Boolean,
    tempValue: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onEditClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = getValidationBackgroundColor(validationStatus)
        ),
        border = BorderStroke(2.dp, getValidationBorderColor(validationStatus))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ValidationIcon(validationStatus)
                    if (!isEditing) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = onEditClick) {
                            Text(
                                text = "Edit",
                                color = Color(0xFF4F46E5),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = tempValue,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onSave,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4F46E5)
                        )
                    ) {
                        Text("Save")
                    }

                    OutlinedButton(onClick = onCancel) {
                        Text("Cancel", color = Color(0xFF6B7280))
                    }
                }
            } else {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color(0xFF1F2937)
                )
            }

            ValidationMessage(validationStatus)
        }
    }
}

@Composable
fun SkillsCard(
    skills: List<String>,
    validationStatus: ValidationStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = getValidationBackgroundColor(validationStatus)
        ),
        border = BorderStroke(2.dp, getValidationBorderColor(validationStatus))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Skills",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151)
                    )
                }
                ValidationIcon(validationStatus)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Skills chips
            skills.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { skill ->
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = skill,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Color(0xFFEEF2FF),
                                labelColor = Color(0xFF4338CA)
                            )
                        )
                    }
                }
                if (row != skills.chunked(3).last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ResumeCard(
    resumeName: String,
    validationStatus: ValidationStatus,
    onReplaceClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = getValidationBackgroundColor(validationStatus)
        ),
        border = BorderStroke(2.dp, getValidationBorderColor(validationStatus))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Resume",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151)
                    )
                }
                ValidationIcon(validationStatus)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = resumeName,
                        fontSize = 16.sp,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "PDF • 2.4 MB",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                OutlinedButton(
                    onClick = onReplaceClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4F46E5)
                    )
                ) {
                    Text("Replace")
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    onSubmitClick: () -> Unit,
    onSaveDraftClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F46E5)
            )
        ) {
            Text(
                text = "Submit for Review",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = onSaveDraftClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF374151)
            )
        ) {
            Text(
                text = "Save Draft",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ValidationIcon(status: ValidationStatus) {
    when (status) {
        ValidationStatus.VALID -> Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Valid",
            tint = Color(0xFF10B981),
            modifier = Modifier.size(16.dp)
        )
        ValidationStatus.INVALID -> Icon(
            imageVector = Icons.Default.Cancel,
            contentDescription = "Invalid",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(16.dp)
        )
        ValidationStatus.PENDING -> Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = "Pending",
            tint = Color(0xFFF59E0B),
            modifier = Modifier.size(16.dp)
        )
        ValidationStatus.MISSING -> Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Missing",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ValidationMessage(status: ValidationStatus) {
    val message = when (status) {
        ValidationStatus.INVALID -> "This field needs attention"
        ValidationStatus.PENDING -> "Validating..."
        ValidationStatus.MISSING -> "This field is required"
        ValidationStatus.VALID -> null
    }

    message?.let {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = it,
            fontSize = 12.sp,
            color = getValidationColor(status)
        )
    }
}

fun getValidationColor(status: ValidationStatus): Color {
    return when (status) {
        ValidationStatus.VALID -> Color(0xFF10B981)
        ValidationStatus.INVALID -> Color(0xFFEF4444)
        ValidationStatus.PENDING -> Color(0xFFF59E0B)
        ValidationStatus.MISSING -> Color(0xFFEF4444)
    }
}

fun getValidationBackgroundColor(status: ValidationStatus): Color {
    return when (status) {
        ValidationStatus.VALID -> Color(0xFFF0FDF4)
        ValidationStatus.INVALID -> Color(0xFFFEF2F2)
        ValidationStatus.PENDING -> Color(0xFFFFFBEB)
        ValidationStatus.MISSING -> Color(0xFFFEF2F2)
    }
}

fun getValidationBorderColor(status: ValidationStatus): Color {
    return when (status) {
        ValidationStatus.VALID -> Color(0xFFBBF7D0)
        ValidationStatus.INVALID -> Color(0xFFFECACA)
        ValidationStatus.PENDING -> Color(0xFFFEF3C7)
        ValidationStatus.MISSING -> Color(0xFFFECACA)
    }
}