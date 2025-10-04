package com.example.myjob.feature.invitation.company

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GenericSearch
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.candidateUser
import com.example.myjob.common.GlobalEntries.notificationMessage
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
    var paymentTerms by remember(contractType) {
        mutableStateOf(
            if (contractType == ContractType.FREELANCE) "Le contract sa sera per hour" else "Le contract sa sera en CDI"
        )
    }
    var location by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val invitationParam by homeViewModel.invitationParam.collectAsState()

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

    var contractWorkOpen by remember { mutableStateOf(false) }
    var secondContractWorkOpen by remember { mutableStateOf(false) }
    val list = listOf(
        "CIVP", "CDI", "CDD", "KARAMA", "AUTRE"
    )

    val secondList = listOf(
        stringResource(id = R.string.intern_user_text),
        stringResource(id = R.string.trainer_user_text),
        stringResource(id = R.string.event_user_text),
    )

    var isErrorDuration by remember { mutableStateOf(false) }
    var isErrorSubject by rememberSaveable { mutableStateOf(false) }
    var isErrorMessage by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    homeViewModel = homeViewModel,
                    isErrorDuration = isErrorDuration,
                    isErrorSubject = isErrorSubject,
                    isErrorMessage = isErrorMessage,
                    onChangeDurationError = {
                        isErrorDuration = false
                    },
                    user = user,
                    contractWorkOpen = contractWorkOpen,
                    nameContract = invitationParam.nameContract,
                    openList = {
                        contractWorkOpen = !contractWorkOpen
                    },
                    secondContractWorkOpen = secondContractWorkOpen,
                    nameSecondContract = invitationParam.nameSecondContract,
                    openSecondList = {
                        secondContractWorkOpen = !secondContractWorkOpen
                    },
                    subject = subject,
                    onSubjectChange = {
                        if (it.isNotEmpty()) isErrorSubject = false
                        subject = it
                    },
                    message = message,
                    onMessageChange = {
                        if (it.isNotEmpty()) isErrorMessage = false
                        message = it
                    },
                    contractType = contractType,
                    onContractTypeChange = { contractType = it },
                    paymentTerms = paymentTerms,
                    onPaymentTermsChange = { paymentTerms = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
                val statusInvitation = stringResource(id = R.string.holding)

                val loadingState by homeViewModel.loadingState.collectAsState()
                val invitationSent by homeViewModel.invitationSent.collectAsState()

                val duration by homeViewModel.durationMission.collectAsState()

                val context = LocalContext.current

                LaunchedEffect(invitationSent) {
                    if (invitationSent) navController.popBackStack()
                    else Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                }

                // Send Button
                Button(
                    onClick = {
                        val durationNotEmpty = duration.isNotEmpty()
                        val subjectNotEmpty = subject.isNotEmpty()
                        val messageNotEmpty = message.isNotEmpty()

                        if (duration.isEmpty()) isErrorDuration = true
                        if (subject.isEmpty()) isErrorSubject = true
                        if (message.isEmpty()) isErrorMessage = true

                        if (durationNotEmpty && subjectNotEmpty && messageNotEmpty) {
                            homeViewModel.clearToken()
                            homeViewModel.getUserToken(user.id ?: -1)
                            homeViewModel.matchCurrentProfile(user, statusInvitation, paymentTerms)
                        }
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
                            "Send",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        AnimatedVisibility(
            visible = contractWorkOpen,
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
            GenericSearch(
                mListOfJobs = list,
                onDismissRequest = {
                    contractWorkOpen = false
                },
                onSelectedBank = { text, index ->
                    homeViewModel.changeContractWork(text)
                    contractWorkOpen = false
                },
                title = "Select work contract"
            )
        }

        AnimatedVisibility(
            visible = secondContractWorkOpen,
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
            GenericSearch(
                mListOfJobs = secondList,
                onDismissRequest = {
                    secondContractWorkOpen = false
                },
                onSelectedBank = { text, index ->
                    homeViewModel.changeSecondContractWork(text)
                    secondContractWorkOpen = false
                },
                title = "Select work contract"
            )
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
    homeViewModel: HomeViewModel,
    isErrorDuration: Boolean,
    isErrorSubject: Boolean,
    isErrorMessage: Boolean,
    onChangeDurationError: () -> Unit,
    user: User,
    contractWorkOpen: Boolean,
    nameContract: String,
    openList: (Boolean) -> Unit,
    secondContractWorkOpen: Boolean,
    nameSecondContract: String,
    openSecondList: (Boolean) -> Unit,
    subject: String,
    onSubjectChange: (String) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    contractType: ContractType,
    onContractTypeChange: (ContractType) -> Unit,
    paymentTerms: String,
    onPaymentTermsChange: (String) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val duration by homeViewModel.durationMission.collectAsState()


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Subject
        OutlinedTextField(
            value = subject,
            onValueChange = onSubjectChange,
            label = { Text("Subject") },
            isError = isErrorSubject,
            supportingText = {
                if (isErrorSubject) {
                    Text("Field cannot be empty")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(Icons.Default.Subject, contentDescription = "Subject")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(id = R.color.whatsapp),
                focusedLabelColor = colorResource(id = R.color.whatsapp),
                focusedLeadingIconColor = colorResource(id = R.color.whatsapp)
            )
        )

        // Message
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text("Message") },
            isError = isErrorMessage,
            supportingText = {
                if (isErrorMessage) {
                    Text("Field cannot be empty")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    Icons.Default.Message,
                    contentDescription = "Message",
                    modifier = Modifier.padding(top = 8.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(id = R.color.whatsapp),
                focusedLabelColor = colorResource(id = R.color.whatsapp),
                focusedLeadingIconColor = colorResource(id = R.color.whatsapp)
            )
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

                val isContract =
                    user.preferredWorkType?.contains(stringResource(id = R.string.type1_text))
                        ?: false
                val isFreelance =
                    user.preferredWorkType?.contains(stringResource(id = R.string.type2_text))
                        ?: false
                val isBoth = isContract && isFreelance

                if (isBoth) {

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

                    if (contractType == ContractType.FREELANCE) {
                        OutlinedTextField(
                            value = duration,
                            onValueChange = {
                                if (it.isNotEmpty()) {
                                    onChangeDurationError()
                                    homeViewModel.changeDuration(it)
                                }
                            },
                            label = { Text(stringResource(id = R.string.duration_text)) },
                            isError = isErrorDuration,
                            supportingText = {
                                if (isErrorDuration) {
                                    Text("Field cannot be empty")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            leadingIcon = {
                                Icon(Icons.Default.Timer, contentDescription = "Duration")
                            },
                            placeholder = { Text("Par mois") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.whatsapp),
                                focusedLabelColor = colorResource(id = R.color.whatsapp),
                                focusedLeadingIconColor = colorResource(id = R.color.whatsapp)
                            )
                        )
                    } else {
                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            expanded = contractWorkOpen,
                            onExpandedChange = {
                                openList(it)
                            }
                        ) {

                            OutlinedTextField(
                                value = nameContract,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select work contract") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = contractWorkOpen) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colorResource(id = R.color.whatsapp),
                                    focusedLabelColor = colorResource(id = R.color.whatsapp)
                                )
                            )
                        }

                        if (nameContract == "AUTRE") {

                            homeViewModel.changeTypeContract(nameContract)

                            ExposedDropdownMenuBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                expanded = secondContractWorkOpen,
                                onExpandedChange = {
                                    openSecondList(it)
                                }
                            ) {

                                OutlinedTextField(
                                    value = nameSecondContract,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Select work contract") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = secondContractWorkOpen
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colorResource(id = R.color.whatsapp),
                                        focusedLabelColor = colorResource(id = R.color.whatsapp)
                                    )
                                )
                            }
                        } else homeViewModel.changeTypeContract(stringResource(id = R.string.type1_text))

                        if (nameContract == "AUTRE" || nameContract == "CDD") {

                            OutlinedTextField(
                                value = duration,
                                onValueChange = {
                                    if (it.isNotEmpty()) {
                                        onChangeDurationError()
                                        homeViewModel.changeDuration(it)
                                    }
                                },
                                isError = isErrorDuration,
                                supportingText = {
                                    if (isErrorDuration) {
                                        Text("Field cannot be empty")
                                    }
                                },
                                label = { Text(stringResource(id = R.string.duration_text)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.Timer, contentDescription = "Duration")
                                },
                                placeholder = { Text("Par mois") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colorResource(id = R.color.whatsapp),
                                    focusedLabelColor = colorResource(id = R.color.whatsapp),
                                    focusedLeadingIconColor = colorResource(id = R.color.whatsapp)
                                )
                            )
                        }
                    }

                } else if (isContract) {

                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        expanded = contractWorkOpen,
                        onExpandedChange = {
                            openList(it)
                        }
                    ) {

                        OutlinedTextField(
                            value = nameContract,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select work contract") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = contractWorkOpen) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.whatsapp),
                                focusedLabelColor = colorResource(id = R.color.whatsapp)
                            )
                        )
                    }

                    if (nameContract == "AUTRE") {

                        homeViewModel.changeTypeContract(nameContract)

                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            expanded = secondContractWorkOpen,
                            onExpandedChange = {
                                openSecondList(it)
                            }
                        ) {

                            OutlinedTextField(
                                value = nameSecondContract,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select work contract") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = secondContractWorkOpen) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colorResource(id = R.color.whatsapp),
                                    focusedLabelColor = colorResource(id = R.color.whatsapp)
                                )
                            )
                        }
                    } else homeViewModel.changeTypeContract(stringResource(id = R.string.type1_text))

                    if (nameContract == "AUTRE" || nameContract == "CDD") {

                        OutlinedTextField(
                            value = duration,
                            onValueChange = {
                                if (it.isNotEmpty()) {
                                    onChangeDurationError()
                                    homeViewModel.changeDuration(it)
                                }
                            },
                            isError = isErrorDuration,
                            supportingText = {
                                if (isErrorDuration) {
                                    Text("Field cannot be empty")
                                }
                            },
                            label = { Text(stringResource(id = R.string.duration_text)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            leadingIcon = {
                                Icon(Icons.Default.Timer, contentDescription = "Duration")
                            },
                            placeholder = { Text("Par mois") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.whatsapp),
                                focusedLabelColor = colorResource(id = R.color.whatsapp),
                                focusedLeadingIconColor = colorResource(id = R.color.whatsapp)
                            )
                        )
                    }

                } else {
                    homeViewModel.changeTypeContract(stringResource(id = R.string.type2_text))
                    OutlinedTextField(
                        value = duration,
                        onValueChange = {
                            if (it.isNotEmpty()) {
                                onChangeDurationError()
                                homeViewModel.changeDuration(it)
                            }
                        },
                        isError = isErrorDuration,
                        supportingText = {
                            if (isErrorDuration) {
                                Text("Field cannot be empty")
                            }
                        },
                        label = { Text(stringResource(id = R.string.duration_text)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Timer, contentDescription = "Duration")
                        },
                        placeholder = { Text("Par mois") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.whatsapp),
                            focusedLabelColor = colorResource(id = R.color.whatsapp),
                            focusedLeadingIconColor = colorResource(id = R.color.whatsapp)
                        )
                    )
                }

                val placeHolder =
                    if (contractType == ContractType.FREELANCE) "Le contract sa sera per hour"
                    else "Le contract va commencer le 10/12/2025"

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
                    placeholder = { Text(placeHolder) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.whatsapp),
                        focusedLabelColor = colorResource(id = R.color.whatsapp),
                        focusedLeadingIconColor = colorResource(id = R.color.whatsapp)
                    )
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

