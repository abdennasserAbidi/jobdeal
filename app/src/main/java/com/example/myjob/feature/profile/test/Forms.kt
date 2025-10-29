package com.example.myjob.feature.profile.test

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.CustomPhoneKit
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.invitation.candidat.FilterBottomSheet
import com.example.myjob.feature.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BasicInfoForm(
    profileViewModel: ProfileViewModel,
    list: List<NewCountry>,
    allSubjects: MutableList<Subject>,
    onSubmit: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var activatedEmailCheck by remember { mutableStateOf(false) }
    var activatedCheck by remember { mutableStateOf(false) }
    val user by profileViewModel.user.collectAsState()
    val isDateShowed by profileViewModel.isDateShowed.collectAsState()
    val isSearch by profileViewModel.isSearch.collectAsState()

    var showCountryPicker by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(NewCountry("tn", "Tunisia", 216)) }
    val isShowed by profileViewModel.isCountryShowed.collectAsState()
    val listNames by profileViewModel.listNames.collectAsState()
    val listFlagLazy = profileViewModel.listFlag.collectAsLazyPagingItems()
    val listFlag = listFlagLazy.itemSnapshotList.items

    var titleGlobal by remember { mutableStateOf("") }
    var showGlobal by remember { mutableStateOf(false) }
    var selectedItemGlobal by remember { mutableStateOf("") }
    var globalList by remember { mutableStateOf(emptyList<Int>()) }
    var action: (name: String) -> Unit = {}

    var showGender by remember { mutableStateOf(false) }
    val userGender by profileViewModel.userSex.collectAsState()
    val gendersOptions by profileViewModel.gendersOptions.collectAsState()
    val selectedGender by remember {
        mutableStateOf(
            userGender?.ifEmpty { "" })
    }

    //SITUATION
    var showSituation by remember { mutableStateOf(false) }
    val userSituation by profileViewModel.userSituation.collectAsState()
    val situationsOptions by profileViewModel.situationsOptions.collectAsState()
    val selectedSituation by remember {
        mutableStateOf(
            userSituation?.ifEmpty { "" })
    }

    //TYPE EMPLOI
    var showType by remember { mutableStateOf(false) }
    val userEmploymentTypeChoice by profileViewModel.userEmploymentTypeChoice.collectAsState()
    val typesOptions by profileViewModel.typesOptions.collectAsState()
    val selectedType by remember {
        mutableStateOf(
            userEmploymentTypeChoice?.ifEmpty { "" })
    }

    val saveUserState by profileViewModel.saveUserState.collectAsState()
    LaunchedEffect(saveUserState) {
        if (saveUserState == "saved successfully") {
            onSubmit()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Name Fields
            val userName by profileViewModel.userFullName.collectAsState()
            val isFirstNameValid by profileViewModel.isFirstNameValid.collectAsState()
            val submitEnabled by remember { derivedStateOf { isFirstNameValid } }

            FormTextField(
                value = userName,
                onValueChange = {
                    if (activatedCheck) profileViewModel.validateFullName(it)
                    profileViewModel.changeUserName(it)
                },
                label = "Full Name",
                isRequired = true
            )

            if (activatedCheck) {
                if (userName.isEmpty() || !submitEnabled) {
                    Text(
                        text = if (submitEnabled) "Valid First name" else stringResource(id = R.string.error_email),
                        color = if (submitEnabled) colorResource(id = R.color.whatsapp) else Color.Red
                    )
                }
            }

            val userEmail by profileViewModel.userEmail.collectAsState()
            val isEmailValid by profileViewModel.isEmailValid.collectAsState()
            val emailCheck by remember { derivedStateOf { isEmailValid } }

            FormTextField(
                value = userEmail ?: "",
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

            if (activatedCheck) {
                if (userEmail?.isEmpty() == true || !emailCheck) {
                    Text(
                        text = if (emailCheck) "Valid First name" else stringResource(id = R.string.error_email),
                        color = if (emailCheck) colorResource(id = R.color.whatsapp) else Color.Red
                    )
                }
            }

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
                },
                label = stringResource(id = R.string.address_text),
                leadingIcon = Icons.Default.LocationOn,
                isRequired = true
            )

            //activity sector
            val title by profileViewModel.titleGeneric.collectAsState()

            FormTextField(
                value = title,
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {},
                label = stringResource(id = R.string.activity_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    profileViewModel.changeVisibilitySearch(true)
                }
            )

            //countries
            val countries = user.country ?: ""

            FormTextField(
                value = countries,
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {
                    //docs[index] = it
                },
                label = stringResource(id = R.string.country_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    profileViewModel.changeVisibilityCountry(true)
                }
            )

            //Date
            val birthDateUser by profileViewModel.birthDateUser.collectAsState()

            FormTextField(
                value = birthDateUser ?: "",
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {},
                leadingIcon = Icons.Filled.CalendarMonth,
                label = stringResource(id = R.string.birth_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    profileViewModel.changeVisibilityDate(true)
                }
            )

            //GENDER
            val titleGender = stringResource(id = R.string.sexe_text)
            FormTextField(
                value = userGender ?: "",
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {},
                leadingIcon = Icons.Filled.Person,
                label = stringResource(id = R.string.sexe_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    titleGlobal = titleGender
                    showGender = true
                }
            )

            //Situation
            val titleSituation = stringResource(id = R.string.situation_text)
            FormTextField(
                value = userSituation ?: "",
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {},
                leadingIcon = Icons.Filled.Person,
                label = stringResource(id = R.string.situation_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    titleGlobal = titleSituation
                    showSituation = true
                }
            )

            //Type emploi
            val titleWork = stringResource(id = R.string.employment_type_choice_text)
            FormTextField(
                value = userEmploymentTypeChoice ?: "",
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {},
                leadingIcon = Icons.Filled.Person,
                label = stringResource(id = R.string.employment_type_choice_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    titleGlobal = titleWork
                    showType = true
                }
            )

            //phone
            val phone by profileViewModel.phone.collectAsState()
            val completePhone by profileViewModel.completePhone.collectAsState()

            CustomPhoneKit(
                modifier = Modifier.padding(top = 10.dp),
                selectedCountry = selectedCountry,
                defaultPhone = if (completePhone.contains(" ")) completePhone.split(" ")[1] else completePhone,
                onClick = {
                    showCountryPicker = true
                },
                onValueChanged = {
                    val phoneComplete = "+${selectedCountry.code} $it"
                    profileViewModel.changePhone(it)
                    profileViewModel.changeCompletePhone(phoneComplete)
                }
            )


            // Bio
            val bio by profileViewModel.bio.collectAsState()
            FormTextField(
                value = bio,
                onValueChange = {
                    profileViewModel.changeBio(it)
                },
                label = stringResource(id = R.string.professional_bio_text),
                placeholder = stringResource(id = R.string.tell_us_text),
                maxLines = 5,
                minLines = 3
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val fullNameValidator = profileViewModel.validateFullName(userName)
                        val addressValidator = profileViewModel.validateAddress(userAddress)
                        val emailValidator = profileViewModel.validateEmail(userEmail?:"")
                        if (!fullNameValidator || !addressValidator || !emailValidator) {
                            activatedCheck = true
                        }

                        if (fullNameValidator && addressValidator && emailValidator) {
                            profileViewModel.saveUserPersonalInfo()
                            profileViewModel.getInitialDetail()
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

    showGlobal = showGender || showSituation || showType
    if (showGender) {
        globalList = gendersOptions
        selectedItemGlobal = selectedGender ?: ""
        action = { name -> profileViewModel.changeSex(name) }
    }

    if (showSituation) {
        globalList = situationsOptions
        selectedItemGlobal = selectedSituation ?: ""
        action = { name -> profileViewModel.changeSituation(name) }
    }

    if (showType) {
        globalList = typesOptions
        selectedItemGlobal = selectedType ?: ""
        action = { name -> profileViewModel.changeEmploymentTypeChoice(name) }
    }

    if (showGlobal) {
        ModalBottomSheet(
            onDismissRequest = {
                showGender = false
                showSituation = false
                showType = false
            }
        ) {
            ItemBottomSheet(
                items = globalList,
                title = titleGlobal,
                selectedItem = selectedItemGlobal,
                onItemSelected = { name ->
                    selectedItemGlobal = name
                    //profileViewModel.changeSex(name)
                    action(name)
                    showGender = false
                    showSituation = false
                    showType = false
                }
            )
        }
    }
}

@Composable
fun ItemBottomSheet(
    items: List<Int>,
    title: String,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        items.forEach { item ->
            val text = stringResource(id = item)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemSelected(text)
                    }
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedItem == stringResource(id = item),
                    onClick = {
                        onItemSelected(text)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.whatsapp),
                        unselectedColor = colorResource(id = R.color.whatsapp)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(id = item),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalForm(
    profileViewModel: ProfileViewModel,
    onDataChange: () -> Unit
) {
    var titleGlobal by remember { mutableStateOf("") }

    var showGlobal by remember { mutableStateOf(false) }
    var selectedItemGlobal by remember { mutableStateOf("") }
    var globalList by remember { mutableStateOf(emptyList<Int>()) }
    var action: (name: String) -> Unit = {}

    var showAvailability by remember { mutableStateOf(false) }
    val availability by profileViewModel.availability.collectAsState()
    val availabilityOptions by profileViewModel.availabilityOptions.collectAsState()
    val selectedAvailability by remember {
        mutableStateOf(
            availability?.ifEmpty { "" })
    }

    var showExperienceLevel by remember { mutableStateOf(false) }
    val experienceLevel by profileViewModel.experienceLevel.collectAsState()
    val experienceOptions by profileViewModel.experienceOptions.collectAsState()
    val selectedExperience by remember {
        mutableStateOf(
            experienceLevel?.ifEmpty { "" })
    }

    val saveCandidateProfessionalState by profileViewModel.saveCandidateProfessionalState.collectAsState()

    LaunchedEffect(saveCandidateProfessionalState) {
        if (saveCandidateProfessionalState == "saved successfully") {
            onDataChange()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val onSitePreference by profileViewModel.onSitePreference.collectAsState()
        val hybridPreference by profileViewModel.hybridPreference.collectAsState()
        val remotePreference by profileViewModel.remotePreference.collectAsState()

        WorkTypePreferences(
            remoteWork = onSitePreference ?: false,
            hybridWork = hybridPreference ?: false,
            onSiteWork = remotePreference ?: false,
            onRemoteChange = {
                profileViewModel.changePreferenceSite(it)
            },
            onHybridChange = {
                profileViewModel.changePreferenceHybrid(it)
            },
            onOnSiteChange = {
                profileViewModel.changePreferenceRemote(it)
            }
        )


        // Experience Level
        val titleExpLevel = stringResource(id = R.string.experience_level_text)
        FormTextField(
            value = experienceLevel ?: "",
            borderColor = colorResource(id = R.color.whatsapp),
            onValueChange = {},
            leadingIcon = Icons.Filled.Person,
            label = stringResource(id = R.string.experience_level_text),
            isRequired = true,
            readOnly = true,
            onClick = {
                titleGlobal = titleExpLevel
                showExperienceLevel = true
            }
        )

        val titleAvailability = stringResource(id = R.string.availability_text)
        FormTextField(
            value = availability  ?: "",
            borderColor = colorResource(id = R.color.whatsapp),
            onValueChange = {},
            leadingIcon = Icons.Filled.Person,
            label = stringResource(id = R.string.availability_text),
            isRequired = true,
            readOnly = true,
            onClick = {
                titleGlobal = titleAvailability
                showAvailability = true
            }
        )

        val userSalary by profileViewModel.preferredSalary.collectAsState()

        // Salary Information
        FormTextField(
            value = userSalary ?: "",
            onValueChange = {
                profileViewModel.changePreferredSalary(it)
            },
            label = "Preferred Salary",
            keyboardType = KeyboardType.Number,
            leadingIcon = Icons.Default.TrendingUp,
            placeholder = "e.g., $100,000",
            isRequired = true
        )

        val userMedium by profileViewModel.userMedium.collectAsState()
        FormTextField(
            value = userMedium ?: "",
            onValueChange = { profileViewModel.changeMedium(it) },
            label = "Medium",
            leadingIcon = Icons.Default.Link,
            placeholder = "@yourusername"
        )

        val userGithub by profileViewModel.userGithub.collectAsState()

        FormTextField(
            value = userGithub ?: "",
            onValueChange = { profileViewModel.changeGithub(it) },
            label = "GitHub Profile",
            leadingIcon = Icons.Default.Code,
            placeholder = "github.com/yourusername"
        )

        val userPortFolio by profileViewModel.userPortFolio.collectAsState()
        FormTextField(
            value = userPortFolio ?: "",
            onValueChange = { profileViewModel.changePortFolio(it) },
            label = "Portfolio Website",
            leadingIcon = Icons.Default.Web,
            placeholder = "yourportfolio.com"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val professionalStatus = ProfessionalStatus(
                        userExperience = experienceLevel,
                        availability = availability,
                        preferredSalary = userSalary,
                        userGithub = userGithub,
                        userMedium = userMedium,
                        userPortfolio = userMedium
                    )
                    profileViewModel.saveUserProfessionalInfo(professionalStatus)
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

    showGlobal = showAvailability || showExperienceLevel
    if (showAvailability) {
        globalList = availabilityOptions
        selectedItemGlobal = selectedAvailability ?: ""
        action = { name -> profileViewModel.changeAvailability(name) }
    }

    if (showExperienceLevel) {
        globalList = experienceOptions
        selectedItemGlobal = selectedExperience ?: ""
        action = { name -> profileViewModel.changeExperienceLevel(name) }
    }

    if (showGlobal) {
        ModalBottomSheet(
            onDismissRequest = {
                showAvailability = false
                showExperienceLevel = false
            }
        ) {
            ItemBottomSheet(
                items = globalList,
                title = titleGlobal,
                selectedItem = selectedItemGlobal,
                onItemSelected = { name ->
                    selectedItemGlobal = name
                    action(name)
                    showAvailability = false
                    showExperienceLevel = false
                }
            )
        }
    }
}

@Composable
fun SkillsForm(
    profileViewModel: ProfileViewModel,
    onDataChange: () -> Unit
) {
    var newSkill by remember { mutableStateOf("") }
    var newCertification by remember { mutableStateOf("") }

    val saveCandidateSkillsState by profileViewModel.saveCandidateSkillsState.collectAsState()

    LaunchedEffect(saveCandidateSkillsState) {
        if (saveCandidateSkillsState == "saved successfully") {
            onDataChange()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val skills by profileViewModel.skills.collectAsState()

        // Skills Section
        SkillsInputSection(
            title = "Technical Skills",
            skills = skills,
            newSkill = newSkill,
            onNewSkillChange = { newSkill = it },
            onAddSkill = {
                profileViewModel.addSkills(newSkill)
                newSkill = ""
            },
            onRemoveSkill = { skill ->
                profileViewModel.removeSkills(skill)
            },
            placeholder = "e.g., Kotlin, React, Python..."
        )

        val certifications by profileViewModel.certifications.collectAsState()

        // Certifications Section
        SkillsInputSection(
            title = "Certifications",
            skills = certifications,
            newSkill = newCertification,
            onNewSkillChange = { newCertification = it },
            onAddSkill = {
                profileViewModel.addCertification(newCertification)
                newCertification = ""
            },
            onRemoveSkill = { cert ->
                profileViewModel.removeCertification(cert)
            },
            placeholder = "e.g., AWS Certified Developer..."
        )

        val languageSkills by profileViewModel.languageSkills.collectAsState()

        // Languages Section
        LanguagesSection(
            languages = languageSkills.toMutableList(),
            onLanguagesChange = {
                profileViewModel.addLanguageSkills(it)
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    profileViewModel.saveCandidateSkills()
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
    profileViewModel: ProfileViewModel,
    onDataChange: (CandidateFormData) -> Unit,
    onNavigateToHome: () -> Unit
) {
    var newPreferredRole by remember { mutableStateOf("") }
    var newPreferredIndustry by remember { mutableStateOf("") }

    val saveCompletedState by profileViewModel.saveCompletedState.collectAsState()

    LaunchedEffect(saveCompletedState) {
        if (saveCompletedState == "saved successfully") {
            onNavigateToHome()
        }
    }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    profileViewModel.saveIsCompletedProfileCandidate()
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