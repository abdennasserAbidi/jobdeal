package com.example.myjob.feature.home.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.home.GetAllUserUseCase
import com.example.myjob.domain.usecase.search.SearchUserUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class FilteredHomeViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

    val filteredUser = MutableStateFlow(emptyList<User>())

    private val _filteringUsers: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(value = PagingData.empty())
    val filteringUsers: MutableStateFlow<PagingData<User>> get() = _filteringUsers


    fun validateFilter(criteria: CriteriaModel) {
        viewModelScope.launch {
            if (!criteria.checkEmpty()) {
                val id = sharedPreference.getInt("idUser", -1)

                getAllUserUseCase.execute(id).collect { res ->
                    val json = sharedPreference.getString("jsonFilter", "") ?: ""
                    if (json.isNotEmpty()) {
                        val objectList = Gson().fromJson(json, Array<User>::class.java).asList()

                        filteredUser.update { objectList }
                    }

                    _filteringUsers.update {
                        res.data ?: PagingData.empty()
                    }
                }
            } else {
                searchUserUseCase.execute(criteria).collect { res ->
                    val json = sharedPreference.getString("jsonFilter", "") ?: ""
                    if (json.isNotEmpty()) {
                        val objectList = Gson().fromJson(json, Array<User>::class.java).asList()

                        filteredUser.update { objectList }
                    }

                    _filteringUsers.update {
                        res.data ?: PagingData.empty()
                    }
                }
            }
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


}