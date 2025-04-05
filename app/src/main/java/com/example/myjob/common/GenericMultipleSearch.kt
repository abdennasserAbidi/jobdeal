package com.example.myjob.common

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.example.myjob.R

@Composable
fun GenericMultipleSearch(
    mListOfJobs: List<String?>,
    savedList: List<String> = emptyList(),
    onDismissRequest: () -> Unit,
    onSelectedBank: (List<String>) -> Unit = { _ -> },
    title: String = ""
) {
    var searchText by remember { mutableStateOf("") }
    val filteredBanks = remember(searchText) {
        mListOfJobs.filter { it?.contains(searchText, ignoreCase = true) == true }
    }

    val checkedStates = remember { mutableStateListOf<Boolean>() }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {

        }
    }

    mListOfJobs.map {
        checkedStates.add(false)
    }
    Log.i("mListOfJobs", "savedList: $savedList")

    val interactionSource = remember { MutableInteractionSource() }
    var showSearch by remember { mutableStateOf(false) }
    val list by remember { mutableStateOf(mutableListOf<String>()) }

    LaunchedEffect(savedList.size) {
        savedList.map {
            list.add(it)
            val index = mListOfJobs.indexOf(it)
            checkedStates[index] = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(0.9f),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
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
                                    onDismissRequest()
                                }
                                .padding(start = 20.dp))

                        val icon = if (showSearch) Icons.Default.Close else Icons.Default.Search
                        Icon(imageVector = icon,
                            contentDescription = "cross",
                            modifier = Modifier
                                .height(30.dp)
                                .align(Alignment.CenterEnd)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    showSearch = !showSearch
                                }
                                .padding(end = 20.dp))
                        Text(
                            text = title,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )


                    }
                    if (showSearch) SearchBar {
                        searchText = it
                    }
                }
            }

            LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
                items(filteredBanks.size) { index ->
                    val item = filteredBanks[index]
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                modifier = Modifier.size(20.dp),
                                checked = checkedStates[index],
                                onCheckedChange = {
                                    checkedStates[index] = it
                                    if (it) {
                                        if (!list.contains(item ?: ""))
                                            list.add(item ?: "")
                                    } else {
                                        if (list.contains(item ?: ""))
                                            list.remove(item ?: "")
                                    }
                                },
                                enabled = true,
                                colors = CheckboxDefaults.colors(colorResource(id = R.color.whatsapp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = item ?: "",
                                modifier = Modifier
                                    .fillMaxWidth(0.9f),
                                fontSize = 20.sp,
                                color = Color.Black
                            )

                        }

                        if (index < filteredBanks.lastIndex)
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                    }
                }

            }
        }

        Box(modifier = Modifier
            .fillMaxWidth(0.85f)
            .align(Alignment.BottomCenter)
            .padding(bottom = 10.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {

                onSelectedBank(list)
            }
            .background(
                colorResource(id = R.color.whatsapp),
                shape = RoundedCornerShape(10.dp)
            ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Valider",
                color = Color.White,
                modifier = Modifier.padding(vertical = 20.dp),
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Medium
            )

        }
    }
}