package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.FlowRowOverflowScope
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GenericMultipleSearch
import com.example.myjob.common.GenericSearch
import com.example.myjob.domain.entities.Subject

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(
    navController: NavController,
    homeViewModel: FilterViewModel = hiltViewModel(),
    allSubjects: MutableList<Subject>,
    listSchools: MutableList<String>,
    listCountries: MutableList<String>,
    listCompany: MutableList<String>
) {

    val interactionSource = remember { MutableInteractionSource() }
    val itemState by homeViewModel.itemState.collectAsState()

    var disponibilityView by remember { mutableStateOf(false) }
    var experienceView by remember { mutableStateOf(false) }
    var typeContractView by remember { mutableStateOf(false) }
    var bottomView by remember { mutableStateOf(true) }
    var showActivitySectorView by remember { mutableStateOf(false) }
    var showInstitutionView by remember { mutableStateOf(false) }
    var showLocationView by remember { mutableStateOf(false) }
    var showCompanyView by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {

                val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = colorResource(id = R.color.whatsapp), shape = shape)
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
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
            }

            var maxLines by remember { mutableStateOf(3) }

            val moreIndicator = @Composable { scope: FlowRowOverflowScope ->
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

                            })
                }
            }

            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                maxLines = maxLines,
                overflow = FlowRowOverflow.expandOrCollapseIndicator(
                    minRowsToShowCollapse = 3,
                    expandIndicator = moreIndicator,
                    collapseIndicator = lessIndicator
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemState.forEachIndexed { index, txt ->
                    Box(
                        modifier = Modifier
                            .border(
                                1.dp,
                                colorResource(id = R.color.whatsapp),
                                RectangleShape
                            )
                            .background(color = White, shape = RectangleShape)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                homeViewModel.removeFromFlow(txt)
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = txt,
                            fontSize = 9.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(vertical = 10.dp)
                        )

                    }

                }

            }

            ItemFilter("Experience", 20.dp) {
                experienceView = !experienceView
            }

            if (experienceView) {

                val experiences by homeViewModel.experiences.collectAsState()

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    experiences.mapIndexed { index, exp ->
                        var selectedExp by remember { mutableStateOf(false) }

                        val color =
                            if (selectedExp) colorResource(id = R.color.whatsapp) else Black

                        val bgColors =
                            if (selectedExp) colorResource(id = R.color.whatsapp) else Color.Transparent

                        val textColors =
                            if (selectedExp) White else Black

                        Box(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = color,
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(bgColors, shape = RoundedCornerShape(40.dp))
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    selectedExp = !selectedExp
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = exp,
                                color = textColors,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            ItemFilter("Disponibility", 40.dp) {
                disponibilityView = !disponibilityView
            }

            if (disponibilityView) {

                val availabilities by homeViewModel.availabilities.collectAsState()
                val selectedAvailability by homeViewModel.selectedAvailability.collectAsState()

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    availabilities.mapIndexed { index, availabilities ->

                        val color =
                            if (selectedAvailability[index]) colorResource(id = R.color.whatsapp) else Black

                        val bgColors =
                            if (selectedAvailability[index]) colorResource(id = R.color.whatsapp) else Color.Transparent

                        val textColors =
                            if (selectedAvailability[index]) White else Black

                        Box(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = color,
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(bgColors, shape = RoundedCornerShape(40.dp))
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    homeViewModel.changeSelection(
                                        index,
                                        !selectedAvailability[index]
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = availabilities.title),
                                color = textColors,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            ItemFilter("Type du contrat", 40.dp) {
                typeContractView = !typeContractView
            }

            if (typeContractView) {
                val employmentType by homeViewModel.employmentType.collectAsState()
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    employmentType.map {
                        var selectedType by remember { mutableStateOf(false) }

                        val color =
                            if (selectedType) colorResource(id = R.color.whatsapp) else Black

                        val bgColors =
                            if (selectedType) colorResource(id = R.color.whatsapp) else Color.Transparent

                        val textColors =
                            if (selectedType) White else Black

                        Box(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = color,
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    selectedType = !selectedType
                                }
                                .background(bgColors, shape = RoundedCornerShape(40.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.type,
                                color = textColors,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            ItemFilter("Activity sector", 40.dp) {
                bottomView = false
                showActivitySectorView = true
            }

            ItemFilter("Institution", 40.dp) {
                bottomView = false
                showInstitutionView = true
            }

            ItemFilter("Location", 40.dp) {
                bottomView = false
                showLocationView = true
            }

            ItemFilter("Company name", 40.dp) {
                bottomView = false
                showCompanyView = true
            }
        }

        AnimatedVisibility(
            visible = bottomView,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .align(Alignment.BottomCenter),
                elevation = 5.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Box(
                        modifier = Modifier
                            .weight(0.45f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                            }
                            .background(
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save",
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                            style = TextStyle(
                                color = White,
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }


                    Box(
                        modifier = Modifier
                            .weight(0.45f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                            }
                            .background(
                                color = Color.Gray,
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                            style = TextStyle(
                                color = White,
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

            }
        }

        val listSubject by homeViewModel.listSubject.collectAsState()
        val criteria by homeViewModel.criteria.collectAsState()
        Log.i("criteria", "FilterScreen: $criteria")

        AnimatedVisibility(
            visible = showActivitySectorView,
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

                GenericMultipleSearch(
                    mListOfJobs = names,
                    savedList = criteria.activitySectors,
                    onDismissRequest = {
                        showActivitySectorView = false
                        bottomView = true
                    },
                    onSelectedBank = { list ->
                        showActivitySectorView = false
                        bottomView = true
                        homeViewModel.changeActivitySector(list)
                        list.map { item ->
                            homeViewModel.addToFlow(item)
                            val f = allSubjects.filter { item == it.libelly }
                            if (f.isNotEmpty()) {
                                f[0].isSelected = true
                            }
                        }


                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        AnimatedVisibility(
            visible = showInstitutionView,
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
                        showInstitutionView = false
                        bottomView = true
                    },
                    onSelectedBank = { item, index ->
                        showInstitutionView = false
                        bottomView = true
                        homeViewModel.addToFlow(item)
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        AnimatedVisibility(
            visible = showLocationView,
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
                    mListOfJobs = listCountries,
                    onDismissRequest = {
                        showLocationView = false
                        bottomView = true
                    },
                    onSelectedBank = { item, index ->
                        showLocationView = false
                        bottomView = true
                        homeViewModel.addToFlow(item)
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

        AnimatedVisibility(
            visible = showCompanyView,
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
                    mListOfJobs = listCompany,
                    onDismissRequest = {
                        showCompanyView = false
                        bottomView = true
                    },
                    onSelectedBank = { item, index ->
                        showCompanyView = false
                        bottomView = true
                        homeViewModel.addToFlow(item)
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

    }

}

@Composable
fun FilterPreview(

) {

}


@Composable
fun ItemFilter(itemText: String, padding: Dp = 10.dp, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = padding)
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