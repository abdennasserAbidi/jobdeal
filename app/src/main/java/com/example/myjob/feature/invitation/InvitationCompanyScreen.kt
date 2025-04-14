package com.example.myjob.feature.invitation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.tablayout.CustomTab
import com.example.myjob.domain.entities.InvitationModel
import com.example.myjob.feature.navigation.Screen

@Composable
fun InvitationCompanyScreen(
    navController: NavController,
    invitationViewModel: InvitationViewModel = hiltViewModel()
) {

    val invitations: LazyPagingItems<InvitationModel> = invitationViewModel.invitations.collectAsLazyPagingItems()
    var selected by remember { mutableStateOf(0) }
    val choiceList by invitationViewModel.choiceList.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()

    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTab(
                    items = choiceList,
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    selectedItemIndex = selected,
                    onClick = {
                        selected = it
                    }
                )
            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
            ) {

                items(invitations.itemCount) { index ->
                    val item = invitations[index] ?: InvitationModel()


                }

                invitations.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = invitations.loadState.refresh as LoadState.Error
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
                            val error = invitations.loadState.append as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier,
                                    message = error.error.localizedMessage!!,
                                    onClickRetry = { retry() })
                            }
                        }
                    }
                }
            }


        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 90.dp),
            onClick = {
                if (selected == 0) navController.navigate(Screen.HomeScreen.route)
            },
            containerColor = colorResource(id = R.color.whatsapp),
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = Color.White,
                contentDescription = "Small floating action button.")
        }

    }

}