package com.example.myjob.feature.demands

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GenericSearch
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.profile.test.FormTextField
import com.example.myjob.ui.theme.WhatsAppDarkGreen
import com.example.myjob.ui.theme.WhatsAppLightGreen

@Composable
fun AddDemandScreen(
    navController: NavController,
    allSubjects: List<Subject>,
    demandsViewModel: DemandsViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }
    var showActivitySector by remember { mutableStateOf(false) }
    var activitySector by remember { mutableStateOf("Choisir un sécteur d'activité") }
    var activatedCheck by remember { mutableStateOf(false) }

    var postName by remember { mutableStateOf("") }
    var descriptions by remember { mutableStateOf("") }


    var showDialog by remember { mutableStateOf(false) }
    var isProgressing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }


    val demandStatus by demandsViewModel.demandStatus.collectAsState()

    if (showDialog) {
        isProgressing = false
        CustomDialog(isSuccess = isSuccess, message = demandStatus) {
            showDialog = false
            isProgressing = false
            if (isSuccess) navController.popBackStack()
        }
    }

    LaunchedEffect(demandStatus) {
        if (demandStatus.isNotEmpty()) {
            isSuccess = demandStatus == "saved successfully"
            showDialog = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 20.dp)
            ) {

                androidx.compose.material.Icon(
                    imageVector = Icons.Filled.Close,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.popBackStack()
                        },
                    contentDescription = ""
                )

                Text(
                    text = stringResource(id = R.string.posts_text),
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FilterChip(
                    onClick = { showActivitySector = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp),
                    label = {
                        Text(text = activitySector)
                    },
                    selected = false,
                    trailingIcon = {
                        androidx.compose.material.Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Filter",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = WhatsAppLightGreen,
                        selectedLabelColor = WhatsAppDarkGreen
                    )
                )

                val validator = activitySector.isEmpty() || activitySector == "Choisir un sécteur d'activité"
                if (activatedCheck && validator) {
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = stringResource(id = R.string.type_annonce_error),
                        color = Color.Red
                    )

                }

                FormTextField(
                    value = postName,
                    borderColor = if (activatedCheck && postName.isEmpty()) Color.Red else colorResource(
                        id = R.color.whatsapp
                    ),
                    onValueChange = {
                        postName = it
                        if (activatedCheck) postName.isNotEmpty()
                        demandsViewModel.changePostName(it)
                    },
                    label = stringResource(id = R.string.post_title_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 40.dp),
                    isRequired = true
                )

                FormTextField(
                    value = descriptions,
                    borderColor = if (activatedCheck && descriptions.isEmpty()) Color.Red else colorResource(
                        id = R.color.whatsapp
                    ),
                    onValueChange = {
                        descriptions = it
                        if (activatedCheck) descriptions.isNotEmpty()
                        demandsViewModel.changeDescriptions(it)
                    },
                    label = stringResource(id = R.string.post_description_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 40.dp),
                    isRequired = true
                )

                Button(
                    onClick = {
                        val postTypeValidator = activitySector.isNotEmpty() && activitySector != "Choisir un sécteur d'activité"
                        val postTitleValidator = postName.isNotEmpty()
                        val postDescriptionValidator = descriptions.isNotEmpty()
                        if (!postTitleValidator || !postDescriptionValidator || !postTypeValidator) {
                            activatedCheck = true
                        }

                        if (postTitleValidator && postDescriptionValidator && postTypeValidator) {
                            isProgressing = true
                            demandsViewModel.saveDemand()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.whatsapp)
                    )
                ) {
                    Text(
                        stringResource(id = R.string.save_text),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }

        }

        if (isProgressing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center)
            {

                Card(
                    modifier = Modifier
                        .size(150.dp)
                        .background(shape = RoundedCornerShape(30.dp), color = Color.White),
                    elevation = 15.dp,
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(20.dp),
                            color = colorResource(id = R.color.whatsapp),
                            strokeWidth = 8.dp,
                            trackColor = Color.LightGray,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            showActivitySector,
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
                val allNames = allSubjects.map { it.libelly ?: "" }
                GenericSearch(
                    mListOfJobs = allNames,
                    onDismissRequest = {
                        showActivitySector = false
                    },
                    onSelectedBank = { item, _ ->
                        showActivitySector = false
                        demandsViewModel.changePostType(item)
                        activitySector = item
                    },
                    title = stringResource(id = R.string.country_text)
                )
            }
        }
    }
}