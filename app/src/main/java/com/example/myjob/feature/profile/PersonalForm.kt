package com.example.myjob.feature.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.SalaryRangeSeekBar
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.home.CountryPicker
import com.example.myjob.feature.home.CustomPhoneKit
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.signup.CustomDropdownMenu

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalForm(
    navController: NavController,
    list: List<NewCountry>,
    allSubjects: MutableList<Subject>,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }
    val user = GlobalEntries.user
    var showCountryPicker by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(NewCountry("tn", "Tunisia", 216)) }
    val isShowed by profileViewModel.isCountryShowed.collectAsState()
    val isSearch by profileViewModel.isSearch.collectAsState()
    val isDateShowed by profileViewModel.isDateShowed.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            val isFullNameValid by profileViewModel.isFirstNameValid.collectAsState()

            var activatedCheck by remember { mutableStateOf(false) }
            var userName by remember { mutableStateOf(user.fullName ?: "") }
            val submitEnabled by remember { derivedStateOf { isFullNameValid } }

            var textFieldVisible by remember { mutableStateOf(true) }
            val focusManager = LocalFocusManager.current

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Filled.Close,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp)
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.popBackStack(Screen.ProfileScreen.route, false)
                        },
                    contentDescription = ""
                )

                Text(
                    text = stringResource(id = R.string.personal_info_text),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Center),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Text(
                text = stringResource(id = R.string.full_name_text),
                modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                style = TextStyle(
                    color = colorResource(id = R.color.whatsapp),
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            if (textFieldVisible) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                        .border(
                            width = 1.dp,
                            color = if (activatedCheck && !submitEnabled) Color.Red else colorResource(
                                id = R.color.whatsapp
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused && !textFieldVisible) {
                                focusManager.clearFocus()
                            }
                        }
                        .clip(shape = RoundedCornerShape(30.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = userName,
                    onValueChange = {
                        userName = it
                        if (activatedCheck) profileViewModel.validateFullName(it)
                        profileViewModel.changeUserName(it)
                    },
                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                )
            }

            if (activatedCheck) {
                if (userName.isEmpty() || !submitEnabled) {

                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                        text = stringResource(id = R.string.error_full_name_text),
                        color = Color.Red
                    )
                }
            }

            val isAddressValid by profileViewModel.isAddressValid.collectAsState()
            var address by remember { mutableStateOf(user.address ?: "") }
            val addressCheck by remember { derivedStateOf { isAddressValid } }
            var addressVisible by remember { mutableStateOf(true) }

            Text(
                text = stringResource(id = R.string.address_text),
                modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                style = TextStyle(
                    color = colorResource(id = R.color.whatsapp),
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            if (addressVisible) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                        .border(
                            width = 1.dp,
                            color = if (activatedCheck && !addressCheck) Color.Red else colorResource(
                                id = R.color.whatsapp
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused && !addressVisible) {
                                focusManager.clearFocus()
                            }
                        }
                        .clip(shape = RoundedCornerShape(30.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = address,
                    onValueChange = {
                        address = it
                        if (activatedCheck) profileViewModel.validateAddress(it)
                        profileViewModel.changeAddress(it)
                    },
                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                )
            }

            if (activatedCheck) {
                if (address.isEmpty() || !addressCheck) {

                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                        text = stringResource(id = R.string.error_address_text),
                        color = Color.Red
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
            ) {
                Text(
                    text = "${stringResource(id = R.string.birth_text)}: ",
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterStart),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    //val birthDateUser by profileViewModel.birthDateUser.collectAsState()
                    val birthDateUser = user.birthDate?: ""

                    Text(text = birthDateUser, color = Color.Black)

                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                //isDateShowed = true
                                profileViewModel.changeVisibilityDate(true)
                            },
                        imageVector = Icons.Filled.CalendarMonth,
                        tint = Color.Black,
                        contentDescription = ""
                    )
                }

            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            val marriedText = stringResource(id = R.string.married_text)
            val singleText = stringResource(id = R.string.single_text)
            val engagedText = stringResource(id = R.string.engaged_text)

            val situationOptions = listOf(marriedText, singleText, engagedText)
            var selectedSituation by remember {
                mutableStateOf(
                    profileViewModel.getSituation().ifEmpty { situationOptions[0] })
            }
            profileViewModel.changeSituation(selectedSituation)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "${stringResource(id = R.string.situation_text)}: ",
                    modifier = Modifier.padding(start = 20.dp),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                RadioOptions(
                    listOptions = situationOptions,
                    defaultSelection = selectedSituation,
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    selectedSituation = it
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            val sexeOptions = listOf(
                stringResource(id = R.string.male_text),
                stringResource(id = R.string.female_text)
            )
            var selectedSexe by remember {
                mutableStateOf(
                    profileViewModel.getSex().ifEmpty { sexeOptions[0] })
            }
            profileViewModel.changeSex(selectedSexe)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "${stringResource(id = R.string.sexe_text)}: ",
                    modifier = Modifier.padding(start = 20.dp),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                RadioOptions(
                    listOptions = sexeOptions,
                    defaultSelection = selectedSexe,
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    selectedSexe = it
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            //activity sector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {

                Log.i("ssdvsbfsbrs", "PersonalForm: $user")
                val title = user.preferredActivitySector ?: ""

                if (title.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                profileViewModel.changeVisibilitySearch(true)
                            }
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.activity_text)}: ",
                            style = TextStyle(
                                color = colorResource(id = R.color.whatsapp),
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {

                            Text(
                                text = title,
                                modifier = Modifier
                                    .padding(top = 10.dp, bottom = 5.dp)
                                    .weight(0.9f),
                                color = Color.Black
                            )

                            Icon(
                                modifier = Modifier
                                    .weight(0.1f)
                                    .size(24.dp),
                                imageVector = Icons.Filled.ArrowDropDown,
                                tint = Color.Black,
                                contentDescription = ""
                            )

                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                profileViewModel.changeVisibilitySearch(true)
                            }
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.activity_text)}: ",
                            modifier = Modifier.align(Alignment.CenterStart),
                            style = TextStyle(
                                color = colorResource(id = R.color.whatsapp),
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Icon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 20.dp),
                            imageVector = Icons.Filled.ArrowDropDown,
                            tint = Color.Black,
                            contentDescription = ""
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            //Countries
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {

                val title = user.newCountry?: ""

                if (title.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                profileViewModel.changeVisibilityCountry(true)
                            }
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.country_text)}: ",
                            style = TextStyle(
                                color = colorResource(id = R.color.whatsapp),
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {

                            Text(
                                text = title,
                                modifier = Modifier
                                    .padding(top = 10.dp, bottom = 5.dp)
                                    .weight(0.9f),
                                color = Color.Black
                            )

                            Icon(
                                modifier = Modifier
                                    .weight(0.1f)
                                    .size(24.dp),
                                imageVector = Icons.Filled.ArrowDropDown,
                                tint = Color.Black,
                                contentDescription = ""
                            )

                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                profileViewModel.changeVisibilityCountry(true)
                            }
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.country_text)}: ",
                            modifier = Modifier.align(Alignment.CenterStart),
                            style = TextStyle(
                                color = colorResource(id = R.color.whatsapp),
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Icon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 20.dp),
                            imageVector = Icons.Filled.ArrowDropDown,
                            tint = Color.Black,
                            contentDescription = ""
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            val rangeSalaryUser = user.rangeSalary?: ""
            var minRange = 50f
            var maxRange = 150f

            if (rangeSalaryUser.isNotEmpty()) {
                val ranges = rangeSalaryUser.split("to")

                minRange = ranges[0].trim().toFloat()
                maxRange = ranges[1].trim().toFloat()
            }

            //marge salariale
            SalaryRangeSeekBar(
                salaryRange = 0f..200f, // Example range in thousands
                initialMin = minRange,
                initialMax = maxRange,
                onRangeChanged = { min, max ->
                    // Handle the updated range
                    val rangeSalary = "${min.toInt()} to ${max.toInt()}"
                    profileViewModel.changeRangeSalary(rangeSalary)
                }
            )

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            //availability
            Text(
                text = "${stringResource(id = R.string.disponibility_text)}: ",
                modifier = Modifier.padding(start = 20.dp),
                style = TextStyle(
                    color = colorResource(id = R.color.whatsapp),
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            val availability by profileViewModel.availability.collectAsState()

            val list = listOf(
                stringResource(R.string.disponibility1_text),
                stringResource(R.string.disponibility2_text),
                stringResource(R.string.disponibility3_text),
                stringResource(R.string.disponibility4_text)
            )
            CustomDropdownMenu(
                list = list,
                defaultSelected = availability,
                color = colorResource(id = R.color.whatsapp),
                withIcon = false,
                onSelected = {
                    profileViewModel.changeAvailability(list[it])
                },
                modifier = Modifier.padding(top = 10.dp, start = 20.dp)
            )

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            Text(
                text = "${stringResource(id = R.string.phone_text)}: ",
                modifier = Modifier.padding(start = 20.dp),
                style = TextStyle(
                    color = colorResource(id = R.color.whatsapp),
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            CustomPhoneKit(
                modifier = Modifier.padding(top = 10.dp, start = 20.dp),
                selectedCountry = selectedCountry
            ) {
                showCountryPicker = true
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(start = 10.dp, top = 20.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            val fullNameValidator = profileViewModel.validateFullName(userName)
                            val addressValidator = profileViewModel.validateAddress(address)
                            if (!fullNameValidator || !addressValidator) activatedCheck = true

                            if (fullNameValidator && addressValidator) {
                                focusManager.clearFocus()
                                textFieldVisible = false
                                addressVisible = false

                                profileViewModel.saveUserPersonalInfo()
                                profileViewModel.getInitialDetail()
                                navController.popBackStack(Screen.ProfileScreen.route, false)
                            }
                        }
                        .background(
                            colorResource(id = R.color.whatsapp),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 20.dp),
                        text = stringResource(id = R.string.next_text),
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.rubikbold,
                                    weight = FontWeight.Bold
                                )
                            )
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        DateContainer(
            isDateShowed = isDateShowed,
            changeDate = {
                profileViewModel.changeBirthDate(it)
                user.birthDate = it
            }, onDismiss = {
                //isDateShowed = false
                profileViewModel.changeVisibilityDate(false)
            })

        AnimatedVisibility(
            visible = isSearch,
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
                val names = allSubjects.map {
                    it.libelly
                }
                GenericSearch(
                    mListOfJobs = names,
                    onDismissRequest = {
                        profileViewModel.changeVisibilitySearch(false)
                    },
                    onSelectedBank = { item, index ->
                        profileViewModel.changeVisibilitySearch(false)
                        profileViewModel.changeTitleGeneric(item)
                        user.preferredActivitySector = item
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        AnimatedVisibility(visible = showCountryPicker) {
            CountryPicker(
                list,
                onClick = { newCountry ->
                    selectedCountry = newCountry
                    showCountryPicker = false
                },
                onBack = {
                    showCountryPicker = false
                }
            )
        }

        AnimatedVisibility(visible = isShowed) {
            /*CountryPicker(
                list,
                onClick = { newCountry ->
                    selectedCountry = newCountry
                    profileViewModel.changeVisibilityCountry(false)
                },
                onBack = {
                    profileViewModel.changeVisibilityCountry(false)
                }
            )*/
        }
    }
}