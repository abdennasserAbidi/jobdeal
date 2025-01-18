package com.example.myjob.base

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myjob.base.workmanager.FileDownloadWorker
import com.example.myjob.base.workmanager.MyWorkerFactory
import com.example.myjob.common.loadJSONFromAsset
import com.example.myjob.domain.entities.AllSchools
import com.example.myjob.domain.entities.AllSubject
import com.example.myjob.domain.entities.School
import com.example.myjob.domain.entities.Subject
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Core Application Class
 */
@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    var allSubjectList: MutableList<Subject> = mutableListOf()
    var listNameCountries: MutableList<String> = mutableListOf()
    var listSchools: MutableList<School> = mutableListOf()

    @Inject
    lateinit var workerFactory: MyWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            generateSubjectList()
        }

        CoroutineScope(Dispatchers.Default).launch {
            generateSchoolList()
        }

        /*CoroutineScope(Dispatchers.Default).launch {
            generateCountriesList()
        }*/

    }

    private fun generateSubjectList() {
        try {
            val regions = Gson().fromJson(loadJSONFromAsset("subjects.json"), AllSubject::class.java)
            allSubjectList = regions.subject
        } catch (ex: java.lang.Exception) {
            Log.i("Alabaman", "Exception: ${ex.message}")
        }
    }

    private fun generateSchoolList() {
        try {
            val school = Gson().fromJson(loadJSONFromAsset("schools.json"), AllSchools::class.java)
            listSchools = school.school
        } catch (ex: java.lang.Exception) {
            Log.i("Alabaman", "Exception: ${ex.message}")
        }
    }

}