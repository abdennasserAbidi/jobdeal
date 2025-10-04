package com.example.myjob.feature.validateprofile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries.stepShared
import com.example.myjob.feature.profile.test.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewValidationScreen(
    navController: NavController,
    viewModel: InterviewValidationViewModel = hiltViewModel()
) {

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

                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.popBackStack()
                        },
                    contentDescription = ""
                )

                Text(
                    text = stringResource(id = R.string.interview_text),
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
                if (message.isNotEmpty()) {
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
                            text = "An email with a link has been sent.",
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
                    )
                    {
                        Text(
                            text = stringResource(id = R.string.interview_validation_text),
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
                    )
                    {
                        FormTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (activatedCheck) viewModel.validateEmail(it)
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
                                    text = if (emailVerified) "Valid email" else stringResource(
                                        id = R.string.error_email
                                    ),
                                    color = if (emailVerified) colorResource(id = R.color.whatsapp) else Color.Red
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val emailValidator = viewModel.validateEmail(email)

                                if (!emailVerified) activatedCheck = true

                                if (emailValidator) {
                                    isProgressing = true
                                    stepShared = 2
                                    val validationProfileStatus = ValidationProfileStatus()
                                    validationProfileStatus.email = email
                                    validationProfileStatus.typeValidation = "interview"
                                    validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name
                                    viewModel.validate(validationProfileStatus)
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
                                "Send",
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