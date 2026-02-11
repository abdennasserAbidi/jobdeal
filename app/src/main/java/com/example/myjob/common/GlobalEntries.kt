package com.example.myjob.common

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.feature.validateprofile.StepStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

object GlobalEntries {
    var isFiltering = false
    var socket: CoroutineWebSocketClient? = null
    var idExp = 0
    var idStudy = 0
    var idInvitation = 0
    var idDemand = 0
    var idHoster = 0
    var countInvitationPending = MutableStateFlow(0)
    var seenInvitation = MutableStateFlow(false)
    var seenMessage = MutableStateFlow(false)
    var seenNotifications = MutableStateFlow(false)
    var seenDemandNotifications = MutableStateFlow(false)
    var idReceiver = -1
    var idSender = -1
    var idNotification = 0
    var tokenForgetPassword = ""
    var idAnnounce = 0
    var isFromNotification = false
    var idCompany = 0
    var listIdToRemove = mutableListOf<Int>()
    var listIdAccepted = mutableListOf<Int>()
    var listExperience = mutableListOf<Experience>()
    var listEducations = mutableListOf<Educations>()
    var experience = Experience()
    var educations = Educations()
    var marketDemand = MarketDemandModel()
    var user = User()
    var candidateUser = User()
    var candidate = Candidate()
    var userCandidate = User()
    var userForCompany = User()
    var language = "Français"
    var emailGoogleAccount = ""
    var preferredRole = "Holding"
    var preferredRoles = mutableListOf<String>()
    var notificationMessage = NotificationMessage()
    var role = ""
    var criteriaModel = CriteriaModel()
    var isFromFilter = false
    var isFromDemand = false
    var invitationModel = InvitationModel()
    var matchInvitation: () -> Unit = {}
    var isFromLogin = false
    var isFromSettings = false
    var isRefreshing = MutableStateFlow(false)
    var isVisibleNav = MutableStateFlow(true)
    var isSubmitAction = MutableStateFlow(false)
    var isSubmitEducationAction = MutableStateFlow(false)
    var isSubmitProfessionalAction = MutableStateFlow(false)
    var isHidden = derivedStateOf { !isVisibleNav.value }
    var langState = MutableStateFlow("Français")
    var languageShared = MutableSharedFlow<String>()
    var stepShared = -1
    var listImageUri by mutableStateOf<MutableList<Pair<String, Uri>>>(mutableListOf())
    var listCompanyImageUri by mutableStateOf<List<Uri?>>(emptyList())

    ///////////////////////////////////////////////////////////////////////////
    // MESSAGES
    ///////////////////////////////////////////////////////////////////////////
    var otherUserId = -1
    var otherUserName = ""

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