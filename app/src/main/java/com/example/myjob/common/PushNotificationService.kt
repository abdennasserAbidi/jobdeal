package com.example.myjob.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.example.myjob.DetailInvitationActivity
import com.example.myjob.MainActivity
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries.notificationMessage
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Respond to received messages
        showNotification(
            notificationMessage.title, notificationMessage.body,
            message.data["idInvitation"],
            message.data["idAnnounce"],
            message.data["idCompany"],
            message.data["validation"]
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "channel_id",
            "channel_name",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService<NotificationManager>()!!
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(
        title: String?,
        body: String?,
        idUser: String?,
        idAnnounce: String?,
        idCompany: String?,
        validation: String?
    ) {
        val channelId = "channel_id"
        val channelName = "Default Channel"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        val intent1 = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("myapp://jobdeal/$idUser")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val targetIntent = Intent(this, MainActivity::class.java).apply {
            // Add navigation data as extras
            putExtra("idInvitation", idUser)
            putExtra("idAnnounce", idAnnounce)
            putExtra("idCompany", idCompany)
            putExtra("validation", validation)
            putExtra("ITEM_ID", "myapp://notification/$idUser")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            targetIntent,
            pendingIntentFlags
        )

        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "ACTION", pendingIntent)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager1 = getSystemService<NotificationManager>()!!
        notificationManager1.notify(1, notification)
    }
}