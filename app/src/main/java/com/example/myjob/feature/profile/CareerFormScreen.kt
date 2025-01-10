package com.example.myjob.feature.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.SimpleSlider
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.signup.CustomDropdownMenu
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CareerFormScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    allSubjects: MutableList<Subject>,
    listCountries: MutableList<String>,
    listCompany: MutableList<String>
) {

    val interactionSource = remember { MutableInteractionSource() }

    val locationExp by profileViewModel.locationExp.collectAsState()
    val companyExp by profileViewModel.companyExp.collectAsState()
    val typeEmploymentExp by profileViewModel.typeEmploymentExp.collectAsState()
    val typeContractExp by profileViewModel.typeContractExp.collectAsState()
    //freelance
    val freelanceFeeType by profileViewModel.freelanceFeeType.collectAsState()
    val hourlyRateExp by profileViewModel.hourlyRateExp.collectAsState()
    val nbHoursExp by profileViewModel.nbHoursExp.collectAsState()
    val nbDaysExp by profileViewModel.nbDaysExp.collectAsState()

    val anotherActivity by profileViewModel.anotherActivity.collectAsState()
    val titleExp by profileViewModel.titleExp.collectAsState()
    val birthDate by profileViewModel.birthDate.collectAsState()
    val endDate by profileViewModel.endDate.collectAsState()
    val salary by profileViewModel.salary.collectAsState()

    val employmentType by profileViewModel.employmentType.collectAsState()

    var isLocationVisible by remember { mutableStateOf(false) }
    var isCompanyVisible by remember { mutableStateOf(false) }
    var isTypeEmploymentVisible by remember { mutableStateOf(false) }
    var isSearch by remember { mutableStateOf(false) }
    var isAnother by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf("") }
    var isDateShowed by remember { mutableStateOf(false) }

    val exp = GlobalEntries.experience

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            profileViewModel.mapperExperience(exp)
        }
    }
    val defaultSalary = if (salary == 0) 10000 else salary
    var sliderPosition by remember { mutableIntStateOf(defaultSalary) }

    val saveExpState by profileViewModel.saveExpState.collectAsState()
    val id = GlobalEntries.idExp

    LaunchedEffect(saveExpState) {
        if (saveExpState.isNotEmpty() && saveExpState == "saved successfully") {
            navController.navigate(Screen.CareerScreen.route)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Card(
                elevation = 5.dp,
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(70.dp)
                )
                {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Close,
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                val previousScreenName = navController.previousBackStackEntry?.destination?.route ?: ""
                                navController.popBackStack(previousScreenName, false)
                            },
                            contentDescription = "arrow back"
                        )

                        Text(
                            text = stringResource(R.string.add_experience_text),
                            modifier = Modifier.padding(start = 20.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.save_text),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                val experienceToAdd = Experience(
                                    id = id,
                                    title = titleExp,
                                    companyName = companyExp,
                                    dateStart = birthDate,
                                    dateEnd = endDate,
                                    place = locationExp,
                                    salary = defaultSalary,
                                    type = typeEmploymentExp,
                                    typeContract = typeContractExp,
                                    freelanceFee = freelanceFeeType,
                                    anotherActivitySector = anotherActivity,
                                    hourlyRate = hourlyRateExp,
                                    nbHours = nbHoursExp,
                                    nbDays = nbDaysExp
                                )

                                profileViewModel.saveExperience(experienceToAdd)

                            },
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            if (!isSearch && !isDateShowed && !isLocationVisible && !isTypeEmploymentVisible) {

                with(GlobalEntries.experience) {


                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    ) {
                        if (titleExp.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isSearch = true
                                        isAnother = false
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
                                        text = titleExp,
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
                                        isSearch = true
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
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )


                    //location
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    ) {

                        if (locationExp.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isLocationVisible = true
                                    }
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.location_text)}: ",
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
                                        text = locationExp,
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
                                        isLocationVisible = true
                                    }
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.location_text)}: ",
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
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )

                    //company name
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    ) {

                        if (companyExp.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isCompanyVisible = true
                                    }
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.company_name_text)}: ",
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
                                        text = companyExp,
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
                                        isCompanyVisible = true
                                    }
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.company_name_text)}: ",
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
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )

                    //type d'emploi
                    Text(
                        text = "${stringResource(id = R.string.employment_type_text)}: ",
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    val list = listOf(
                        stringResource(R.string.type1_text),
                        stringResource(R.string.type2_text)
                    )

                    CustomDropdownMenu(
                        list = list,
                        defaultSelected = typeEmploymentExp,
                        color = colorResource(id = R.color.whatsapp),
                        withIcon = false,
                        onSelected = {
                            profileViewModel.changeTypeEmpExp(list[it])
                        },
                        modifier = Modifier.padding(top = 10.dp, start = 20.dp)
                    )

                    if (typeEmploymentExp == list[0]) {
                        //type
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        ) {

                            if (typeContractExp.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            isTypeEmploymentVisible = true
                                        }
                                ) {
                                    Text(
                                        text = "${stringResource(id = R.string.contract_type_text)}: ",
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
                                            text = typeContractExp,
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
                                            isTypeEmploymentVisible = true
                                        }
                                ) {
                                    Text(
                                        text = "${stringResource(id = R.string.contract_type_text)}: ",
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
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 30.dp)
                        ) {
                            Text(
                                text = "${stringResource(id = R.string.salary_text)}: ",
                                style = TextStyle(
                                    color = colorResource(id = R.color.whatsapp),
                                    fontFamily = FontFamily.Default,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Text(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(end = 20.dp),
                                text = defaultSalary.toString()
                            )
                        }

                        SimpleSlider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            defaultSalary = defaultSalary.toFloat()
                        ) {
                            sliderPosition = it
                            profileViewModel.changeSalary(exp, it)
                        }

                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        ) {
                            Text(
                                text = "${stringResource(id = R.string.start_date_text)}: ",
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

                                Text(text = birthDate, color = Color.Black)

                                Icon(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            type = "start"
                                            isDateShowed = true
                                        },
                                    imageVector = Icons.Filled.CalendarMonth,
                                    tint = Color.Black,
                                    contentDescription = ""
                                )
                            }

                        }

                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )

                        val switchValue by profileViewModel.switchValue.collectAsState()

                        val switchOn = if (endDate.isNotEmpty()) false
                        else if (endDate == "Present") true
                        else switchValue

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(top = 15.dp, start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Text(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(start = 10.dp),
                                text = stringResource(id = R.string.work_still_text),
                                color = colorResource(id = R.color.whatsapp),
                                style = TextStyle(
                                    fontFamily = FontFamily.Default,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Switch(
                                checked = switchOn,
                                onCheckedChange = {
                                    profileViewModel.changeSwitch(it)
                                }
                            )
                        }

                        //switch
                        if (!switchOn) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 30.dp)
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.end_date_text)}: ",
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

                                    Text(text = endDate, color = Color.Black)

                                    Icon(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                type = "end"
                                                isDateShowed = true
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
                        }

                    }
                    else {

                        Text(
                            text = "${stringResource(id = R.string.fee_freelance_text)}: ",
                            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                            style = TextStyle(
                                color = colorResource(id = R.color.whatsapp),
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        val listFreelanceFee = listOf(
                            stringResource(id = R.string.hourly_text),
                            stringResource(id = R.string.daily_text)
                        )

                        //if (hourlyRateExp != 10) freelanceFeeType = listFreelanceFee[0]
                        CustomDropdownMenu(
                            list = listFreelanceFee,
                            defaultSelected = freelanceFeeType,
                            color = colorResource(id = R.color.whatsapp),
                            withIcon = false,
                            onSelected = {
                                profileViewModel.changeFeeType(listFreelanceFee[it])
                            },
                            modifier = Modifier.padding(top = 10.dp, start = 20.dp)
                        )

                        /*Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        ) {
                            if (anotherActivity.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            isSearch = true
                                            isAnother = true
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
                                            text = anotherActivity,
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
                                            isSearch = true
                                            isAnother = true
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
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )*/

                        //hourly rate
                        val textId = if (freelanceFeeType == listFreelanceFee[0]) R.string.hourly_rate_text
                        else R.string.daily_rate_text

                        Log.i("freelance", "hourlyRateExp: $hourlyRateExp")
                        Log.i("freelance", "nbHoursExp: $nbHoursExp")
                        Log.i("freelance", "nbDaysExp: $nbDaysExp")

                        FreelanceForm(defaultValue = "$hourlyRateExp", text = "${stringResource(id = textId)}: ", false) {
                            profileViewModel.changeHourlyRateExp(it)
                        }

                        if (freelanceFeeType == listFreelanceFee[0]) {
                            //nb hour per day
                            FreelanceForm(defaultValue = "$nbHoursExp", text = "${stringResource(id = R.string.nb_hours_text)}: ", false) {
                                profileViewModel.changeNbHoursExp(it)
                            }
                        }

                        //nb day per week
                        FreelanceForm(defaultValue = "$nbDaysExp", text = "${stringResource(id = R.string.nb_day_text)}: ", true) {
                            profileViewModel.changeNbDaysExp(it)
                        }
                    }

                }
            }

        }

        DateContainer(isDateShowed, startDate = type, changeDate = {
            if (type == "start") profileViewModel.changeStartDateExp(it) else profileViewModel.changeEndDateExp(
                it
            )
        }, onDismiss = {
            isDateShowed = false
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
                        isSearch = false
                    },
                    onSelectedBank = { item, index ->
                        isSearch = false
                        if (isAnother) profileViewModel.changeAnotherActivityExp(item)
                        else profileViewModel.changeTitleExp(item)
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        AnimatedVisibility(
            visible = isCompanyVisible,
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
                    mListOfJobs = listCompany,
                    onDismissRequest = {
                        isCompanyVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isCompanyVisible = false
                        profileViewModel.changeCompanyExp(item)
                    },
                    title = stringResource(id = R.string.companies_text)
                )
            }
        }


        AnimatedVisibility(
            visible = isLocationVisible,
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
                    mListOfJobs = listCountries,
                    onDismissRequest = {
                        isLocationVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isLocationVisible = false
                        profileViewModel.changeLocationExp(item)
                    },
                    title = stringResource(id = R.string.location_text)
                )
            }
        }

        AnimatedVisibility(
            visible = isTypeEmploymentVisible,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 5.dp
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ) {

                            Icon(imageVector = Icons.Default.ArrowBack,
                                contentDescription = "back",
                                modifier = Modifier
                                    .height(30.dp)
                                    .align(Alignment.CenterStart)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isTypeEmploymentVisible = false
                                    }
                                    .padding(start = 20.dp))

                            Text(
                                text = stringResource(id = R.string.employment_type_text),
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(items = employmentType) { index, item ->
                        Column {
                            Text(text = item.type,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isTypeEmploymentVisible = false
                                        profileViewModel.changeTypeContractExp(item.type)
                                    }
                                    .fillMaxWidth(0.9f)
                                    .padding(top = 20.dp, start = 20.dp),
                                fontSize = 20.sp,
                                color = Color.Black)

                            if (index < employmentType.lastIndex)
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                        }
                    }
                }
            }
        }
    }
}