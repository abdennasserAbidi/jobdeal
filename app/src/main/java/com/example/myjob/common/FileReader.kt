package com.example.myjob.common

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException

object FileReader {

    fun readAssetFile(context: Context, name: String): String? {
        return try {
            return context.assets.open(name)
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            null
        }
    }

    fun getFile(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val tempFile = File(context.cacheDir, "tempfile.pdf")
        contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }
}