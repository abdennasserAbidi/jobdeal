package com.example.myjob.common

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

// Helper function to retrieve the file name from a URI
fun Context.getFileNameFromUri(uri: Uri): String? {
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                return it.getString(nameIndex) // Return the file name
            }
        }
    }
    return null // Return null if the name couldn't be retrieved
}