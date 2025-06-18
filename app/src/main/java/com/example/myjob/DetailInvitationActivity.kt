package com.example.myjob

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInvitationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_invitation)
        handleIntent(intent)
        val data: Uri? = intent?.data
        if (data != null) {
            val userId = data.lastPathSegment
            Log.i("lkfngrzjggtrlkfsrr", "lastPathSegment: $userId")

        }

        val destination = intent.getStringExtra("navigate_to")
        val jobId = intent.getStringExtra("idUser")
        Log.i("lkfngrzjggtrlkfsrr", "destination: $destination")

        jobId?.let {
            Log.i("lkfngrzjggtrlkfsrr", "jobId: $it")
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null && Intent.ACTION_VIEW == intent.action) {
            val data: Uri? = intent.data
            Log.d("DeepLink", "URI: $data") // Log the URI
            // Handle deep link data here, e.g., navigate using NavController
        }
    }
}