package com.example.myjob.feature.demands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.myjob.base.messages.StompChatService
import com.example.myjob.base.messages.StompInvitationService
import com.example.myjob.base.messages.StompNotificationService
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.isUpdatingDemand
import com.example.myjob.common.GlobalEntries.marketDemand
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.usecase.demand.CountDownTrialUseCase
import com.example.myjob.domain.usecase.demand.DeleteDemandUseCase
import com.example.myjob.domain.usecase.demand.GetAllDemandUseCase
import com.example.myjob.domain.usecase.demand.GetDemandUseCase
import com.example.myjob.domain.usecase.demand.GetFilteredDemandUseCase
import com.example.myjob.domain.usecase.demand.SaveDemandUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.notification.SeenNotificationUseCase
import com.example.myjob.domain.usecase.notification.SendNotificationsUseCase
import com.example.myjob.domain.usecase.notification.UpdateTokenUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DemandsViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getUserUseCase: GetUserUseCase,
    private val getAllDemandUseCase: GetAllDemandUseCase,
    private val seenNotificationUseCase: SeenNotificationUseCase,
    private val saveDemandUseCase: SaveDemandUseCase,
    private val getDemandUseCase: GetDemandUseCase,
    private val getFilteredDemandUseCase: GetFilteredDemandUseCase,
    private val countDownTrialUseCase: CountDownTrialUseCase,
    private val deleteDemandUseCase: DeleteDemandUseCase,
    private val updateTokenUseCase: UpdateTokenUseCase,
    private val sendNotificationsUseCase: SendNotificationsUseCase
) : ViewModel() {

    init {
        connect()
        getNotificationCount()
    }

    ///////////////////////////////////////////////////////////////////////////
    // NOTIFICATION
    ///////////////////////////////////////////////////////////////////////////

    val invitationSent = MutableStateFlow("")
    val idUserTo = MutableStateFlow(-1)
    val fcmToken = MutableStateFlow("")

    fun updateToken() {
        if (fcmToken.value.isEmpty()) {
            viewModelScope.launch {
                val localToken = Firebase.messaging.token.await()
                val email = GlobalEntries.user.id ?: -1
                val pair = Pair(email, localToken)
                updateTokenUseCase.execute(pair).collectLatest {

                }
            }
        }
    }

    fun getUserToken(id: Int? = sharedPreference.getInt("idUser", -1)) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    Log.i("fcmTokenfreg", "id: $id")
                    Log.i("fcmTokenfreg", "getUserToken: ${u.fcmToken}")
                    fcmToken.update { u.fcmToken ?: "" }
                }
            }
        }
    }

    fun clearToken() {
        fcmToken.update { "" }
    }

    fun sendNotification(title: String, message: String) {
        viewModelScope.launch {

            val notificationMessage = NotificationMessage(
                recipientToken = fcmToken.value,
                title = title,
                body = message,
                data = mapOf("idInvitation" to "85")
            )
            sendNotificationsUseCase.execute(notificationMessage).collect {

            }
        }
    }

    fun connect() {
        val id = sharedPreference.getInt("idUser", 0)
        StompInvitationService.connect("$id")
        StompChatService.connect("$id")
    }
    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: MutableStateFlow<Int> get() = _notificationCount

    fun getNotificationCount() {
        viewModelScope.launch {
            StompNotificationService.messagesDemand.collect {
                var count = _notificationCount.value
                count += 1
                _notificationCount.update { count }
            }
        }
    }

    val seenNotification = MutableStateFlow("")

    fun seenNotification(item: Int) {
        viewModelScope.launch {
            seenNotificationUseCase.execute(item).collect { res ->
                if (res.status == ResourceState.SUCCESS) {
                    seenNotification.update {
                        res.data?.message ?: ""
                    }
                }
            }
        }
    }

    val userSender = MutableStateFlow(User())

    fun getUserById(id: Int = sharedPreference.getInt("idUser", 0)) {
        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    GlobalEntries.user = u
                    userSender.update { u }
                }
            }
        }
    }

    fun isNotMe(userSender: Int): Boolean =
        userSender != sharedPreference.getInt("idUser", 0)

    val marketDemandModel = MutableStateFlow(MarketDemandModel())

    fun changeAllModel() {
        marketDemandModel.update {
            marketDemand
        }
    }

    fun changePostName(name: String) {
        marketDemandModel.update {
            it.title = name
            it
        }
    }

    fun changeDescriptions(name: String) {
        marketDemandModel.update {
            it.description = name
            it
        }
    }

    fun changeLocation(name: String) {
        marketDemandModel.update {
            it.location = name
            it
        }
    }

    fun changeUrgency(name: String) {
        marketDemandModel.update {
            it.urgency = name
            it
        }
    }

    fun changeDeadline(name: String) {
        marketDemandModel.update {
            it.deadline = name
            it
        }
    }

    fun changeBudget(name: String) {
        marketDemandModel.update {
            it.budget = name
            it
        }
    }

    fun changePostType(name: ServiceCategory) {
        marketDemandModel.update {
            it.category = name
            it
        }
    }

    fun changeOtherCategory(name: String) {
        marketDemandModel.update {
            it.otherCategory = name
            it
        }
    }

    fun changeTool(name: ToolCategory) {
        marketDemandModel.update {
            it.tools = name
            it
        }
    }

    fun changeOtherTool(name: String) {
        marketDemandModel.update {
            it.otherTools = name
            it
        }
    }

    fun changePostId(id: Int) {
        marketDemandModel.update {
            it.id = id
            it
        }
    }

    fun countDownTrial(param: Int) {
        viewModelScope.launch {
            countDownTrialUseCase.execute(param).collect {

            }
        }
    }

    val demandStatus = MutableStateFlow("")

    fun saveDemand() {
        val id = sharedPreference.getInt("idUser", 0)

        val currentDate = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)

        val marketDemandModels = marketDemandModel.value
        marketDemandModels.date = formattedDate
        marketDemandModels.idSender = id

        marketDemandModel.update { marketDemandModels }

        viewModelScope.launch {
            if (isUpdatingDemand) {
                marketDemandModel.update {
                    it.id = marketDemand.id
                    it
                }
            }

            saveDemandUseCase.execute(marketDemandModel.value)
                .collectLatest { res ->
                    if (res.status == ResourceState.SUCCESS) {
                        demandStatus.update { res.data?.message ?: "" }
                    } else if (res.status == ResourceState.ERROR) {
                        demandStatus.update { res.message ?: "" }
                    }
                }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DEMAND BY ID
    ///////////////////////////////////////////////////////////////////////////
    private val _demand = MutableStateFlow(MarketDemandModel())
    val demand: StateFlow<MarketDemandModel> get() = _demand.asStateFlow()

    fun getDemandById(idDemand: Int) {
        viewModelScope.launch {
            getDemandUseCase.execute(idDemand).collect { res ->
                if (res.status == ResourceState.SUCCESS)
                    _demand.update {
                        res.data ?: MarketDemandModel()
                    }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // LIST DEMANDS
    ///////////////////////////////////////////////////////////////////////////
    private val _localItemRemoves = MutableStateFlow(-1)
    private val _demands = MutableStateFlow(PagingData.empty<MarketDemandModel>())
    val demands = _demands
        .combine(_localItemRemoves) { pagingData, removedId ->
            pagingData.filter { it.id != removedId }
        }
        .cachedIn(viewModelScope)

    fun getDemands() {
        viewModelScope.launch {
            getAllDemandUseCase.execute().collect { res ->
                if (res.status == ResourceState.SUCCESS)
                    _demands.update { res.data ?: PagingData.empty() }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DELETE
    ///////////////////////////////////////////////////////////////////////////
    val deleteDemandState = MutableStateFlow("")
    fun deleteDemand(item: Int) {
        viewModelScope.launch {
            deleteDemandUseCase.execute(item).collect { res ->
                if (res.status == ResourceState.SUCCESS) {
                    deleteDemandState.update {
                        res.data?.message ?: ""
                    }
                    _localItemRemoves.update { item }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // SEARCH
    ///////////////////////////////////////////////////////////////////////////
    fun filterDemands(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                getFilteredDemandUseCase.execute(query).collectLatest { res ->
                    _demands.update {
                        res.data ?: PagingData.empty()
                    }
                }
            }

        } else getDemands()
    }

    ///////////////////////////////////////////////////////////////////////////
    // FILTER
    ///////////////////////////////////////////////////////////////////////////
    fun searchDemands(query: List<String>) {
        query.map {
            filterDemands(it)
        }
    }

    fun getType(): JobType {
        val type = sharedPreference.getString("offerDemand", "")
        return if (type == "service") JobType.GET
        else JobType.NORMAL
    }

    fun logout() {
        sharedPreference.putString("token", "")
        sharedPreference.putString("offerDemand", "")
    }

    fun changeToJobDeal() {
        sharedPreference.putString("offerDemand", "")
    }

    fun convertDate(date: String): String {
        return if (date.isNotEmpty()) {
            val allDate = date.split(", ")
            val completeMonth = allDate[1]
            val year = allDate[2]
            val day = completeMonth.split(" ")[0]
            val month = completeMonth.split(" ")[1]
            val correctMonth = when (month) {
                "janvier" -> "jan"
                "février" -> "fev"
                "mars" -> "mars"
                "avril" -> "avril"
                "mai" -> "mai"
                "juin" -> "juin"
                "juillet" -> "juillet"
                "août" -> "aout"
                "septembre" -> "sep"
                "octobre" -> "oct"
                "novembre" -> "nov"
                "décembre" -> "dec"
                else -> ""
            }

            "$day $correctMonth $year"
        } else ""
    }
}