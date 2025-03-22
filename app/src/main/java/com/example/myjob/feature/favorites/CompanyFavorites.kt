package com.example.myjob.feature.favorites

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.domain.entities.User

@Composable
fun CompanyFavorites(
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }

    val favorites: LazyPagingItems<User> =
        favoritesViewModel.favorites.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = colorResource(id = R.color.whatsapp), shape = shape)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 75.dp),
                    elevation = 5.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 15.dp)
                    ) {

                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterStart)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    navController.popBackStack()
                                },
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.width(50.dp))

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Favorites",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.rubik_medium,
                                        weight = FontWeight.Medium
                                    )
                                )
                            )
                        )
                    }
                }
            }

        }

        favorites.itemSnapshotList.map {
            Log.i("ffjlebfjkefbe", "CompanyFavorites: ${it?.email}")

        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(favorites.itemCount) { index ->
                val item = favorites[index] ?: User()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    shape = RectangleShape,
                    elevation = 5.dp
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item.fullName ?: "",
                            Modifier
                                .padding(top = 10.dp)
                                .padding(horizontal = 10.dp)
                        )

                        Text(
                            text = item.email ?: "",
                            Modifier
                                .padding(top = 10.dp)
                                .padding(horizontal = 10.dp)
                        )

                        Text(
                            text = item.preferredActivitySector ?: "",
                            Modifier
                                .padding(top = 10.dp)
                                .padding(horizontal = 10.dp)
                        )
                    }



                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            favorites.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = favorites.loadState.refresh as LoadState.Error
                        item {
                            ErrorMessage(
                                modifier = Modifier.fillParentMaxSize(),
                                message = error.error.localizedMessage ?: "",
                                onClickRetry = { retry() })
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { LoadingNextPageItem(modifier = Modifier) }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = favorites.loadState.append as LoadState.Error
                        /*item {
                            ErrorMessage(
                                modifier = Modifier,
                                message = error.error.localizedMessage!!,
                                onClickRetry = { retry() })
                        }*/
                    }
                }
            }

        }
    }


}