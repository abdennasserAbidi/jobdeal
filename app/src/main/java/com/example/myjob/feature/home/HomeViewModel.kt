package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.myjob.R
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.listIdToRemove
import com.example.myjob.domain.entities.Availabilities
import com.example.myjob.domain.entities.CategoryChoices
import com.example.myjob.domain.entities.Choices
import com.example.myjob.domain.entities.ContractTypeChoices
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.ExperienceChoices
import com.example.myjob.domain.entities.HOME_ENTITY
import com.example.myjob.domain.entities.ParentChoices
import com.example.myjob.domain.entities.SexChoices
import com.example.myjob.domain.entities.SituationChoices
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationParams
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.SaveToFavoriteUseCase
import com.example.myjob.domain.usecase.SendInvitationUseCase
import com.example.myjob.domain.usecase.home.GetAllUserUseCase
import com.example.myjob.domain.usecase.home.SearchUserUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject
import java.util.Calendar
import java.util.Date
import java.util.Locale

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val sendInvitationUseCase: SendInvitationUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

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
            R.string.categories_text -> selectedCategory.value.toMutableList()
            R.string.experience_text -> selectedExperience.value.toMutableList()
            R.string.disponibility_text -> availabilities.value.toMutableList()
            R.string.employment_type_text -> selectedTypeContract.value.toMutableList()
            R.string.situation_text -> situations.value.toMutableList()
            R.string.sexe_text -> sexChoices.value.toMutableList()
            else -> null
        }

        choiceParentSelect.update { title }

        /*R.string.activity_text ->
            R.string.institution_text ->
            R.string.location_text ->
            R.string.company_name_text -> */

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
    val selectedAvailability = MutableStateFlow(listOf(false, false, false, false))
    fun clearSelectionAvailability() {
        val availability = availabilities.value.toMutableList()
        for (i in 0 until availability.size) {
            availability[i].isSelected = false
        }
        availabilities.update {
            availability
        }

        val selectedAvailabilities = selectedAvailability.value.toMutableList()
        for (i in 0 until selectedAvailabilities.size) {
            selectedAvailabilities[i] = false
        }
        selectedAvailability.update {
            selectedAvailabilities
        }

        criteria.update {
            it.disponibility = mutableListOf()
            it
        }
    }

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

        val listDisponibility = criteria.value.disponibility

        if (isSelected) {
            availability.map {
                if (it.titleString.isNotEmpty() && !listDisponibility.contains(it.titleString)) listDisponibility.add(it.titleString)
            }
        } else {
            availability.map {
                if (it.titleString.isNotEmpty() && listDisponibility.contains(it.titleString)) listDisponibility.remove(it.titleString)
            }
        }

        criteria.update {
            it.disponibility = listDisponibility
            it
        }
    }

    val selectedExperience = MutableStateFlow(emptyList<ExperienceChoices>())
    val selectedExp = MutableStateFlow(listOf(false, false, false, false, false, false))

    fun clearSelectionExp() {
        val availability = selectedExperience.value.toMutableList()
        for (i in 0 until availability.size) {
            availability[i].isSelected = false
        }
        selectedExperience.update {
            availability
        }

        val selectedAvailabilities = selectedExp.value.toMutableList()
        for (i in 0 until selectedAvailabilities.size) {
            selectedAvailabilities[i] = false
        }
        selectedExp.update {
            selectedAvailabilities
        }

        criteria.update {
            it.experiences = mutableListOf()
            it
        }
    }

    val selectedCategory = MutableStateFlow(emptyList<CategoryChoices>())
    val selectedCat = MutableStateFlow(listOf(false, false))

    fun clearSelectionCategories() {
        val availability = selectedCategory.value.toMutableList()
        for (i in 0 until availability.size) {
            availability[i].isSelected = false
        }
        selectedCategory.update {
            availability
        }

        val selectedAvailabilities = selectedCat.value.toMutableList()
        for (i in 0 until selectedAvailabilities.size) {
            selectedAvailabilities[i] = false
        }
        selectedCat.update {
            selectedAvailabilities
        }

        criteria.update {
            it.categories = mutableListOf()
            it
        }
    }

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

        if (isSelected) {
            availability.map {
                if (it.titleString.isNotEmpty() && !list.contains(it.titleString)) list.add(it.titleString)
            }
        } else {
            availability.map {
                if (it.titleString.isNotEmpty() && list.contains(it.titleString)) list.remove(it.titleString)
            }
        }

        criteria.update {
            it.categories = list
            it
        }

    }

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

        if (isSelected) {
            availability.map {
                if (it.titleString.isNotEmpty() && !list.contains(it.titleString)) list.add(it.titleString)
            }
        } else {
            availability.map {
                if (it.titleString.isNotEmpty() && list.contains(it.titleString)) list.remove(it.titleString)
            }
        }

        criteria.update {
            it.experiences = list
            it
        }

    }

    val selectedTypeContract = MutableStateFlow(emptyList<ContractTypeChoices>())
    val selectedType = MutableStateFlow(listOf(false, false, false, false, false, false))

    fun clearSelectionContract() {
        val availability = selectedTypeContract.value.toMutableList()
        for (i in 0 until availability.size) {
            availability[i].isSelected = false
        }
        selectedTypeContract.update {
            availability
        }

        val selectedAvailabilities = selectedType.value.toMutableList()
        for (i in 0 until selectedAvailabilities.size) {
            selectedAvailabilities[i] = false
        }
        selectedType.update {
            selectedAvailabilities
        }

        criteria.update {
            it.typeContract = mutableListOf()
            it
        }
    }

    fun changeSelectionContract(index: Int, title: String, isSelected: Boolean) {
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

        if (isSelected) {
            availability.map {
                if (it.titleString.isNotEmpty() && !list.contains(it.titleString)) list.add(it.titleString)
            }
        } else {
            availability.map {
                if (it.titleString.isNotEmpty() && list.contains(it.titleString)) list.remove(it.titleString)
            }
        }

        criteria.update {
            it.typeContract = list
            it
        }

    }

    val situations = MutableStateFlow(emptyList<SituationChoices>())
    val selectedSituation = MutableStateFlow(listOf(false, false, false))

    fun clearSelectionSituation() {
        val availability = situations.value.toMutableList()
        for (i in 0 until availability.size) {
            availability[i].isSelected = false
        }
        situations.update {
            availability
        }

        val selectedAvailabilities = selectedSituation.value.toMutableList()
        for (i in 0 until selectedAvailabilities.size) {
            selectedAvailabilities[i] = false
        }
        selectedSituation.update {
            selectedAvailabilities
        }

        criteria.update {
            it.situation = mutableListOf()
            it
        }
    }

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

        if (isSelected) {
            availability.map {
                if (it.titleString.isNotEmpty() && !list.contains(it.titleString)) list.add(it.titleString)
            }
        } else {
            availability.map {
                if (it.titleString.isNotEmpty() && list.contains(it.titleString)) list.remove(it.titleString)
            }
        }

        criteria.update {
            it.situation = list
            it
        }

    }

    val sexChoices = MutableStateFlow(emptyList<SexChoices>())
    val selectedSex = MutableStateFlow(listOf(false, false))
    fun clearSelectionSex() {
        val availability = sexChoices.value.toMutableList()
        for (i in 0 until availability.size) {
            availability[i].isSelected = false
        }
        sexChoices.update {
            availability
        }

        val selectedAvailabilities = selectedSex.value.toMutableList()
        for (i in 0 until selectedAvailabilities.size) {
            selectedAvailabilities[i] = false
        }
        selectedSex.update {
            selectedAvailabilities
        }

        criteria.update {
            it.sex = mutableListOf()
            it
        }
    }

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

        if (isSelected) {
            availability.map {
                if (it.titleString.isNotEmpty() && !list.contains(it.titleString)) list.add(it.titleString)
            }
        } else {
            availability.map {
                if (it.titleString.isNotEmpty() && list.contains(it.titleString)) list.remove(it.titleString)
            }
        }

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

    val resume = MutableStateFlow("")

    fun getResume(user: User) {
        resume.update { user.resumeUser() }
    }

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
            it.tgm = name
            it
        }
    }

    fun matchCurrentProfile(user: User, status: String) {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)

        invitationParam.update {
            it.idCompany = sharedPreference.getInt("idUser", -1)
            it.companyName = GlobalEntries.user.companyName ?: ""
            it.idTo = user.id ?: -1
            it.fullName = user.fullName
            it.gender = user.sexe
            it.date = formattedDate
            it.status = status
            it
        }
        val invitationParams = InvitationParams(
            idConnected = sharedPreference.getInt("idUser", -1),
            invitationModel = invitationParam.value
        )
        viewModelScope.launch {
            sendInvitationUseCase.execute(invitationParams).collect { res ->
                when (res.status) {
                    ResourceState.SUCCESS -> {
                        Log.i("responseDataMessage", "matchCurrentProfile: ${res.data?.message}")
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
            getAllUserUseCase.execute().collectLatest { res ->
                _user.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    fun validateFilter(criteria: CriteriaModel) {
        viewModelScope.launch {
            if (!criteria.checkEmpty()) {
                searchUserUseCase.execute(criteria).collect { res ->

                    _user.update {
                        res.data ?: PagingData.empty()
                    }
                }
            } else getAllUser()
        }
    }

    fun skipCurrentProfile(user: User) {
        // Handle skipping the profile (e.g., move to the next profile)
        viewModelScope.launch {
            val s = _user.value.filter {
                it.id != user.id
            }
            _user.update { s }
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

        val listParent = listOf(
            ParentChoices(title = R.string.categories_text, isSelected = false),
            ParentChoices(title = R.string.experience_text, isSelected = false),
            ParentChoices(title = R.string.disponibility_text, isSelected = false),
            ParentChoices(title = R.string.employment_type_text, isSelected = false),
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
            Availabilities(title = R.string.disponibility1_text, isSelected = false),
            Availabilities(title = R.string.disponibility2_text, isSelected = false),
            Availabilities(title = R.string.disponibility3_text, isSelected = false),
            Availabilities(title = R.string.disponibility4_text, isSelected = false)
        )

        availabilities.update {
            list
        }

        val listExp = listOf(
            ExperienceChoices(title = R.string.intern_text, isSelected = false),
            ExperienceChoices(title = R.string.first_employemnt_text, isSelected = false),
            ExperienceChoices(title = R.string.confirmed_text, isSelected = false),
            ExperienceChoices(title = R.string.lead_text, isSelected = false),
            ExperienceChoices(title = R.string.manager_text, isSelected = false),
            ExperienceChoices(title = R.string.superior_text, isSelected = false)
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

    }

    fun updateQuery(newQuery: String) {
        _query.update { newQuery }
    }
}