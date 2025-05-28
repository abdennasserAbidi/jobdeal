package com.example.myjob.feature.home.filter

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.SearchHistory
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    homeViewModel: SearchViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }

    val removeHistoryState by homeViewModel.removeHistoryState.collectAsState()

    val query by homeViewModel.query.collectAsState()

    val user: LazyPagingItems<SearchHistory> =
        homeViewModel.words.collectAsLazyPagingItems()

    var isSearching by remember { mutableStateOf(false) }

    //always false
    val lifecycle = rememberLifecycleEvent()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_RESUME) {
            isSearching = false
            homeViewModel.getAllSearch()
        }

    }

    val searchHistory: LazyPagingItems<SearchHistory> =
        homeViewModel.searchHistories.collectAsLazyPagingItems()

    Log.i("searchHistory", "SearchScreen: ${searchHistory.itemSnapshotList.items}")


    val searchs = if (isSearching) user else searchHistory

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

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.popBackStack()
                        },
                    tint = Color.White,
                    contentDescription = ""
                )

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
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.93f)
                    .fillMaxHeight(0.9f)
                    .padding(top = 50.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(0.8f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                            },
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(id = R.color.lighter_gray)
                        )
                    ) {
                        TextField(
                            value = query,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = colorResource(id = R.color.lighter_gray),
                                focusedLabelColor = colorResource(id = R.color.whatsapp),
                                disabledTextColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            trailingIcon = {

                                Row {

                                    VerticalDivider(thickness = 1.dp, modifier = Modifier.height(30.dp))

                                    Icon(
                                        painter = painterResource(id = R.drawable.filter),
                                        tint = colorResource(id = R.color.whatsapp),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(start = 10.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                navController.navigate(Screen.FilterScreen.route)
                                            }
                                    )
                                }
                            },
                            onValueChange = {
                                isSearching = it.isNotEmpty()
                                homeViewModel.updateQuery(it)
                            },
                            label = { Text("Rechercher un mot") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(top = 20.dp)
                    ) {

                        val text = if (!isSearching) "History"
                        else "${user.itemCount} Results"

                        val textMeasurer = rememberTextMeasurer()
                        val measuredText = textMeasurer.measure(AnnotatedString(text))
                        val textWidth =
                            with(LocalDensity.current) { measuredText.size.width.toDp() }

                        Text(
                            text = text,
                            color = colorResource(id = R.color.whatsapp),
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.rubikbold,
                                        weight = FontWeight.Bold
                                    )
                                )
                            )
                        )

                        HorizontalDivider(
                            thickness = 4.dp,
                            modifier = Modifier
                                .width(textWidth)
                                .padding(top = 5.dp),
                            color = colorResource(id = R.color.whatsapp)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(top = 20.dp)
                    ) {

                        items(searchs.itemCount) { index ->
                            val userHistory = searchs[index] ?: SearchHistory()


                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                                    .padding(top = 10.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {

                                        homeViewModel.addToSearchHistory(userHistory)
                                        val userToDetail = User()
                                        userToDetail.fullName = userHistory.fullName
                                        userToDetail.id = userHistory.idUser

                                        GlobalEntries.userForCompany = userToDetail
                                        navController.navigate(Screen.DetailScreen.route)
                                    }
                            ) {
                                val experiences = userHistory.experience

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    val gender =
                                        if (userHistory.gender == "Homme" || userHistory.gender == "Male") R.drawable.menavatar
                                        else R.drawable.femaleavatar

                                    val color =
                                        if (userHistory.gender == "Homme" || userHistory.gender == "Male") Color.Cyan
                                        else Color(0xFFFF8C00)

                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = color,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = gender),
                                            modifier = Modifier
                                                .size(70.dp)
                                                .padding(10.dp),
                                            contentDescription = ""
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(0.4f)
                                            .padding(start = 20.dp)
                                            .padding(horizontal = 10.dp)
                                    ) {

                                        Text(
                                            text = userHistory.fullName ?: "",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                        )

                                        Text(
                                            text = userHistory.experience ?: "",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp
                                            ),
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(top = 5.dp)
                                        )

                                    }

                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(start = 20.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                //delete user from history
                                                userHistory.idUser?.let {
                                                    homeViewModel.removeSearchHistory(it)
                                                }

                                            },
                                        contentDescription = ""
                                    )

                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                        }

                        searchs.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                                }

                                loadState.refresh is LoadState.Error -> {
                                    val error = searchs.loadState.refresh as LoadState.Error
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
                                    val error = searchs.loadState.append as LoadState.Error
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

        }

    }


}