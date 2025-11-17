package com.example.myjob.feature.forgotpassword

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.CustomDialog
import com.example.myjob.domain.entities.ResetPasswordParam
import com.example.myjob.feature.profile.test.FormTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    token: String = ""
) {

    Log.i("testscreen", "ForgotPasswordScreen: $token")

    val message by viewModel.message.collectAsState()
    var isProgressing by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
            ) {

                if (token.isEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                if (message.isNotEmpty()) viewModel.clearMessage()
                                else navController.popBackStack()
                            },
                        contentDescription = ""
                    )
                }

                val title = if (token.isNotEmpty()) "Change password"
                else if (message.isNotEmpty()) "Password reset email sent"
                else "Forgot password"

                Text(
                    text = title,
                    modifier = Modifier.align(Alignment.TopCenter),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                if (token.isNotEmpty()) {
                    //le5ra
                    isProgressing = false
                    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
                    val isConfirmPasswordValid by viewModel.isConfirmPasswordValid.collectAsState()
                    val resetMessage by viewModel.resetMessage.collectAsState()

                    var password by remember { mutableStateOf("") }
                    val passwordVerified by remember { derivedStateOf { isPasswordValid } }
                    val confirmPasswordVerified by remember { derivedStateOf { isConfirmPasswordValid } }

                    var confirmPassword by remember { mutableStateOf("") }
                    var activatedCheckPassword by remember { mutableStateOf(false) }

                    var showDialog by remember { mutableStateOf(false) }

                    val isAllChecked = confirmPasswordVerified && passwordVerified
                    /*LaunchedEffect(isAllChecked) {
                        if (isAllChecked) viewModel.resetPassword(ResetPasswordParam(token, password))
                    }*/

                    LaunchedEffect(resetMessage) {
                        if (resetMessage.isNotEmpty()) showDialog = true
                    }

                    if (showDialog) {
                        isProgressing = false
                        val isSuccess = resetMessage == "Password successfully reset."
                        CustomDialog(isSuccess = isSuccess, message = resetMessage) {
                            isProgressing = false
                            viewModel.changeMessageText()
                            showDialog = false
                            if (isSuccess) navController.popBackStack()
                        }
                    }

                    FormTextField(
                        value = password,
                        borderColor = if (activatedCheckPassword && !passwordVerified) Color.Red else colorResource(
                            id = R.color.whatsapp
                        ),
                        onValueChange = {
                            password = it
                            if (activatedCheckPassword) viewModel.validatePassword(it)
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
                                text = stringResource(id = R.string.error_password),
                                color = Color.Red
                            )
                        }
                    }


                    FormTextField(
                        value = confirmPassword,
                        borderColor = if (activatedCheckPassword && !confirmPasswordVerified) Color.Red else colorResource(
                            id = R.color.whatsapp
                        ),
                        onValueChange = {
                            confirmPassword = it
                            viewModel.checkConfirmPassword(password, it)
                        },
                        label = stringResource(id = R.string.confirm_password_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp),
                        isRequired = true,
                        isPassword = true
                    )

                    if (activatedCheckPassword) {
                        if (confirmPassword.isEmpty() || !confirmPasswordVerified) {

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = stringResource(id = R.string.confirm_password_error),
                                color = Color.Red
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val confirmValidator =
                                viewModel.checkConfirmPassword(password, confirmPassword)
                            val passwordValidator = viewModel.validatePassword(password)

                            if (!confirmPasswordVerified || !passwordVerified) activatedCheckPassword =
                                true

                            if (passwordValidator && confirmValidator) {
                                isProgressing = true
                                viewModel.resetPassword(ResetPasswordParam(token, password))
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
                            stringResource(id = R.string.change_password_text),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }

                } else if (message.isNotEmpty()) {
                    //second
                    isProgressing = false
                    viewModel.clearValidity()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(horizontal = 20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.forget_password_image),
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = ""
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.send_email_text),
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = FontFamily.Default,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                } else {
                    var email by remember { mutableStateOf("abidi.abdennasser@gmail.com") }
                    val isEmailValid by viewModel.isEmailValid.collectAsState()
                    var activatedCheck by remember { mutableStateOf(false) }

                    val emailVerified by remember { derivedStateOf { isEmailValid } }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.forget_password_intro_text),
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(top = 20.dp)
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 20.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = FontFamily.Default,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                    ) {

                        FormTextField(
                            value = email,
                            borderColor = if (activatedCheck && !emailVerified) Color.Red else colorResource(
                                id = R.color.whatsapp
                            ),
                            onValueChange = {
                                email = it
                                if (activatedCheck) viewModel.validateEmail(it)
                                //viewModel.changeUserEmail(it)
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

                                Text(
                                    modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                    text = stringResource(id = R.string.error_email),
                                    color = Color.Red
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val emailValidator = viewModel.validateEmail(email)

                                if (!emailVerified) activatedCheck = true

                                if (emailValidator) {
                                    isProgressing = true
                                    viewModel.forgotPasswordUseCase(email)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 20.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.whatsapp)
                            )
                        ) {
                            Text(
                                stringResource(id = R.string.send_text),
                                modifier = Modifier.padding(vertical = 5.dp)
                            )
                        }
                    }
                }

            }

            if (isProgressing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                        .align(Alignment.Center),
                    color = Color.Blue,
                    strokeWidth = 8.dp,
                    trackColor = Color.LightGray,
                    strokeCap = StrokeCap.Round
                )
            }
        }

    }
}