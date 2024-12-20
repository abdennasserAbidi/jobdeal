package com.example.myjob.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericSearch(
    mListOfJobs: List<String?>,
    onDismissRequest: () -> Unit,
    onSelectedBank: (String, Int) -> Unit,
    title: String = "",
) {

    val coroutineScope = rememberCoroutineScope()
    var selectedText by remember { mutableStateOf(mListOfJobs?.get(0) ?: "") }
    var searchText by remember { mutableStateOf("") }
    val isSearching = remember { mutableStateOf(false) }
    val filteredBanks = remember(searchText) {
        mListOfJobs.filter { it?.contains(searchText, ignoreCase = true) == true }
    }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current as MainActivity
    val errorMsg = remember {
        mutableStateOf("")
    }
    val interactionSource = remember { MutableInteractionSource() }


    val contextLocal = LocalContext.current
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") } // Query for SearchBar
    var active by remember { mutableStateOf(false) } // Active state for SearchBar

    var openDialog by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }

    /*if (errorMsg.value != "") {
        openDialog = true
        AlertDialog(openDialog = openDialog,
            title = "Message",
            desc = errorMsg.value,
            onDism = {
                errorMsg.value = ""
            },
            okCLick = {
                errorMsg.value = ""
            },
            openDialogState = {
                openDialog.value = it
            })
    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(0.9f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
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
                    Column {
                        Text(text = filteredBanks[index] ?: "",
                            modifier = Modifier
                                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                                    searchText = filteredBanks[index] ?: ""
                                    onSelectedBank(filteredBanks[index] ?: "", index)
                                    onDismissRequest()
                                }
                                .fillMaxWidth(0.9f)
                                .padding(top = 20.dp, start = 20.dp),
                            fontSize = 20.sp,
                            color = Color.Black)

                        if (index < filteredBanks.lastIndex)
                            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(top = 10.dp))
                    }
                }

            }
        }
    }
}