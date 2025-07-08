package com.example.myjob.feature.invitation.company

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.common.GlobalEntries.notificationMessage
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.ContractType
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.home.HomeViewModel
import com.example.myjob.ui.theme.WhatsAppGreenSurface
import com.example.myjob.ui.theme.WhatsAppLightGreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendInvitationCompany(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val user = candidateUser

    var subject by remember { mutableStateOf("Dveloppeur Android") }
    var message by remember { mutableStateOf("Creer une application pour connecter les entreprises avec les candidats facilement.") }
    var contractType by remember { mutableStateOf(ContractType.CONTRACT) }
    var hourlyRate by remember { mutableStateOf("") }
    var projectFee by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("USD") }
    var paymentTerms by remember(contractType) { mutableStateOf(
        if (contractType == ContractType.FREELANCE) "Le contract sa sera per hour" else "Le contract sa sera en CDI"
    ) }
    var location by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val fcmToken by homeViewModel.fcmToken.collectAsState()

    LaunchedEffect(fcmToken) {
        if (fcmToken.isNotEmpty()) {

            GlobalEntries.user.companyName?.let {
                val title = it
                val messages = "This company have sended you an invitaion"
                notificationMessage.title = it
                notificationMessage.body = "This company have sended you an invitaion"
                //homeViewModel.sendNotification(title, messages)
            }
        }
    }

    Log.i("rlekrgkjg", "CandidateListScreen: $fcmToken")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Send Invitation",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.whatsapp),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Candidate Info Card
            CandidateInfoCard(candidate = user)

            // Interview Details Section
            InvitationFormSection(
                subject = subject,
                onSubjectChange = { subject = it },
                message = message,
                onMessageChange = { message = it },
                contractType = contractType,
                onContractTypeChange = { contractType = it },
                hourlyRate = hourlyRate,
                onHourlyRateChange = { hourlyRate = it },
                projectFee = projectFee,
                onProjectFeeChange = { projectFee = it },
                currency = currency,
                onCurrencyChange = { currency = it },
                paymentTerms = paymentTerms,
                onPaymentTermsChange = { paymentTerms = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
            val statusInvitation = stringResource(id = R.string.holding)

            val loadingState by homeViewModel.loadingState.collectAsState()
            val invitationSent by homeViewModel.invitationSent.collectAsState()

            val context = LocalContext.current

            LaunchedEffect(invitationSent) {
                if (invitationSent) navController.popBackStack()
                else Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }

            // Send Button
            Button(
                onClick = {
                    homeViewModel.clearToken()
                    homeViewModel.getUserToken(user.id ?: -1)
                    homeViewModel.matchCurrentProfile(user, statusInvitation, paymentTerms)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !loadingState && subject.isNotBlank() && message.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.whatsapp),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(id = R.color.whatsapp).copy(alpha = 0.3f)
                )
            ) {
                if (loadingState) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sending...")
                } else {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Send Invitation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Handle loading state
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(2000) // Simulate API call
            isLoading = false
            showSuccessDialog = true
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        SuccessDialog(
            candidateName = user.fullName ?: "",
            onDismiss = {
                showSuccessDialog = false
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun CandidateInfoCard(candidate: User) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhatsAppLightGreen
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Initials Avatar
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = colorResource(id = R.color.whatsapp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = candidate.fullName?.trimStart()?.split(" ")?.map { it.first() }
                                ?.joinToString("") ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {

                    //candidate.getYearsExp()
                    val exp = candidate.experience ?: emptyList()

                    val lastExperience = if (exp.isNotEmpty()) exp[exp.lastIndex]
                    else Experience()

                    Text(
                        text = candidate.fullName ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = candidate.preferredActivitySector ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${lastExperience.companyName} • 5+ years",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationFormSection(
    subject: String,
    onSubjectChange: (String) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    contractType: ContractType,
    onContractTypeChange: (ContractType) -> Unit,
    hourlyRate: String,
    onHourlyRateChange: (String) -> Unit,
    projectFee: String,
    onProjectFeeChange: (String) -> Unit,
    currency: String,
    onCurrencyChange: (String) -> Unit,
    paymentTerms: String,
    onPaymentTermsChange: (String) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Subject
        OutlinedTextField(
            value = subject,
            onValueChange = onSubjectChange,
            label = { Text("Subject") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(Icons.Default.Subject, contentDescription = "Subject")
            }
        )

        // Message
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    Icons.Default.Message,
                    contentDescription = "Message",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        )
        val whatsAppGreen = colorResource(id = R.color.whatsapp)
        // Fee Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = WhatsAppGreenSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.fee_text),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // Contract Type Selection
                Text(
                    text = stringResource(id = R.string.post_type_text),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Contract Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onContractTypeChange(ContractType.CONTRACT) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (contractType == ContractType.CONTRACT)
                                WhatsAppLightGreen else MaterialTheme.colorScheme.surface
                        ),
                        border = if (contractType == ContractType.CONTRACT)
                            BorderStroke(2.dp, whatsAppGreen) else null
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Work,
                                contentDescription = stringResource(id = R.string.type1_text),
                                tint = if (contractType == ContractType.CONTRACT)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = R.string.type1_text),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (contractType == ContractType.CONTRACT)
                                    FontWeight.Bold else FontWeight.Normal,
                                color = if (contractType == ContractType.CONTRACT)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Freelance Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onContractTypeChange(ContractType.FREELANCE) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (contractType == ContractType.FREELANCE)
                                WhatsAppLightGreen else MaterialTheme.colorScheme.surface
                        ),
                        border = if (contractType == ContractType.FREELANCE)
                            BorderStroke(2.dp, whatsAppGreen) else null
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = stringResource(id = R.string.type2_text),
                                tint = if (contractType == ContractType.FREELANCE)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = R.string.type2_text),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (contractType == ContractType.FREELANCE)
                                    FontWeight.Bold else FontWeight.Normal,
                                color = if (contractType == ContractType.FREELANCE)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Fee Input Fields
               /* if (contractType == ContractType.FREELANCE) {
                    OutlinedTextField(
                        value = projectFee,
                        onValueChange = onProjectFeeChange,
                        label = { Text(stringResource(id = R.string.salary_text)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        leadingIcon = {
                            Text(text = "TND", fontWeight = FontWeight.Bold)
                        },
                        placeholder = { Text("5000") }
                    )
                } else {
                    OutlinedTextField(
                        value = projectFee,
                        onValueChange = onProjectFeeChange,
                        label = { Text(stringResource(id = R.string.salary_text)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        leadingIcon = {
                            Text(text = "TND", fontWeight = FontWeight.Bold)
                        },
                        placeholder = { Text("5000") }
                    )

                }*/

                val placeHolder = if (contractType == ContractType.FREELANCE) "Le contract sa sera per hour"
                else "Le contract sa sera en CDI"

                // Payment Terms
                OutlinedTextField(
                    value = paymentTerms,
                    onValueChange = onPaymentTermsChange,
                    label = { Text("Contract description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Description, contentDescription = "Payment Terms")
                    },
                    placeholder = { Text(placeHolder) }
                )
            }
        }
    }
}

@Composable
fun SuccessDialog(
    candidateName: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(32.dp),
                color = colorResource(id = R.color.whatsapp).copy(alpha = 0.1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        modifier = Modifier.size(32.dp),
                        tint = colorResource(id = R.color.whatsapp)
                    )
                }
            }
        },
        title = {
            Text(
                text = "Invitation Sent!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Your interview invitation has been successfully sent to $candidateName. They will receive an email with all the details.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.whatsapp),
                    contentColor = Color.White
                )
            ) {
                Text("Done")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

