package com.example.myjob.feature.signup

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.CircularProgressBar
import com.example.myjob.common.CustomDialog
import com.example.myjob.feature.login.gmail.GoogleAuthUiClient
import com.example.myjob.feature.navigation.Screen
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
    val isSamePassword by viewModel.confirmPassword.collectAsState()
    val isFirstNameValid by viewModel.isFirstNameValid.collectAsState()
    val isLastNameValid by viewModel.isLastNameValid.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val user by viewModel.user.collectAsState()
    val isCompanyNameValid by viewModel.isCompanyNameValid.collectAsState()

    var selectedIndex by remember { mutableStateOf(-1) }

    var activatedCheck by remember { mutableStateOf(false) }

    var userFirstName by remember { mutableStateOf(user.firstName ?: "") }
    val submitEnabled by remember { derivedStateOf { isFirstNameValid } }

    var userLastName by remember { mutableStateOf(user.lastName ?: "") }
    val lastNameVerified by remember { derivedStateOf { isLastNameValid } }

    var companyName by remember { mutableStateOf(user.companyName ?: "") }
    val companyNameVerified by remember { derivedStateOf { isCompanyNameValid } }

    var email by remember { mutableStateOf(user.email ?: "") }
    val emailVerified by remember { derivedStateOf { isEmailValid } }

    var password by remember { mutableStateOf(user.password ?: "") }
    val passwordVerified by remember { derivedStateOf { isPasswordValid } }

    var confirmPassword by remember { mutableStateOf("") }
    val isConfirmPasswordValid by viewModel.isConfirmPasswordValid.collectAsState()
    val confirmPasswordVerified by remember { derivedStateOf { isConfirmPasswordValid } }

    var isProgressing by remember { mutableStateOf(false) }
    var activatedCheckPassword by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    val saveUserRes by viewModel.saveUserRes.collectAsState()
    val token by viewModel.token.collectAsState()

    var error by remember { mutableStateOf("") }

    LaunchedEffect(saveUserRes.token?.isNotEmpty()) {
        isProgressing = false
        if (saveUserRes.token?.isNotEmpty() == true) navController.navigate(Screen.HomeScreen.route)
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

    //gmail
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            //onDirect()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    val user = signInResult.data
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

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
    //end gmail


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

                    Spacer(modifier = Modifier.height(50.dp))

                    CustomDropdownMenu(
                        list = listOf(
                            stringResource(id = R.string.choose_companies_text),
                            stringResource(id = R.string.choose_candidate_text)
                        ),
                        defaultSelected = user.role ?: "",
                        color = if (activatedCheck && selectedIndex == -1) Red else colorResource(id = R.color.whatsapp),
                        onSelected = {
                            selectedIndex = it
                            viewModel.changeRole(if (it == 0) "Company" else "Candidate")
                        },
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                if (activatedCheck) {
                    if (selectedIndex == -1) {
                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = stringResource(id = R.string.choose_type_warning),
                            color = Red
                        )
                    }
                }

                if (selectedIndex == 1) {
                    Text(
                        text = stringResource(id = R.string.first_name_text),
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = if (activatedCheck && !submitEnabled) Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = userFirstName,
                        onValueChange = {
                            userFirstName = it
                            if (activatedCheck) viewModel.validateFirstName(it)
                            viewModel.changeUserFirstName(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    if (activatedCheck) {
                        if (userFirstName.isEmpty() || !submitEnabled) {

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (submitEnabled) "Valid First name" else stringResource(id = R.string.first_name_error),
                                color = if (submitEnabled) Color.Green else Red
                            )
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.last_name_text),
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = if (activatedCheck && !lastNameVerified) Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = userLastName,
                        onValueChange = {
                            userLastName = it
                            if (activatedCheck) viewModel.validateLastName(it)
                            viewModel.changeUserLastName(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    if (activatedCheck) {
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
                    Text(
                        text = stringResource(id = R.string.company_name_text),
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = if (activatedCheck && !companyNameVerified) Red else colorResource(
                                    id = R.color.whatsapp
                                ),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = companyName,
                        onValueChange = {
                            companyName = it
                            if (activatedCheck) viewModel.validateCompanyName(it)
                            viewModel.changeCompanyName(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    if (activatedCheck) {
                        if (userFirstName.isEmpty() || !companyNameVerified) {
                            Log.i("submitEnabled", "SignUpScreen: $companyNameVerified")

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (companyNameVerified) "" else stringResource(id = R.string.company_name_error),
                                color = if (companyNameVerified) Color.Green else Red
                            )
                        }
                    }
                }

                Text(
                    text = "Email",
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                        .border(
                            width = 1.dp,
                            color = if (activatedCheck && !emailVerified) Red else colorResource(id = R.color.whatsapp),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clip(shape = RoundedCornerShape(30.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = email,
                    onValueChange = {
                        email = it
                        if (activatedCheck) viewModel.validateEmail(it)
                        viewModel.changeUserEmail(it)
                    },
                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                )

                if (activatedCheck) {
                    if (email.isEmpty() || !emailVerified) {
                        Log.i("submitEnabled", "SignUpScreen: $emailVerified")

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = if (emailVerified) "Valid First name" else stringResource(id = R.string.error_email),
                            color = if (emailVerified) Color.Green else Red
                        )
                    }
                }

                Text(
                    text = stringResource(id = R.string.password_text),
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                var showPassword by remember { mutableStateOf(false) }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                        .border(
                            width = 1.dp,
                            color = if (activatedCheck && !passwordVerified) Red else colorResource(
                                id = R.color.whatsapp
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clip(shape = RoundedCornerShape(30.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = password,
                    onValueChange = {
                        password = it
                        if (activatedCheck) viewModel.validatePassword(it)
                        viewModel.changeUserPassword(it)
                    },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }, trailingIcon = {
                        if (showPassword) {
                            IconButton(onClick = { showPassword = false }) {
                                Icon(imageVector = Icons.Filled.Visibility, contentDescription = "")
                            }
                        } else {
                            IconButton(onClick = { showPassword = true }) {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = ""
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                )
                Log.i("passwordVerified", "SignUpScreen: $passwordVerified")

                if (activatedCheck) {
                    if (password.isEmpty() || !passwordVerified) {

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = if (passwordVerified) "Valid First name" else stringResource(id = R.string.error_password),
                            color = if (passwordVerified) Color.Green else Red
                        )
                    }
                }

                Text(
                    text = stringResource(id = R.string.confirm_password_text),
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                var showConfirmPassword by remember { mutableStateOf(false) }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                        .border(
                            width = 1.dp,
                            color = if (activatedCheckPassword && !confirmPasswordVerified) Red else colorResource(
                                id = R.color.whatsapp
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clip(shape = RoundedCornerShape(30.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        viewModel.confirmPassword(it)
                    },
                    visualTransformation = if (showConfirmPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }, trailingIcon = {
                        if (showConfirmPassword) {
                            IconButton(onClick = { showConfirmPassword = false }) {
                                Icon(imageVector = Icons.Filled.Visibility, contentDescription = "")
                            }
                        } else {
                            IconButton(onClick = { showConfirmPassword = true }) {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = ""
                                )
                            }
                        }
                    },
                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                )

                if (activatedCheckPassword) {
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                val checkAll =
                                    if (selectedIndex == 0) viewModel.validateCompanyName(
                                        companyName
                                    )
                                    else {
                                        val firstNameValidator =
                                            viewModel.validateFirstName(userFirstName)
                                        val lastNameValidator =
                                            viewModel.validateLastName(userLastName)
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

                                if (localCheck && selectedIndex == -1) activatedCheck = true
                                if (!confirmPasswordVerified) activatedCheckPassword = true

                                if (selectedIndex != -1 && checkAll && emailValidator && passwordValidator && confirmValidator) {
                                    isProgressing = true
                                    viewModel.saveUser(user)
                                }
                            }
                            .background(
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.create_new_account_text),
                            modifier = Modifier.padding(vertical = 20.dp),
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
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
                    contentAlignment = Alignment.Center) {

                    Card(
                        modifier = Modifier
                            .size(200.dp)
                            .background(shape = RoundedCornerShape(30.dp), color = Color.White),
                        elevation = 15.dp,
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(70.dp),
                                color = Blue,
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