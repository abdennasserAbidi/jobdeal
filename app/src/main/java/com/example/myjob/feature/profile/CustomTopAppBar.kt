package com.example.myjob.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

@Composable
fun CustomTopAppBar(
    shouldAutoScroll: Boolean,
    toolbarHeightPx: Float,
    tabItem: List<TabItem>,
    selectedTabIndex: Int,
    changeIndex: (index: Int) -> Unit
) {

    Column(
        modifier = Modifier
            .height(with(LocalDensity.current) { toolbarHeightPx.toDp() })
            .fillMaxWidth()
            .padding(0.dp),
    ) {

        val heightToolbar = with(LocalDensity.current) { toolbarHeightPx.toDp() }

        // Animate padding based on the toolbar height
        val topPadding by animateDpAsState(
            targetValue = if (heightToolbar == 200.dp) 20.dp else 10.dp, label = ""
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = topPadding),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "share"
            )

            Icon(
                imageVector = Icons.Filled.Settings,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                contentDescription = "setting"
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(visible = heightToolbar == 200.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aladin Abidi",
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = stringResource(id = R.string.location_add_text),
                        modifier = Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily.Default,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Transparent,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = colorResource(id = R.color.whatsapp)
                    )
                }
            }
        ) {
            tabItem.forEachIndexed { index, tabItem ->
                val colors =
                    if (index == selectedTabIndex) colorResource(id = R.color.whatsapp) else Color.Black
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        changeIndex(index)
                    },
                    text = { Text(text = tabItem.title, color = colors) }
                )
            }
        }




        Box(
            modifier = Modifier.fillMaxSize()
        ) {



            /*if (heightToolbar == 200.dp) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "share"
                    )

                    Icon(
                        imageVector = Icons.Filled.Settings,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        contentDescription = "setting"
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "name",
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = stringResource(id = R.string.location_add_text),
                        modifier = Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily.Default,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    backgroundColor = Color.Transparent,
                    edgePadding = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart),
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = colorResource(id = R.color.whatsapp)
                            )
                        }
                    }
                ) {
                    tabItem.forEachIndexed { index, tabItem ->
                        val colors =
                            if (index == selectedTabIndex) colorResource(id = R.color.whatsapp) else Color.Black
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                changeIndex(index)
                            },
                            text = { Text(text = tabItem.title, color = colors) }
                        )
                    }
                }
            } else {

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "share"
                    )

                    Icon(
                        imageVector = Icons.Filled.Settings,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        contentDescription = "setting"
                    )
                }

                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    backgroundColor = Color.Transparent,
                    edgePadding = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .align(Alignment.BottomStart),
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = colorResource(id = R.color.whatsapp)
                            )
                        }
                    }
                ) {
                    tabItem.forEachIndexed { index, tabItem ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                changeIndex(index)
                            },
                            text = { Text(text = tabItem.title, color = Color.Black) }
                        )
                    }
                }
            }*/
        }
    }

}