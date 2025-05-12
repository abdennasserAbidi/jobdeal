package com.example.myjob.base

import com.example.myjob.R

sealed class StatusObject {
    data class InvitationStatus(val status: String, val color: Int): StatusObject()
}
