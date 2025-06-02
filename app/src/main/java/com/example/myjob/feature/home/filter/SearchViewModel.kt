package com.example.myjob.feature.home.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.usecase.search.GetAllSearchUseCase
import com.example.myjob.domain.usecase.search.RemoveSearchHistoryUseCase
import com.example.myjob.domain.usecase.search.SaveSearchUseCase
import com.example.myjob.domain.usecase.search.SearchCandidateUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val searchCandidateUseCase: SearchCandidateUseCase,
    private val saveSearchUseCase: SaveSearchUseCase,
    private val getAllSearchUseCase: GetAllSearchUseCase,
    private val removeSearchHistoryUseCase: RemoveSearchHistoryUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _words: MutableStateFlow<PagingData<SearchHistory>> = MutableStateFlow(value = PagingData.empty())
    val words: MutableStateFlow<PagingData<SearchHistory>> get() = _words

    val allHistories = MutableStateFlow<List<SearchHistory>>(emptyList())

    private val _searchHistories: MutableStateFlow<PagingData<SearchHistory>> =
        MutableStateFlow(value = PagingData.empty())
    val searchHistories: MutableStateFlow<PagingData<SearchHistory>> get() = _searchHistories

    init {
        val idUserConnected = sharedPreference.getInt("idUser", -1)

        viewModelScope.launch {
            _query
                .debounce(300) // Attendre 300ms après la dernière saisie
                .distinctUntilChanged() // Éviter les requêtes inutiles si même texte
                .filter { it.isNotEmpty() } // Ne pas lancer de requête si vide
                .flatMapLatest { searchCandidateUseCase.execute(Pair(it, idUserConnected)) } // Appel API
                .catch { e -> println("Error: ${e.message}") }
                .collect { res ->
                    _words.update {
                        res.data ?: PagingData.empty()
                    }
                }
        }
    }

    fun getAllSearch() {
        val idUserConnected = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            getAllSearchUseCase.execute(idUserConnected).collect { res ->

                val json = sharedPreference.getString("jsonSearch", "") ?: ""
                if (json.isNotEmpty()) {
                    val objectList = Gson().fromJson(json, Array<SearchHistory>::class.java).asList()

                    allHistories.update {
                        objectList
                    }
                }

                _searchHistories.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    fun addToSearchHistory(searchHistory: SearchHistory) {
        val idUserConnected = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            val pair = Pair(idUserConnected, searchHistory)
            saveSearchUseCase.execute(pair).collect { res ->
                when(res.status) {
                    ResourceState.SUCCESS -> {
                        Log.i("responseDataMessage", "matchCurrentProfile: ${res.data?.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    val removeHistoryState = MutableStateFlow("")

    fun removeSearchHistory(idUser: Int) {
        val idUserConnected = sharedPreference.getInt("idUser", -1)
        viewModelScope.launch {
            val pair = Pair(idUserConnected, idUser)
            removeSearchHistoryUseCase.execute(pair).collect { res ->
                removeHistoryState.update { res.data?.message ?: "" }
                if (res.data?.message == "removed successfully") getAllSearch()
            }
        }
    }

    fun updateQuery(newQuery: String) {
        //if (newQuery.isEmpty())
        _query.update { newQuery }
    }
}