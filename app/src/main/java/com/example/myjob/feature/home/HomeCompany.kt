package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
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
    val allUser by homeViewModel.users.collectAsState()
    val updateFavoriteState by homeViewModel.updateFavoriteState.collectAsState()
    val qs by homeViewModel.qs.collectAsState()
    val resume by homeViewModel.resume.collectAsState()
    val lazyPagingItems = homeViewModel.user.collectAsLazyPagingItems()

    lazyPagingItems.itemSnapshotList.items.map {
        Log.i("fkngkrzlglzrz", "HomeCandidate: $it")
    }

    val users = lazyPagingItems.itemSnapshotList.items

    val filteredItems by homeViewModel.filterdUser.collectAsState()

    val scope = rememberCoroutineScope()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            onResumed(0)
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 10.dp)
                        .clickable {
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
        })
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val list = if (qs.isNotEmpty()) filteredItems else users
            if (list.isNotEmpty()) visibleUser = list[0]
            Log.i("zalazlmzlmzalmaz", "HomeCompany: $list")

            if (list.isNotEmpty()) {
                val userState = list.reversed().map { it to rememberSwipeableCardState() }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f),
                    contentAlignment = Alignment.Center
                ) {

                    userState.forEach { (user, state) ->
                        homeViewModel.getResume(user)
                        if (state.swipedDirection == null) {
                            ProfileCard(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .fillMaxHeight(0.7f)
                                    .swipableCard(
                                        state = state,
                                        blockedDirections = listOf(Direction.Down),
                                        onSwiped = {
                                            homeViewModel.removeFromGlobal(user.id ?: 0)
                                            if (it == Direction.Left)
                                                homeViewModel.skipCurrentProfile(users)
                                            else homeViewModel.matchCurrentProfile()
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

                Spacer(modifier = Modifier.height(50.dp))
                ActionButtons(
                    onSave = {
                        homeViewModel.saveToFavorites(visibleUser.id ?: 0)
                    },
                    onSkip = {
                        scope.launch {
                            val last = userState.reversed()
                                .firstOrNull {
                                    it.second.offset.value == Offset(0f, 0f)
                                }?.second
                            last?.swipe(Direction.Left)
                        }

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

                        homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                        homeViewModel.matchCurrentProfile()
                    },
                )

            } else Text("No more profiles!")
        }
    }
}