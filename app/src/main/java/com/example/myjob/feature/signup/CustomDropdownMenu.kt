package com.example.myjob.feature.signup

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myjob.R

@Composable
fun CustomDropdownMenu(
    list: List<String>, // Menu Options
    defaultSelected: String, // Default Selected Option on load
    color: Color, // Color
    withIcon: Boolean = false, //
    expanded: Boolean = false,
    onSelected: (Int) -> Unit, // Pass the Selected Option
    modifier: Modifier
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var expand by remember { mutableStateOf(expanded) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(contentAlignment = Alignment.Center) {

            if (expand) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .padding(top = 20.dp)
                        .border(
                            1.dp,
                            Color.LightGray,
                            RoundedCornerShape(30.dp)
                        )
                        .align(Alignment.TopCenter)
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center
                ) {

                    list.mapIndexed { index, item ->
                        val paddingTop = if (index == 0) 40.dp else 20.dp
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = paddingTop, bottom = 20.dp)
                            .padding(start = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selectedIndex = index
                                onSelected(selectedIndex)
                                expand = false
                            }) {
                            Text(text = item, color = Color.Black)
                        }

                        if (index < list.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(
                    1.dp,
                    color,
                    RoundedCornerShape(30.dp)
                )
                .align(Alignment.TopCenter)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    expand = !expand
                }
                .background(color = Color.White, shape = RoundedCornerShape(30.dp)))
            {

                if (withIcon) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(start = 10.dp)
                            .align(Alignment.CenterStart),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = if (defaultSelected == "English") painterResource(id = R.drawable.flag_gb)
                        else painterResource(id = R.drawable.flag_fr)
                        Icon(
                            painter = icon,
                            modifier = Modifier.size(24.dp),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )

                        Text(
                            text = defaultSelected,
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Black
                        )
                    }
                } else {
                    val defaultText = defaultSelected.ifEmpty { stringResource(R.string.disponibility1_text) }
                    Text(
                        text = defaultText,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(start = 10.dp)
                            .align(Alignment.CenterStart),
                    )
                }

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)
                )

            }
        }
    }
}