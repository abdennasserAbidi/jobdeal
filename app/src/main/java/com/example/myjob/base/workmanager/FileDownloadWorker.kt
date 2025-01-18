package com.example.myjob.base.workmanager

import android.content.Context
import android.os.Environment
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.myjob.remote.api.ApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

@HiltWorker
class FileDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val apiService : ApiService
    //private val sharedPreference : SharedPreference
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val fileName = inputData.getString("fileName") ?: return Result.failure()
        return try {
            val response = apiService.downloadFile(fileName)

            downloadFileFlow(response, fileName).collect { progress ->
                setProgressAsync(workDataOf("progress" to progress))
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun downloadFileFlow(body: ResponseBody, fileName: String): Flow<Int> = flow {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        val inputStream = body.byteStream()
        val outputStream = FileOutputStream(filePath)

        val buffer = ByteArray(8 * 1024)
        val fileSize = body.contentLength()
        var totalBytesRead = 0L

        try {
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                emit((totalBytesRead * 100 / fileSize).toInt()) // Emit progress
            }
            outputStream.flush()
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }

    private fun saveFile(body: ResponseBody, fileName: String) {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )

        body.byteStream().use { inputStream ->
            FileOutputStream(filePath).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

}