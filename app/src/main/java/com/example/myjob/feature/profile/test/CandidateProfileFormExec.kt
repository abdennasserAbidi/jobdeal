package com.example.myjob.feature.profile.test

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.profile.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CandidateProfileFormExec(
    navController: NavController,
    list: List<NewCountry>,
    clearData: () -> Unit = {},
    allSubjects: MutableList<Subject>,
    listStudyField: MutableList<String>,
    listSchools: MutableList<String>,
    listGrade: MutableList<String>,
    listCompany: MutableList<String>
) {


}
