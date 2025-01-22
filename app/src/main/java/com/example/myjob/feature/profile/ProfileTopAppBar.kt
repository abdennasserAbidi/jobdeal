package com.example.myjob.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

@Composable
fun ProfileTopAppBar(
    topBarHeight: Dp,
    username: String,
    userFullName: String,
    shouldAutoScroll: Boolean,
    toolbarHeightPx: Float,
    tabItem: List<TabItem>,
    selectedTabIndex: Int,
    navigate: () -> Unit,
    changeIndex: (index: Int) -> Unit
) {

    Column(
        modifier = Modifier
            .height(topBarHeight)
            .fillMaxWidth()
            .padding(0.dp)
    ) {

        val heightToolbar = with(LocalDensity.current) { toolbarHeightPx.toDp() }
        val interactionSource = remember { MutableInteractionSource() }

        // Animate padding based on the toolbar height
        val topPadding by animateDpAsState(
            targetValue = if (heightToolbar == 200.dp) 20.dp else 10.dp, label = ""
        )

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
                    if (topBarHeight != 56.dp) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(top = 30.dp)
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(colorResource(id = R.color.whatsapp), shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = username,
                                        color = Color.White,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.rubikbold,
                                                    weight = FontWeight.Bold
                                                )
                                            )
                                        )
                                    )
                                }

                                Text(
                                    text = userFullName,
                                    modifier = Modifier.padding(top = 10.dp),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.rubikbold,
                                                weight = FontWeight.Bold
                                            )
                                        )
                                    )
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp)
                                    .align(Alignment.TopStart)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        navigate()
                                    },
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Transparent,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth(),
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
    }

}