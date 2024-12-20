package com.example.myjob.common

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

fun Context.loadJSONFromAsset(name: String): String {
    val json: String = try {
        val inputStream: InputStream = this.assets.open(name)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return ""
    }
    return json
}