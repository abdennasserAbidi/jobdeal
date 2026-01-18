package com.example.myjob.feature.profile.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.base.MyApp
import com.example.myjob.common.CustomPhoneKit
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProfileFormScreen(
    navController: NavController,
    allSubjects: List<Subject>,
    clearData: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user by profileViewModel.user.collectAsState()

    var companyName by remember(user.companyName) { mutableStateOf(user.companyName ?: "") }
    var activitySector by remember(user.companyActivitySector) { mutableStateOf(user.companyActivitySector ?: "") }
    var description by remember(user.companyDescription) { mutableStateOf(user.companyDescription ?: "") }
    var address by remember(user.companyAddress) { mutableStateOf(user.companyAddress ?: "") }
    var country by remember(user.country) { mutableStateOf(user.country ?: "") }
    var secondAddress by remember(user.companySecondAddress) { mutableStateOf(user.companySecondAddress ?: "") }
    var showCountryPicker by remember { mutableStateOf(false) }
    var showSecondCountryPicker by remember { mutableStateOf(false) }
    var isActivityShowed by remember { mutableStateOf(false) }

    val listNames by profileViewModel.listNames.collectAsState()
    val isShowed by profileViewModel.isCountryShowed.collectAsState()

    val isFirstTime = GlobalEntries.user.firstTimeUse ?: true

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            profileViewModel.getUserById()
            profileViewModel.mapperPersonalInfo(user)
        }
    }

    val savedCompanyInfo by profileViewModel.savedCompanyInfo.collectAsState()
    LaunchedEffect(savedCompanyInfo) {
        if (savedCompanyInfo == "saved successfully") {
            navController.navigate(Screen.HomeScreen.route)
        }
    }

    val listCompanies by profileViewModel.listCompanies.collectAsState()

    val context = LocalContext.current
    val app = context.applicationContext as MyApp
    LaunchedEffect(listCompanies) {
        app.listCompanies.removeLast()
        if (!app.listCompanies.containsAll(listCompanies)) app.listCompanies.addAll(listCompanies.distinctBy { it })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isFirstTime) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.complete_profile_text),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            profileViewModel.logout()
                            clearData()
                            navController.navigate(Screen.LoginScreen.route)
                        }) {
                            Icon(Icons.Default.Logout, contentDescription = "Logout")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.complete_profile_text),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            profileViewModel.saveCompanyInfo()
                        }) {
                            Text(
                                text = stringResource(id = R.string.save_text),
                                color = colorResource(id = R.color.whatsapp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // Form Fields
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {

                        FormTextField(
                            value = companyName,
                            onValueChange = {
                                companyName = it
                                profileViewModel.changeCompanyName(it)
                            },
                            label = stringResource(id = R.string.company_name_text),
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )

                    }

                    item {

                        FormTextField(
                            value = activitySector,
                            onValueChange = {},
                            readOnly = true,
                            onClick = {
                                isActivityShowed = true
                            },
                            label = stringResource(id = R.string.activity_text),
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }

                    item {

                        FormTextField(
                            value = description,
                            onValueChange = {
                                description = it
                                profileViewModel.changeCompanyDescription(it)
                            },
                            label = "Description",
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }

                    item {

                        //phone
                        val phone by profileViewModel.phoneCompany.collectAsState()
                        val completePhone by profileViewModel.completePhoneCompany.collectAsState()
                        var selectedCountry by remember { mutableStateOf(NewCountry("tn", "Tunisia", 216)) }

                        CustomPhoneKit(
                            modifier = Modifier.padding(top = 10.dp),
                            selectedCountry = selectedCountry,
                            hint = "Numéro téléphone",
                            defaultPhone = if (completePhone.contains(" ")) completePhone.split(" ")[1] else completePhone,
                            onClick = {
                                showCountryPicker = true
                            },
                            onValueChanged = {
                                val phoneComplete = "+${selectedCountry.code} $it"
                                profileViewModel.changePhoneCompany(it)
                                profileViewModel.changeCompletePhoneCompany(phoneComplete)
                            }
                        )

                    }

                    item {

                        val secondPhoneCompany by profileViewModel.secondPhoneCompany.collectAsState()
                        val completeSecondPhoneCompany by profileViewModel.completeSecondPhoneCompany.collectAsState()
                        var selectedSecondCountry by remember { mutableStateOf(NewCountry("tn", "Tunisia", 216)) }

                        CustomPhoneKit(
                            modifier = Modifier.padding(top = 10.dp),
                            selectedCountry = selectedSecondCountry,
                            hint = "Extra phone",
                            defaultPhone = if (completeSecondPhoneCompany.contains(" ")) completeSecondPhoneCompany.split(" ")[1] else completeSecondPhoneCompany,
                            onClick = {
                                showSecondCountryPicker = true
                            },
                            onValueChanged = {
                                val phoneComplete = "+${selectedSecondCountry.code} $it"
                                profileViewModel.changePhoneCompany(it)
                                profileViewModel.changeCompletePhoneCompany(phoneComplete)
                            }
                        )
                    }

                    item {

                        FormTextField(
                            value = address,
                            onValueChange = {
                                address = it
                                profileViewModel.changeCompanyAddress(it)
                            },
                            label = "Address",
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }

                    item {
                        FormTextField(
                            value = country,
                            onClick = {
                                profileViewModel.changeVisibilityCountry(true)
                            },
                            label = "Address",
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false,
                            onValueChange = {},
                            readOnly = true
                        )
                    }


                    item {
                        FormTextField(
                            value = secondAddress,
                            onValueChange = {
                                secondAddress = it
                                profileViewModel.changeCompanySecondAddress(it)
                            },
                            label = "Extra Address",
                            modifier = Modifier.fillMaxWidth(),
                            isRequired = false
                        )
                    }


                    if (isFirstTime) {
                        item {

                            Button(
                                onClick = {
                                    profileViewModel.saveCompanyInfo()
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(top = 20.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.whatsapp)
                                )
                            ) {
                                Text(
                                    stringResource(id = R.string.save_text),
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }


        AnimatedVisibility(
            visible = isShowed,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                GenericSearch(
                    mListOfJobs = listNames,
                    onDismissRequest = {
                        profileViewModel.changeVisibilityCountry(false)
                    },
                    onSelectedBank = { item, index ->
                        profileViewModel.changeVisibilityCountry(false)
                        profileViewModel.changeCountryGeneric(item)
                        user.country = item
                        country = item
                    },
                    title = stringResource(id = R.string.country_text)
                )
            }
        }

        AnimatedVisibility(
            visible = isActivityShowed,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val allNames = allSubjects.map { it.libelly ?: "" }
                GenericSearch(
                    mListOfJobs = allNames,
                    onDismissRequest = {
                        isActivityShowed = false
                    },
                    onSelectedBank = { item, index ->
                        isActivityShowed = false
                        profileViewModel.changeCompanyActivitySector(item)
                        user.companyActivitySector = item
                        activitySector = item
                    },
                    title = stringResource(id = R.string.country_text)
                )
            }
        }
    }
}