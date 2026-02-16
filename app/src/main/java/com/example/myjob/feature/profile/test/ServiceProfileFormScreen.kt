package com.example.myjob.feature.profile.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.CountrySelector
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.feature.demands.CategoryDialogItem
import com.example.myjob.feature.demands.FormTextFieldPhone
import com.example.myjob.feature.demands.ServiceCategory
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceProfileFormScreen(
    navController: NavController,
    list: List<String>,
    clearData: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user by profileViewModel.user.collectAsState()

    var selectedCategory by remember {
        mutableStateOf(
            if (user.category == ServiceCategory.IDLE) null
            else user.category
        )
    }
    var selectedCategoryText by remember { mutableStateOf(user.otherCategory) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var userServiceName by remember(user.userServiceName) { mutableStateOf(user.userServiceName ?: "") }
    var email by remember(user.email) { mutableStateOf(user.email ?: "") }
    var activitySector by remember(user.category) {
        mutableStateOf(user.category)
    }
    var description by remember(user.bio) {
        mutableStateOf(
            user.bio ?: ""
        )
    }
    var country by remember(user.country) { mutableStateOf(user.country ?: "") }
    var city by remember(user.city) { mutableStateOf(user.city ?: "") }

    var listPhones by remember(user.phoneList) {
        mutableStateOf(
            if (user.phoneList.isNullOrEmpty()) mutableListOf("") else user.phoneList
                ?: mutableListOf("")
        )
    }

    var listAddress by remember(user.addressList) {
        mutableStateOf(
            if (user.addressList.isNullOrEmpty()) mutableListOf("") else user.addressList
                ?: mutableListOf("")
        )
    }

    var isShowedCities by remember { mutableStateOf(false) }
    var showCountryPicker by remember { mutableStateOf(false) }
    var showSecondCountryPicker by remember { mutableStateOf(false) }
    var isActivityShowed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val listNames by profileViewModel.listNames.collectAsState()
    val isShowed by profileViewModel.isCountryShowed.collectAsState()


    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            profileViewModel.getUserById()
            profileViewModel.mapperPersonalInfo(user)
            //profileViewModel.mapperToListNames(list)
        }
    }

    val savedServiceInfo by profileViewModel.savedServiceInfo.collectAsState()
    LaunchedEffect(savedServiceInfo) {
        if (savedServiceInfo == "saved successfully") {
            navController.navigate(Screen.DemandServiceScreen.route)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
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

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // Form Fields
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    item {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Catégorie de service *",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1F2937)
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF9FAFB))
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) { showCategoryDialog = true }
                                        .padding(16.dp)
                                ) {
                                    selectedCategory?.let { category ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(text = category.icon, fontSize = 24.sp)
                                            Text(
                                                text = category.displayName,
                                                color = Color(0xFF1F2937),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    } ?: run {
                                        Text(
                                            text = "Sélectionner une catégorie",
                                            color = Color(0xFF9CA3AF),
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (selectedCategory?.displayName == "Autre") {
                        item {
                            com.example.myjob.feature.demands.FormTextField(
                                value = selectedCategoryText,
                                onValueChange = {
                                    selectedCategoryText = it
                                    profileViewModel.changeOtherCategory(selectedCategoryText)
                                },
                                label = "Autre catégorie *",
                                modifier = Modifier.padding(top = 16.dp),
                                placeholder = "Ex: Forgeron"
                            )
                        }
                    }

                    item {
                        com.example.myjob.feature.demands.FormTextField(
                            value = userServiceName,
                            onValueChange = {
                                userServiceName = it
                                profileViewModel.changeServiceUserName(userServiceName)
                            },
                            label = "Nom Complet *",
                            modifier = Modifier.padding(top = 16.dp),
                            placeholder = "Ex: Aladin Abidi"
                        )
                    }

                    item {
                        com.example.myjob.feature.demands.FormTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                profileViewModel.changeServiceEmail(email)
                            },
                            label = "Email *",
                            modifier = Modifier.padding(top = 16.dp),
                            placeholder = "Ex: ala@gmail.com"
                        )
                    }

                    item {
                        // Description Field
                        com.example.myjob.feature.demands.FormTextField(
                            value = description,
                            onValueChange = {
                                description = it
                                profileViewModel.changeDescriptions(description)
                            },
                            label = "Description détaillée",
                            modifier = Modifier.padding(top = 16.dp),
                            placeholder = "Décrivez votre besoin en détail...",
                            minLines = 4,
                            maxLines = 6
                        )
                    }

                    listPhones.mapIndexed { index, phone ->
                        item {
                            //phone
                            var selectedCountry by remember {
                                mutableStateOf(
                                    NewCountry(
                                        "tn",
                                        "Tunisia",
                                        216
                                    )
                                )
                            }

                            val pad = if (index == 0) 16.dp else 8.dp

                            FormTextFieldPhone(
                                selectedCountry = selectedCountry,
                                defaultPhone = if (phone.contains(" ")) phone.split(" ")[1] else phone,
                                onValueChange = {
                                    /*email = it
                                    profileViewModel.changeServiceEmail(email)*/
                                },
                                onValueChanged = {
                                    val phoneComplete = "+${selectedCountry.code} $it"

                                    listPhones = listPhones.mapIndexed { i, value ->
                                        if (index == i) it else value
                                    }

                                },
                                label = "Numéro téléphone",
                                modifier = Modifier.padding(top = pad),
                                placeholder = "",
                                onAdd = {
                                    listPhones = (listPhones + "").toMutableList()
                                }
                            )
                        }
                    }

                    item {

                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Pays",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1F2937)
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF9FAFB))
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) { profileViewModel.changeVisibilityCountry(true) }
                                        .padding(16.dp)
                                ) {

                                    if (country.isNotEmpty()) {
                                        Text(
                                            text = country,
                                            color = Color(0xFF1F2937),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    } else {
                                        Text(
                                            text = "Sélectionner un pays",
                                            color = Color(0xFF9CA3AF),
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {

                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Villes",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1F2937)
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF9FAFB))
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) { isShowedCities = true }
                                        .padding(16.dp)
                                ) {

                                    if (city.isNotEmpty()) {
                                        Text(
                                            text = city,
                                            color = Color(0xFF1F2937),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    } else {
                                        Text(
                                            text = "Sélectionner une ville",
                                            color = Color(0xFF9CA3AF),
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    listAddress.mapIndexed { index, addresses ->
                        val pad = if (index == 0) 16.dp else 8.dp
                        item {

                            com.example.myjob.feature.demands.FormTextFieldAddress(
                                value = addresses,
                                onValueChange = {
                                    listAddress = listAddress.mapIndexed { i, value ->
                                        if (index == i) it else value
                                    }
                                },
                                label = "Adresse",
                                modifier = Modifier.padding(top = 16.dp),
                                placeholder = "",
                                onAdd = {
                                    listAddress = (listAddress + "").toMutableList()
                                }
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                profileViewModel.changeAddress(listAddress)
                                profileViewModel.changeListPhoneCompany(listPhones)
                                profileViewModel.saveServiceInfo()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                                .padding(horizontal = 20.dp),
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

            CountrySelector(
                onSelect = { item ->
                    profileViewModel.changeVisibilityCountry(false)
                    profileViewModel.changeCountryGeneric(item)
                    user.country = item
                    country = item
                },
                onDismissRequest = {
                    profileViewModel.changeVisibilityCountry(false)
                }
            )
        }

        AnimatedVisibility(
            visible = isShowedCities,
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
                    mListOfJobs = list,
                    onDismissRequest = {
                        isShowedCities = false
                    },
                    onSelectedBank = { item, index ->
                        isShowedCities = false
                        profileViewModel.changeCity(item)
                        user.city = item
                        city = item
                    },
                    title = "Villes"
                )
            }
        }

        // Category Selection Dialog
        if (showCategoryDialog) {
            AlertDialog(
                onDismissRequest = { showCategoryDialog = false },
                title = { Text("Choisir une catégorie") },
                text = {
                    LazyColumn {
                        items(ServiceCategory.entries.toTypedArray()) { category ->
                            CategoryDialogItem(
                                category = category,
                                onClick = {
                                    selectedCategory = category
                                    profileViewModel.changePostType(
                                        selectedCategory ?: ServiceCategory.IDLE
                                    )
                                    showCategoryDialog = false
                                }
                            )
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showCategoryDialog = false }) {
                        Text("Annuler", color = Color(0xFF049344))
                    }
                }
            )
        }
    }
}