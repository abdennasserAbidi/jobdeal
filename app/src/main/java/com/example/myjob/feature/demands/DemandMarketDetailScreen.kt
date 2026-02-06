package com.example.myjob.feature.demands

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.idDemand
import com.example.myjob.common.GlobalEntries.idHoster
import com.example.myjob.common.GlobalEntries.isFromDemand
import com.example.myjob.common.GlobalEntries.userForCompany
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.feature.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemandMarketDetailScreen(
    navController: NavController,
    makeCall: (String) -> Unit = {},
    demandsViewModel: DemandsViewModel = hiltViewModel()
) {

    val demand by demandsViewModel.demand.collectAsState()
    val userSender by demandsViewModel.userSender.collectAsState()

    var showContact by remember { mutableStateOf(false) }
    var selectedPhone by remember { mutableStateOf("") }

    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            demandsViewModel.getDemandById(idDemand)
            demandsViewModel.getUserById(idHoster)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails de la demande") },
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
        bottomBar = {
            if (demandsViewModel.isNotMe(demand.idSender)) {
                BottomActionBar(
                    onContactClick = {
                        showContact = true
                    }
                )
            }
        },
        containerColor = Color(0xFFF3F4F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF049344).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {

                            val icon = if (demand.otherCategory.isNotEmpty()) "⚙️"
                            else if (demand.category.icon.isNotEmpty()) demand.category.icon
                            else if (demand.otherTools.isNotEmpty()) "🔧"
                            else demand.tools?.icon

                            icon?.let { ic ->
                                Text(text = ic, fontSize = 36.sp)
                            }
                        }

                        StatusBadge(status = demand.status)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = demand.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF049344).copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {

                            val name =
                                demand.otherCategory.ifEmpty { demand.category.displayName.ifEmpty { demand.otherTools.ifEmpty { demand.tools?.displayName } } }

                            Text(
                                text = name ?: "",
                                color = Color(0xFF049344),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        UrgencyBadge(urgency = demand.urgency)
                    }
                }
            }

            if (demand.category.displayName.isNotEmpty()) {
                // Quick Info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickInfoCard(
                        icon = Icons.Filled.AttachMoney,
                        label = "Budget",
                        value = demand.budget,
                        modifier = Modifier.weight(1f)
                    )
                    QuickInfoCard(
                        icon = Icons.Filled.CalendarToday,
                        label = "Deadline",
                        value = demandsViewModel.convertDate(demand.deadline),
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            DetailSection(title = "Description") {
                Text(
                    text = demand.description,
                    fontSize = 15.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 22.sp
                )
            }

            // Location Section
            DetailSection(title = "Localisation") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.LocationOn,
                        null,
                        tint = Color(0xFF049344),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = demand.location,
                        fontSize = 15.sp,
                        color = Color(0xFF1F2937),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            val role = demand.userSender?.role ?: ""
            val createdBy =
                if (role == "candidate" || role == "candidat") demand.userSender?.fullName ?: ""
                else demand.userSender?.companyName ?: ""

            // Contact Info Section
            DetailSection(title = "Contact") {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ContactRow(
                            icon = Icons.Filled.Person,
                            label = "Publié par",
                            value = createdBy
                        )
                        ContactRow(
                            icon = Icons.Filled.CalendarToday,
                            label = "Publié le",
                            value = demand.date ?: ""
                        )
                    }


                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                isFromDemand = true
                                userForCompany = demand.userSender ?: User()
                                val route =
                                    if (role == "candidate" || role == "candidat") Screen.DetailScreen.route
                                    else Screen.DetailCompanyScreen.route
                                navController.navigate(route)
                            }
                            .clip(RoundedCornerShape(8.dp))
                            .background(colorResource(R.color.whatsapp).copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Voir profile",
                            color = colorResource(R.color.whatsapp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showContact) {
        ModalBottomSheet(
            onDismissRequest = { showContact = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.type_change_text),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val isShown = if (demand.paidUser == true) true
                else if (demand.countTrial > 0) true
                else false

                if (isShown) {
                    demand.userSender?.phoneList?.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {

                                }
                                .padding(vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPhone == item,
                                onClick = {
                                    selectedPhone = item
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.whatsapp),
                                    unselectedColor = colorResource(id = R.color.whatsapp)
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Button(
                            onClick = {
                                demandsViewModel.countDownTrial(demand.id)
                                makeCall(selectedPhone)
                                showContact = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF049344)
                            ),
                            enabled = selectedPhone.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Phone,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Appeler",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                    }

                } else
                    Text(
                        text = stringResource(id = R.string.end_trial_text),
                        style = MaterialTheme.typography.titleLarge,
                        color = Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}