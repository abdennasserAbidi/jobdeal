package com.example.myjob.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.work.Configuration
import com.example.myjob.MainActivity
import com.example.myjob.R
import com.example.myjob.base.workmanager.MyWorkerFactory
import com.example.myjob.common.CoroutineWebSocketClient
import com.example.myjob.common.GlobalEntries.socket
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

    val client = CoroutineWebSocketClient("ws://192.168.1.23:9090/ws/websocket")

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        client.connect()
        socket = client
        CoroutineScope(Dispatchers.Default).launch {
            generateSubjectList()
        }

        CoroutineScope(Dispatchers.Default).launch {
            generateSchoolList()
        }
    }

    private fun showNotification() {
        val activityIntent = Intent(this, MainActivity::class.java).apply {
            data = "myApp://notification".toUri()
        }
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("App Launched!")
            .setContentText("Tap to open deep link")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService<NotificationManager>()!!
        notificationManager.notify(1, notification)
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