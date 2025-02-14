package com.example.myjob.feature.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.navigation.Screen
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun HomeCompany(
    navController: NavController,
    onResumed: (index: Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    var visibleUser by remember { mutableStateOf(User()) }
    var selected by remember { mutableStateOf(0) }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val currentPage by homeViewModel.currentPage.collectAsState()
    val allUser by homeViewModel.users.collectAsState()
    val updateFavoriteState by homeViewModel.updateFavoriteState.collectAsState()
    val qs by homeViewModel.qs.collectAsState()
    val resume by homeViewModel.resume.collectAsState()
    val lazyPagingItems = homeViewModel.user.collectAsLazyPagingItems()

    lazyPagingItems.itemSnapshotList.items.map {
        Log.i("fkngkrzlglzrz", "HomeCandidate: $it")
    }

    val users = lazyPagingItems.itemSnapshotList.items
    val usersProfiles by homeViewModel.users.collectAsState()

    val filteredItems by homeViewModel.filterdUser.collectAsState()

    val scope = rememberCoroutineScope()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            onResumed(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
        ) {

            val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = colorResource(id = R.color.whatsapp), shape = shape))

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 75.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigate(Screen.FilterScreen.route)
                        },
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
                                .align(Alignment.CenterStart),
                            imageVector = Icons.Filled.Search,
                            contentDescription = ""
                        )

                        Spacer(modifier = Modifier.width(50.dp))

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Find your candidate",
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

        val list = if (qs.isNotEmpty()) filteredItems else users
        if (list.isNotEmpty()) visibleUser = list[0]

        if (usersProfiles.isNotEmpty()) {
            val userState = usersProfiles.reversed().map { it to rememberSwipeableCardState() }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth(0.6f)
            ) {

                userState.forEach { (user, state) ->
                    homeViewModel.getResume(user)
                    if (state.swipedDirection == null) {
                        ProfileCard(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxWidth(0.6f)
                                .align(Alignment.TopCenter)
                                .swipableCard(
                                    state = state,
                                    blockedDirections = listOf(Direction.Down),
                                    onSwiped = {
                                        homeViewModel.removeFromGlobal(user.id ?: 0)
                                        if (it == Direction.Left)
                                            homeViewModel.skipCurrentProfile(users)
                                        else homeViewModel.matchCurrentProfile(visibleUser.id ?: 0)
                                    },
                                    onSwipeCancel = {
                                        Log.d("Swipeable-Card", "Cancelled swipe")
                                        //hint = "You canceled the swipe"
                                    }
                                )
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            openProfile = {
                                val gson = Gson()
                                val userJson = gson.toJson(user, User::class.java)
                                GlobalEntries.userForCompany = user
                                navController.navigate(Screen.DetailScreen.route)
                                //navController.navigate("${Screen.DetailScreen.route}/$userJson")
                            },
                            lang = resume,
                            matchProfile = user
                        )
                    }
                    LaunchedEffect(user, state.swipedDirection) {
                        if (state.swipedDirection != null) {
                            //hint = "You swiped ${stringFrom(state.swipedDirection!!)}"
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            ActionButtons(
                onSave = {
                    homeViewModel.saveToFavorites(GlobalEntries.user.id?: -1, visibleUser.id ?: 0)
                },
                onSkip = {
                    scope.launch {
                        val last = userState.reversed()
                            .firstOrNull {
                                it.second.offset.value == Offset(0f, 0f)
                            }?.second
                        last?.swipe(Direction.Left)
                    }

                    homeViewModel.updateCurrentPage()

                    homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                    homeViewModel.skipCurrentProfile(users)

                },
                onMatch = {
                    scope.launch {
                        val last = userState.reversed()
                            .firstOrNull {
                                it.second.offset.value == Offset(0f, 0f)
                            }?.second

                        last?.swipe(Direction.Right)
                    }

                    homeViewModel.updateCurrentPage()
                    homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                    homeViewModel.matchCurrentProfile(visibleUser.id ?: 0)
                },
            )

        } else Text("No more profiles!")
    }
}