package com.example.myjob.feature.signup

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
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
import com.example.myjob.feature.home.filter.flowHandling
import com.example.myjob.feature.login.gmail.GoogleAuthUiClient
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.test.FormTextField
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

    var roleWorkOpen by remember { mutableStateOf(false) }
    var secondRoleWorkOpen by remember { mutableStateOf(false) }

    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedCategories by viewModel.selectedCat.collectAsState()

    val selectedSecondRole by viewModel.selectedSecondRole.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()


    val isFirstNameValid by viewModel.isFirstNameValid.collectAsState()
    val isLastNameValid by viewModel.isLastNameValid.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val user by viewModel.user.collectAsState()
    val isCompanyNameValid by viewModel.isCompanyNameValid.collectAsState()

    var selectedIndex by remember { mutableStateOf(0) }

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

    Log.i("emailGoogleAccount", "SignUpScreen: $emailGoogleAccount")

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

    val listCompanies by viewModel.listCompanies.collectAsState()

    val app = context.applicationContext as MyApp
    LaunchedEffect(listCompanies) {
        app.listCompanies.removeLast()
        if (!app.listCompanies.containsAll(listCompanies)) app.listCompanies.addAll(listCompanies)
    }

    LaunchedEffect(saveUserRes.token?.isNotEmpty()) {
        isProgressing = false
        if (saveUserRes.token?.isNotEmpty() == true) {
            if (selectedIndex == 1) {
                navController.navigate(Screen.SearchWordScreen.route)
            } else navController.navigate(Screen.CompanyProfileForm.route)
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

    /*val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    val userData = signInResult.data
                    Log.i("feklzhgzg", "SignUpScreen: $userData")
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )*/

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
                    .padding(horizontal = 10.dp)
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

                    Spacer(modifier = Modifier.height(30.dp))

                    val listRole = listOf(
                        stringResource(id = R.string.choose_companies_text),
                        stringResource(id = R.string.choose_candidate_text)
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

                if (selectedIndex == 1) {

                    FormTextField(
                        value = userFirstName,
                        onValueChange = {
                            userFirstName = it
                            if (activatedCheckFirstName) viewModel.validateFirstName(it)
                            viewModel.changeUserFirstName(it)
                        },
                        label = stringResource(id = R.string.first_name_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        isRequired = true
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

                    FormTextField(
                        value = userLastName,
                        onValueChange = {
                            userLastName = it
                            if (activatedCheckLastName) viewModel.validateLastName(it)
                            viewModel.changeUserLastName(it)
                        },
                        label = stringResource(id = R.string.last_name_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        isRequired = true
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

                    val selectedWorkPref by viewModel.selectedWorkPref.collectAsState()

                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        expanded = roleWorkOpen,
                        onExpandedChange = {
                            roleWorkOpen = !roleWorkOpen
                        }
                    ) {

                        OutlinedTextField(
                            value = selectedWorkPref,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select work preference") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleWorkOpen) },
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

                } else if (selectedIndex == 0) {
                    //company
                    FormTextField(
                        value = companyName,
                        onValueChange = {
                            companyName = it
                            if (activatedCheckCompanyName) viewModel.validateCompanyName(it)
                            viewModel.changeCompanyName(it)
                        },
                        label = stringResource(id = R.string.company_name_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        isRequired = true
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

                FormTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (activatedCheckEmail) viewModel.validateEmail(it)
                        viewModel.changeUserEmail(it)
                    },
                    label = "Email",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    isRequired = true
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

                FormTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (activatedCheckPassword) viewModel.validatePassword(it)
                        viewModel.changeUserPassword(it)
                    },
                    label = stringResource(id = R.string.password_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    isRequired = true,
                    isPassword = true
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

                FormTextField(
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    isRequired = true,
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
                            val checkAll = if (selectedIndex == 0) {
                                val companyNameValidator =
                                    viewModel.validateCompanyName(companyName)
                                if (!companyNameValidator) activatedCheckCompanyName = true
                                companyNameValidator
                            } else {
                                val firstNameValidator =
                                    viewModel.validateFirstName(userFirstName)
                                val lastNameValidator =
                                    viewModel.validateLastName(userLastName)

                                if (!firstNameValidator) activatedCheckFirstName = true
                                if (!lastNameValidator) activatedCheckLastName = true

                                firstNameValidator && lastNameValidator
                            }

                            val emailValidator = viewModel.validateEmail(email)
                            val passwordValidator = viewModel.validatePassword(password)
                            val confirmValidator =
                                viewModel.checkConfirmPassword(password, confirmPassword)

                            val localCheck = when (selectedIndex) {
                                1 -> !submitEnabled || !lastNameVerified || !emailVerified || !passwordVerified || !confirmPasswordVerified
                                0 -> !companyNameVerified || !emailVerified || !passwordVerified || !confirmPasswordVerified
                                else -> true
                            }

                            if (!emailValidator) activatedCheckEmail = true
                            if (!passwordValidator) activatedCheckPassword = true
                            if (!confirmValidator) activatedCheckConfirmPassword = true

                            if (checkAll && emailValidator && passwordValidator && confirmValidator) {
                                CoroutineScope(Dispatchers.Main).launch {
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

            AnimatedVisibility(
                visible = roleWorkOpen,
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

                val height = screenHeight / 2
                val heightDp = with(density) { height.toDp() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightDp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 15.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                modifier = Modifier
                                    .height(2.dp)
                                    .width(70.dp)
                                    .padding(top = 20.dp)
                                    .background(Red, RoundedCornerShape(20.dp)),
                                text = ""
                            )

                            Text(
                                text = "Select work preference",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                thickness = 1.dp
                            )

                            flowHandling(selectedCategory, selectedCategories) { index, title, isSelected ->
                                viewModel.changeSelectionCategory(context, index, title, isSelected)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .fillMaxWidth(0.8f)
                                .align(Alignment.BottomCenter)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    roleWorkOpen = false
                                }
                                .background(
                                    color = colorResource(id = R.color.whatsapp),
                                    RoundedCornerShape(30.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Validate",
                                color = Color.White,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                        }
                    }
                }
            }


            AnimatedVisibility(
                visible = secondRoleWorkOpen,
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

                val height = screenHeight / 2
                val heightDp = with(density) { height.toDp() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightDp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 15.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                modifier = Modifier
                                    .height(2.dp)
                                    .width(70.dp)
                                    .padding(top = 20.dp)
                                    .background(Red, RoundedCornerShape(20.dp)),
                                text = ""
                            )

                            Text(
                                text = "Select work preference",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                thickness = 1.dp
                            )

                            flowHandling(selectedSecondRole, selectedRole) { index, title, isSelected ->
                                viewModel.changeSelectionSecondRole(context, index, title, isSelected)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .fillMaxWidth(0.8f)
                                .align(Alignment.BottomCenter)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    secondRoleWorkOpen = false

                                }
                                .background(
                                    color = colorResource(id = R.color.whatsapp),
                                    RoundedCornerShape(30.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Validate",
                                color = Color.White,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(vertical = 20.dp)
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

            var selected by remember { mutableStateOf(0) }

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