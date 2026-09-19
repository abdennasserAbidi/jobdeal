package com.example.myjob.domain.entities

data class TrialModel(
    var id: Int = 0,
    var idUserCall: Int = 0,
    var idUserCalled: Int = 0,
    var phoneNumberUserCalled: String = "",
    var duration: String = "",
    var date: String = "",
    var timestamp: Long = 0,
    var count: Int = 0
)