package com.example.myjob.domain.response

import com.example.myjob.domain.entities.User

data class LoginResponse(
    var token: String?= "",
    var expiresIn: Long?= 0,
    var user: User? = User(),
    var messageError: String?= ""
)