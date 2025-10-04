package com.example.myjob.feature.profile.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myjob.R
import com.example.myjob.feature.profile.ProfileViewModel

@Composable
fun BasicInfoForm(
    formData: CandidateFormData,
    profileViewModel: ProfileViewModel,
    onDataChange: (CandidateFormData) -> Unit
) {
    var activatedCheck by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Name Fields
        val userName by profileViewModel.userFullName.collectAsState()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = formData.firstName,
                onValueChange = { onDataChange(formData.copy(firstName = it)) },
                label = "First Name",
                modifier = Modifier.weight(1f),
                isRequired = true
            )
            FormTextField(
                value = formData.lastName,
                onValueChange = { onDataChange(formData.copy(lastName = it)) },
                label = "Last Name",
                modifier = Modifier.weight(1f),
                isRequired = true
            )
        }

        // Contact Information
        val userEmail by profileViewModel.userEmail.collectAsState()
        val isEmailValid by profileViewModel.isEmailValid.collectAsState()
        val emailCheck by remember { derivedStateOf { isEmailValid } }

        FormTextField(
            value = userEmail,
            borderColor = if (activatedCheck && !emailCheck) Color.Red else colorResource(
                id = R.color.whatsapp
            ),
            onValueChange = {
                if (activatedCheck) profileViewModel.validateEmail(it)
                profileViewModel.changeAddressMail(it)
            },
            label = "Email",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Email,
            isRequired = true
        )

        val phone by profileViewModel.phone.collectAsState()
        val completePhone by profileViewModel.completePhone.collectAsState()

        FormTextField(
            value = formData.phone,
            onValueChange = { onDataChange(formData.copy(phone = it)) },
            label = "Phone Number",
            keyboardType = KeyboardType.Phone,
            leadingIcon = Icons.Default.Phone,
            isRequired = true
        )

        // Professional Title
        /*FormTextField(
            value = formData.position,
            onValueChange = { onDataChange(formData.copy(position = it)) },
            label = "Current Position/Title",
            leadingIcon = Icons.Default.Work,
            isRequired = true
        )

        FormTextField(
            value = formData.currentCompany,
            onValueChange = { onDataChange(formData.copy(currentCompany = it)) },
            label = "Current Company",
            leadingIcon = Icons.Default.Business
        )*/

        val userAddress by profileViewModel.userAddress.collectAsState()
        val isAddressValid by profileViewModel.isAddressValid.collectAsState()
        val addressCheck by remember { derivedStateOf { isAddressValid } }

        FormTextField(
            value = userAddress,
            borderColor = if (activatedCheck && !addressCheck) Color.Red else colorResource(
                id = R.color.whatsapp
            ),
            onValueChange = {
                if (activatedCheck) profileViewModel.validateAddress(it)
                profileViewModel.changeAddress(it)
                //onDataChange(formData.copy(location = it))
            },
            label = stringResource(id = R.string.address_text),
            leadingIcon = Icons.Default.LocationOn,
            isRequired = true
        )

        // Bio
        FormTextField(
            value = formData.bio,
            onValueChange = { onDataChange(formData.copy(bio = it)) },
            label = "Professional Bio",
            placeholder = "Tell us about your professional background, expertise, and career goals...",
            maxLines = 5,
            minLines = 3
        )

        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val fullNameValidator = profileViewModel.validateFullName(userName)
                    val addressValidator = profileViewModel.validateAddress(userAddress)
                    val emailValidator = profileViewModel.validateEmail(userEmail)
                    if (!fullNameValidator || !addressValidator || !emailValidator) activatedCheck = true

                    if (fullNameValidator && addressValidator && emailValidator) {

                        profileViewModel.saveUserPersonalInfo()
                        profileViewModel.getInitialDetail()
                        //navController.popBackStack(Screen.ProfileScreen.route, false)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.whatsapp)
                )
            ) {
                Text(stringResource(id = R.string.save_text))
            }
        }
    }
}

