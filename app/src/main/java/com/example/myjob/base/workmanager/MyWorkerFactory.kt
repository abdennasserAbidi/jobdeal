package com.example.myjob.base.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class MyWorkerFactory @Inject constructor(private val repository: ApiService) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = FileDownloadWorker(appContext, workerParameters, repository)
}