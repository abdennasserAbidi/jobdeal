package com.example.myjob.feature.signup

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.CustomDialog
import com.example.myjob.domain.entities.ResetPasswordParam
import com.example.myjob.feature.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPassword(
    viewModel: LoginViewModel,
    onBack: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val saveUserRes by viewModel.saveUserRes.collectAsState()

    var isProgressing by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    val passwordVerified by remember { derivedStateOf { isPasswordValid } }

    var confirmPassword by remember { mutableStateOf("") }
    val isConfirmPasswordValid by viewModel.isConfirmPasswordValid.collectAsState()
    val confirmPasswordVerified by remember { derivedStateOf { isConfirmPasswordValid } }
    var activatedCheckPassword by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(saveUserRes.data) {
        if (saveUserRes.status != ResourceState.IDLE) showDialog = true
    }

    if (showDialog) {
        isProgressing = false
        val isSuccess = saveUserRes.data?.messageError?.isEmpty() ?: false
        val message = if (isSuccess) "User successfully added" else "there is some error"
        CustomDialog(isSuccess = isSuccess, message = message) {
            isProgressing = false
            showDialog = false
            if (isSuccess) onBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .align(Alignment.TopCenter)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Add password!",
                    modifier = Modifier.padding(top = 50.dp),
                    style = TextStyle(
                        color = colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "Complete your registration",
                    modifier = Modifier.padding(top = 5.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .align(Alignment.BottomCenter)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {

                Box(modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 20.dp)
                    .border(
                        1.dp,
                        colorResource(id = R.color.whatsapp),
                        RoundedCornerShape(30.dp)
                    )
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    .background(color = Color.LightGray, shape = RoundedCornerShape(30.dp)))
                {

                    Text(
                        text = "Candidate",
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(start = 10.dp)
                            .align(Alignment.CenterStart),
                    )

                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                    )

                }


                Text(
                    text = "Password",
                    modifier = Modifier.padding(top = 40.dp, start = 20.dp),
                    style = TextStyle(
                        color = if (activatedCheckPassword && !passwordVerified) Color.Red else colorResource(id = R.color.whatsapp),
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
                            color = if (activatedCheckPassword && !passwordVerified) Color.Red else colorResource(
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
                        if (activatedCheckPassword) viewModel.validatePassword(it)
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

                if (activatedCheckPassword) {
                    if (password.isEmpty() || !passwordVerified) {

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = "Must contain uppercase and number",
                            color = Color.Red
                        )
                    }
                }
                Text(
                    text = "Confirm password",
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    style = TextStyle(
                        color = if (activatedCheckPassword && !confirmPasswordVerified) Color.Red else colorResource(id = R.color.whatsapp),
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                var showConfirmPassword by remember { mutableStateOf(false) }

                Log.i("confirmPasswordVerified", "AddPassword: $confirmPasswordVerified")
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 10.dp)
                        .border(
                            width = 1.dp,
                            color = if (activatedCheckPassword && !confirmPasswordVerified) Color.Red else colorResource(
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
                        viewModel.checkConfirmPassword(password, it)
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

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = "Is not the same password",
                            color = Color.Red
                        )
                    }
                }

                val userGmail by viewModel.userGmail.collectAsState()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                                val confirmValidator =
                                    viewModel.checkConfirmPassword(password, confirmPassword)
                                val passwordValidator = viewModel.validatePassword(password)

                                if (!confirmPasswordVerified || !passwordVerified) activatedCheckPassword =
                                    true

                                if (passwordValidator && confirmValidator) {
                                    isProgressing = true
                                    viewModel.addPassword(userGmail)
                                }
                            }
                            .background(
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add password",
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
                            text = "Or",
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 20.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.whatsapp),
                                RoundedCornerShape(30.dp)
                            )
                            .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                                onBack()
                            }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Back to login",
                            modifier = Modifier.padding(vertical = 20.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }


        /*Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {





            Spacer(modifier = Modifier.fillMaxHeight(0.3f))


        }*/

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