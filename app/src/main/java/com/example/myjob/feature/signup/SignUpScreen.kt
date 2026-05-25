package com.example.myjob.feature.signup

//noinspection UsingMaterialAndMaterial3Libraries
import FreelanceSector
import FreelanceSectorSelectionScreen
import FreelanceService
import FreelanceServiceSelectionScreen
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.base.MyApp
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GlobalEntries.emailGoogleAccount
import com.example.myjob.feature.demands.NewFormTextField
import com.example.myjob.feature.demands.ServiceCategory
import com.example.myjob.feature.login.gmail.GoogleAuthUiClient
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    lifecycleScope: LifecycleCoroutineScope,
    googleAuthUiClient: GoogleAuthUiClient
) {

    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx().toInt()
    }

    val isFirstNameValid by viewModel.isFirstNameValid.collectAsState()
    val isLastNameValid by viewModel.isLastNameValid.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val user by viewModel.user.collectAsState()
    val isCompanyNameValid by viewModel.isCompanyNameValid.collectAsState()

    var selectedCategory by remember {
        mutableStateOf(
            if (user.category == ServiceCategory.IDLE) null
            else user.category
        )
    }

    var selectedFreelanceSector by remember {
        mutableStateOf(
            if (user.freelanceSector.id == "") null
            else user.freelanceSector
        )
    }

    var selectedFreelanceService by remember {
        mutableStateOf(
            if (user.freelanceService.id == "") null
            else user.freelanceService
        )
    }

    var selectedCategoryText by remember { mutableStateOf(user.otherCategory) }
    var activatedCheckCategory by remember { mutableStateOf(false) }
    var selectedSectorText by remember { mutableStateOf(user.otherSector) }

    var activatedCheckSector by remember { mutableStateOf(false) }
    var activatedCheckSectorName by remember { mutableStateOf(false) }
    var activatedCheckServiceName by remember { mutableStateOf(false) }

    var serviceDescription by remember { mutableStateOf("") }
    var serviceName by remember { mutableStateOf("") }
    var sectorName by remember { mutableStateOf("") }

    var showCategoryDialog by remember { mutableStateOf(false) }
    var showSectorDialog by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(user.userServiceName) }
    var description by remember { mutableStateOf(user.bio) }

    var textErrorCategory by remember { mutableStateOf("") }
    var activatedCheckUsername by remember { mutableStateOf(false) }
    var textErrorUsername by remember { mutableStateOf("") }

    var selectedIndex by remember { mutableIntStateOf(0) }

    var activatedCheckCompanyName by remember { mutableStateOf(false) }
    var activatedCheckEmail by remember { mutableStateOf(false) }
    var activatedCheckPassword by remember { mutableStateOf(false) }
    var activatedCheckFirstName by remember { mutableStateOf(false) }
    var activatedCheckLastName by remember { mutableStateOf(false) }
    var activatedCheckConfirmPassword by remember { mutableStateOf(false) }

    var userFirstName by remember { mutableStateOf(user.firstName ?: "") }
    val submitEnabled by remember { derivedStateOf { isFirstNameValid } }

    var userLastName by remember { mutableStateOf(user.lastName ?: "") }
    val lastNameVerified by remember { derivedStateOf { isLastNameValid } }

    var companyName by remember { mutableStateOf(user.companyName ?: "") }
    val companyNameVerified by remember { derivedStateOf { isCompanyNameValid } }

    var email by remember { mutableStateOf(emailGoogleAccount) }
    val emailVerified by remember { derivedStateOf { isEmailValid } }

    var password by remember { mutableStateOf(user.password ?: "") }
    val passwordVerified by remember { derivedStateOf { isPasswordValid } }

    var confirmPassword by remember { mutableStateOf("") }
    val isConfirmPasswordValid by viewModel.isConfirmPasswordValid.collectAsState()
    val confirmPasswordVerified by remember { derivedStateOf { isConfirmPasswordValid } }

    var isProgressing by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    val saveUserRes by viewModel.saveUserRes.collectAsState()

    var error by remember { mutableStateOf("") }

    val app = context.applicationContext as MyApp

    LaunchedEffect(saveUserRes.token?.isNotEmpty()) {
        isProgressing = false
        if (saveUserRes.token?.isNotEmpty() == true) {
            when (selectedIndex) {
                1 -> navController.navigate(Screen.SearchWordScreen.route)
                0 -> navController.navigate(Screen.CompanyProfileForm.route)
                else -> navController.navigate(Screen.ServiceProfileForm.route)
            }
        }
    }

    if (showDialog) {
        isProgressing = false
        CustomDialog(isSuccess = false, message = error) {
            showDialog = false
            isProgressing = false
        }
    }

    LaunchedEffect(saveUserRes.messageError?.isNotEmpty()) {
        if (saveUserRes.messageError?.isNotEmpty() == true) {
            showDialog = true
            error = saveUserRes.messageError.toString()
        }
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            Log.i("feklzhgzg", "SignUpScreen: ${googleAuthUiClient.getSignedInUser()}")
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            //onDirect()
            viewModel.resetState()
        }
    }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    viewModel.changeRole(stringResource(id = R.string.choose_companies_text))

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(id = R.string.create_account_text),
                        modifier = Modifier.padding(top = 20.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily.Default,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    val listRole = listOf(
                        stringResource(id = R.string.choose_companies_text),
                        stringResource(id = R.string.choose_candidate_text),
                        stringResource(id = R.string.service_text)
                    )

                    RoleSection(
                        listRole = listRole,
                        interactionSource = interactionSource,
                        onClick = {
                            selectedIndex = it
                            viewModel.changeRole(listRole[it])
                            viewModel.changeRoleIndex(it)
                        }
                    )
                }

                if (selectedIndex == 2) {

                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Sécteur de service *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val sectorCheck = (selectedFreelanceSector == null || selectedFreelanceSector?.id == "")

                            val color =
                                if (activatedCheckSector && sectorCheck) Red
                                else Transparent

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF9FAFB))
                                    .border(1.dp, color, RoundedCornerShape(12.dp))
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { showSectorDialog = true }
                                    .padding(16.dp)
                            ) {
                                selectedFreelanceSector?.let { category ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = category.icon, fontSize = 24.sp)
                                        Text(
                                            text = category.name,
                                            color = Color(0xFF1F2937),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } ?: run {
                                    Text(
                                        text = "Sélectionner un sécteur",
                                        color = Color(0xFF9CA3AF),
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            if (activatedCheckSector && sectorCheck) {
                                Text(
                                    modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                    text = "Vous devez choisir un sécteur",
                                    color = Red
                                )
                            }

                        }
                    }

                    if (selectedFreelanceSector?.name?.contains("Autres") == true) {
                        com.example.myjob.feature.demands.FormTextField(
                            value = sectorName,
                            onValueChange = {
                                sectorName = it
                            },
                            isCheckActivated = activatedCheckSectorName,
                            isError = sectorName.isEmpty(),
                            errorText = "Vous devez remplir un sécteur",
                            label = "Nom du sécteur *",
                            modifier = Modifier.padding(top = 16.dp),
                            placeholder = ""
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        com.example.myjob.feature.demands.FormTextField(
                            value = serviceName,
                            onValueChange = {
                                serviceName = it
                            },
                            isCheckActivated = activatedCheckServiceName,
                            isError = serviceName.isEmpty(),
                            errorText = "Vous devez remplir un sérvice",
                            label = "Nom du sérvice *",
                            modifier = Modifier.padding(top = 16.dp),
                            placeholder = "Ex: Forgeron"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        com.example.myjob.feature.demands.FormTextField(
                            value = serviceDescription,
                            onValueChange = {
                                serviceDescription = it
                            },
                            isCheckActivated = false,
                            minLines = 2,
                            maxLines = 3,
                            errorText = "",
                            label = "Déscription du sérvice *",
                            modifier = Modifier.padding(top = 16.dp),
                            placeholder = "Ex: Forgeron est une métier..."
                        )

                    } else {
                        Spacer(modifier = Modifier.height(10.dp))

                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
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

                                val serviceCheck = (selectedFreelanceService == null || selectedFreelanceService?.id == "")
                                val categoryCheck = (selectedCategory == ServiceCategory.IDLE || selectedCategory == null)

                                val color =
                                    if (activatedCheckCategory && serviceCheck) Red
                                    else Transparent

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF9FAFB))
                                        .border(1.dp, color, RoundedCornerShape(12.dp))
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) { showCategoryDialog = true }
                                        .padding(16.dp)
                                ) {
                                    selectedFreelanceService?.let { category ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(text = category.icon, fontSize = 24.sp)
                                            Text(
                                                text = category.name,
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

                                if (activatedCheckCategory && serviceCheck) {
                                    Text(
                                        modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                        text = "Vous devez choisir une categorie",
                                        color = Red
                                    )
                                }

                            }
                        }
                    }

                    // Title Field
                    com.example.myjob.feature.demands.FormTextField(
                        value = title ?: "",
                        onValueChange = {
                            title = it
                            viewModel.changeServiceUserName(title ?: "")
                        },
                        isCheckActivated = activatedCheckUsername,
                        isError = title?.isEmpty() == true,
                        errorText = "Vous devez remplir le nom",
                        label = "Nom Complet *",
                        modifier = Modifier.padding(top = 16.dp),
                        placeholder = "Ex: Aladin Abidi"
                    )

                } else if (selectedIndex == 1) {
                    NewFormTextField(
                        value = userFirstName,
                        onValueChange = {
                            userFirstName = it
                            if (activatedCheckFirstName) viewModel.validateFirstName(it)
                            viewModel.changeUserFirstName(it)
                        },
                        label = stringResource(id = R.string.first_name_text),
                        modifier = Modifier.padding(top = 16.dp),
                        isRequired = true,
                        placeholder = stringResource(id = R.string.first_name_text)
                    )

                    if (activatedCheckFirstName) {
                        if (userFirstName.isEmpty() || !submitEnabled) {

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (submitEnabled) "Valid First name" else stringResource(id = R.string.first_name_error),
                                color = if (submitEnabled) Color.Green else Red
                            )
                        }
                    }

                    NewFormTextField(
                        value = userLastName,
                        onValueChange = {
                            userLastName = it
                            if (activatedCheckLastName) viewModel.validateLastName(it)
                            viewModel.changeUserLastName(it)
                        },
                        label = stringResource(id = R.string.last_name_text),
                        modifier = Modifier.padding(top = 16.dp),
                        isRequired = true,
                        placeholder = stringResource(id = R.string.last_name_text)
                    )

                    if (activatedCheckLastName) {
                        if (userLastName.isEmpty() || !lastNameVerified) {
                            Log.i("submitEnabled", "SignUpScreen: $lastNameVerified")

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = stringResource(id = R.string.last_name_error),
                                color = Red
                            )
                        }
                    }

                } else if (selectedIndex == 0) {
                    //company
                    NewFormTextField(
                        value = companyName,
                        onValueChange = {
                            companyName = it
                            if (activatedCheckCompanyName) viewModel.validateCompanyName(it)
                            viewModel.changeCompanyName(it)
                        },
                        label = stringResource(id = R.string.company_name_text),
                        modifier = Modifier.padding(top = 16.dp),
                        isRequired = true,
                        placeholder = stringResource(id = R.string.company_name_text)
                    )

                    if (activatedCheckCompanyName) {
                        if (companyName.isEmpty() || !companyNameVerified) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (companyNameVerified) "" else stringResource(id = R.string.company_name_error),
                                color = if (companyNameVerified) Color.Green else Red
                            )
                        }
                    }
                }

                NewFormTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (activatedCheckEmail) viewModel.validateEmail(it)
                        viewModel.changeUserEmail(it)
                    },
                    label = "Email",
                    modifier = Modifier.padding(top = 16.dp),
                    isRequired = true,
                    placeholder = "Email"
                )

                if (activatedCheckEmail) {
                    if (email.isEmpty() || !emailVerified) {
                        Log.i("submitEnabled", "SignUpScreen: $emailVerified")

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = if (emailVerified) "Valid First name" else stringResource(id = R.string.error_email),
                            color = if (emailVerified) Color.Green else Red
                        )
                    }
                }

                NewFormTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (activatedCheckPassword) viewModel.validatePassword(it)
                        viewModel.changeUserPassword(it)
                    },
                    isPassword = true,
                    label = stringResource(id = R.string.password_text),
                    modifier = Modifier.padding(top = 16.dp),
                    isRequired = true,
                    placeholder = stringResource(id = R.string.password_text)
                )

                if (activatedCheckPassword) {
                    if (password.isEmpty() || !passwordVerified) {

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = if (passwordVerified) "Valid First name" else stringResource(id = R.string.error_password),
                            color = if (passwordVerified) Color.Green else Red
                        )
                    }
                }

                NewFormTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        viewModel.confirmPassword(it)
                        if (activatedCheckConfirmPassword) viewModel.checkConfirmPassword(
                            password,
                            confirmPassword
                        )
                    },
                    label = stringResource(id = R.string.confirm_password_text),
                    modifier = Modifier.padding(top = 16.dp),
                    isRequired = true,
                    placeholder = stringResource(id = R.string.confirm_password_text),
                    isPassword = true
                )

                if (activatedCheckConfirmPassword) {
                    if (confirmPassword.isEmpty() || !confirmPasswordVerified) {
                        Log.i("confirmPasswordVerified", "SignUpScreen: $confirmPasswordVerified")

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = stringResource(id = R.string.confirm_password_error),
                            color = Red
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val checkAll = when (selectedIndex) {
                                0 -> {
                                    val companyNameValidator =
                                        viewModel.validateCompanyName(companyName)
                                    if (!companyNameValidator) activatedCheckCompanyName = true
                                    companyNameValidator
                                }
                                1 -> {
                                    val firstNameValidator =
                                        viewModel.validateFirstName(userFirstName)
                                    val lastNameValidator =
                                        viewModel.validateLastName(userLastName)

                                    if (!firstNameValidator) activatedCheckFirstName = true
                                    if (!lastNameValidator) activatedCheckLastName = true

                                    firstNameValidator && lastNameValidator
                                }
                                else -> {
                                    val serviceCheck = (selectedFreelanceService != null && selectedFreelanceService?.id != "")
                                    val sectorCheck = (selectedFreelanceSector != null && selectedFreelanceSector?.id != "")

                                    val categoryValidator =
                                        serviceCheck || selectedCategoryText.isNotEmpty()

                                    val usernameValidator = title?.isNotEmpty() == true


                                    if (!usernameValidator) activatedCheckUsername = true

                                    if (!sectorCheck) {
                                        if (selectedFreelanceSector?.name?.contains("Autres") == true) {
                                            if (!sectorName.isNotEmpty()) activatedCheckSectorName = true
                                            if (!serviceName.isNotEmpty()) activatedCheckServiceName = true
                                        } else if (!categoryValidator) activatedCheckCategory = true

                                        activatedCheckSector = true
                                    }

                                    val isOther = if (selectedFreelanceSector?.name?.contains("Autres") == true)
                                        sectorName.isNotEmpty() && serviceName.isNotEmpty()
                                    else categoryValidator

                                    usernameValidator && sectorCheck && isOther
                                }
                            }

                            val emailValidator = viewModel.validateEmail(email)
                            val passwordValidator = viewModel.validatePassword(password)
                            val confirmValidator =
                                viewModel.checkConfirmPassword(password, confirmPassword)

                            if (!emailValidator) activatedCheckEmail = true
                            if (!passwordValidator) activatedCheckPassword = true
                            if (!confirmValidator) activatedCheckConfirmPassword = true

                            if (checkAll && emailValidator && passwordValidator && confirmValidator) {
                                CoroutineScope(Dispatchers.Main).launch {

                                    if (selectedFreelanceSector?.name?.contains("Autres") == true) {
                                        val freelanceSector = FreelanceSector()

                                        freelanceSector.id = sectorName
                                        freelanceSector.name = sectorName
                                        freelanceSector.icon = "⚙️"

                                        val freelanceService = FreelanceService()

                                        freelanceService.id = serviceName
                                        freelanceService.name = serviceName
                                        freelanceService.description = serviceDescription
                                        freelanceService.icon = "⚙️"

                                        user.freelanceSector = freelanceSector
                                        user.freelanceService = freelanceService
                                    }

                                    isProgressing = true
                                    delay(1000L)
                                    viewModel.saveUser(user)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.whatsapp)
                        )
                    ) {
                        Text(
                            stringResource(id = R.string.create_new_account_text),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Screen.LoginScreen.route)
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.have_account_text))
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = stringResource(id = R.string.log_in_text),
                            textDecoration = TextDecoration.Underline,
                            color = Blue
                        )
                    }
                }
            }

            /*

            filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Category Filter
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    val listSector = getAllFreelanceSectors()

                    items(listSector.take(10).size) { index ->
                        if (index != 0) {
                            val sector = listSector[index]
                            FilterChip(
                                selected = listSelected[index],
                                onClick = {
                                    listSelected = listSelected.mapIndexed { i, item ->
                                        if (i == index) {
                                            !item
                                        } else item
                                    }.toMutableList()

                                    /*if (listSelected[index]) {
                                        if (!listFilter.contains(category))
                                            listFilter =
                                                (listFilter + category).toMutableList()
                                    } else {
                                        if (listFilter.contains(category))
                                            listFilter =
                                                (listFilter - category).toMutableList()
                                    }*/
                                },
                                label = {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(sector.icon)
                                        Text(sector.name)
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF049344),
                                    selectedLabelColor = White
                                )
                            )
                        }
                    }

                    item {
                        FilterChip(
                            selected = true,
                            onClick = {
                                showCategoryDialog = true
                            },
                            label = {
                                Text("Tous")
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF049344),
                                selectedLabelColor = White
                            )
                        )
                    }
                }
            }


            if (showCategoryDialog) {
                AlertDialog(
                    onDismissRequest = { showCategoryDialog = false },
                    title = { Text("Choisir une catégorie") },
                    text = {
                        selectedFreelanceSector?.let {
                            LazyColumn {
                                items(it.services) { category ->
                                    FreelanceDialogItem(
                                        category = category,
                                        onClick = {
                                            selectedFreelanceService = category

                                            viewModel.changeFreelanceService(
                                                selectedFreelanceService ?: FreelanceService()
                                            )
                                            showCategoryDialog = false
                                        }
                                    )
                                }
                            }
                        } ?: run {
                            Text("Vous devez choisir un secteur")
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

            if (showSectorDialog) {
                AlertDialog(
                    onDismissRequest = { showSectorDialog = false },
                    title = { Text("Choisir un sécteur") },
                    text = {
                        LazyColumn {
                            items(getAllFreelanceSectors()) { category ->
                                FreelanceSectorDialogItem(
                                    category = category,
                                    onClick = {
                                        selectedFreelanceSector = category
                                        viewModel.changeFreelanceSector(
                                            selectedFreelanceSector ?: FreelanceSector()
                                        )
                                        showSectorDialog = false
                                    }
                                )
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showSectorDialog = false }) {
                            Text("Annuler", color = Color(0xFF049344))
                        }
                    }
                )
            }*/

            AnimatedVisibility(
                visible = showSectorDialog,
                enter = slideInVertically(
                    initialOffsetY = { it }, // Slide from below the screen
                    animationSpec = tween(durationMillis = 600) // Set animation duration
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, // Slide out upwards
                    animationSpec = tween(durationMillis = 600) // Set animation duration
                )
            ) {
                FreelanceSectorSelectionScreen (
                    onSelectSector = { service ->
                        selectedFreelanceSector = service
                        viewModel.changeFreelanceSector(
                            selectedFreelanceSector ?: FreelanceSector()
                        )
                        showSectorDialog = false
                    },
                    dismiss = {
                        showSectorDialog = false
                    }
                )
            }

            AnimatedVisibility(
                visible = showCategoryDialog,
                enter = slideInVertically(
                    initialOffsetY = { it }, // Slide from below the screen
                    animationSpec = tween(durationMillis = 600) // Set animation duration
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, // Slide out upwards
                    animationSpec = tween(durationMillis = 600) // Set animation duration
                )
            ) {
                selectedFreelanceSector?.let {
                    FreelanceServiceSelectionScreen(
                        selectedSector = it,
                        onSelectService = { service ->

                            selectedFreelanceService = service

                            viewModel.changeFreelanceService(
                                selectedFreelanceService ?: FreelanceService()
                            )
                            showCategoryDialog = false
                        },
                        dismiss = {
                            showCategoryDialog = false
                        }
                    )
                } ?: run {
                    Text("Vous devez choisir un secteur")
                }
            }

            if (isProgressing) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

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
        }
    }
}

@Composable
fun RoleSection(
    listRole: List<String>,
    interactionSource: MutableInteractionSource,
    onClick: (Int) -> Unit = {}
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF25D366),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(id = R.string.choose_role_text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            var selected by remember { mutableIntStateOf(0) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listRole.forEachIndexed { index, lang ->
                    val isSelected = index == selected
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected)
                                    colorResource(id = R.color.whatsapp)
                                else
                                    Color(0xFFF3F4F6)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selected = index
                                onClick(index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected)
                                Color(0xFFF3F4F6)
                            else
                                Color(0xFF6B7280)
                        )
                    }
                }
            }
        }
    }
}