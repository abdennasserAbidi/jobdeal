package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.GlobalEntries.matchInvitation
import com.example.myjob.common.ImageCarousel
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.navigation.Screen
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalSwipeableCardApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeCompany(
    navController: NavController,
    onResumed: (index: Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    var visibleUser by remember { mutableStateOf(User()) }
    var selected by remember { mutableStateOf(0) }
    var openFormInvitation by remember { mutableStateOf(false) }
    var us by remember { mutableStateOf<List<Pair<User, SwipeableCardState>>>(emptyList()) }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var search by remember { mutableStateOf(false) }

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
            //onVisible(true)
        }
    }

    LaunchedEffect(openFormInvitation) {
        GlobalEntries.isVisibleNav.update { !openFormInvitation }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .background(
                    color = colorResource(id = R.color.whatsapp),
                    shape = shape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                elevation = 0.dp
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
                                //search = true
                                navController.navigate(Screen.SearchWordScreen.route)
                            },
                        imageVector = Icons.Filled.Search,
                        tint = Color.White,
                        contentDescription = ""
                    )

                    Spacer(modifier = Modifier.width(50.dp))

                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Find your candidate",
                        color = Color.White,
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

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Screen.FilterScreen.route)
                            }
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(10.dp),
                            imageVector = Icons.Filled.Menu,
                            contentDescription = ""
                        )

                    }
                }
            }

            if (users.isNotEmpty()) {
                Log.i("visibleUser", "usersProfiles: ${users[0]}")

                val userState = users.reversed().map { it to rememberSwipeableCardState() }
                visibleUser = users[0]
                Log.i("visibleUser", "HomeCompany: ${userState.size}")

                us = userState

                /*ImageCarousel(
                    users,
                    modifier = Modifier
                        .padding(top = 20.dp)
                )*/

                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                ) {

                    userState.forEach { (user, state) ->

                        homeViewModel.getResume(user)
                        if (state.swipedDirection == null) {
                            ProfileCard(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .fillMaxHeight(0.85f)
                                    .align(Alignment.TopCenter)
                                    /*.swipableCard(
                                        state = state,
                                        blockedDirections = listOf(Direction.Down),
                                        onSwiped = {
                                            homeViewModel.removeFromGlobal(user.id ?: 0)
                                            if (it == Direction.Left)
                                                homeViewModel.skipCurrentProfile(user)
                                            else homeViewModel.matchCurrentProfile(
                                                visibleUser.id ?: 0
                                            )
                                        },
                                        onSwipeCancel = {
                                            Log.d("Swipeable-Card", "Cancelled swipe")
                                            //hint = "You canceled the swipe"
                                        }
                                    )*/
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
                                matchProfile = user,
                                onSave = {
                                    homeViewModel.saveToFavorites(
                                        GlobalEntries.user.id ?: -1,
                                        visibleUser.id ?: 0
                                    )
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
                                    homeViewModel.skipCurrentProfile(visibleUser)

                                },
                                onMatch = {
                                    scope.launch {
                                        val last = us
                                            .reversed()
                                            .firstOrNull {
                                                it.second.offset.value == Offset(0f, 0f)
                                            }?.second

                                        last?.swipe(Direction.Right)
                                    }

                                    homeViewModel.updateCurrentPage()
                                    homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                                    homeViewModel.matchCurrentProfile(visibleUser.id ?: 0)

                                    matchInvitation = {


                                        Log.i("fnelkajgjrkzl", "HomeCompany: ${visibleUser.id}")

                                        homeViewModel.updateCurrentPage()
                                        homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                                        homeViewModel.matchCurrentProfile(visibleUser.id ?: 0)
                                    }
                                    //navController.navigate(Screen.SendInvitationScreen.route)
                                    openFormInvitation = true

                                }
                            )
                        }
                        LaunchedEffect(user, state.swipedDirection) {
                            if (state.swipedDirection != null) {
                                //hint = "You swiped ${stringFrom(state.swipedDirection!!)}"
                            }
                        }
                    }


                }

            } else Text("No more profiles!")

        }


        AnimatedVisibility(
            visible = openFormInvitation,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(top = 20.dp)
                ) {

                    Icon(
                        imageVector = Icons.Filled.Close,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                               openFormInvitation = false
                            },
                        contentDescription = ""
                    )

                    Text(
                        text = "Send Invitation",
                        modifier = Modifier.align(Alignment.Center),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val invitationParam by homeViewModel.invitationParam.collectAsState()
                    var postName by remember { mutableStateOf("Dveloppeur Android") }

                    Text(
                        text = "Post name",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = postName,
                        onValueChange = {
                            postName = it
                            homeViewModel.changePostName(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var descriptions by remember { mutableStateOf("Creer une application pour connecter les entreprises avec les candidats facilement.") }

                    Text(
                        text = "Description",
                        modifier = Modifier.padding(top = 10.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = descriptions,
                        onValueChange = {
                            descriptions = it
                            homeViewModel.changeDescriptions(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var type by remember { mutableStateOf("CDI") }

                    Text(
                        text = "Type de contrat",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = type,
                        onValueChange = {
                            type = it
                            homeViewModel.changeTypeContract(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var disponibility by remember { mutableStateOf("Immidiat") }

                    Text(
                        text = "Disponibilité",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = disponibility,
                        onValueChange = {
                            disponibility = it
                            homeViewModel.changeDisponibility(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )

                    var salary by remember { mutableStateOf("1000") }

                    Text(
                        text = "TGM",
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        style = TextStyle(
                            color = colorResource(id = R.color.whatsapp),
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(shape = RoundedCornerShape(30.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        value = salary,
                        onValueChange = {
                            salary = it
                            homeViewModel.changeSalary(it)
                        },
                        textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Box(
                        modifier = Modifier
                            .weight(0.45f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                scope.launch {
                                    val last = us
                                        .reversed()
                                        .firstOrNull {
                                            it.second.offset.value == Offset(0f, 0f)
                                        }?.second

                                    last?.swipe(Direction.Right)
                                }

                                homeViewModel.updateCurrentPage()
                                homeViewModel.removeFromGlobal(visibleUser.id ?: 0)
                                homeViewModel.matchCurrentProfile(visibleUser.id ?: 0)
                                openFormInvitation = false

                            }
                            .background(
                                color = colorResource(id = R.color.whatsapp),
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Send",
                            modifier = Modifier.padding(
                                vertical = 20.dp,
                                horizontal = 20.dp
                            ),
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }


                    Box(
                        modifier = Modifier
                            .weight(0.45f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                openFormInvitation = false
                            }
                            .background(
                                color = Color.Gray,
                                shape = RoundedCornerShape(30.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            modifier = Modifier.padding(
                                vertical = 20.dp,
                                horizontal = 20.dp
                            ),
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = FontFamily.Default,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        /*val query by homeViewModel.query.collectAsState()
        val user by homeViewModel.words.collectAsState()

        AnimatedVisibility(
            visible = search,
            enter = slideInVertically(
                initialOffsetY = { it }, // Slide from below the screen
                animationSpec = tween(durationMillis = 600) // Set animation duration
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out upwards
                animationSpec = tween(durationMillis = 600) // Set animation duration
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                search = false
                            },
                        contentDescription = ""
                    )

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .fillMaxWidth(0.8f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                            },
                        elevation = 5.dp
                    ) {
                        TextField(
                            value = query,
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            onValueChange = { homeViewModel.updateQuery(it) }, // Met à jour la requête
                            label = { Text("Rechercher un mot") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    items(items = user) { user ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    GlobalEntries.userForCompany = user
                                    navController.navigate(Screen.DetailScreen.route)
                                },
                            shape = RectangleShape,
                            elevation = 5.dp
                        ) {
                            val experiences = user.experience
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                            ) {

                                Text(
                                    text = user.fullName ?: "",
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .padding(horizontal = 10.dp)
                                )
                                Text(
                                    text = user.availability ?: "",
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .padding(horizontal = 10.dp)
                                )
                                Text(
                                    text = user.email ?: "",
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .padding(horizontal = 10.dp)
                                )
                                if (experiences?.isNotEmpty() == true) {
                                    val nameCompany =
                                        experiences[experiences.lastIndex].companyName
                                    Text(
                                        text = nameCompany ?: "",
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp)
                                    )
                                }
                            }

                        }

                        Spacer(modifier = Modifier.height(14.dp))

                    }

                }
            }
        }*/


    }


}