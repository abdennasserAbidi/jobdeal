package com.example.myjob.feature.home.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.usecase.SaveToFavoriteUseCase
import com.example.myjob.domain.usecase.SendInvitationUseCase
import com.example.myjob.domain.usecase.home.GetAllUserUseCase
import com.example.myjob.domain.usecase.home.SearchCandidateUseCase
import com.example.myjob.local.database.SharedPreference
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
    private val getAllUserUseCase: GetAllUserUseCase,
    private val sendInvitationUseCase: SendInvitationUseCase,
    private val saveToFavoriteUseCase: SaveToFavoriteUseCase,
    private val searchCandidateUseCase: SearchCandidateUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _words = MutableStateFlow<List<User>>(emptyList())
    val words: StateFlow<List<User>> = _words

    init {
        viewModelScope.launch {
            _query
                .debounce(300) // Attendre 300ms après la dernière saisie
                .distinctUntilChanged() // Éviter les requêtes inutiles si même texte
                .filter { it.isNotEmpty() } // Ne pas lancer de requête si vide
                .flatMapLatest { searchCandidateUseCase.execute(it) } // Appel API
                .catch { e -> println("Error: ${e.message}") }
                .collect { _words.value = it.data ?: emptyList() } // Mise à jour de l’UI
        }
    }

    fun updateQuery(newQuery: String) {
        _query.update { newQuery }
    }
}