package com.example.myjob.common.horizontalcalendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarApp(
    listTime: List<String>,
    timeClose: Boolean,
    modifier: Modifier = Modifier,
    getSelectedDate: (date: String) -> Unit
) {
    val dataSource = CalendarDataSource()
    var data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    var viewTime by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(top = 10.dp)) {
        /*if (viewTime) {
            LazyRow(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                itemsIndexed(
                    items = listTime,
                    key = { _, _ ->
                        ViewCompat.generateViewId()
                    }
                ) { index, item ->

                    *//*val listDuration by authViewModel.listDuration.collectAsState()
                    val listDurationSelected by authViewModel.listDurationSelected.collectAsState()

                    val col =
                        if (listDurationSelected[index]) Color.Green else Color.Transparent
                    val colBorder =
                        if (listDurationSelected[index]) Color.Green else Color.Black*//*

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .border(
                                1.dp,
                                Color.Black,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                            }
                            //.background(col, shape = RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                            color = Color.Black,
                            text = item
                        )
                    }

                }
            }

        } else {
            Header(
                data = data,
                onPrevClickListener = { startDate ->
                    val finalStartDate = startDate.minusDays(1)
                    data = dataSource.getData(startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
                },
                onNextClickListener = { endDate ->
                    val finalStartDate = endDate.plusDays(2)
                    data = dataSource.getData(startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
                }
            )
        }*/

        Content(data = data) { date ->
            val formatter = DateTimeFormatter.ofPattern("dd/M/yyyy")
            val st = date.date.format(formatter)
            Log.i("selectedDaternenge", "CalendarApp: $st")
            getSelectedDate(st)
            viewTime = true
            data = data.copy(
                selectedDate = date,
                visibleDates = data.visibleDates.map {
                    it.copy(
                        isSelected = it.date.isEqual(date.date)
                    )
                }
            )
        }

        /*if (timeClose) {
            LazyRow(
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                itemsIndexed(
                    items = listTime,
                    key = { _, _ ->
                        ViewCompat.generateViewId()
                    }
                ) { index, item ->

                    *//*val listDuration by authViewModel.listDuration.collectAsState()
                    val listDurationSelected by authViewModel.listDurationSelected.collectAsState()

                    val col =
                        if (listDurationSelected[index]) Color.Green else Color.Transparent
                    val colBorder =
                        if (listDurationSelected[index]) Color.Green else Color.Black*//*

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .border(
                                1.dp,
                                Color.Black,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                            }
                        //.background(col, shape = RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                            color = Color.Black,
                            text = item
                        )
                    }

                }
            }
        }*/
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header(
    data: CalendarUiModel,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit,
) {
    Row {
        Text(
            text = if (data.selectedDate.isToday) {
                "Today"
            } else {
                data.selectedDate.date.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                )
            },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Content(
    data: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit,
) {
    Log.i("visibleDates", "Content: ${data.visibleDates}")
    var newList = listOf<CalendarUiModel.Date>()
    val items = data.visibleDates.filterIndexed { index, date -> date.isToday }
    if (items.isNotEmpty()) {
        val index = data.visibleDates.indexOf(items[0])
        Log.i("visibleDates", "Content: $index")
        newList = data.visibleDates.subList(index, data.visibleDates.size)
        Log.i("newList", "Content: $newList")
    }

    LazyRow {
        items(data.visibleDates.size) { index ->
            ContentItem(
                date = data.visibleDates[index],
                onDateClickListener
            )
        }
    }
    /*LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 48.dp)) {
        items(data.visibleDates.size) { index ->
            ContentItem(
                date = data.visibleDates[index],
                onDateClickListener
            )
        }
    }*/
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItem(
    date: CalendarUiModel.Date,
    onClickListener: (CalendarUiModel.Date) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                onClickListener(date)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) {
                Color.Green
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .width(40.dp)
                .height(48.dp)
                .padding(4.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}