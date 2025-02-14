package com.example.myjob.feature.favorites

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.FavoriteModel

@Composable
fun CompanyFavorites(
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {

    val favorites: LazyPagingItems<FavoriteModel> =
        favoritesViewModel.favorites.collectAsLazyPagingItems()

    Log.i("favorites", "CompanyFavorites: ${favorites.itemSnapshotList}")



}