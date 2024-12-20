package com.example.myjob.feature.login.gmail

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val email: String?,
    val phone: String?,
    val profilePictureUrl: String?
)