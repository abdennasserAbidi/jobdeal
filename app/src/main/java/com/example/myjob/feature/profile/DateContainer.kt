package com.example.myjob.feature.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.myjob.R
import com.example.myjob.common.DateUtils
import com.example.myjob.common.GlobalEntries.start
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateContainer(
    isDateShowed: Boolean,
    startDate: String = "start",
    changeDate: (date: String) -> Unit,
    onDismiss: () -> Unit
) {

    var isError by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val dateState = rememberDatePickerState()
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = millisToLocalDate?.let {
        if (startDate == "start") start = millisToLocalDate
        DateUtils().dateToString(millisToLocalDate)
    } ?: "Choose Date"

    if (startDate == "end") {
        millisToLocalDate?.let {
            isCorrect = DateUtils().compare(start, it)
        }
    }

    if (isDateShowed) {
        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    colors = ButtonColors(
                        contentColor = colorResource(id = R.color.whatsapp),
                        disabledContentColor = colorResource(id = R.color.whatsapp),
                        containerColor = colorResource(id = R.color.whatsapp),
                        disabledContainerColor = colorResource(id = R.color.whatsapp)
                    ),
                    onClick = {
                        if (startDate == "end") {
                            if (!isCorrect) {
                                isError = true
                            } else {
                                changeDate(dateToString)
                                onDismiss()
                            }
                        } else {
                            changeDate(dateToString)
                            onDismiss()
                        }
                    }
                ) {
                    Text(text = "OK", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonColors(
                        contentColor = colorResource(id = R.color.whatsapp),
                        disabledContentColor = colorResource(id = R.color.whatsapp),
                        containerColor = colorResource(id = R.color.whatsapp),
                        disabledContainerColor = colorResource(id = R.color.whatsapp)
                    ),
                    onClick = { onDismiss() }
                ) {
                    Text(text = "Cancel", color = Color.White)
                }
            }
        ) {
            if (isError) {
                DatePicker(
                    state = dateState,
                    title = {
                        Text(
                            text = "The end date should be after start date",
                            color = Color.Red,
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                        )
                    },
                    showModeToggle = true,
                    colors = DatePickerDefaults.colors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        selectedDayContainerColor = colorResource(id = R.color.whatsapp),
                        disabledSelectedDayContainerColor = colorResource(id = R.color.whatsapp),
                        todayDateBorderColor = colorResource(id = R.color.whatsapp),
                    )
                )
            } else {
                DatePicker(
                    state = dateState,
                    showModeToggle = true,
                    colors = DatePickerDefaults.colors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        selectedDayContainerColor = colorResource(id = R.color.whatsapp),
                        disabledSelectedDayContainerColor = colorResource(id = R.color.whatsapp),
                        todayDateBorderColor = colorResource(id = R.color.whatsapp),
                    )
                )
            }

        }
    }


}