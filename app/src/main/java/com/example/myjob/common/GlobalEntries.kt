package com.example.myjob.common

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.ZonedDateTime

object GlobalEntries {
    var isFiltering = false
    var idExp = 0
    var idStudy = 0
    var experience = Experience()
    var educations = Educations()
    var user = User()
    var language = "Français"
    var isVisibleNav = MutableStateFlow(true)
    var langState = MutableStateFlow("Français")
    var languageShared = MutableSharedFlow<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    var start: ZonedDateTime = ZonedDateTime.parse("2018-04-06T16:01:00.000+03:00")
    var isVisibleNavigation = true
}