@Composable
fun ProfessionalForm(
    formData: CandidateFormData,
    onDataChange: (CandidateFormData) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Experience Level
        ExperienceSelector(
            selectedExperience = formData.experience,
            onExperienceSelected = { onDataChange(formData.copy(experience = it)) }
        )

        // Salary Information
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = formData.currentSalary,
                onValueChange = { onDataChange(formData.copy(currentSalary = it)) },
                label = "Current Salary",
                keyboardType = KeyboardType.Number,
                leadingIcon = Icons.Default.AttachMoney,
                modifier = Modifier.weight(1f),
                placeholder = "e.g., $80,000"
            )
            FormTextField(
                value = formData.expectedSalary,
                onValueChange = { onDataChange(formData.copy(expectedSalary = it)) },
                label = "Expected Salary",
                keyboardType = KeyboardType.Number,
                leadingIcon = Icons.Default.TrendingUp,
                modifier = Modifier.weight(1f),
                placeholder = "e.g., $100,000",
                isRequired = true
            )
        }

        // Availability
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AvailabilitySelector(
                selectedAvailability = formData.availability,
                onAvailabilitySelected = { onDataChange(formData.copy(availability = it)) },
                modifier = Modifier.weight(1f)
            )

            NoticePeriodSelector(
                selectedNoticePeriod = formData.noticePeriod,
                onNoticePeriodSelected = { onDataChange(formData.copy(noticePeriod = it)) },
                modifier = Modifier.weight(1f)
            )
        }

        FormTextField(
            value = formData.medium,
            onValueChange = { onDataChange(formData.copy(medium = it)) },
            label = "Medium",
            leadingIcon = Icons.Default.Link,
            placeholder = "@yourusername"
        )

        FormTextField(
            value = formData.github,
            onValueChange = { onDataChange(formData.copy(github = it)) },
            label = "GitHub Profile",
            leadingIcon = Icons.Default.Code,
            placeholder = "github.com/yourusername"
        )

        FormTextField(
            value = formData.portfolio,
            onValueChange = { onDataChange(formData.copy(portfolio = it)) },
            label = "Portfolio Website",
            leadingIcon = Icons.Default.Web,
            placeholder = "yourportfolio.com"
        )

        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(0.5f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.whatsapp)
                )
            ) {
                Text(stringResource(id = R.string.save_text))
            }
        }
    }
}

@Composable
fun SkillsForm(
    formData: CandidateFormData,
    onDataChange: (CandidateFormData) -> Unit
) {
    var newSkill by remember { mutableStateOf("") }
    var newCertification by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Skills Section
        SkillsInputSection(
            title = "Technical Skills",
            skills = formData.skills,
            newSkill = newSkill,
            onNewSkillChange = { newSkill = it },
            onAddSkill = {
                if (newSkill.isNotBlank() && !formData.skills.contains(newSkill)) {
                    formData.skills.add(newSkill)
                    onDataChange(formData)
                    newSkill = ""
                }
            },
            onRemoveSkill = { skill ->
                formData.skills.remove(skill)
                onDataChange(formData)
            },
            placeholder = "e.g., Kotlin, React, Python..."
        )

        // Certifications Section
        SkillsInputSection(
            title = "Certifications",
            skills = formData.certifications,
            newSkill = newCertification,
            onNewSkillChange = { newCertification = it },
            onAddSkill = {
                if (newCertification.isNotBlank() && !formData.certifications.contains(
                        newCertification
                    )
                ) {
                    formData.certifications.add(newCertification)
                    onDataChange(formData)
                    newCertification = ""
                }
            },
            onRemoveSkill = { cert ->
                formData.certifications.remove(cert)
                onDataChange(formData)
            },
            placeholder = "e.g., AWS Certified Developer..."
        )

        // Languages Section
        LanguagesSection(
            languages = formData.languages,
            onLanguagesChange = {
                formData.languages.clear()
                formData.languages.addAll(it)
                onDataChange(formData)
            }
        )
    }
}

