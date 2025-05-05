package com.example.myjob.feature.home.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.CriteriaModel
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.home.SearchUserUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilteredHomeViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val searchUserUseCase: SearchUserUseCase
): ViewModel() {

    val filteredUser = MutableStateFlow(emptyList<User>())
    fun validateFilter(criteria: CriteriaModel) {
        viewModelScope.launch {
            Log.i("fffffffffffffffffffff", "validateFilter: $criteria")
            searchUserUseCase.execute(criteria).collect { res ->
                when(res.status) {
                    ResourceState.SUCCESS -> {
                        filteredUser.update { res.data ?: emptyList() }
                        Log.i("fffffffffffffffffffff", "treethehethe: ${res.data ?: emptyList()}")

                    }
                    else -> {

                    }
                }
            }
        }
    }


}