package com.example.myjob.feature.profile.test

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.feature.profile.ProfileViewModel
import com.example.myjob.feature.signup.CustomDropdownMenu
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPreferenceSelector(
    modifier: Modifier = Modifier,
    workOptions: List<String>,
    selectedPreference: String,
    onWorkPreferenceSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    ExposedDropdownMenuBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPreference,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select work preference") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = whatsAppGreen,
                focusedLabelColor = whatsAppGreen
            )
        )

        ExposedDropdownMenu(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            workOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onWorkPreferenceSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkRoleSelector(
    modifier: Modifier = Modifier,
    workOptions: List<String>,
    selectedPreference: String,
    onWorkRoleSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPreference,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select other preference") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = whatsAppGreen,
                focusedLabelColor = whatsAppGreen
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            workOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onWorkRoleSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceSelector(
    selectedExperience: String,
    onExperienceSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val experienceOptions = listOf(
        "Entry Level (0-1 years)",
        "Junior (1-3 years)",
        "Mid-Level (3-5 years)",
        "Senior (5-8 years)",
        "Lead/Principal (8+ years)",
        "Executive (10+ years)"
    )

    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedExperience,
            onValueChange = {},
            readOnly = true,
            label = { Text("Experience Level *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = whatsAppGreen,
                focusedLabelColor = whatsAppGreen
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            experienceOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onExperienceSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilitySelector(
    selectedAvailability: String,
    onAvailabilitySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val availabilityOptions = listOf(
        "Available immediately",
        "Available in 1 week",
        "Available in 2 weeks",
        "Available in 1 month",
        "Available in 2 months",
        "Available in 3+ months"
    )

    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedAvailability,
            onValueChange = {},
            readOnly = true,
            label = { Text("Availability *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = whatsAppGreen,
                focusedLabelColor = whatsAppGreen
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availabilityOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onAvailabilitySelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticePeriodSelector(
    selectedNoticePeriod: String,
    onNoticePeriodSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    var expanded by remember { mutableStateOf(false) }
    val noticePeriodOptions = listOf(
        "Immediate",
        "1 week",
        "2 weeks",
        "1 month",
        "2 months",
        "3 months"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedNoticePeriod,
            onValueChange = {},
            readOnly = true,
            label = { Text("Notice Period") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = whatsAppGreen,
                focusedLabelColor = whatsAppGreen
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            noticePeriodOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onNoticePeriodSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SkillsInputSection(
    title: String,
    skills: List<String>,
    newSkill: String,
    onNewSkillChange: (String) -> Unit,
    onAddSkill: () -> Unit,
    onRemoveSkill: (String) -> Unit,
    placeholder: String
) {

    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormTextField(
                value = newSkill,
                onValueChange = onNewSkillChange,
                label = "",
                placeholder = placeholder,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onAddSkill,
                enabled = newSkill.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = whatsAppGreen
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

        // Skills Display
        if (skills.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(skills) { skill ->
                    SkillChipRemovable(
                        skill = skill,
                        onRemove = { onRemoveSkill(skill) }
                    )
                }
            }
        }
    }
}

@Composable
fun SkillChipRemovable(
    skill: String,
    onRemove: () -> Unit
) {

    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = whatsAppGreen,
        modifier = Modifier.clickable { onRemove() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = skill,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun LanguagesSection(
    languages: MutableList<LanguageForm>,
    onLanguagesChange: (List<LanguageForm>) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.language_title_text),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            TextButton(
                onClick = {
                    languages.add(LanguageForm())
                    onLanguagesChange(languages.toList())
                }
            ) {
                Icon(Icons.Default.Add,
                    tint = colorResource(id = R.color.whatsapp),
                    contentDescription = "Add Language")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.language_add_text),
                    color = colorResource(id = R.color.whatsapp)
                )
            }
        }

        languages.forEachIndexed { index, language ->
            LanguageCard(
                language = language,
                onLanguageChange = { updatedLanguage ->
                    languages[index] = updatedLanguage
                    onLanguagesChange(languages.toList())
                },
                onRemove = {
                    languages.removeAt(index)
                    onLanguagesChange(languages.toList())
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageCard(
    language: LanguageForm,
    onLanguageChange: (LanguageForm) -> Unit,
    onRemove: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val basic = stringResource(id = R.string.basic_text)
    val conversational = stringResource(id = R.string.conversational_text)
    val fluent = stringResource(id = R.string.fluent_text)
    val native = stringResource(id = R.string.native_text)
    val proficiencyLevels = listOf(basic, conversational, fluent, native)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.language_title_text),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormTextField(
                    value = language.name,
                    onValueChange = { onLanguageChange(language.copy(name = it)) },
                    label = stringResource(id = R.string.language_title_text),
                    placeholder = "English...",
                    modifier = Modifier.weight(1f)
                )

                /*keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeActions
            ),
            keyboardActions = keyboardActions*/

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = language.proficiency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(id = R.string.proficiency_text)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = colorResource(id = R.color.whatsapp),
                            focusedBorderColor = colorResource(id = R.color.whatsapp),
                            cursorColor = Color.Black
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        proficiencyLevels.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    onLanguageChange(language.copy(proficiency = level))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkExperienceCard(
    experience: Experience,
    profileViewModel: ProfileViewModel,
    onExperienceTypeChange: (String) -> Unit,
    onCompanyNameChange: () -> Unit,
    onFreelanceSalaryChange: (String) -> Unit,
    onHourPaymentChange: (Boolean) -> Unit,
    onDayPaymentChange: (Boolean) -> Unit,
    onProjectPaymentChange: (Boolean) -> Unit,
    onDateStartChange: () -> Unit,
    onDateEndChange: () -> Unit,
    onChangeCurrent: (Boolean) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    addTechnologiesChanged: (String) -> Unit,
    onRemove: () -> Unit,
    onSubmit: () -> Unit,
) {
    var newTechnology by remember { mutableStateOf("") }
    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    var activatedCheck by remember { mutableStateOf(false) }
    var isContract by remember(experience.type) {
        mutableStateOf(experience.type?.isNotEmpty() == true && experience.type == "Contract")
    }

    var isFreelance by remember(experience.type) {
        mutableStateOf(experience.type?.isNotEmpty() == true && experience.type != "Contract")
    }

    var isPerHour by remember {
        mutableStateOf(experience.perHourPaymentMethod)
    }

    var isPerDay by remember {
        mutableStateOf(experience.perDayPaymentMethod)
    }

    var isPerProject by remember {
        mutableStateOf(experience.perProjectPaymentMethod)
    }

    var isCurrent by remember { mutableStateOf(experience.current) }
    val textSalaryFreelance = if (experience.freelanceSalary == 0) ""
    else "${experience.freelanceSalary}"

    var freelanceSalary by remember { mutableStateOf(textSalaryFreelance) }
    var title by remember {
        mutableStateOf(experience.title ?: "")
    }

    var type by remember {
        mutableStateOf(experience.type ?: "")
    }

    var companyName by remember {
        mutableStateOf(experience.companyName ?: "")
    }

    val workValidator = type.isNotEmpty()
    val companyNameValidator = experience.companyName?.isNotEmpty() == true
    val dateStartValidator = experience.dateStart?.isNotEmpty() == true
    val dateEndValidator = if (isCurrent == true) true else experience.dateEnd?.isNotEmpty() == true
    val titleValidator = title.isNotEmpty()
    val freelanceSalaryValidator = freelanceSalary.isNotEmpty()
    val paymentMethodValidator = isPerDay || isPerHour || isPerProject

    val isSubmitAction by GlobalEntries.isSubmitAction.collectAsState()

    LaunchedEffect(isSubmitAction) {
        if (isSubmitAction) {

            val isAllCheck = if (type == "Freelance" && isCurrent == true) {
                workValidator
                        && paymentMethodValidator
                        && freelanceSalaryValidator
                        && companyNameValidator
                        && dateStartValidator
                        && titleValidator
            } else if (type == "Freelance") {
                workValidator
                        && paymentMethodValidator
                        && freelanceSalaryValidator
                        && companyNameValidator
                        && dateStartValidator
                        && dateEndValidator
                        && titleValidator
            } else if (type == "Contract" && isCurrent == true) {
                workValidator
                        && companyNameValidator
                        && dateStartValidator
                        && dateEndValidator
                        && titleValidator
            } else if (type == "Contract") {
                workValidator
                        && companyNameValidator
                        && dateStartValidator
                        && titleValidator
            } else {
                workValidator
                        && paymentMethodValidator
                        && freelanceSalaryValidator
                        && companyNameValidator
                        && dateStartValidator
                        && titleValidator
            }

            val hasError = if (type == "Freelance" && isCurrent == true) {
                !workValidator ||
                        !paymentMethodValidator ||
                        !freelanceSalaryValidator ||
                        !companyNameValidator ||
                        !dateStartValidator ||
                        !titleValidator
            } else if (type == "Freelance") {
                !workValidator ||
                        !paymentMethodValidator ||
                        !freelanceSalaryValidator ||
                        !companyNameValidator ||
                        !dateStartValidator ||
                        !dateEndValidator ||
                        !titleValidator
            } else if (type == "Contract" && isCurrent == true) {
                        !workValidator ||
                        !companyNameValidator ||
                        !dateStartValidator ||
                        !titleValidator
            } else if (type == "Contract") {
                !workValidator ||
                        !companyNameValidator ||
                        !dateStartValidator ||
                        !dateEndValidator ||
                        !titleValidator
            } else {
                !workValidator ||
                        !paymentMethodValidator ||
                        !freelanceSalaryValidator ||
                        !companyNameValidator ||
                        !dateStartValidator ||
                        !titleValidator
            }

            if (hasError) {
                activatedCheck = true
                GlobalEntries.isSubmitAction.update { false }
                profileViewModel.triggerExperienceCheck(false)
            }

            if (isAllCheck) {
                onSubmit()
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Work Experience",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                }
            }

            WorkTypeContract(
                contract = isContract,
                freelance = isFreelance,
                onContractChange = {
                    isContract = it
                    isFreelance = false
                    type = "Contract"
                    onExperienceTypeChange("Contract")
                },
                onFreelanceChange = {
                    isFreelance = it
                    isContract = false
                    type = "Freelance"
                    onExperienceTypeChange("Freelance")
                }
            )

            if (activatedCheck && !workValidator) {
                Text(
                    text = stringResource(id = R.string.error_work_type),
                    color = Color.Red
                )
            }

            if (isFreelance) {
                WorkFeePreferences(
                    perHour = isPerHour,
                    perDay = isPerDay,
                    perProject = isPerProject,
                    onHourChange = {
                        isPerHour = it
                        isPerDay = false
                        isPerProject = false
                        onHourPaymentChange(it)
                    },
                    onDayChange = {
                        isPerHour = false
                        isPerDay = it
                        isPerProject = false
                        onDayPaymentChange(it)
                    },
                    onProjectChange = {
                        isPerHour = false
                        isPerDay = false
                        isPerProject = it
                        onProjectPaymentChange(it)
                    }
                )

                if (activatedCheck && !paymentMethodValidator) {
                    Text(
                        text = stringResource(id = R.string.error_work_type),
                        color = Color.Red
                    )
                }

                FormTextField(
                    value = freelanceSalary,
                    borderColor = if (activatedCheck && freelanceSalary.isEmpty()) Color.Red else colorResource(
                        id = R.color.whatsapp
                    ),
                    onValueChange = {
                        if (activatedCheck) it.isNotEmpty()
                        if (it.isNotEmpty()) {
                            freelanceSalary = it
                            onFreelanceSalaryChange(it)
                        }
                    },
                    label = stringResource(id = R.string.freelance_salary_text),
                    isRequired = true
                )
            }

            FormTextField(
                value = experience.companyName ?: "",
                borderColor = if (activatedCheck && experience.companyName?.isNotEmpty() == false) Color.Red else colorResource(
                    id = R.color.whatsapp
                ),
                onValueChange = {

                },
                label = stringResource(id = R.string.company_name_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    onCompanyNameChange()
                }
            )

            FormTextField(
                value = title,
                borderColor = if (activatedCheck && title.isEmpty()) Color.Red else colorResource(
                    id = R.color.whatsapp
                ),
                onValueChange = {
                    if (activatedCheck) it.isNotEmpty()
                    title = it
                    onTitleChange(it)
                },
                label = "Position/Title",
                isRequired = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val modifier = if (isCurrent == false) Modifier.weight(1f)
                else Modifier.fillMaxWidth()

                FormTextField(
                    value = experience.dateStart ?: "",
                    borderColor = if (activatedCheck && experience.dateStart?.isNotEmpty() == false) Color.Red else colorResource(
                        id = R.color.whatsapp
                    ),
                    onValueChange = {},
                    label = stringResource(id = R.string.start_date_text),
                    placeholder = "MM/YYYY",
                    modifier = modifier,
                    isRequired = true,
                    readOnly = true,
                    onClick = {
                        onDateStartChange()
                    }
                )

                if (isCurrent == false) {
                    FormTextField(
                        value = experience.dateEnd ?: "",
                        borderColor = if (activatedCheck && experience.dateEnd?.isNotEmpty() == false) Color.Red else colorResource(
                            id = R.color.whatsapp
                        ),
                        onValueChange = {},
                        label = stringResource(id = R.string.end_date_text),
                        placeholder = "MM/YYYY",
                        modifier = Modifier.weight(1f),
                        isRequired = true,
                        readOnly = true,
                        onClick = {
                            onDateEndChange()
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCurrent ?: false,
                    onCheckedChange = {
                        isCurrent = it
                        onChangeCurrent(it)
                    },
                    colors = CheckboxDefaults.colors(checkedColor = whatsAppGreen)
                )
                Text(
                    text = "I currently work here",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            var description by remember {
                mutableStateOf(experience.description ?: "")
            }

            FormTextField(
                value = description,
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {
                    description = it
                    onDescriptionChange(it)
                },
                label = "Job Description",
                placeholder = "Describe your responsibilities and achievements...",
                maxLines = 4,
                minLines = 2
            )

            // Technologies Section
            Text(
                text = "Technologies Used",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormTextField(
                    value = newTechnology,
                    onValueChange = { newTechnology = it },
                    label = "",
                    placeholder = "Add technology...",
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        addTechnologiesChanged(newTechnology)
                        newTechnology = ""
                    },
                    enabled = newTechnology.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = whatsAppGreen)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

            if (experience.listSkills?.isNotEmpty() == true) {
                val listTech = experience.listSkills?.toMutableList() ?: mutableListOf()
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(listTech) { tech ->
                        SkillChipRemovable(
                            skill = tech,
                            onRemove = {

                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun EducationCard(
    education: Educations,
    onChangeDegree: () -> Unit,
    onChangeFieldOfStudy: () -> Unit,
    onChangeGrade: (String) -> Unit,
    onChangeInstitution: () -> Unit,
    onSubmit: () -> Unit,
    onRemove: () -> Unit
) {

    var activatedCheck by remember { mutableStateOf(false) }

    val schoolNameValidator = education.schoolName?.isNotEmpty() == true
    val degreeValidator = education.degree?.isNotEmpty() == true
    val fieldStudyValidator = education.fieldStudy?.isNotEmpty() == true

    val isSubmitEducationAction by GlobalEntries.isSubmitEducationAction.collectAsState()

    LaunchedEffect(isSubmitEducationAction) {
        if (isSubmitEducationAction) {

            if (!schoolNameValidator || !degreeValidator || !fieldStudyValidator) {
                activatedCheck = true
                GlobalEntries.isSubmitEducationAction.update { false }
            }

            if (schoolNameValidator && degreeValidator && fieldStudyValidator) {
                onSubmit()
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.education_text),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                }
            }

            FormTextField(
                value = education.schoolName ?: "",
                borderColor = if (activatedCheck && education.schoolName?.isNotEmpty() == false) Color.Red else colorResource(
                    id = R.color.whatsapp
                ),
                onValueChange = { },
                label = "Institution/University",
                isRequired = true,
                readOnly = true,
                onClick = {
                    onChangeInstitution()
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormTextField(
                    value = education.degree ?: "",
                    borderColor = if (activatedCheck && education.degree?.isNotEmpty() == false) Color.Red else colorResource(
                        id = R.color.whatsapp
                    ),
                    onValueChange = { },
                    label = stringResource(id = R.string.degree_text),
                    placeholder = "Bachelor's, Master's...",
                    modifier = Modifier.weight(1f),
                    isRequired = true,
                    readOnly = true,
                    onClick = {
                        onChangeDegree()
                    }
                )

                FormTextField(
                    value = education.fieldStudy ?: "",
                    borderColor = if (activatedCheck && education.fieldStudy?.isNotEmpty() == false) Color.Red else colorResource(
                        id = R.color.whatsapp
                    ),
                    onValueChange = { },
                    label = "Field of Study",
                    placeholder = "Computer Science...",
                    modifier = Modifier.weight(1f),
                    isRequired = true,
                    readOnly = true,
                    onClick = {
                        onChangeFieldOfStudy()
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                var year by remember { mutableStateOf("") }

                FormTextField(
                    value = year,
                    onValueChange = {
                         year = it
                    },
                    label = "Graduation Year",
                    placeholder = "2023",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                var grade by remember { mutableStateOf(education.grade ?: "") }

                FormTextField(
                    value = grade,
                    onValueChange = {
                        grade = it
                        onChangeGrade(it)
                    },
                    label = "GPA (Optional)",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WorkTypePreferences(
    remoteWork: Boolean,
    hybridWork: Boolean,
    onSiteWork: Boolean,
    onRemoteChange: (Boolean) -> Unit,
    onHybridChange: (Boolean) -> Unit,
    onOnSiteChange: (Boolean) -> Unit
) {
    Column {
        Text(
            text = "Work Type Preferences",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WorkTypeChip(
                text = "Remote",
                icon = Icons.Default.Home,
                isSelected = remoteWork,
                onSelectionChange = onRemoteChange,
                modifier = Modifier.weight(1f)
            )

            WorkTypeChip(
                text = "Hybrid",
                icon = Icons.Default.Business,
                isSelected = hybridWork,
                onSelectionChange = onHybridChange,
                modifier = Modifier.weight(1f)
            )

            WorkTypeChip(
                text = "On-site",
                icon = Icons.Default.LocationOn,
                isSelected = onSiteWork,
                onSelectionChange = onOnSiteChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProjectCard(
    project: ProjectForm,
    onProjectChange: (ProjectForm) -> Unit,
    onRemove: () -> Unit
) {
    var newTechnology by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Project",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Red
                    )
                }
            }

            FormTextField(
                value = project.name,
                onValueChange = { onProjectChange(project.copy(name = it)) },
                label = "Project Name",
                isRequired = true
            )

            FormTextField(
                value = project.description,
                onValueChange = { onProjectChange(project.copy(description = it)) },
                label = "Project Description",
                placeholder = "Describe what you built and your role...",
                maxLines = 3,
                minLines = 2
            )

            FormTextField(
                value = project.link,
                onValueChange = { onProjectChange(project.copy(link = it)) },
                label = "Project Link",
                placeholder = "GitHub, live demo, etc."
            )

            // Technologies Section
            Text(
                text = "Technologies Used",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            val whatsAppGreen = colorResource(id = R.color.whatsapp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormTextField(
                    value = newTechnology,
                    onValueChange = { newTechnology = it },
                    label = "",
                    placeholder = "Add technology...",
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        if (newTechnology.isNotBlank() && !project.technologies.contains(
                                newTechnology
                            )
                        ) {
                            project.technologies.add(newTechnology)
                            onProjectChange(project)
                            newTechnology = ""
                        }
                    },
                    enabled = newTechnology.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = whatsAppGreen)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

            if (project.technologies.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(project.technologies) { tech ->
                        SkillChipRemovable(
                            skill = tech,
                            onRemove = {
                                project.technologies.remove(tech)
                                onProjectChange(project)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkFeePreferences(
    perHour: Boolean,
    perDay: Boolean,
    perProject: Boolean,
    onHourChange: (Boolean) -> Unit,
    onDayChange: (Boolean) -> Unit,
    onProjectChange: (Boolean) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.fee_freelance_text),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            WorkTypeChip(
                text = stringResource(id = R.string.per_hour_text),
                icon = Icons.Default.AccessTime,
                isSelected = perHour,
                onSelectionChange = {
                    onHourChange(it)
                },
                modifier = Modifier.weight(1f)
            )

            WorkTypeChip(
                text = stringResource(id = R.string.per_day_text),
                icon = Icons.Default.CalendarToday,
                isSelected = perDay,
                onSelectionChange = {
                    onDayChange(it)
                },
                modifier = Modifier.weight(1f)
            )

            WorkTypeChip(
                text = stringResource(id = R.string.per_project_text),
                icon = Icons.Default.Dashboard,
                isSelected = perProject,
                onSelectionChange = {
                    onProjectChange(it)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun WorkTypeContract(
    contract: Boolean,
    freelance: Boolean,
    onContractChange: (Boolean) -> Unit,
    onFreelanceChange: (Boolean) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.fee_text),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WorkTypeChip(
                text = stringResource(id = R.string.type1_text),
                icon = Icons.Default.Work,
                isSelected = contract,
                onSelectionChange = onContractChange,
                modifier = Modifier.weight(1f)
            )

            WorkTypeChip(
                text = stringResource(id = R.string.type2_text),
                icon = Icons.Default.Person,
                isSelected = freelance,
                onSelectionChange = onFreelanceChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun WorkTypeChip(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    Card(
        modifier = modifier
            .clickable { onSelectionChange(!isSelected) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) whatsAppGreen else Color.White
        ),
        border = BorderStroke(
            2.dp,
            whatsAppGreen
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = if (!isSelected) whatsAppGreen else Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (!isSelected) whatsAppGreen else Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelWillingnessSelector(
    selectedTravel: String,
    onTravelSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val travelOptions = listOf(
        "No travel",
        "Occasional (10-25%)",
        "Regular (25-50%)",
        "Frequent (50%+)"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedTravel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Travel Willingness") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            travelOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onTravelSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanySizeSelector(
    selectedSize: String,
    onSizeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val sizeOptions = listOf(
        "Startup (1-50 employees)",
        "Small (51-200 employees)",
        "Medium (201-1000 employees)",
        "Large (1001-5000 employees)",
        "Enterprise (5000+ employees)"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedSize,
            onValueChange = {},
            readOnly = true,
            label = { Text("Preferred Company Size") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sizeOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSizeSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}