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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import com.example.myjob.R
import com.example.myjob.common.DateUtils
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.SalaryRangeSeekBar
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.signup.CustomDropdownMenu

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GenericForm(
    user: User?,
    profileViewModel: ProfileViewModel,
    allSubjects: MutableList<Subject>,
    skipp: () -> Unit,
    next: () -> Unit,
    back: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var isSearch by remember { mutableStateOf(false) }

    val title by profileViewModel.titleGeneric.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {

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
                            back()
                        },
                    contentDescription = ""
                )

                Text(
                    text = stringResource(id = R.string.generic_info_text),
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
            ) {

                val activitySector = user?.activitySector ?: ""

                if (title.isNotEmpty()) {
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
                                text = activitySector,
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
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(start = 10.dp, top = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            skipp()
                        },
                    text = stringResource(id = R.string.skipp_text),
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.rubik_medium,
                                weight = FontWeight.Medium
                            )
                        )
                    )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            /*val fullNameValidator = profileViewModel.validateFullName(userName)
                            val addressValidator = profileViewModel.validateAddress(address)
                            if (!fullNameValidator || !addressValidator) activatedCheck =
                                true

                            if (fullNameValidator && addressValidator) {
                                focusManager.clearFocus()
                                textFieldVisible = false
                                addressVisible = false

                                profileViewModel.saveUserPersonalInfo()

                                next()
                            }*/
                        }
                        .background(colorResource(id = R.color.whatsapp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(13.dp),
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
        }

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
                        profileViewModel.changeTitleGeneric(item)
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }
    }
}