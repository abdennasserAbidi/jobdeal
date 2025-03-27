package com.example.myjob.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R

@Composable
fun FilterScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val interactionSource = remember { MutableInteractionSource() }

    Filter(navController, interactionSource, homeViewModel)

}

@Composable
fun FilterPreview(

) {

}

@Composable
fun Filter(
    navController: NavController,
    interactionSource: MutableInteractionSource,
    homeViewModel: HomeViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = colorResource(id = R.color.whatsapp), shape = shape)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 75.dp),
                    elevation = 5.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 15.dp)
                    ) {

                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterStart)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    navController.popBackStack()
                                },
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.width(50.dp))

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Filter",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.rubik_medium,
                                        weight = FontWeight.Medium
                                    )
                                )
                            )
                        )
                    }
                }
            }

        }

        var maxLines by remember { mutableStateOf(3) }

        /*val moreIndicator = @Composable { scope: FlowRowOverflowScope ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp)
            ) {
                Text(text = "Tout afficher",
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        maxLines += 3
                    })
                Text(text = "Tout supprimer",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            filterViewModel.clearFlow()
                            options = false
                        })
            }
        }

        val lessIndicator = @Composable { scope: FlowRowOverflowScope ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp)
            ) {
                Text(text = "Tout masquer",
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        maxLines = 3
                    })
                Text(text = "Tout supprimer",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            filterViewModel.clearFlow()
                            options = false
                        })
            }
        }

        FlowRow(
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            maxLines = maxLines,
            overflow = FlowRowOverflow.expandOrCollapseIndicator(
                minRowsToShowCollapse = 3,
                expandIndicator = moreIndicator,
                collapseIndicator = lessIndicator
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            itemState.forEachIndexed { index, txt ->

                Row(modifier = Modifier
                    .background(colorResource(id = R.color.marron))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        filterViewModel.removeFromUnknown(txt ?: "")
                        filterViewModel.removeFromFlow(txt ?: "")
                    }) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_close_black),
                        modifier = Modifier.padding(vertical = 10.dp),
                        contentDescription = ""
                    )

                    Text(
                        text = txt ?: "",
                        fontSize = 9.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 10.dp)
                    )

                }

            }

        }*/

        ItemFilter("Search word")


    }
}

@Composable
fun ItemFilter(itemText: String, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = itemText,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart),
                color = colorResource(id = R.color.whatsapp),
                textAlign = TextAlign.Start,
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

            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                modifier = Modifier.align(Alignment.CenterEnd),
                tint = colorResource(id = R.color.whatsapp),
                contentDescription = ""
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        )
    }

}


@Composable
@Preview
fun PreviewFilter() {
    FilterPreview()
}