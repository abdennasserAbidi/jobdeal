package com.example.myjob.base

import android.app.Application
import android.util.Log
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

/**
 * Core Application Class
 */
@HiltAndroidApp
class MyApp : Application() {

    var allSubjectList: MutableList<Subject> = mutableListOf()
    var listNameCountries: MutableList<String> = mutableListOf()
    var listSchools: MutableList<School> = mutableListOf()

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