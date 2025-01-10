package com.example.myjob.feature.profile

import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.DEFAULT_DEGREE
import com.example.myjob.domain.entities.DEFAULT_ROLE
import com.example.myjob.domain.entities.DEFAULT_TYPE
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.GetAllEducationUseCase
import com.example.myjob.domain.usecase.GetAllExperienceUseCase
import com.example.myjob.domain.usecase.GetUserUseCase
import com.example.myjob.domain.usecase.RemoveEducationUseCase
import com.example.myjob.domain.usecase.RemoveExperienceUseCase
import com.example.myjob.domain.usecase.SaveEducationUseCase
import com.example.myjob.domain.usecase.SaveExperienceUseCase
import com.example.myjob.domain.usecase.SavePersonalUseCase
import com.example.myjob.domain.usecase.home.GetAllEducUseCase
import com.example.myjob.domain.usecase.home.GetAllExpUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val saveExperienceUseCase: SaveExperienceUseCase,
    private val getAllExperienceUseCase: GetAllExperienceUseCase,
    private val getAllExpUseCase: GetAllExpUseCase,
    private val getAllEducUseCase: GetAllEducUseCase,
    private val saveEducationUseCase: SaveEducationUseCase,
    private val getAllEducationUseCase: GetAllEducationUseCase,
    private val savePersonalUseCase: SavePersonalUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val removeExperienceUseCase: RemoveExperienceUseCase,
    private val removeEducationUseCase: RemoveEducationUseCase
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    // PDF
    ///////////////////////////////////////////////////////////////////////////

    val pageIndex = MutableStateFlow(1)

    fun changeIndex() {
        Log.i("pageIndex", "before: ${pageIndex.value}")
        pageIndex.update {
            it + 1
        }
        Log.i("pageIndex", "after: ${pageIndex.value}")
    }

    fun createPage(pdfDocument: PdfDocument, pageIndex: Int): PdfDocument.Page {
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, pageIndex).create()
        return pdfDocument.startPage(pageInfo)
    }


    var switchValue = MutableStateFlow(true)
    var isSearch = MutableStateFlow(false)
    var isCountryShowed = MutableStateFlow(false)
    var isDateShowed = MutableStateFlow(false)

    fun isFromLogin(fromLogin: Boolean) {
        sharedPreference.putBoolean("isFromLogin", fromLogin)
    }

    fun changeVisibilitySearch(search: Boolean) {
        isSearch.update {
            search
        }
    }

    fun changeVisibilityCountry(search: Boolean) {
        isCountryShowed.update {
            search
        }
    }

    fun changeVisibilityDate(isShowed: Boolean) {
        isDateShowed.update {
            isShowed
        }
    }

    var lang = ""
    var langState = MutableStateFlow(lang)

    fun changeSwitch(isCurrent: Boolean) {
        switchValue.update {
            isCurrent
        }
    }

    val roles = MutableStateFlow(DEFAULT_ROLE)

    val user = MutableStateFlow(User(id = sharedPreference.getInt("idUser", 0)))

    var withPersonalDetail = MutableStateFlow(user.value.showUser(lang).isNotEmpty())

    fun getInitialDetail() {
        withPersonalDetail.update {
            user.value.showUser(lang).isNotEmpty()
        }
    }

    fun changeWithDetailPersonal(isDetails: Boolean) {
        withPersonalDetail.update {
            isDetails
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GENERIC INFO
    ///////////////////////////////////////////////////////////////////////////

    var withGenericDetail = MutableStateFlow(user.value.showUser(lang).isNotEmpty())

    fun changeWithGenericPersonal(isDetails: Boolean) {
        withGenericDetail.update {
            isDetails
        }
    }

    var titleGeneric = MutableStateFlow("")

    fun changeTitleGeneric(item: String) {
        titleGeneric.update { item }

        user.update {
            it.preferredActivitySector = item
            it
        }

    }

    var completePhone = MutableStateFlow("")
    var phone = MutableStateFlow("")

    fun changePhone(search: String) {
        phone.update {
            search
        }
    }
    fun changeCompletePhone(search: String) {
        completePhone.update {
            search
        }

        user.update {
            it.phone = search
            it
        }
    }

    var availability = MutableStateFlow("")

    fun changeAvailability(search: String) {
        availability.update {
            search
        }

        user.update {
            it.availability = search
            it
        }
    }

    var rangeSalary = MutableStateFlow("")

    fun changeRangeSalary(search: String) {
        rangeSalary.update {
            search
        }

        user.update {
            it.rangeSalary = search
            it
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // FULLNAME
    ///////////////////////////////////////////////////////////////////////////
    val isFirstNameValid = MutableStateFlow(false)
    val userFullName = MutableStateFlow("")

    fun validateFullName(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isFirstNameValid.update {
                text.isNotEmpty()
            }
            t = text.isNotEmpty()
        }
        return t
    }

    fun changeUserName(name: String) {
        userFullName.update { name }
        user.update {
            it.fullName = name
            if (it.fullName?.contains(" ") == true) {
                it.firstName = name.split(" ")[0]
                it.lastName = name.split(" ")[1]
            }
            it
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ADDRESS
    ///////////////////////////////////////////////////////////////////////////
    val isAddressValid = MutableStateFlow(false)
    val userAddress = MutableStateFlow("")

    fun validateAddress(text: String): Boolean {
        var t = false
        viewModelScope.launch {
            isAddressValid.update {
                text.isNotEmpty()
            }
            t = text.isNotEmpty()
        }
        return t
    }

    fun changeAddress(name: String) {
        userAddress.update { name }
        user.update {
            it.address = name
            it
        }
    }

    var birthDateUser = MutableStateFlow("")

    fun changeBirthDate(name: String) {
        user.update {
            it.birthDate = name
            it
        }

        birthDateUser.update { name }

    }

    var userSex = MutableStateFlow("")

    fun getSex(): String {
        val lang = sharedPreference.getString("lang", "") ?: ""
        return user.value.getSex(lang)
    }

    fun getSituation(): String {
        val lang = sharedPreference.getString("lang", "") ?: ""
        return user.value.getSituation(lang)
    }

    fun changeSex(name: String) {
        val lang = sharedPreference.getString("lang", "") ?: ""
        userSex.update {
            user.value.changeSex(name, lang)
        }
        user.update {
            it.changeSex(name, lang)
            it
        }
    }
    var userSituation = MutableStateFlow("")
    fun changeSituation(name: String) {
        val lang = sharedPreference.getString("lang", "") ?: ""
        userSituation.update {
            user.value.changeSituation(name, lang)
        }
        user.update {
            it.changeSituation(name, lang)
            it
        }
    }

    fun changeActivitySector(activity: String) {
        user.update {
            it.activitySector = activity
            it
        }
    }

    fun mapperPersonalInfo(user: User) {
        availability.update { user.availability ?: "" }
        rangeSalary.update { user.rangeSalary ?: "" }

        with(user) {
            titleGeneric.update { preferredActivitySector ?: "" }
            userSituation.update { situation ?: "" }
            userSex.update { sexe ?: "" }
            birthDateUser.update { birthDate ?: "" }
            userAddress.update { address ?: "" }
            userFullName.update { fullName ?: "" }
            completePhone.update { phone ?: "" }
        }
    }

    fun saveUserPersonalInfo() {
        viewModelScope.launch {
            Log.i("userValue", "saveUserPersonalInfo: ${user.value}")
            savePersonalUseCase.execute(user.value).collect {
                if (it.data?.message == "saved successfully") {
                    getUser(lang, user.value.id ?: 0)
                }
            }
        }
    }

    fun getMapUser(): Map<String, String> {
        return GlobalEntries.user.showUser(lang)
    }

    val showUser = MutableStateFlow(mapOf<String, String>())

    private fun getUser(lang: String, id: Int) {

        viewModelScope.launch {
            getUserUseCase.execute(id).collect {
                it.data?.let { u ->
                    user.update { u }
                    GlobalEntries.user = u
                    sharedPreference.putString("username", u.fullName ?: "")
                    Log.i("userValue", "getUser: $u")
                    Log.i("userValue", "getUser: ${u.showUser(lang)}")
                    showUser.update {
                        u.showUser(lang)
                    }
                }
            }
        }
    }

    val employmentType = MutableStateFlow(DEFAULT_TYPE)

    ///////////////////////////////////////////////////////////////////////////
    // EDUCATIONS
    ///////////////////////////////////////////////////////////////////////////

    val degreeList = MutableStateFlow(DEFAULT_DEGREE)

    val educations = MutableStateFlow<List<Educations>>(emptyList())
    val allEduc = MutableStateFlow<List<Educations>>(emptyList())

    private val _education: MutableStateFlow<PagingData<Educations>> =
        MutableStateFlow(value = PagingData.empty())
    val education: MutableStateFlow<PagingData<Educations>> get() = _education

    var educationDetail = MutableStateFlow(false)

    fun getInitialEducationDetail() {
        educationDetail.update {
            educations.value.isEmpty()
        }
    }

    fun changeEducationWithDetail(isDetails: Boolean) {
        educationDetail.update {
            isDetails
        }

    }

    var switchEducationValue = MutableStateFlow(true)

    fun changeSwitchEducation(isCurrent: Boolean) {
        switchEducationValue.update {
            isCurrent
        }
    }

    var locationEducation = MutableStateFlow("")
    var schoolName = MutableStateFlow("")
    var degree = MutableStateFlow("")
    var startDateEducation = MutableStateFlow("")
    var endDateEducation = MutableStateFlow("")
    var grade = MutableStateFlow("")
    var fieldStudy = MutableStateFlow("")
    var description = MutableStateFlow("")

    fun changeDescriptionEducation(item: String) {
        description.update { item }
    }

    fun changeLocationEducation(item: String) {
        locationEducation.update { item }
    }

    fun changeFieldOfStudy(item: String) {
        fieldStudy.update { item }
    }

    fun changeSchool(item: String) {
        schoolName.update { item }
    }

    fun changeStartDateEducation(item: String) {
        startDateEducation.update { item }
    }

    fun changeDegree(item: String) {
        degree.update { item }
    }

    fun changeEndDateEducation(item: String) {
        endDateEducation.update { item }
    }

    fun changeGrade(item: String) {
        grade.update { item }
    }

    fun mapperEducation(educations: Educations) {
        educations.let { item ->
            schoolName.update { item.schoolName ?: "" }
            locationEducation.update { item.place ?: "" }
            fieldStudy.update { item.fieldStudy ?: "" }
            startDateEducation.update { item.dateStart ?: "" }
            endDateEducation.update { item.dateEnd ?: "" }
            grade.update { item.grade ?: "" }
            degree.update { item.degree ?: "" }
            description.update { item.description ?: "" }
            switchEducationValue.update { item.stillStudying ?: true }
        }
    }

    fun changeEducationUpdateOrAdd(item: Educations = Educations()) {
        schoolName.update { item.schoolName ?: "" }
        locationEducation.update { item.place ?: "" }
        fieldStudy.update { item.fieldStudy ?: "" }
        startDateEducation.update { item.dateStart ?: "" }
        endDateEducation.update { item.dateEnd ?: "" }
        grade.update { item.grade ?: "" }
        degree.update { item.degree ?: "" }
        description.update { item.description ?: "" }
    }

    fun getAllEduc(idUser: Int) {
        viewModelScope.launch {
            getAllEducUseCase.execute(idUser)
                .collectLatest { res ->
                    allEduc.update {
                        res.data ?: emptyList()
                    }
                }
        }
    }

    fun getAllEducations(idUser: Int) {
        viewModelScope.launch {
            getAllEducationUseCase.execute(idUser).collectLatest { res ->

                val json = sharedPreference.getString("jsonEducation", "") ?: ""
                if (json.isNotEmpty()) {
                    val objectList = Gson().fromJson(json, Array<Educations>::class.java).asList()

                    educations.update {
                        objectList
                    }
                }

                _education.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }


    val destinationForm = MutableStateFlow("profile")
    fun changeDestinationForm(title: String) {
        destinationForm.update { title }
    }

    val saveEducationState = MutableStateFlow("")

    fun saveEducation(educations: Educations) {
        viewModelScope.launch {
            educations.idUser = user.value.id
            saveEducationUseCase.execute(educations).collect { res ->
                saveEducationState.update { res.data?.message ?: "" }
                if (res.data?.message == "saved successfully") getAllEducations(user.value.id ?: 0)
            }
        }
    }

    val removeEducationState = MutableStateFlow("")

    fun removeEducation(educationId: Int) {
        viewModelScope.launch {
            val pair = Pair(user.value.id ?: -1, educationId)
            removeEducationUseCase.execute(pair).collect { res ->
                removeEducationState.update { res.data?.message ?: "" }
                if (res.data?.message == "removed successfully") getAllEducations(user.value.id ?: 0)
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // EXPERIENCE
    ///////////////////////////////////////////////////////////////////////////

    private val _experience: MutableStateFlow<PagingData<Experience>> =
        MutableStateFlow(value = PagingData.empty())
    val experience: MutableStateFlow<PagingData<Experience>> get() = _experience

    val allExp: MutableStateFlow<List<Experience>> = MutableStateFlow(emptyList())
    val exp: MutableStateFlow<List<Experience>> = MutableStateFlow(emptyList())
    val exp1: MutableStateFlow<Experience> = MutableStateFlow(Experience())

    var withDetail = MutableStateFlow(false)

    fun getInitialExperienceDetail() {
        withDetail.update {
            exp.value.isEmpty()
        }
    }

    fun changeWithDetail(isDetails: Boolean) {
        withDetail.update {
            isDetails
        }
    }

    var locationExp = MutableStateFlow("")
    var companyExp = MutableStateFlow("")
    var typeEmploymentExp = MutableStateFlow("")
    var typeContractExp = MutableStateFlow("")
    var freelanceFeeType = MutableStateFlow("Hourly")
    var hourlyRateExp = MutableStateFlow(0)
    var nbHoursExp = MutableStateFlow(0)
    var nbDaysExp = MutableStateFlow(0)
    var birthDate = MutableStateFlow("")
    var endDate = MutableStateFlow("")
    var salary = MutableStateFlow(0)
    var id = MutableStateFlow(0)
    var titleExp = MutableStateFlow("")
    var anotherActivity = MutableStateFlow("")

    fun changeSalary(exp: Experience, search: Int) {
        salary.update { search }

        user.update {
            val e = it.experience?.find { experience ->
                experience.id == exp.id
            } ?: Experience()

            var i = 0

            val list = it.experience?.filterIndexed { index, experience ->
                if (experience.id == exp.id) i = index
                experience.id == exp.id
            } ?: emptyList()

            if (list.isNotEmpty()) {
                val e1 = list[0]
                val s = e1.copy(salary = search)
                var sq = it.experience?.get(i)
                sq = s
            }

            it
        }
    }

    fun mapperExperience(experiences: Experience) {
        with(experiences) {
            titleExp.update { title ?: "" }
            anotherActivity.update { anotherActivitySector ?: "" }
            locationExp.update { place ?: "" }
            companyExp.update { companyName ?: "" }
            val typeLang = if (lang == "French") "Contrat" else "Contract"
            typeEmploymentExp.update { if (type.isNullOrEmpty()) typeLang else type }
            val typeFee = if (lang == "French") "Par heurs" else "Hourly"
            freelanceFeeType.update { if (freelanceFee.isNullOrEmpty()) typeFee else freelanceFee }
            typeContractExp.update { typeContract ?: "" }
            hourlyRateExp.update { hourlyRate ?: 0 }
            nbHoursExp.update { nbHours ?: 0 }
            nbDaysExp.update { nbDays ?: 0 }
            birthDate.update { dateStart ?: "" }
            endDate.update { dateEnd ?: "" }
        }
        salary.update { experiences.salary ?: 0 }
    }

    fun changeAnotherActivityExp(item: String) {
        anotherActivity.update { item }
    }
    fun changeLocationExp(item: String) {
        locationExp.update { item }
    }

    fun changeCompanyExp(item: String) {
        companyExp.update { item }
    }

    fun changeStartDateExp(item: String) {
        birthDate.update { item }
    }

    fun changeTypeEmpExp(item: String) {
        typeEmploymentExp.update { item }
    }

    fun changeHourlyRateExp(item: Int) {
        hourlyRateExp.update { item }
    }
    fun changeNbHoursExp(item: Int) {
        nbHoursExp.update { item }
    }
    fun changeNbDaysExp(item: Int) {
        nbDaysExp.update { item }
    }

    fun changeFeeType(item: String) {
        freelanceFeeType.update { item }
    }

    fun changeTypeContractExp(item: String) {
        typeContractExp.update { item }
    }

    fun changeEndDateExp(item: String) {
        endDate.update { item }
    }

    fun changeTitleExp(item: String) {
        titleExp.update { item }
    }

    fun changeUpdateOrAdd(item: Experience = Experience()) {
        titleExp.update { item.title ?: "" }
        anotherActivity.update { item.anotherActivitySector ?: "" }
        locationExp.update { item.place ?: "" }
        companyExp.update { item.companyName ?: "" }
        val typeLang = if (lang == "French") "Contrat" else "Contract"
        typeEmploymentExp.update { if (item.type.isNullOrEmpty()) typeLang else item.type }
        typeContractExp.update { item.typeContract ?: "" }
        val typeFee = if (lang == "French") "Par heurs" else "Hourly"
        freelanceFeeType.update { if (item.freelanceFee.isNullOrEmpty()) typeFee else item.freelanceFee }
        nbHoursExp.update { item.nbHours ?: 10 }
        nbDaysExp.update { item.nbDays ?: 10 }
        birthDate.update { item.dateStart ?: "" }
        endDate.update { item.dateEnd ?: "" }
        salary.update { item.salary ?: 0 }
        hourlyRateExp.update { item.hourlyRate ?: 10 }

        exp1.update {
            val e = Experience(
                id = 0,
                title = item.title ?: "",
                place = item.place ?: "",
                companyName = item.companyName ?: "",
                dateStart = item.dateStart ?: "",
                dateEnd = item.dateEnd ?: "",
                type = item.type ?: "",
                typeContract = item.typeContract ?: "",
                freelanceFee = item.freelanceFee ?: "",
                anotherActivitySector = item.anotherActivitySector ?: "",
                nbHours = item.nbHours ?: 0,
                nbDays = item.nbDays ?: 0,
                salary = item.salary ?: 0,
                idUser = user.value.id ?: 0,
            )

            e
        }
        Log.i("ezeezezze", "1: ${exp1.value}")

    }

    fun getAllExp(idUser: Int) {
        viewModelScope.launch {
            getAllExpUseCase.execute(idUser)
                .collectLatest { res ->
                    allExp.update {
                        res.data ?: emptyList()
                    }
                }
        }
    }
    fun getAllExperience(idUser: Int) {
        viewModelScope.launch {
            getAllExperienceUseCase.execute(idUser)
                .collectLatest { res ->
                    val json = sharedPreference.getString("jsonExperience", "")
                    if (!json.isNullOrEmpty()) {
                        val objectList = Gson().fromJson(json, Array<Experience>::class.java).asList()
                        exp.update {
                            objectList
                        }
                    }
                    _experience.update {
                        res.data ?: PagingData.empty()
                    }

                }
        }
    }

    val destinationExpForm = MutableStateFlow("profile")
    fun changeDestinationExpForm(title: String) {
        destinationExpForm.update { title }
    }

    val saveExpState = MutableStateFlow("")

    fun saveExperience(experiences: Experience) {
        viewModelScope.launch {
            experiences.idUser = user.value.id
            saveExperienceUseCase.execute(experiences).collect { res ->
                saveExpState.update { res.data?.message?: "" }
                if (res.data?.message == "saved successfully") getAllExperience(user.value.id ?: 0)
            }
        }
    }

    val removeExpState = MutableStateFlow("")

    fun removeExperience(experienceId: Int) {
        viewModelScope.launch {
            val pair = Pair(user.value.id ?: -1, experienceId)
            removeExperienceUseCase.execute(pair).collect { res ->
                removeExpState.update { res.data?.message?: "" }
                if (res.data?.message == "removed successfully") getAllExperience(user.value.id ?: 0)
            }
        }
    }

    fun getUserById() {
        getUser(lang, sharedPreference.getInt("idUser", 0))
    }

    init {
        lang = sharedPreference.getString("lang", "") ?: ""
        langState.update { lang }

        //getInitialEducationDetail()
        getInitialExperienceDetail()

        getUser(lang, sharedPreference.getInt("idUser", 0))

        CoroutineScope(Dispatchers.Default).launch {
            getAllExperience(user.value.id ?: 0)
        }

        CoroutineScope(Dispatchers.Default).launch {
            getAllEducations(user.value.id ?: 0)
        }
    }

}