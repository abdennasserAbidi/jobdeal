package com.example.myjob.domain.entities

import android.view.View
import com.example.myjob.R

data class SettingsParams(
    var id: Int = View.generateViewId(),
    var icon: Int? = R.drawable.ic_settings_account,
    var title: String? = "",
)