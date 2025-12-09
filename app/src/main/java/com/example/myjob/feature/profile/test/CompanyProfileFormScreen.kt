package com.example.myjob.feature.profile.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProfileFormScreen(
    navController: NavController,
    clearData: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user by profileViewModel.user.collectAsState()

    var companyName by remember { mutableStateOf(user.companyName ?: "") }
    var activitySector by remember { mutableStateOf(user.companyActivitySector ?: "") }
    var description by remember { mutableStateOf(user.companyDescription ?: "") }
    var phone by remember { mutableStateOf("+1 234 567 8900") }
    var secondPhone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf(user.companyAddress ?: "") }
    var secondAddress by remember { mutableStateOf(user.companySecondAddress ?: "") }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navController.popBackStack()
            }
    ) {
        if (isFirstTime) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.complete_profile_text),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Row {
                        IconButton(onClick = {
                            profileViewModel.logout()
                            clearData()
                            navController.navigate(Screen.LoginScreen.route)
                        }) {
                            Icon(Icons.Default.Logout, contentDescription = "Logout")
                        }

                        TextButton(onClick = {
                            profileViewModel.saveCompanyInfo()
                        }) {
                            Text(
                                text = stringResource(id = R.string.save_text),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
                        onValueChange = {
                            activitySector = it
                            profileViewModel.changeCompanyActivitySector(it)
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
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone*") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = secondPhone,
                        onValueChange = { secondPhone = it },
                        label = { Text("Extra Phone") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
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
            }
        }
    }
}