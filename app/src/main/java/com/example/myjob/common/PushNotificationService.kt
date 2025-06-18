package com.example.myjob.common

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myjob.DetailInvitationActivity
import com.example.myjob.MainActivity
import com.example.myjob.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Update server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Respond to received messages
        showNotification(message.data["title"], message.data["body"], message.data["idUser"])
    }

    private fun showNotification(title: String?, body: String?, idUser: String?) {
        val channelId = "default_channel"
        val channelName = "Default Channel"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /*val intent = Intent(this, MyReceiver::class.java).apply {
            putExtra("MESSAGE", "Clicked!")
        }
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            flag
        )


        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "http://192.168.10.32?idUser=$idUser".toUri(),
            this,
            MainActivity::class.java
        )
        val clickPendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(1, flag)
        }*/

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        /*val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("navigate_to", "to-detail-invitation")
            putExtra("idUser", idUser)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }*/

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://app/$idUser")
        ).apply {
            setPackage(packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val clickPendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(1, flag)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(0, "ACTION", pendingIntent)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(0, notification)
    }
}