package com.example.myjob.feature.home

import androidx.lifecycle.ViewModel
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.Availabilities
import com.example.myjob.domain.entities.CategoryChoices
import com.example.myjob.domain.entities.ContractTypeChoices
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.DEFAULT_TYPE
import com.example.myjob.domain.entities.ExperienceChoices
import com.example.myjob.domain.entities.SexChoices
import com.example.myjob.domain.entities.SituationChoices
import com.example.myjob.domain.entities.Subject
import com.example.myjob.domain.entities.USER_EXP
import com.example.myjob.domain.usecase.home.SearchUserUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

    val itemState = MutableStateFlow(emptyList<String>())
    val criteria = MutableStateFlow(CriteriaModel())

    val experiences = MutableStateFlow(USER_EXP)
    val employmentType = MutableStateFlow(DEFAULT_TYPE)
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

        availability.map {
            if (it.titleString.isNotEmpty() && !listDisponibility.contains(it.titleString)) listDisponibility.add(
                it.titleString
            )
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

        availability.map {
            if (it.titleString.isNotEmpty() && !list.contains(it.titleString))
                list.add(it.titleString)
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

        val list = criteria.value.situation

        availability.map {
            if (it.titleString.isNotEmpty() && !list.contains(it.titleString))
                list.add(it.titleString)
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

        availability.map {
            if (it.titleString.isNotEmpty() && !list.contains(it.titleString))
                list.add(it.titleString)
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

        availability.map {
            if (it.titleString.isNotEmpty() && !list.contains(it.titleString))
                list.add(it.titleString)
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

        availability.map {
            if (it.titleString.isNotEmpty() && !list.contains(it.titleString)) list.add(it.titleString)
        }

        criteria.update {
            it.sex = list
            it
        }

    }

    fun addToFlow(item: String) {
        val list = itemState.value.toMutableList()
        if (!list.contains(item)) list.add(item)
        itemState.update {
            list
        }
    }

    fun removeFromFlow(item: String) {
        val list = itemState.value.toMutableList()
        if (list.contains(item)) list.remove(item)
        itemState.update {
            list
        }
    }

    val listSubject = MutableStateFlow(emptyList<Subject>())

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

    fun validateFilter() {
        GlobalEntries.criteriaModel = criteria.value
    }

    init {

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

}