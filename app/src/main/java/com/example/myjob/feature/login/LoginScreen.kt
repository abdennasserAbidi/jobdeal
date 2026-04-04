package com.example.myjob.feature.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GlobalEntries.emailGoogleAccount
import com.example.myjob.common.hideKeyboard
import com.example.myjob.feature.demands.NewFormTextField
import com.example.myjob.feature.login.gmail.GoogleAuthUiClient
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.profile.test.FormTextField
import com.example.myjob.feature.signup.AddPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    lifecycleScope: LifecycleCoroutineScope,
    googleAuthUiClient: GoogleAuthUiClient
) {

    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    //gmail
    val state by viewModel.state.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val user by viewModel.user.collectAsState()

    var activatedCheck by remember { mutableStateOf(false) }
    var isProgressing by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf(user.email ?: "") }
    val emailVerified by remember { derivedStateOf { isEmailValid } }

    var password by remember { mutableStateOf(user.password ?: "") }
    val passwordVerified by remember { derivedStateOf { isPasswordValid } }

    var errorLogin by remember { mutableStateOf("") }

    val login by viewModel.login.collectAsState()
    val token by viewModel.token.collectAsState()
    val errorText by viewModel.errorText.collectAsState()
    val errorValidate by remember {
        derivedStateOf { errorText.isNotEmpty() }
    }
    val isProgressing1 by viewModel.isProgressing.collectAsState()
    val stateToken by viewModel.stateToken.collectAsState()
    val verificationText by viewModel.verificationText.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var addPassword by remember { mutableStateOf(false) }

    if (showDialog) {
        isProgressing = false
        CustomDialog(isSuccess = false, message = errorLogin) {
            showDialog = false
            isProgressing = false
            if (addPassword) {
                viewModel.clearText()
                navController.navigate(Screen.SignupScreen.route)
                addPassword = false
            }
        }
    }

    LaunchedEffect(errorText) {
        if (errorText.isNotEmpty() && login.messageError?.isNotEmpty() == true) {
            showDialog = true
            errorLogin = login.messageError.toString()
        }
    }

    LaunchedEffect(errorValidate) {
        if (errorValidate) showDialog = true
    }

    LaunchedEffect(token) {
        isProgressing = false
        if (token.isNotEmpty()) {
            viewModel.isFromLogin(true)
            val type = viewModel.getType()
            val isFirstTime = login.user?.firstTimeUse ?: true
            if (isFirstTime) {
                when (login.user?.role) {
                    "Candidate", "Candidat" -> navController.navigate(Screen.SearchWordScreen.route)
                    "Services" -> navController.navigate(Screen.ServiceProfileForm.route)
                    else -> navController.navigate(Screen.CompanyProfileForm.route)
                }
            } else {
                if (login.user?.role == "Services" || type == "service") navController.navigate(Screen.DemandServiceScreen.route)
                else navController.navigate(Screen.HomeScreen.route)
            }
        }
    }

    LaunchedEffect(verificationText) {
        if (verificationText == "Email already exist") {
            navController.navigate(Screen.HomeScreen.route)
        } else if (verificationText.isNotEmpty()) {
            addPassword = true
            showDialog = true
            errorLogin = "Il n'y a pas de compte associé a cet email"
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            //onDirect()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            Log.i("klfzefhgzr", "login: ${result.resultCode}")

            lifecycleScope.launch {
                val signInResult = googleAuthUiClient.signInWithIntent(
                    intent = result.data ?: return@launch
                )
                val userGmail = signInResult.data
                emailGoogleAccount = userGmail?.email ?: ""
                viewModel.onSignInResult(signInResult)
            }

            if (result.resultCode == Activity.RESULT_OK) {

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
            //viewModel.resetState()
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

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.welcome),
                    modifier = Modifier.padding(top = 50.dp),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = stringResource(id = R.string.sub_title_welcome),
                    modifier = Modifier.padding(top = 5.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            var showPassword by remember { mutableStateOf(false) }

            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 40.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (activatedCheck) viewModel.validateEmail(it)
                            viewModel.changeUserEmail(it)
                        },
                        readOnly = false,
                        interactionSource = interactionSource,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email", color = Color(0xFF9CA3AF)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (activatedCheck && email.isEmpty() && !emailVerified) Red else Color(0xFF049344),
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedTextColor = Color(0xFF1F2937),
                            unfocusedTextColor = Color(0xFF1F2937)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        minLines = 1,
                        maxLines = 1
                    )

                    if (activatedCheck) {
                        if (email.isEmpty() || !emailVerified) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = stringResource(id = R.string.error_email),
                                color = Red
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(id = R.string.password_text),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (activatedCheck) viewModel.validatePassword(it)
                            viewModel.changeUserPassword(it)
                        },
                        readOnly = false,
                        interactionSource = interactionSource,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(id = R.string.password_text), color = Color(0xFF9CA3AF)) },
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (activatedCheck && password.isEmpty() && !passwordVerified) Red else Color(0xFF049344),
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedTextColor = Color(0xFF1F2937),
                            unfocusedTextColor = Color(0xFF1F2937)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        minLines = 1,
                        maxLines = 1
                    )

                    if (activatedCheck) {
                        if (password.isEmpty() || !passwordVerified) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = stringResource(id = R.string.error_password),
                                color = Red
                            )
                        }
                    }
                }
            }


            /*FormTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (activatedCheck) viewModel.validateEmail(it)
                    viewModel.changeUserEmail(it)
                },
                label = "Email",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 40.dp),
                isRequired = true
            )

            if (activatedCheck) {
                if (email.isEmpty() || !emailVerified) {
                    Log.i("submitEnabled", "SignUpScreen: $emailVerified")

                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                        text = if (emailVerified) "Valid First name" else stringResource(id = R.string.error_email),
                        color = if (emailVerified) colorResource(id = R.color.whatsapp) else Color.Red
                    )
                }
            }

            FormTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (activatedCheck) viewModel.validatePassword(it)
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

            if (activatedCheck) {
                if (password.isEmpty() || !passwordVerified) {

                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                        text = stringResource(id = R.string.error_password),
                        color = Color.Red
                    )
                }
            }*/

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigate(Screen.ForgotPasswordScreen.route)
                        },
                    text = stringResource(id = R.string.forgot_password_text),
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {
                        val s = viewModel.validateEmail(email)
                        val p = viewModel.validatePassword(password)

                        val activity = context as? Activity
                        if (!emailVerified || !passwordVerified) activatedCheck = true

                        if (s && p) {
                            CoroutineScope(Dispatchers.Main).launch {
                                isProgressing = true
                                activity?.hideKeyboard()
                                delay(1000L)
                                viewModel.login(user)
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
                        stringResource(id = R.string.login_text),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                Row(
                    Modifier
                        .padding(top = 25.dp)
                        .fillMaxWidth(0.8f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .weight(1f)
                            .background(Color.LightGray)
                    ) {}

                    Text(
                        text = stringResource(id = R.string.or_text),
                        modifier = Modifier.weight(1f),
                        color = Color.Gray,
                        style = TextStyle(
                            textAlign = TextAlign.Center
                        )
                    )

                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .weight(1f)
                            .background(Color.LightGray)
                    ) {}
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 25.dp)
                        .border(
                            1.dp,
                            Color.LightGray,
                            RoundedCornerShape(8.dp)
                        )
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest
                                        .Builder(
                                            signInIntentSender ?: return@launch
                                        )
                                        .build()
                                )
                            }
                        }
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "Google Button",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = R.string.google_signin_text),
                        modifier = Modifier.padding(vertical = 20.dp),
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigate(Screen.SignupScreen.route)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.sign_up_hint))
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(id = R.string.sign_up_text),
                        textDecoration = TextDecoration.Underline,
                        color = Color.Blue
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
    }

}