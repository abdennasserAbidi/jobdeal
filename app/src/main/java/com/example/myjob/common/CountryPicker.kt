package com.example.myjob.common

import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.myjob.common.phonekit.getFlagResource
import com.example.myjob.domain.entities.NewCountry
import com.example.myjob.feature.profile.ProfileViewModel

@Composable
fun CountryPicker(
    listFlagLazy: LazyPagingItems<NewCountry>,
    profileViewModel: ProfileViewModel,
    onClick: (newCountry: NewCountry) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var items by remember { mutableStateOf<List<NewCountry>>(emptyList()) }
    var page by remember { mutableStateOf(0) }
    val pageSize = 30
    var isLoading by remember { mutableStateOf(false) }
    var endReached by remember { mutableStateOf(false) }

    LaunchedEffect(page) {

        if (!isLoading && !endReached) {
            isLoading = true
            val newItems = profileViewModel.loadItems(page, pageSize, context)
            if (newItems.isEmpty()) {
                endReached = true
            } else {
                items = items + newItems
            }
            isLoading = false
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = 10.dp)
        ) {

            Text(
                text = "Country Code", modifier = Modifier
                    .weight(0.85f)
            )
            Icon(
                imageVector = Icons.Default.Close,
                modifier = Modifier
                    .weight(0.15f)
                    .size(22.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onBack()
                    },
                contentDescription = ""
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            SearchView {
                //TODO("add feature to seach for country code")
            }

        }

        val list = listFlagLazy.itemSnapshotList.items
        val lazyListState = rememberLazyListState()
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            itemsIndexed(
                items = items,
                key = { i, _ ->
                    View.generateViewId()
                }
            ) { index, item ->
                Log.i("hifeoahegheauhifae", "CountryPicker: ${item.code}")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onClick(item)
                        }
                ) {

                    Row(
                        modifier = Modifier.align(Alignment.CenterStart),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = context.getFlagResource(item.iso2)),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(30.dp)
                        )

                        Text(
                            text = item.name,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Text(text = "${item.code}", modifier = Modifier.align(Alignment.CenterEnd))
                }

            }

            if (isLoading) {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }

            if (!isLoading && !endReached) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


            /*listFlagLazy.apply {
                Log.i("efzfrzfrzgfrz", "CountryPicker: ${loadState.append}")
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { LoadingNextPageItem(modifier = Modifier) }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val e = listFlagLazy.loadState.refresh as LoadState.Error
                        item {
                            Text(
                                text = "Error: ${e.error.localizedMessage}",
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }*/


        }

        LaunchedEffect(Unit) {
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
                    if (visibleItems.isNotEmpty() &&
                        visibleItems.last().index == items.size - 1 &&
                        !isLoading && !endReached
                    ) {
                        page += 1
                    }
                }

        }
    }
}