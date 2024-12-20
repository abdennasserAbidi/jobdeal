package com.example.myjob.base.connectivity

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import org.greenrobot.eventbus.EventBus

class NetworkSchedulerService : JobService(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var mConnectivityReceiver: ConnectivityReceiver? = null

    override fun onCreate() {
        super.onCreate()
        mConnectivityReceiver = ConnectivityReceiver(this)
    }

    /**
     * When the app's NetworkConnectionActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth.
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int =
        Service.START_NOT_STICKY

    @Suppress("DEPRECATION")
    override fun onStartJob(params: JobParameters): Boolean {
        registerReceiver(mConnectivityReceiver, IntentFilter(CONNECTIVITY_ACTION))
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        unregisterReceiver(mConnectivityReceiver)
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        EventBus.getDefault()
            .post(Pair(NetworkSchedulerService::class.java.canonicalName, isConnected))
    }
}
