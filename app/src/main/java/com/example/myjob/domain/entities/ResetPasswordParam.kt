package com.example.myjob.domain.entities

data class ResetPasswordParam(
    var token: String ?= "",
    var newPassword: String ?= ""
)