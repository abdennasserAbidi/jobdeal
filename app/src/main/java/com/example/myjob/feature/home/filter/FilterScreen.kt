package com.example.myjob.feature.home.filter

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GenericMultipleSearch
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen

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
    var categoryView by remember { mutableStateOf(false) }

    var typeContractView by remember { mutableStateOf(false) }
    var situationView by remember { mutableStateOf(false) }
    var sexView by remember { mutableStateOf(false) }
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
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 10.dp, top = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.popBackStack()
                            },
                        tint = White,
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 10.dp, top = 10.dp),
                        text = "Reset",
                        color = White,
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

                                /*Icon(
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

                                Spacer(modifier = Modifier.width(50.dp))*/

                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "Filter",
                                    color = Black,
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
                    Text(text = stringResource(id = R.string.show_all_text),
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            maxLines += 3
                        })
                    Text(text = stringResource(id = R.string.delete_all_text),
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

            ItemFilter(stringResource(id = R.string.categories_text), 20.dp) {
                categoryView = !categoryView
            }

            val selectedCategory by homeViewModel.selectedCategory.collectAsState()
            val selectedCat by homeViewModel.selectedCat.collectAsState()


            if (categoryView) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    selectedCategory.mapIndexed { index, exp ->

                        val iconCheck =
                            if (selectedCat[index]) Icons.Filled.Check else Icons.Filled.Add

                        val title = stringResource(id = exp.title)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(
                                    colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    homeViewModel.changeSelectionCategory(
                                        index,
                                        title,
                                        !selectedCat[index]
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = exp.title),
                                color = White,
                                modifier = Modifier.padding(10.dp)
                            )

                            Icon(
                                imageVector = iconCheck,
                                tint = White,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(end = 10.dp),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            ItemFilter(stringResource(id = R.string.experience_text), 20.dp) {
                experienceView = !experienceView
            }

            if (experienceView) {

                val experiences by homeViewModel.selectedExperience.collectAsState()
                val selectedExp by homeViewModel.selectedExp.collectAsState()
                Log.i("selectedCategory", "FilterScreen: $experiences")

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    experiences.mapIndexed { index, exp ->

                        val iconCheck =
                            if (selectedExp[index]) Icons.Filled.Check else Icons.Filled.Add

                        val title = stringResource(id = exp.title)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(
                                    colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    homeViewModel.changeSelectionExp(
                                        index,
                                        title,
                                        !selectedExp[index]
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = exp.title),
                                color = White,
                                modifier = Modifier.padding(10.dp)
                            )

                            Icon(
                                imageVector = iconCheck,
                                tint = White,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(end = 10.dp),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            ItemFilter(stringResource(id = R.string.disponibility_text), 40.dp) {
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

                        val iconCheck =
                            if (selectedAvailability[index]) Icons.Filled.Check else Icons.Filled.Add

                        val title = stringResource(id = availabilities.title)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(
                                    colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    homeViewModel.changeSelectionAvailability(
                                        index,
                                        title,
                                        !selectedAvailability[index]
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = availabilities.title),
                                color = White,
                                modifier = Modifier.padding(10.dp)
                            )

                            Icon(
                                imageVector = iconCheck,
                                tint = White,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(end = 10.dp),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            ItemFilter(stringResource(id = R.string.employment_type_text), 40.dp) {
                typeContractView = !typeContractView
            }

            if (typeContractView) {

                val typeContract by homeViewModel.selectedTypeContract.collectAsState()
                val selectedType by homeViewModel.selectedType.collectAsState()


                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    typeContract.mapIndexed { index, type ->

                        val iconCheck =
                            if (selectedType[index]) Icons.Filled.Check else Icons.Filled.Add

                        val title = stringResource(id = type.title)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(
                                    colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    homeViewModel.changeSelectionContract(
                                        index,
                                        title,
                                        !selectedType[index]
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = type.title),
                                color = White,
                                modifier = Modifier.padding(10.dp)
                            )

                            Icon(
                                imageVector = iconCheck,
                                tint = White,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(end = 10.dp),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            ItemFilter(stringResource(id = R.string.situation_text), 40.dp) {
                situationView = !situationView
            }

            if (situationView) {

                val situations by homeViewModel.situations.collectAsState()
                val selectedSituation by homeViewModel.selectedSituation.collectAsState()


                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    situations.mapIndexed { index, type ->

                        val iconCheck =
                            if (selectedSituation[index]) Icons.Filled.Check else Icons.Filled.Add

                        val title = stringResource(id = type.title)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(
                                    colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    homeViewModel.changeSelectionSituation(
                                        index,
                                        title,
                                        !selectedSituation[index]
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = type.title),
                                color = White,
                                modifier = Modifier.padding(10.dp)
                            )

                            Icon(
                                imageVector = iconCheck,
                                tint = White,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(end = 10.dp),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            ItemFilter(stringResource(id = R.string.sexe_text), 40.dp) {
                sexView = !sexView
            }

            if (sexView) {
                val sexChoices by homeViewModel.sexChoices.collectAsState()
                val selectedSex by homeViewModel.selectedSex.collectAsState()

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(colorResource(id = R.color.lighter_gray))
                ) {
                    sexChoices.mapIndexed { index, type ->

                        val iconCheck =
                            if (selectedSex[index]) Icons.Filled.Check else Icons.Filled.Add

                        val title = stringResource(id = type.title)

                        Row(
                            modifier = Modifier
                                .padding(vertical = 15.dp)
                                .padding(start = 10.dp)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .background(
                                    colorResource(id = R.color.whatsapp),
                                    shape = RoundedCornerShape(40.dp)
                                )
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    homeViewModel.changeSelectionSex(
                                        index,
                                        title,
                                        !selectedSex[index]
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = type.title),
                                color = White,
                                modifier = Modifier.padding(10.dp)
                            )

                            Icon(
                                imageVector = iconCheck,
                                tint = White,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(end = 10.dp),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            ItemFilter(stringResource(id = R.string.activity_text), 40.dp) {
                bottomView = false
                showActivitySectorView = true
            }

            ItemFilter(stringResource(id = R.string.institution_text), 40.dp) {
                bottomView = false
                showInstitutionView = true
            }

            ItemFilter(stringResource(id = R.string.location_text), 40.dp) {
                bottomView = false
                showLocationView = true
            }

            ItemFilter(stringResource(id = R.string.company_name_text), 40.dp) {
                bottomView = false
                showCompanyView = true
            }

            Spacer(modifier = Modifier.height(70.dp))
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
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter),
                elevation = 5.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            homeViewModel.validateFilter()
                            navController.navigate(Screen.FilteredHome.route)
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

            }
        }

        val criteria by homeViewModel.criteria.collectAsState()

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
                    savedList = criteria.preferredActivitySector,
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
                GenericMultipleSearch(
                    mListOfJobs = listSchools,
                    savedList = criteria.institutions,
                    onDismissRequest = {
                        showInstitutionView = false
                        bottomView = true
                    },
                    onSelectedBank = { list ->
                        showInstitutionView = false
                        bottomView = true
                        homeViewModel.changeInstitutions(list)
                        list.map { item ->
                            homeViewModel.addToFlow(item)

                        }
                    },
                    title = "Institution"
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

                GenericMultipleSearch(
                    mListOfJobs = listCountries,
                    savedList = criteria.location,
                    onDismissRequest = {
                        showLocationView = false
                        bottomView = true
                    },
                    onSelectedBank = { list ->
                        showLocationView = false
                        bottomView = true
                        homeViewModel.changeLocation(list)
                        list.map { item ->
                            homeViewModel.addToFlow(item)
                        }


                    },
                    title = stringResource(id = R.string.activity_text)
                )


                /*GenericSearch(
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
                )*/
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

                GenericMultipleSearch(
                    mListOfJobs = listCompany,
                    savedList = criteria.companies,
                    onDismissRequest = {
                        showCompanyView = false
                        bottomView = true
                    },
                    onSelectedBank = { list ->
                        showCompanyView = false
                        bottomView = true
                        homeViewModel.changeCompanies(list)
                        list.map { item ->
                            homeViewModel.addToFlow(item)
                        }
                    },
                    title = stringResource(id = R.string.activity_text)
                )
            }
        }

    }
}