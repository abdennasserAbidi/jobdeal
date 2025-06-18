package com.example.myjob.common

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myjob.base.workmanager.FileDownloadWorker
import com.example.myjob.domain.entities.Candidate
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

object GlobalEntries {
    var isFiltering = false
    var idExp = 0
    var idStudy = 0
    var listIdToRemove = mutableListOf<Int>()
    var listIdAccepted = mutableListOf<Int>()
    var experience = Experience()
    var educations = Educations()
    var user = User()
    var candidateUser = User()
    var candidate = Candidate()
    var userCandidate = User()
    var userForCompany = User()
    var language = "Français"
    var role = ""
    var criteriaModel = CriteriaModel()
    var isFromFilter = false
    var invitationModel = InvitationModel()
    var matchInvitation: () -> Unit = {}
    var isFromLogin = false
    var isFromSettings = false
    var isVisibleNav = MutableStateFlow(true)
    var isHidden = derivedStateOf { !isVisibleNav.value }
    var langState = MutableStateFlow("Français")
    var languageShared = MutableSharedFlow<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    var start: ZonedDateTime = ZonedDateTime.parse("2018-04-06T16:01:00.000+03:00")
    var isVisibleNavigation = true

    fun scheduleFileDownload(context: Context, fileName: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val data = Data.Builder()
            .putString("fileName", fileName)
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(uploadWorkRequest)

        //val workRequest = OneTimeWorkRequest.Builder(RefreshToken::class.java).build()

        val workInfo: Flow<WorkInfo> =
            workManager.getWorkInfoByIdLiveData(uploadWorkRequest.id).asFlow()
        CoroutineScope(Dispatchers.Default).launch {
            workInfo.collect { workInfo ->
                Log.i("stateWorkManager", "bgTask: ${workInfo.outputData}")
                if (workInfo.state.isFinished) {
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        println("File download succeeded")
                    } else {
                        println("File download failed or retried")
                    }
                    val progress = workInfo.progress.getInt("progress", 0)
                    println("Download progress: $progress%")
                }
            }
        }
    }
}