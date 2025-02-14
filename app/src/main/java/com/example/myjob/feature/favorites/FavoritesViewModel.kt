package com.example.myjob.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.domain.entities.FavoriteModel
import com.example.myjob.domain.usecase.home.GetAllFavoritesUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    val sharedPreference: SharedPreference,
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase
): ViewModel() {

    val allFavorites = MutableStateFlow<List<FavoriteModel>>(emptyList())

    private val _favorites: MutableStateFlow<PagingData<FavoriteModel>> =
        MutableStateFlow(value = PagingData.empty())
    val favorites: MutableStateFlow<PagingData<FavoriteModel>> get() = _favorites

    private fun getFavorites(idUserConnected: Int) {
        viewModelScope.launch {
            getAllFavoritesUseCase.execute(idUserConnected).collect { res ->

                val json = sharedPreference.getString("jsonFavorites", "") ?: ""
                if (json.isNotEmpty()) {
                    val objectList = Gson().fromJson(json, Array<FavoriteModel>::class.java).asList()

                    allFavorites.update {
                        objectList
                    }
                }

                _favorites.update {
                    res.data ?: PagingData.empty()
                }
            }
        }
    }

    init {
        val id = sharedPreference.getInt("idUser", -1)
        getFavorites(id)
    }
}