@Composable
fun ExperienceForm(
    formData: CandidateFormData,
    onDataChange: (CandidateFormData) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormSectionHeader(
            title = "Work Experience",
            subtitle = "Your professional journey"
        )

        // Add Experience Button
        OutlinedButton(
            onClick = {
                formData.workExperience.add(WorkExperienceForm())
                onDataChange(formData)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Experience")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Work Experience")
        }

        // Experience Items
        formData.workExperience.forEachIndexed { index, experience ->
            WorkExperienceCard(
                experience = experience,
                onExperienceChange = { updatedExp ->
                    formData.workExperience[index] = updatedExp
                    onDataChange(formData)
                },
                onRemove = {
                    formData.workExperience.removeAt(index)
                    onDataChange(formData)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Projects Section
        /*FormSectionHeader(
            title = "Projects",
            subtitle = "Showcase your work"
        )*/

        // Add Project Button
        /*OutlinedButton(
            onClick = {
                formData.projects.add(ProjectForm())
                onDataChange(formData)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Project")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Project")
        }*/

        // Project Items
        /*formData.projects.forEachIndexed { index, project ->
            ProjectCard(
                project = project,
                onProjectChange = { updatedProject ->
                    formData.projects[index] = updatedProject
                    onDataChange(formData)
                },
                onRemove = {
                    formData.projects.removeAt(index)
                    onDataChange(formData)
                }
            )
        }*/
    }
}

@Composable
fun EducationFormSection(
    formData: CandidateFormData,
    onDataChange: (CandidateFormData) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormSectionHeader(
            title = "Education",
            subtitle = "Your academic background"
        )

        // Add Education Button
        OutlinedButton(
            onClick = {
                formData.education.add(EducationForm())
                onDataChange(formData)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Education")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Education")
        }

        // Education Items
        formData.education.forEachIndexed { index, education ->
            EducationCard(
                education = education,
                onEducationChange = { updatedEdu ->
                    formData.education[index] = updatedEdu
                    onDataChange(formData)
                },
                onRemove = {
                    formData.education.removeAt(index)
                    onDataChange(formData)
                }
            )
        }
    }
}

@Composable
fun PreferencesForm(
    formData: CandidateFormData,
    onDataChange: (CandidateFormData) -> Unit
) {
    var newPreferredRole by remember { mutableStateOf("") }
    var newPreferredIndustry by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Work Type Preferences
        WorkTypePreferences(
            remoteWork = formData.remoteWork,
            hybridWork = formData.hybridWork,
            onSiteWork = formData.onSiteWork,
            onRemoteChange = { onDataChange(formData.copy(remoteWork = it)) },
            onHybridChange = { onDataChange(formData.copy(hybridWork = it)) },
            onOnSiteChange = { onDataChange(formData.copy(onSiteWork = it)) }
        )

        // Travel & Working Hours
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TravelWillingnessSelector(
                selectedTravel = formData.travelWillingness,
                onTravelSelected = { onDataChange(formData.copy(travelWillingness = it)) },
                modifier = Modifier.weight(1f)
            )

            FormTextField(
                value = formData.preferredWorkingHours,
                onValueChange = { onDataChange(formData.copy(preferredWorkingHours = it)) },
                label = "Preferred Hours",
                placeholder = "9 AM - 5 PM",
                modifier = Modifier.weight(1f)
            )
        }

        // Preferred Roles
        SkillsInputSection(
            title = "Preferred Job Roles",
            skills = formData.preferredRoles,
            newSkill = newPreferredRole,
            onNewSkillChange = { newPreferredRole = it },
            onAddSkill = {
                if (newPreferredRole.isNotBlank() && !formData.preferredRoles.contains(
                        newPreferredRole
                    )
                ) {
                    formData.preferredRoles.add(newPreferredRole)
                    onDataChange(formData)
                    newPreferredRole = ""
                }
            },
            onRemoveSkill = { role ->
                formData.preferredRoles.remove(role)
                onDataChange(formData)
            },
            placeholder = "e.g., Senior Developer, Tech Lead..."
        )

        // Preferred Industries
        SkillsInputSection(
            title = "Preferred Industries",
            skills = formData.preferredIndustries,
            newSkill = newPreferredIndustry,
            onNewSkillChange = { newPreferredIndustry = it },
            onAddSkill = {
                if (newPreferredIndustry.isNotBlank() && !formData.preferredIndustries.contains(
                        newPreferredIndustry
                    )
                ) {
                    formData.preferredIndustries.add(newPreferredIndustry)
                    onDataChange(formData)
                    newPreferredIndustry = ""
                }
            },
            onRemoveSkill = { industry ->
                formData.preferredIndustries.remove(industry)
                onDataChange(formData)
            },
            placeholder = "e.g., Technology, Healthcare, Finance..."
        )

        // Company Size Preference
        CompanySizeSelector(
            selectedSize = formData.companySizePreference,
            onSizeSelected = { onDataChange(formData.copy(companySizePreference = it)) }
        )

        // Personal Information
        FormSectionHeader(
            title = "Additional Information",
            subtitle = "Optional details"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = formData.nationality,
                onValueChange = { onDataChange(formData.copy(nationality = it)) },
                label = "Nationality",
                modifier = Modifier.weight(1f)
            )
            FormTextField(
                value = formData.visaStatus,
                onValueChange = { onDataChange(formData.copy(visaStatus = it)) },
                label = "Visa Status",
                placeholder = "e.g., US Citizen, H1B...",
                modifier = Modifier.weight(1f)
            )
        }

        FormTextField(
            value = formData.address,
            onValueChange = { onDataChange(formData.copy(address = it)) },
            label = "Full Address",
            placeholder = "Street, City, State, Country"
        )
    }
}