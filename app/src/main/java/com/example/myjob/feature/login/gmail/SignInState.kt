package com.example.myjob.feature.login.gmail

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)