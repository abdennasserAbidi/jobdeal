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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Switch
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Educations
import com.example.myjob.feature.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EducationForm(
    navController: NavController,
    listStudyField: MutableList<String>,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    listSchools: MutableList<String>,
    listGrade: MutableList<String>
) {

    val interactionSource = remember { MutableInteractionSource() }

    val locationEducation by profileViewModel.locationEducation.collectAsState()
    val schoolName by profileViewModel.schoolName.collectAsState()
    val degree by profileViewModel.degree.collectAsState()
    val startDateEducation by profileViewModel.startDateEducation.collectAsState()
    val endDateEducation by profileViewModel.endDateEducation.collectAsState()
    val grade by profileViewModel.grade.collectAsState()
    val fieldStudy by profileViewModel.fieldStudy.collectAsState()
    val description by profileViewModel.description.collectAsState()

    val degreeList by profileViewModel.degreeList.collectAsState()

    var isActivitySectorVisible by remember { mutableStateOf(false) }
    var isGradeVisible by remember { mutableStateOf(false) }
    var isDegreeVisible by remember { mutableStateOf(false) }
    var isSearch by remember { mutableStateOf(false) }

    var type by remember { mutableStateOf("") }
    var isDateShowed by remember { mutableStateOf(false) }

    var isDegreeErrorVisible by remember { mutableStateOf(false) }
    var isSchoolErrorVisible by remember { mutableStateOf(false) }
    var isFieldOfStudyErrorVisible by remember { mutableStateOf(false) }
    var isStartDateErrorVisible by remember { mutableStateOf(false) }
    var isEndDateErrorVisible by remember { mutableStateOf(false) }


    val id = GlobalEntries.educations.id
    val switchValue by profileViewModel.switchEducationValue.collectAsState()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        Log.i("uniqueid", "login: $lifecycleEvent")

        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            profileViewModel.mapperEducation(GlobalEntries.educations)
        }
    }

    val saveEducationState by profileViewModel.saveEducationState.collectAsState()

    LaunchedEffect(saveEducationState) {
        if (saveEducationState.isNotEmpty() && saveEducationState == "saved successfully") {
            navController.navigate(Screen.EducationScreen.route)
            Log.i("saveEducationState", "EducationForm: ${saveEducationState.isNotEmpty()}")
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
                                val previousScreenName =
                                    navController.previousBackStackEntry?.destination?.route ?: ""
                                navController.popBackStack(previousScreenName, false)
                            },
                            contentDescription = "arrow back"
                        )

                        val title = stringResource(id = R.string.add_education_text)

                        Text(
                            text = title,
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
                                if (degree.isEmpty()) isDegreeErrorVisible = true
                                if (schoolName.isEmpty()) isSchoolErrorVisible = true
                                if (fieldStudy.isEmpty()) isFieldOfStudyErrorVisible = true
                                if (startDateEducation.isEmpty()) isStartDateErrorVisible = true
                                if (endDateEducation.isEmpty()) isEndDateErrorVisible = true

                                val isWrong = isDegreeErrorVisible
                                        && isSchoolErrorVisible
                                        && isFieldOfStudyErrorVisible
                                        && isStartDateErrorVisible
                                        && isEndDateErrorVisible

                                if (!isWrong) {
                                    val educ = Educations(
                                        id = id,
                                        schoolName = schoolName,
                                        dateStart = startDateEducation,
                                        dateEnd = endDateEducation,
                                        place = locationEducation,
                                        fieldStudy = fieldStudy,
                                        degree = degree,
                                        grade = grade,
                                        description = description,
                                        stillStudying = switchValue
                                    )

                                    profileViewModel.saveEducation(educ)
                                }

                            },
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }


            if (!isSearch && !isDateShowed && !isActivitySectorVisible && !isDegreeVisible && !isGradeVisible) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    //degree
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    ) {

                        if (degree.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isDegreeVisible = true
                                    }
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.degree_text)}: ",
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
                                        text = degree,
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
                                        isDegreeVisible = true
                                    }
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.degree_text)}: ",
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

                    if (isDegreeErrorVisible) {
                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            text = stringResource(id = R.string.required_field_text),
                            color = Color.Red
                        )
                    }

                    if (degree == "Pas de bac") {
                        //type
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                        ) {

                            if (grade.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            isGradeVisible = true
                                        }
                                ) {
                                    Text(
                                        text = "${stringResource(id = R.string.Grade_text)}: ",
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
                                            text = grade,
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
                                            isGradeVisible = true
                                        }
                                ) {
                                    Text(
                                        text = "${stringResource(id = R.string.Grade_text)}: ",
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
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    ) {

                        if (schoolName.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isSearch = true
                                    }
                            ) {
                                Text(
                                    text = "${stringResource(id = R.string.School_text)}: ",
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
                                        text = schoolName,
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
                                    text = "${stringResource(id = R.string.School_text)}: ",
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

                    if (isSchoolErrorVisible) {
                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            text = stringResource(id = R.string.required_field_text),
                            color = Color.Red
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    ) {

                        if (fieldStudy.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isActivitySectorVisible = true
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
                                        text = fieldStudy,
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
                                        isActivitySectorVisible = true
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

                    if (isFieldOfStudyErrorVisible) {
                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            text = stringResource(id = R.string.required_field_text),
                            color = Color.Red
                        )
                    }

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

                            Text(text = startDateEducation, color = Color.Black)

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

                    if (isStartDateErrorVisible) {
                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            text = stringResource(id = R.string.required_field_text),
                            color = Color.Red
                        )
                    }

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
                            text = stringResource(id = R.string.study_still_text),
                            color = colorResource(id = R.color.whatsapp),
                            style = TextStyle(
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Switch(
                            checked = switchValue,
                            onCheckedChange = {
                                profileViewModel.changeSwitchEducation(it)
                            }
                        )
                    }

                    //switch
                    if (!switchValue) {
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

                                Text(text = endDateEducation, color = Color.Black)

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

                        if (isEndDateErrorVisible) {
                            Text(
                                modifier = Modifier.padding(start = 20.dp),
                                text = stringResource(id = R.string.required_field_text),
                                color = Color.Red
                            )
                        }
                    }

                }
            }

        }

        DateContainer(
            isDateShowed = isDateShowed,
            startDate = type,
            changeDate = {
                if (type == "start") {
                    isStartDateErrorVisible = false
                    profileViewModel.changeStartDateEducation(it)
                } else {
                    isEndDateErrorVisible = false
                    profileViewModel.changeEndDateEducation(it)
                }
            }, onDismiss = {
                if (type == "start") isStartDateErrorVisible = false
                else isEndDateErrorVisible = false
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
                GenericSearch(
                    mListOfJobs = listSchools,
                    onDismissRequest = {
                        isSearch = false
                    },
                    onSelectedBank = { item, index ->
                        isSearch = false
                        isSchoolErrorVisible = false
                        profileViewModel.changeSchool(item)
                    },
                    title = stringResource(id = R.string.School_text)
                )
            }
        }


        AnimatedVisibility(
            visible = isActivitySectorVisible,
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
                    mListOfJobs = listStudyField,
                    onDismissRequest = {
                        isActivitySectorVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isActivitySectorVisible = false
                        isFieldOfStudyErrorVisible = false
                        profileViewModel.changeFieldOfStudy(item)
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        AnimatedVisibility(
            visible = isGradeVisible,
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
                    mListOfJobs = listGrade, //formation
                    onDismissRequest = {
                        isGradeVisible = false
                    },
                    onSelectedBank = { item, index ->
                        isGradeVisible = false
                        profileViewModel.changeGrade(item)
                    },
                    title = stringResource(id = R.string.Grade_text)
                )
            }
        }

        AnimatedVisibility(
            visible = isDegreeVisible,
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
                                        isDegreeVisible = false
                                    }
                                    .padding(start = 20.dp))

                            Text(
                                text = stringResource(id = R.string.degree_text),
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
                    itemsIndexed(items = degreeList) { index, item ->
                        Column {
                            Text(text = item.type,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        isDegreeVisible = false
                                        isDegreeErrorVisible = false
                                        profileViewModel.changeDegree(item.type)
                                    }
                                    .fillMaxWidth(0.9f)
                                    .padding(top = 20.dp, start = 20.dp),
                                fontSize = 20.sp,
                                color = Color.Black)

                            if (index < degreeList.lastIndex)
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