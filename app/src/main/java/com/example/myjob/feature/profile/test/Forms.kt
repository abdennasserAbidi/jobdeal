package com.example.myjob.feature.profile.test

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.CustomPhoneKit
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.listEducations
import com.example.myjob.common.GlobalEntries.listExperience
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.FilterType
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.ProfessionalStatus
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.invitation.candidat.FilterBottomSheet
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.ProfileViewModel
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BasicInfoForm(
    profileViewModel: ProfileViewModel,
    list: List<NewCountry>,
    allSubjects: MutableList<Subject>,
    onBirthDateChange: () -> Unit,
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

            val userAddress by profileViewModel.userAddress.collectAsState()
            val isAddressValid by profileViewModel.isAddressValid.collectAsState()
            val addressCheck by remember { derivedStateOf { isAddressValid } }

            //activity sector
            val title by profileViewModel.titleGeneric.collectAsState()
            val countries = user.country ?: ""
            val birthDateUser by profileViewModel.birthDateUser.collectAsState()
            val titleGender = stringResource(id = R.string.sexe_text)
            val titleSituation = stringResource(id = R.string.situation_text)
            val titleWork = stringResource(id = R.string.employment_type_choice_text)
            val phone by profileViewModel.phone.collectAsState()
            val completePhone by profileViewModel.completePhone.collectAsState()
            val bio by profileViewModel.bio.collectAsState()

            FormTextField(
                value = userName,
                borderColor = if (activatedCheck && !submitEnabled) Color.Red else colorResource(
                    id = R.color.whatsapp
                ),
                onValueChange = {
                    if (activatedCheck) profileViewModel.validateFullName(it)
                    profileViewModel.changeUserName(it)
                },
                label = "Full Name",
                isRequired = true
            )

            val userEmail by profileViewModel.userEmail.collectAsState()
            val isEmailValid by profileViewModel.isEmailValid.collectAsState()
            val emailCheck by remember { derivedStateOf { isEmailValid } }

            val isCheckPersonal by profileViewModel.isCheckPersonal.collectAsState()

            LaunchedEffect(isCheckPersonal) {
                if (isCheckPersonal) {
                    val fullNameValidator = profileViewModel.validateFullName(userName)
                    val addressValidator = profileViewModel.validateAddress(userAddress)
                    val emailValidator = profileViewModel.validateEmail(userEmail ?: "")
                    if (!fullNameValidator || !addressValidator || !emailValidator) {
                        activatedCheck = true
                    }

                    if (fullNameValidator && addressValidator && emailValidator) {
                        profileViewModel.saveUserPersonalInfo()
                        profileViewModel.getInitialDetail()
                    }
                }
            }

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
            FormTextField(
                value = birthDateUser ?: "",
                borderColor = colorResource(id = R.color.whatsapp),
                onValueChange = {},
                leadingIcon = Icons.Filled.CalendarMonth,
                label = stringResource(id = R.string.birth_text),
                isRequired = true,
                readOnly = true,
                onClick = {
                    onBirthDateChange()
                }
            )

            //GENDER
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalForm(
    profileViewModel: ProfileViewModel,
    onDataChange: () -> Unit
) {

    val user by profileViewModel.user.collectAsState()

    var titleGlobal by remember { mutableStateOf("") }
    var activatedCheck by remember { mutableStateOf(false) }

    var showGlobal by remember { mutableStateOf(false) }
    var selectedItemGlobal by remember { mutableStateOf("") }
    var globalList by remember { mutableStateOf(emptyList<Int>()) }
    var action: (name: String) -> Unit = {}

    var showWorkType by remember { mutableStateOf(false) }
    val workType by profileViewModel.workType.collectAsState()
    val workTypeOptions by profileViewModel.workTypeOptions.collectAsState()
    val selectedWorkType by remember {
        mutableStateOf(
            workType?.ifEmpty { "" })
    }

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

    val expLevel = GlobalEntries.user.professionalStatus.userExperience ?: ""
    val availabilities = GlobalEntries.user.professionalStatus.availability ?: ""
    val workTypes = GlobalEntries.user.professionalStatus.workType ?: ""
    var salaryPreferred by remember {
        mutableStateOf(user.professionalStatus.preferredSalary ?: "")
    }

    val userMedium by profileViewModel.userMedium.collectAsState()
    val userGithub by profileViewModel.userGithub.collectAsState()
    val userPortFolio by profileViewModel.userPortFolio.collectAsState()

    val isSubmitProfessionalAction by GlobalEntries.isSubmitProfessionalAction.collectAsState()

    var hybridPreference by remember {
        mutableStateOf(GlobalEntries.user.professionalStatus.hybridPreference)
    }

    var remotePreference by remember {
        mutableStateOf(GlobalEntries.user.professionalStatus.remotePreference)
    }

    var onSitePreference by remember {
        mutableStateOf(GlobalEntries.user.professionalStatus.onSitePreference)
    }

    LaunchedEffect(isSubmitProfessionalAction) {
        if (isSubmitProfessionalAction) {
            val expValidator = expLevel.isNotEmpty()
            val availabilityValidator = availabilities.isNotEmpty()
            val workTypesValidator = workTypes.isNotEmpty()
            val salaryValidator = salaryPreferred.isNotEmpty()
            val workValidator =
                onSitePreference == true || hybridPreference == true || remotePreference == true
            if (!expValidator || !availabilityValidator || !workValidator || !salaryValidator || !workTypesValidator) {
                activatedCheck = true
                GlobalEntries.isSubmitProfessionalAction.update { false }
            }

            if (expValidator && availabilityValidator && workValidator && salaryValidator && workTypesValidator) {
                profileViewModel.saveUserProfessionalInfo(GlobalEntries.user.professionalStatus)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WorkTypePreferences(
            remoteWork = remotePreference ?: false,
            hybridWork = hybridPreference ?: false,
            onSiteWork = onSitePreference ?: false,
            onRemoteChange = {
                profileViewModel.changePreferenceRemote(it)
                remotePreference = it
                GlobalEntries.user.professionalStatus.remotePreference = it
            },
            onHybridChange = {
                hybridPreference = it
                GlobalEntries.user.professionalStatus.hybridPreference = it
                profileViewModel.changePreferenceHybrid(it)
            },
            onOnSiteChange = {
                onSitePreference = it
                GlobalEntries.user.professionalStatus.onSitePreference = it
                profileViewModel.changePreferenceSite(it)
            }
        )
        val workValidator =
            onSitePreference == true || hybridPreference == true || remotePreference == true

        if (activatedCheck && !workValidator) {
            Text(
                text = stringResource(id = R.string.error_work_type),
                color = Color.Red
            )
        }

        // Experience Level
        val titleExpLevel = stringResource(id = R.string.experience_level_text)
        FormTextField(
            value = expLevel,
            borderColor = if (activatedCheck && !expLevel.isNotEmpty()) Color.Red else colorResource(
                id = R.color.whatsapp
            ),
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
            value = availabilities,
            borderColor = if (activatedCheck && !availabilities.isNotEmpty()) Color.Red else colorResource(
                id = R.color.whatsapp
            ),
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

        val titleTypeJob = stringResource(id = R.string.employment_type_text)
        FormTextField(
            value = workTypes,
            borderColor = if (activatedCheck && workTypes.isEmpty()) Color.Red else colorResource(
                id = R.color.whatsapp
            ),
            onValueChange = {},
            leadingIcon = Icons.Filled.Business,
            label = stringResource(id = R.string.employment_type_text),
            isRequired = true,
            readOnly = true,
            onClick = {
                titleGlobal = titleTypeJob
                showWorkType = true
            }
        )

        // Salary Information
        FormTextField(
            value = salaryPreferred,
            borderColor = if (activatedCheck && salaryPreferred.isEmpty()) Color.Red else colorResource(
                id = R.color.whatsapp
            ),
            onValueChange = {
                if (activatedCheck) it.isNotEmpty()
                profileViewModel.changePreferredSalary(it)
                salaryPreferred = it
                GlobalEntries.user.professionalStatus.preferredSalary = it
            },
            label = stringResource(id = R.string.preferred_salary_text),
            keyboardType = KeyboardType.Number,
            leadingIcon = Icons.Default.TrendingUp,
            placeholder = "e.g., $100,000",
            isRequired = true
        )


        FormTextField(
            value = userMedium ?: "",
            borderColor = colorResource(id = R.color.whatsapp),
            onValueChange = {
                profileViewModel.changeMedium(it)
            },
            label = "Medium",
            leadingIcon = Icons.Default.Link,
            placeholder = "@yourusername"
        )


        FormTextField(
            value = userGithub ?: "",
            borderColor = colorResource(id = R.color.whatsapp),
            onValueChange = {
                profileViewModel.changeGithub(it)
            },
            label = "GitHub Profile",
            leadingIcon = Icons.Default.Code,
            placeholder = "github.com/yourusername"
        )


        FormTextField(
            value = userPortFolio ?: "",
            borderColor = colorResource(id = R.color.whatsapp),
            onValueChange = {
                profileViewModel.changePortFolio(it)
            },
            label = "Portfolio Website",
            leadingIcon = Icons.Default.Web,
            placeholder = "yourportfolio.com"
        )
    }

    showGlobal = showAvailability || showExperienceLevel || showWorkType
    if (showAvailability) {
        globalList = availabilityOptions
        selectedItemGlobal = selectedAvailability ?: ""
        action = { name ->
            GlobalEntries.user.professionalStatus.availability = name
            profileViewModel.changeAvailability(name)
        }
    }

    if (showWorkType) {
        globalList = workTypeOptions
        selectedItemGlobal = selectedWorkType ?: ""
        action = { name ->
            GlobalEntries.user.professionalStatus.workType = name
            profileViewModel.changeWorkType(name)
        }
    }

    if (showExperienceLevel) {
        globalList = experienceOptions
        selectedItemGlobal = selectedExperience ?: ""
        action = { name ->
            GlobalEntries.user.professionalStatus.userExperience = name
            profileViewModel.changeExperienceLevel(name)
        }
    }

    if (showGlobal) {
        ModalBottomSheet(
            onDismissRequest = {
                showAvailability = false
                showExperienceLevel = false
                showWorkType = false
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
                    showWorkType = false
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

        //val skills by profileViewModel.skills.collectAsState()
        var skills by remember {
            mutableStateOf(GlobalEntries.user.candidateSkills.listSkills)
        }

        // Skills Section
        SkillsInputSection(
            title = stringResource(id = R.string.technical_skills_text),
            skills = skills,
            newSkill = newSkill,
            onNewSkillChange = { newSkill = it },
            onAddSkill = {
                //skills.add(newSkill)
                skills = (skills + newSkill).toMutableList()
                GlobalEntries.user.candidateSkills.listSkills = skills
                profileViewModel.addSkills(newSkill)
                newSkill = ""
            },
            onRemoveSkill = { skill ->
                //skills.remove(newSkill)
                skills = (skills - skill).toMutableList()
                GlobalEntries.user.candidateSkills.listSkills = skills
                profileViewModel.removeSkills(skill)
            },
            placeholder = "e.g., Kotlin, React, Python..."
        )

        //val certifications by profileViewModel.certifications.collectAsState()
        var certifications by remember {
            mutableStateOf(GlobalEntries.user.candidateSkills.listCertification)
        }

        // Certifications Section
        SkillsInputSection(
            title = "Certifications",
            skills = certifications,
            newSkill = newCertification,
            onNewSkillChange = { newCertification = it },
            onAddSkill = {
                certifications = (certifications + newCertification).toMutableList()
                GlobalEntries.user.candidateSkills.listCertification = certifications
                profileViewModel.addCertification(newCertification)
                newCertification = ""
            },
            onRemoveSkill = { cert ->
                certifications = (certifications - cert).toMutableList()
                GlobalEntries.user.candidateSkills.listCertification = certifications
                profileViewModel.removeCertification(cert)
            },
            placeholder = "e.g., AWS Certified Developer..."
        )

        //val languageSkills by profileViewModel.languageSkills.collectAsState()
        var languageSkills by remember {
            mutableStateOf(GlobalEntries.user.candidateSkills.listLanguages)
        }

        // Languages Section
        LanguagesSection(
            languages = languageSkills.toMutableList(),
            onLanguagesChange = {
                languageSkills = it.toMutableList()
                GlobalEntries.user.candidateSkills.listLanguages = languageSkills
                profileViewModel.addLanguageSkills(it)
            }
        )
    }
}

@Composable
fun ExperienceForm(
    profileViewModel: ProfileViewModel,
    onCompanyChange: (index: Int) -> Unit,
    onDateStartChange: (index: Int) -> Unit,
    onDateEndChange: (index: Int) -> Unit,
    onDataChange: (CandidateFormData) -> Unit
) {

    val experience: LazyPagingItems<Experience> =
        profileViewModel.experience.collectAsLazyPagingItems()

    val user by profileViewModel.user.collectAsState()
    val allExp by profileViewModel.allExp.collectAsState()

    var experiences by remember {
        mutableStateOf(user.experience ?: mutableListOf())
    }

    listExperience = user.experience ?: mutableListOf()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        FormSectionHeader(
            title = stringResource(id = R.string.work_experience_text),
            subtitle = stringResource(id = R.string.pro_journey_text)
        )

        // Add Experience Button
        OutlinedButton(
            onClick = {
                val newExperience = Experience()
                experiences = (experiences + newExperience).toMutableList()
                profileViewModel.addNewExperience(newExperience)
            },
            border = BorderStroke(1.dp, colorResource(id = R.color.whatsapp)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                Icons.Default.Add,
                tint = colorResource(id = R.color.whatsapp),
                contentDescription = "Add Experience"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(id = R.string.add_experience_pro_text))
        }


        experiences.forEachIndexed { index, item ->

            WorkExperienceCard(
                experience = item,
                profileViewModel = profileViewModel,
                onExperienceTypeChange = {
                    if (it == "Contract") {
                        profileViewModel.changePreferenceContract(index, it)
                    } else {
                        profileViewModel.changePreferenceFreelance(index, it)
                    }
                },
                onCompanyNameChange = {
                    onCompanyChange(index)
                },
                onFreelanceSalaryChange = {
                    if (it.isNotEmpty()) {
                        profileViewModel.changeFreelanceSalary(index, it.toInt())
                    }
                },
                onHourPaymentChange = {
                    profileViewModel.changePerHourWorkMethod(index, it)
                    profileViewModel.changePerDayWorkMethod(index, false)
                    profileViewModel.changePerProjectWorkMethod(index, false)
                },
                onDayPaymentChange = {
                    profileViewModel.changePerHourWorkMethod(index, false)
                    profileViewModel.changePerDayWorkMethod(index, it)
                    profileViewModel.changePerProjectWorkMethod(index, false)
                },
                onProjectPaymentChange = {
                    profileViewModel.changePerHourWorkMethod(index, false)
                    profileViewModel.changePerDayWorkMethod(index, false)
                    profileViewModel.changePerProjectWorkMethod(index, it)
                },
                onDateStartChange = {
                    onDateStartChange(index)
                },
                onDateEndChange = {
                    onDateEndChange(index)
                },
                onChangeCurrent = {
                    profileViewModel.changeCurrent(index, it)
                },
                onTitleChange = {
                    profileViewModel.changeTitleExperience(index, it)
                },
                onDescriptionChange = {
                    profileViewModel.changeDescriptionExperience(index, it)
                },
                addTechnologiesChanged = {
                    profileViewModel.addTechnologyExperience(index, it)
                },
                onRemove = {
                    experiences = (experiences - experiences[index]).toMutableList()
                    profileViewModel.removeExperience(item.id)
                },
                onSubmit = {
                    profileViewModel.saveExperiences(user.experience ?: mutableListOf())
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EducationFormSection(
    profileViewModel: ProfileViewModel,
    onChangeDegree: (index: Int) -> Unit,
    onChangeFieldOfStudy: (index: Int) -> Unit,
    onChangeInstitution: (index: Int) -> Unit
) {
    val user by profileViewModel.user.collectAsState()

    var educations by remember {
        mutableStateOf(user.education ?: mutableListOf())
    }

    listEducations = user.education ?: mutableListOf()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormSectionHeader(
            title = stringResource(id = R.string.education_title_text),
            subtitle = stringResource(id = R.string.academic_text)
        )

        // Add Education Button
        OutlinedButton(
            onClick = {
                val education = Educations()
                educations = (educations + education).toMutableList()
                profileViewModel.addNewEducation(education)
            },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, colorResource(id = R.color.whatsapp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.Add,
                tint = colorResource(id = R.color.whatsapp),
                contentDescription = "Add Education"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(id = R.string.education_add_text))
        }

        // Education Items
        educations.forEachIndexed { index, education ->
            EducationCard(
                education = education,
                onChangeDegree = {
                    onChangeDegree(index)
                },
                onChangeFieldOfStudy = {
                    onChangeFieldOfStudy(index)
                },
                onChangeInstitution = {
                    onChangeInstitution(index)
                },
                onChangeGrade = {
                    profileViewModel.changeGradeEducation(index, it)
                },
                onSubmit = {
                    profileViewModel.saveEducations(user.education ?: mutableListOf())
                },
                onRemove = {
                    educations = (educations - education).toMutableList()
                    profileViewModel.removeEducation(education.id)
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