package com.example.myjob.feature.demands

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GlobalEntries.marketDemand
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.feature.profile.DateContainer

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDemandScreen(
    navController: NavController,
    listCountries: List<String>,
    demandsViewModel: DemandsViewModel = hiltViewModel()
) {

    var selectedCategory by remember {
        mutableStateOf(
            if (marketDemand.category == ServiceCategory.IDLE) null
            else marketDemand.category
        )
    }
    var selectedCategoryText by remember { mutableStateOf(marketDemand.otherCategory) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    var selectedTool by remember {
        mutableStateOf(
            if (marketDemand.tools == ToolCategory.IDLE) null
            else marketDemand.tools
        )
    }
    var selectedToolText by remember { mutableStateOf(marketDemand.otherTools) }
    var showToolsDialog by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(marketDemand.title) }
    var description by remember { mutableStateOf(marketDemand.description) }
    var location by remember { mutableStateOf(marketDemand.location) }
    var budget by remember { mutableStateOf(marketDemand.budget) }
    var urgency by remember { mutableStateOf(Urgency.MOYEN) }
    var deadline by remember { mutableStateOf(marketDemand.deadline) }
    var phone by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    var activatedCheck by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showCitiesDialog by remember { mutableStateOf(false) }
    var isProgressing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var isDateShowed by remember { mutableStateOf(false) }

    Log.i("gkzghrzjgrnzlrg", "AddDemandScreen: $marketDemand")

    var selectedIndex by remember {
        mutableIntStateOf(
            if (marketDemand.category != ServiceCategory.IDLE) 0
            else if (marketDemand.tools != ToolCategory.IDLE) 1
            else 0
        )
    }

    Log.i("gkzghrzjgrnzlrg", "index: $selectedIndex")


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
            marketDemand = MarketDemandModel()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouvelle demande de service") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Retour", tint = Color(0xFF049344))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                val listRole = listOf(
                    stringResource(id = R.string.service_text),
                    stringResource(id = R.string.tools_text)
                )

                TypeDemandSection(
                    listDemand = listRole,
                    selectedIndex = selectedIndex,
                    interactionSource = interactionSource,
                    onClick = {
                        selectedIndex = it
                    }
                )

                if (selectedIndex == 0) {
                    // Category Selection Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Catégorie de service *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF9FAFB))
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { showCategoryDialog = true }
                                    .padding(16.dp)
                            ) {
                                selectedCategory?.let { category ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = category.icon, fontSize = 24.sp)
                                        Text(
                                            text = category.displayName,
                                            color = Color(0xFF1F2937),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } ?: run {
                                    Text(
                                        text = "Sélectionner une catégorie",
                                        color = Color(0xFF9CA3AF),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    if (selectedCategory?.displayName == "Autre") {
                        FormTextField(
                            value = selectedCategoryText,
                            onValueChange = {
                                selectedCategoryText = it
                                demandsViewModel.changeOtherCategory(it)
                            },
                            label = "Autre catégorie *",
                            placeholder = "Ex: Forgeron"
                        )
                    }

                } else {
                    //TOOLS
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Tools *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF9FAFB))
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { showToolsDialog = true }
                                    .padding(16.dp)
                            ) {
                                selectedTool?.let { tool ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = tool.icon, fontSize = 24.sp)
                                        Text(
                                            text = tool.displayName,
                                            color = Color(0xFF1F2937),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } ?: run {
                                    Text(
                                        text = "Sélectionner un outil",
                                        color = Color(0xFF9CA3AF),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    if (selectedTool?.displayName == "Autre Outil") {
                        FormTextField(
                            value = selectedToolText,
                            onValueChange = {
                                selectedToolText = it
                                demandsViewModel.changeOtherTool(it)
                            },
                            label = "Autre outil *",
                            placeholder = "Ex: Perceuse"
                        )
                    }
                }

                // Title Field
                FormTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        demandsViewModel.changePostName(it)
                    },
                    label = "Titre de la demande *",
                    placeholder = "Ex: Installation d'une porte en bois"
                )

                // Description Field
                FormTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        demandsViewModel.changeDescriptions(it)
                    },
                    label = "Description détaillée *",
                    placeholder = "Décrivez votre besoin en détail...",
                    minLines = 4,
                    maxLines = 6
                )

                // Location Field
                FormTextField(
                    value = location,
                    readOnly = true,
                    onClick = {
                        showCitiesDialog = true
                    },
                    onValueChange = {

                    },
                    label = "Localisation *",
                    placeholder = "Ville, quartier",
                    leadingIcon = Icons.Filled.LocationOn
                )

                if (selectedIndex == 0) {
                    // Budget Field
                    FormTextField(
                        value = budget,
                        onValueChange = {
                            budget = it
                            demandsViewModel.changeBudget(it)
                        },
                        label = "Budget estimé",
                        placeholder = "Ex: 500 DT",
                        leadingIcon = Icons.Filled.AttachMoney,
                        keyboardType = KeyboardType.Number
                    )

                    // Deadline Field
                    FormTextField(
                        value = deadline,
                        readOnly = true,
                        onClick = {
                            isDateShowed = true
                        },
                        onValueChange = {

                            /*deadline = it
                            demandsViewModel.changeDeadline(it)*/
                        },
                        label = "Date limite souhaitée",
                        placeholder = "JJ/MM/AAAA",
                        leadingIcon = Icons.Filled.CalendarToday
                    )
                }


                // Urgency Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Urgence",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Urgency.entries.forEach { urg ->
                                UrgencyChip(
                                    urgency = urg,
                                    selected = urgency == urg,
                                    onClick = {
                                        urgency = urg
                                        demandsViewModel.changeUrgency(urg.name)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Submit Button
                Button(
                    onClick = {
                        val isOneWrong = selectedCategory == null || title.isNotEmpty() ||
                                description.isNotEmpty() || location.isNotEmpty()
                        if (isOneWrong) {
                            activatedCheck = true
                        }

                        val isAllTrue =
                            (selectedCategory != null || selectedTool != null) && title.isNotEmpty() &&
                                    description.isNotEmpty() && location.isNotEmpty()

                        if (isAllTrue) {
                            isProgressing = true
                            demandsViewModel.saveDemand()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF049344)),
                    enabled = (selectedCategory != null || selectedTool != null) && title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty()
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publier la demande", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }


            DateContainer(
                isDateShowed = isDateShowed,
                changeDate = {
                    deadline = it
                    demandsViewModel.changeDeadline(it)
                },
                onDismiss = {
                    isDateShowed = false
                }
            )
        }
    }

    if (isProgressing) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            androidx.compose.material.Card(
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

    // Category Selection Dialog
    if (showCitiesDialog) {
        AlertDialog(
            onDismissRequest = { showCitiesDialog = false },
            title = { Text("Choisir votre pays") },
            text = {
                LazyColumn {
                    items(listCountries.toTypedArray()) { city ->

                        val interactionSource = remember { MutableInteractionSource() }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    location = city
                                    demandsViewModel.changeLocation(city)
                                    showCitiesDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = city,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937)
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCitiesDialog = false }) {
                    Text("Annuler", color = Color(0xFF049344))
                }
            }
        )
    }

    // Category Selection Dialog
    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Choisir une catégorie") },
            text = {
                LazyColumn {
                    items(ServiceCategory.entries.toTypedArray()) { category ->
                        CategoryDialogItem(
                            category = category,
                            onClick = {
                                selectedCategory = category
                                demandsViewModel.changePostType(category)
                                showCategoryDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Annuler", color = Color(0xFF049344))
                }
            }
        )
    }

    // Category Selection Dialog
    if (showToolsDialog) {
        AlertDialog(
            onDismissRequest = { showToolsDialog = false },
            title = { Text("Choisir une catégorie") },
            text = {
                LazyColumn {
                    items(ToolCategory.entries.toTypedArray()) { tool ->
                        ToolDialogItem(
                            category = tool,
                            onClick = {
                                selectedTool = tool
                                demandsViewModel.changeTool(tool)
                                showToolsDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showToolsDialog = false }) {
                    Text("Annuler", color = Color(0xFF049344))
                }
            }
        )
    }
}