package com.example.myjob.feature.home

import FreelanceSector
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.R
import com.example.myjob.base.messages.StompChatService
import com.example.myjob.base.messages.StompInvitationService
import com.example.myjob.base.messages.StompNotificationService
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.listIdToRemove
import com.example.myjob.domain.entities.Availabilities
import com.example.myjob.domain.entities.CategoryChoices
import com.example.myjob.domain.entities.CategoryModel
import com.example.myjob.domain.entities.Choices
import com.example.myjob.domain.entities.ContractTypeChoices
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.ExperienceChoices
import com.example.myjob.domain.entities.HOME_ENTITY
import com.example.myjob.domain.entities.JobType
import com.example.myjob.domain.entities.ParentChoices
import com.example.myjob.domain.entities.SexChoices
import com.example.myjob.domain.entities.SituationChoices
import com.example.myjob.domain.entities.StatusChoices
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.domain.entities.notification.NotificationMessage
import com.example.myjob.domain.usecase.home.CountDownTrialUserUseCase
import com.example.myjob.domain.usecase.home.GetAllUserServiceUseCase
import com.example.myjob.domain.usecase.home.GetAllUserUseCase
import com.example.myjob.domain.usecase.home.GetFilteredUserServiceUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.domain.usecase.home.SaveToFavoriteUseCase
import com.example.myjob.domain.usecase.invitation.FinishProcessUseCase
import com.example.myjob.domain.usecase.invitation.SendInvitationUseCase
import com.example.myjob.domain.usecase.notification.SendNotificationsUseCase
import com.example.myjob.domain.usecase.notification.UpdateTokenUseCase
import com.example.myjob.domain.usecase.search.GetFilteredUserUseCase
import com.example.myjob.domain.usecase.search.SearchUserServiceUseCase
import com.example.myjob.domain.usecase.search.SearchUserUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val updateTokenUseCase: UpdateTokenUseCase,
    private val sendNotificationsUseCase: SendNotificationsUseCase,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val getAllUserServiceUseCase: GetAllUserServiceUseCase,
    private val getFilteredUserServiceUseCase: GetFilteredUserServiceUseCase,
    private val sendInvitationUseCase: SendInvitationUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getFilteredUserUseCase: GetFilteredUserUseCase,
    private val finishProcessUseCase: FinishProcessUseCase,
    private val countDownTrialUserUseCase: CountDownTrialUserUseCase,
    private val searchUserServiceUseCase: SearchUserServiceUseCase
) : ViewModel() {

    fun getType(): JobType {
        val type = sharedPreference.getString("offerDemand", "")
        return if (type == "service") JobType.GET
        else JobType.NORMAL
    }

    fun changeToJobDeal() {
        sharedPreference.putString("offerDemand", "")
    }

    private val _invitations: MutableStateFlow<PagingData<InvitationModel>> =
        MutableStateFlow(value = PagingData.empty())
    val invitations: MutableStateFlow<PagingData<InvitationModel>> get() = _invitations

    fun connect() {
        val id = sharedPreference.getInt("idUser", 0)
        StompInvitationService.connect("$id")
        StompChatService.connect("$id")
    }

    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: MutableStateFlow<Int> get() = _notificationCount
    fun getNotificationCount() {
        viewModelScope.launch {
            StompNotificationService.messages.collect {
                var count = _notificationCount.value
                count += 1
                _notificationCount.update { count }
            }
        }
    }

    fun resetCountNotifications() {
        _notificationCount.update { 0 }
    }

    private val _messageCount = MutableStateFlow(0)
    val messageCount: MutableStateFlow<Int> get() = _messageCount
    fun getMessageCount() {
        viewModelScope.launch {
            StompChatService.messages.collect {
                var count = _messageCount.value
                count += 1
                _messageCount.update { count }
            }
        }
    }

    fun resetCountMessages() {
        _messageCount.update { 0 }
    }

    private val _invitationCount = MutableStateFlow(0)
    val invitationCount: MutableStateFlow<Int> get() = _invitationCount

    fun getInvitations() {
        viewModelScope.launch {
            StompInvitationService.messages.collect {
                if (it.status == InvitationStatus.ON_HOLD.name) {
                    var count = _invitationCount.value
                    count += 1
                    _invitationCount.update { count }
                }
            }
        }
    }

    fun resetCountInvitation() {
        _invitationCount.update { 0 }
    }

    ///////////////////////////////////////////////////////////////////////////
    // FINISH PROCESS
    ///////////////////////////////////////////////////////////////////////////
    private val _invitation: MutableStateFlow<InvitationParams> =
        MutableStateFlow(InvitationParams())
    val invitation: MutableStateFlow<InvitationParams> get() = _invitation
    fun finishProcess(invitationModel: InvitationModel) {

        val id = sharedPreference.getInt("idUser", 0)
        val invitationParams = InvitationParams(
            idConnected = id,
            invitationModel = invitationModel
        )

        viewModelScope.launch {
            finishProcessUseCase.execute(invitationParams)
                .collectLatest { res ->
                    _invitation.update {
                        res.data ?: InvitationParams()
                    }

                }
        }
    }

    val listTypeContract = MutableStateFlow(emptyList<String>())
    fun addToList(itemOne: String, itemTwo: String) {
        val list = listTypeContract.value.toMutableList()
        list.add(itemOne)
        list.add(itemTwo)
        listTypeContract.update {
            list
        }
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

    val choiceParentSelect = MutableStateFlow("")
    val listChoiceParentSelect = MutableStateFlow<List<Choices>?>(null)
    val listChoiceParentSelected = MutableStateFlow<List<Boolean>>(emptyList())
    val parentChoices = MutableStateFlow(emptyList<ParentChoices>())
    val selectedParentChoices = MutableStateFlow<List<Boolean>>(emptyList())
    fun clearSelectionParentChoices() {
        val availability = parentChoices.value.toMutableList()
        for (i in 0 until availability.size) {
            availability[i].isSelected = false
        }
        parentChoices.update {
            availability
        }

        val selectedAvailabilities = selectedParentChoices.value.toMutableList()
        for (i in 0 until selectedAvailabilities.size) {
            selectedAvailabilities[i] = false
        }
        selectedParentChoices.update {
            selectedAvailabilities
        }
    }

    fun changeUnKnown(titleRes: Int, index: Int, title: String, isSelected: Boolean) {
        when (titleRes) {
            R.string.categories_text -> {
                changeSelectionCategory(index, title, isSelected)
                listChoiceParentSelected.update {
                    selectedCat.value
                }
            }

            R.string.experience_text -> {
                changeSelectionExp(index, title, isSelected)
                listChoiceParentSelected.update {
                    selectedExp.value
                }
            }

            R.string.disponibility_text -> {
                changeSelectionAvailability(index, title, isSelected)
                listChoiceParentSelected.update {
                    selectedAvailability.value
                }
            }

            R.string.employment_type_text -> {
                changeSelectionContract(index, title, isSelected)
                listChoiceParentSelected.update {
                    selectedType.value
                }
            }

            R.string.situation_text -> {
                changeSelectionSituation(index, title, isSelected)
                listChoiceParentSelected.update {
                    selectedSituation.value
                }
            }

            R.string.status_type_text -> {
                val titleStatus = when (title) {
                    "Holding", "En attente" -> InvitationStatus.ON_HOLD.name
                    "In process", "En cours de traitement" -> InvitationStatus.IN_PROCESS.name
                    "Hired", "Embauché" -> InvitationStatus.HIRED.name
                    "Not Interested", "Pas intéressé" -> InvitationStatus.NOT_INTERESTED.name
                    else -> InvitationStatus.REJECTED.name
                }

                changeSelectionStatusCategory(index, titleStatus, isSelected)
                listChoiceParentSelected.update {
                    selectedStatusChoice.value
                }
            }

            R.string.sexe_text -> {
                changeSelectionSex(index, title, isSelected)
                listChoiceParentSelected.update {
                    selectedSex.value
                }
            }
        }
    }

    fun changeOption(title: String, titleRes: Int) {
        val listChoices: List<Choices>? = when (titleRes) {
            R.string.status_type_text -> selectedStatus.value.toMutableList()
            R.string.categories_text -> selectedCategory.value.toMutableList()
            R.string.experience_text -> selectedExperience.value.toMutableList()
            R.string.disponibility_text -> availabilities.value.toMutableList()
            R.string.employment_type_text -> selectedTypeContract.value.toMutableList()
            R.string.situation_text -> situations.value.toMutableList()
            R.string.sexe_text -> sexChoices.value.toMutableList()
            else -> null
        }

        choiceParentSelect.update { title }

        val l = listChoiceParentSelected.value.toMutableList()
        listChoices?.map {
            l.add(false)
        }
        listChoiceParentSelected.update {
            l
        }

        listChoiceParentSelect.update { listChoices }
    }

    fun changeSelectionParentChoices(index: Int, title: String, isSelected: Boolean) {
        val availability = parentChoices.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        parentChoices.update {
            availability
        }

        val selectedAvailabilities = selectedParentChoices.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedParentChoices.update {
            selectedAvailabilities
        }
    }

    val criteria = MutableStateFlow(CriteriaModel())

    val availabilities = MutableStateFlow(emptyList<Availabilities>())
    val selectedAvailability = MutableStateFlow(listOf(false, false, false, false, false, false))

    fun changeSelectionAvailability(index: Int, title: String, isSelected: Boolean) {
        val availability = availabilities.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        availabilities.update {
            availability
        }

        val selectedAvailabilities = selectedAvailability.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedAvailability.update {
            selectedAvailabilities
        }


        val list = criteria.value.disponibility
        if (isSelected && !list.contains(title))
            list.add(title) else list.remove(title)

        criteria.update {
            it.disponibility = list
            it
        }
    }

    val selectedExperience = MutableStateFlow(emptyList<ExperienceChoices>())
    val selectedExp = MutableStateFlow(listOf(false, false, false, false, false, false))

    fun changeSelectionExp(index: Int, title: String, isSelected: Boolean) {

        val availability = selectedExperience.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        selectedExperience.update {
            availability
        }

        val selectedAvailabilities = selectedExp.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedExp.update {
            selectedAvailabilities
        }

        val list = criteria.value.experiences
        if (isSelected && !list.contains(title))
            list.add(title) else list.remove(title)

        criteria.update {
            it.experiences = list
            it
        }

    }

    val selectedCategory = MutableStateFlow(emptyList<CategoryChoices>())
    val selectedCat = MutableStateFlow(listOf(false, false))

    fun changeSelectionCategory(index: Int, title: String, isSelected: Boolean) {
        val availability = selectedCategory.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        selectedCategory.update {
            availability
        }

        val selectedAvailabilities = selectedCat.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedCat.update {
            selectedAvailabilities
        }

        val list = criteria.value.categories
        if (isSelected && !list.contains(title))
            list.add(title) else list.remove(title)

        criteria.update {
            it.categories = list
            it
        }

    }

    val selectedStatus = MutableStateFlow(emptyList<StatusChoices>())
    val selectedStatusChoice = MutableStateFlow(listOf(false, false, false, false))
    fun changeSelectionStatusCategory(index: Int, title: String, isSelected: Boolean) {
        val availability = selectedStatus.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        selectedStatus.update {
            availability
        }

        val selectedAvailabilities = selectedStatusChoice.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedStatusChoice.update {
            selectedAvailabilities
        }

        val list = criteria.value.status
        if (isSelected && !list.contains(title))
            list.add(title) else list.remove(title)

        criteria.update {
            it.status = list
            it
        }

    }


    val selectedTypeContract = MutableStateFlow(emptyList<ContractTypeChoices>())
    val selectedType = MutableStateFlow(listOf(false, false, false, false, false, false))

    private fun changeSelectionContract(index: Int, title: String, isSelected: Boolean) {
        val availability = selectedTypeContract.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        selectedTypeContract.update {
            availability
        }

        val selectedAvailabilities = selectedType.value.toMutableList()
        selectedAvailabilities[index] = isSelected
        selectedType.update {
            selectedAvailabilities
        }

        val list = criteria.value.typeContract
        if (isSelected && !list.contains(title))
            list.add(title) else list.remove(title)

        criteria.update {
            it.typeContract = list
            it
        }

    }

    val situations = MutableStateFlow(emptyList<SituationChoices>())
    val selectedSituation = MutableStateFlow(listOf(false, false, false))

    fun changeSelectionSituation(index: Int, title: String, isSelected: Boolean) {

        val availability = situations.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        situations.update {
            availability
        }

        val selectedSituations = selectedSituation.value.toMutableList()
        selectedSituations[index] = isSelected
        selectedSituation.update {
            selectedSituations
        }

        val list = criteria.value.situation
        if (isSelected && !list.contains(title))
            list.add(title) else list.remove(title)

        criteria.update {
            it.situation = list
            it
        }

    }

    val sexChoices = MutableStateFlow(emptyList<SexChoices>())
    val selectedSex = MutableStateFlow(listOf(false, false))

    fun changeSelectionSex(index: Int, title: String, isSelected: Boolean) {
        val availability = sexChoices.value.toMutableList()
        availability[index].titleString = title
        availability[index].isSelected = isSelected
        sexChoices.update {
            availability
        }

        val selectedSexChoice = selectedSex.value.toMutableList()
        selectedSexChoice[index] = isSelected
        selectedSex.update {
            selectedSexChoice
        }

        val list = criteria.value.sex
        if (isSelected && !list.contains(title))
            list.add(title) else list.remove(title)

        criteria.update {
            it.sex = list
            it
        }
    }

    fun changeActivitySector(list: List<String>) {

        val listSector = criteria.value.preferredActivitySector
        list.map {
            if (!listSector.contains(it)) listSector.add(it)
        }

        criteria.update {
            it.preferredActivitySector = listSector
            it
        }
    }

    fun changeLocation(list: List<String>) {

        val listSector = criteria.value.location
        list.map {
            if (!listSector.contains(it)) listSector.add(it)
        }

        criteria.update {
            it.location = listSector
            it
        }
    }

    fun changeCompanies(list: List<String>) {

        val listSector = criteria.value.companies
        list.map {
            if (!listSector.contains(it)) listSector.add(it)
        }

        criteria.update {
            it.companies = listSector
            it
        }
    }

    fun changeInstitutions(list: List<String>) {

        val listInstitution = criteria.value.institutions
        list.map {
            if (!listInstitution.contains(it)) listInstitution.add(it)
        }

        criteria.update {
            it.institutions = listInstitution
            it
        }
    }


    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _words = MutableStateFlow<List<User>>(emptyList())
    val words: StateFlow<List<User>> = _words

    val users = MutableStateFlow<List<User>>(emptyList())
    val listHomeEntity = MutableStateFlow(HOME_ENTITY)

    var currentProfile by mutableStateOf(User())

    var lang = ""
    var langState = MutableStateFlow(lang)

    val invitationParam = MutableStateFlow(InvitationModel())
    fun changePostName(name: String) {
        invitationParam.update {
            it.message = name
            it
        }
    }

    fun changeDescriptions(name: String) {
        invitationParam.update {
            it.description = name
            it
        }
    }

    fun changeTypeContract(name: String) {
        invitationParam.update {
            it.typeContract = name
            it
        }
    }

    fun changeDisponibility(name: String) {
        invitationParam.update {
            it.disponibility = name
            it
        }
    }

    fun changeSalary(name: String) {
        invitationParam.update {
            it.salary = name
            it
        }
    }

    fun changeTgm(name: String) {
        invitationParam.update {
            it.tgm = name
            it
        }
    }

    fun changeNbDays(name: String) {
        invitationParam.update {
            it.nbDaysPerWeek = name
            it
        }
    }

    val loadingState = MutableStateFlow(false)

    fun changeContractWork(name: String) {
        invitationParam.update {
            it.nameContract = name
            it
        }
    }

    fun changeSecondContractWork(name: String) {
        invitationParam.update {
            it.nameSecondContract = name
            it
        }
    }

    val durationMission = MutableStateFlow("")
    fun changeDuration(duration: String) {
        durationMission.update { duration }
    }

    fun matchCurrentProfile(
        user: User,
        status: String,
        descriptionContract: String,
        duration: String
    ) {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)

        invitationParam.update {
            it.idCompany = sharedPreference.getInt("idUser", -1)
            it.companyName = GlobalEntries.user.companyName ?: ""
            it.idTo = user.id ?: -1
            it.roleReceiver = user.role ?: "Company"
            it.fullName = user.fullName
            it.gender = if (lang == "French" || lang == "Français") user.sexe?.genderFr else user.sexe?.genderEng
            it.date = formattedDate
            it.status = status
            it.duration = duration
            it.descriptionContract = descriptionContract

            if (it.nameContract == "AUTRE") {
                it.nameContract = it.nameSecondContract
            }

            it
        }
        val invitationParams = InvitationParams(
            idConnected = sharedPreference.getInt("idUser", -1),
            invitationModel = invitationParam.value
        )
        viewModelScope.launch {
            sendInvitationUseCase.execute(invitationParams).collect { res ->
                when (res.status) {

                    ResourceState.LOADING -> {
                        loadingState.update { true }
                    }

                    ResourceState.ERROR -> {
                        loadingState.update { false }
                    }

                    ResourceState.SUCCESS -> {
                        loadingState.update { false }

                        invitationSent.update {
                            res.data?.message ?: ""
                        }
                        idUserTo.update { user.id ?: -1 }
                        getCurrent()
                    }

                    else -> {}
                }
            }
        }
    }

    val qs = MutableStateFlow(emptyList<Int>())

    fun removeFromGlobal(id: Int) {
        listIdToRemove.add(id)
        qs.update {
            listIdToRemove
        }
    }

    val filterdUser = MutableStateFlow(emptyList<User>())

    fun filtering(list: List<User>) {
        val s = list.filter { user ->
            !listIdToRemove.contains(user.id)
        }

        filterdUser.update { s }
    }

    private val _user: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(value = PagingData.empty())

    val user: MutableStateFlow<PagingData<User>> get() = _user

    fun filterUser(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                val id = sharedPreference.getInt("idUser", 0)
                val params = Pair(query, id)
                getFilteredUserUseCase.execute(params).collectLatest { res ->
                    _user.update {
                        res.data ?: PagingData.empty()
                    }
                }
            }

        } else getAllUser()
    }

    fun getPDFName(): String {
        val fullName = sharedPreference.getString("username", "") ?: ""
        return if (fullName.contains(" "))
            "${fullName.replace(" ", "").trim()}Detail.pdf" else ""
    }


    val currentPage = MutableStateFlow(1)

    fun updateCurrentPage() {
        var page = currentPage.value
        currentPage.update {
            page++
            page
        }
    }

    fun getAllUser() {
        viewModelScope.launch {
            val id = sharedPreference.getInt("idUser", -1)
            getAllUserUseCase.execute(id).collectLatest { res ->
                _user.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    private val _userService: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(value = PagingData.empty())

    val userService: MutableStateFlow<PagingData<User>> get() = _userService

    fun getAllUserService() {
        viewModelScope.launch {
            val id = sharedPreference.getInt("idUser", -1)
            getAllUserServiceUseCase.execute(id).collectLatest { res ->
                _userService.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // SEARCH
    ///////////////////////////////////////////////////////////////////////////
    fun filterUserService(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                getFilteredUserServiceUseCase.execute(query).collectLatest { res ->
                    _userService.update {
                        res.data ?: PagingData.empty()
                    }
                }
            }

        } else getAllUserService()
    }

    fun countDownTrial() {
        viewModelScope.launch {
            countDownTrialUserUseCase.execute(sharedPreference.getInt("idUser", 0)).collect {

            }
        }
    }

    fun isNotMe(userSender: Int): Boolean =
        userSender != sharedPreference.getInt("idUser", 0)

    ///////////////////////////////////////////////////////////////////////////
    // FILTER
    ///////////////////////////////////////////////////////////////////////////

    val isFilterFinished = MutableStateFlow(false)

    fun searchUserService(categoryModel: CategoryModel) {

        viewModelScope.launch {

            if (categoryModel.listSector.isNotEmpty() || categoryModel.listService.isNotEmpty()) {
                searchUserServiceUseCase.execute(categoryModel).collect { res ->
                    isFilterFinished.update { true }
                    _userService.update { res.data ?: PagingData.empty() }
                }
            } else getAllUserService()
        }
    }

    fun validateFilter(criteria: CriteriaModel) {
        viewModelScope.launch {
            criteria.language = sharedPreference.getString("lang", "Français") ?: "Français"
            if (criteria.checkEmpty()) {
                criteria.idUser = sharedPreference.getInt("idUser", -1)
                searchUserUseCase.execute(criteria).collect { res ->
                    _user.update {
                        res.data ?: PagingData.empty()
                    }
                }
            } else getAllUser()
        }
    }

    fun extractExp(experience: MutableList<Experience>): String {
        var res = "new"
        if (experience.isNotEmpty()) {
            val exp = experience[0]
            val date = exp.dateStart ?: ""
            if (date.contains(",")) {
                val dates = date.split(", ")
                if (dates.isNotEmpty()) {
                    val year = dates[2].toInt()

                    val calendar: Calendar = Calendar.getInstance()
                    val currentYear: Int = calendar.get(Calendar.YEAR)

                    val diff = currentYear - year

                    if (diff > 0) res = "$diff years experiences"
                }
            }
        }
        return res
    }

    val updateFavoriteState = MutableStateFlow(false)

    fun saveToFavorites(idUserConnected: Int, candidateId: Int) {
        viewModelScope.launch {
            saveToFavoriteUseCase.execute(Pair(idUserConnected, candidateId)).collect { res ->
                updateFavoriteState.update { res.data?.message == "saved successfully" }
            }
        }
    }

    init {

        lang = sharedPreference.getString("lang", "") ?: ""
        langState.update { lang }
        connect()
        getCurrent()

        val listParent = listOf(
            ParentChoices(title = R.string.status_type_text, isSelected = false),
            ParentChoices(title = R.string.employment_type_text, isSelected = false),
            ParentChoices(title = R.string.categories_text, isSelected = false),
            ParentChoices(title = R.string.experience_text, isSelected = false),
            ParentChoices(title = R.string.disponibility_text, isSelected = false),
            ParentChoices(title = R.string.situation_text, isSelected = false),
            ParentChoices(title = R.string.sexe_text, isSelected = false),
            ParentChoices(title = R.string.activity_text, isSelected = false),
            ParentChoices(title = R.string.institution_text, isSelected = false),
            ParentChoices(title = R.string.location_text, isSelected = false),
            ParentChoices(title = R.string.company_name_text, isSelected = false)
        )

        val selectedParentChoice = selectedParentChoices.value.toMutableList()
        listParent.map {
            selectedParentChoice.add(false)
        }

        selectedParentChoices.update {
            selectedParentChoice
        }

        parentChoices.update {
            listParent
        }

        val list = listOf(
            Availabilities(title = R.string.immediately_text, isSelected = false),
            Availabilities(title = R.string.one_week_text, isSelected = false),
            Availabilities(title = R.string.two_week_text, isSelected = false),
            Availabilities(title = R.string.one_month_text, isSelected = false),
            Availabilities(title = R.string.two_months_text, isSelected = false),
            Availabilities(title = R.string.more_3_months_text, isSelected = false)
        )

        availabilities.update {
            list
        }


        val listExp = listOf(
            ExperienceChoices(title = R.string.entry_level_text, isSelected = false),
            ExperienceChoices(title = R.string.junior_text, isSelected = false),
            ExperienceChoices(title = R.string.mid_level_text, isSelected = false),
            ExperienceChoices(title = R.string.senior_text, isSelected = false),
            ExperienceChoices(title = R.string.lead_team_text, isSelected = false),
            ExperienceChoices(title = R.string.executive_text, isSelected = false)
        )

        selectedExperience.update {
            listExp
        }

        val listContract = listOf(
            ContractTypeChoices(title = R.string.full_time_text, isSelected = false),
            ContractTypeChoices(title = R.string.part_time_text, isSelected = false),
            ContractTypeChoices(title = R.string.self_employed_text, isSelected = false),
            ContractTypeChoices(title = R.string.internship_text, isSelected = false),
            ContractTypeChoices(title = R.string.apprenticeship_text, isSelected = false),
            ContractTypeChoices(title = R.string.seasonal_text, isSelected = false)
        )

        selectedTypeContract.update {
            listContract
        }

        val listSituation = listOf(
            SituationChoices(title = R.string.single_text, isSelected = false),
            SituationChoices(title = R.string.engaged_text, isSelected = false),
            SituationChoices(title = R.string.married_text, isSelected = false)
        )

        situations.update {
            listSituation
        }

        val listSex = listOf(
            SexChoices(title = R.string.male_text, isSelected = false),
            SexChoices(title = R.string.female_text, isSelected = false)
        )

        sexChoices.update {
            listSex
        }

        val listCategories = listOf(
            CategoryChoices(title = R.string.type1_text, isSelected = false),
            CategoryChoices(title = R.string.type2_text, isSelected = false)
        )
        selectedCategory.update {
            listCategories
        }

        val listStatus = listOf(
            StatusChoices(title = R.string.holding, isSelected = false),
            StatusChoices(title = R.string.in_process_text, isSelected = false),
            StatusChoices(title = R.string.hired_text, isSelected = false),
            StatusChoices(title = R.string.not_interested_text, isSelected = false)
        )

        selectedStatus.update {
            listStatus
        }

        getInvitations()
        getMessageCount()
    }

    fun getCurrent() {
        viewModelScope.launch {
            getUserUseCase.execute(sharedPreference.getInt("idUser", 0)).collect {
                it.data?.let { u ->
                    GlobalEntries.user = u
                }
            }
        }
    }

    fun logout() {
        sharedPreference.putString("token", "")
        sharedPreference.putString("offerDemand", "")
    }

    fun offerDemand() {
        sharedPreference.putString("offerDemand", "service")
    }

    fun updateQuery(newQuery: String) {
        _query.update { newQuery }
    }
}