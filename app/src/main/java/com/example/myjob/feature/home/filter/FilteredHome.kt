package com.example.myjob.feature.home.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.navigation.Screen

@Composable
fun FilteredHome(
    navController: NavController,
    filterViewModel: FilteredHomeViewModel = hiltViewModel()
) {

    val filteredUser: LazyPagingItems<User> =
        filterViewModel.filteringUsers.collectAsLazyPagingItems()

    val interactionSource = remember { MutableInteractionSource() }

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            filterViewModel.validateFilter(GlobalEntries.criteriaModel)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lighter_gray))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            val green = colorResource(id = R.color.whatsapp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {

                val shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(color = colorResource(id = R.color.whatsapp), shape = shape)
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 10.dp, top = 10.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.popBackStack()
                            },
                        tint = Color.White,
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(end = 10.dp, top = 10.dp),
                        text = "Filter",
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

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
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

                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "${filteredUser.itemCount} Results",
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
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(filteredUser.itemCount) { index ->
                    val user = filteredUser[index] ?: User()

                    Card(
                        elevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        shape = RoundedCornerShape(30.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    GlobalEntries.userForCompany = user
                                    GlobalEntries.isFromDemand = false
                                    navController.navigate(Screen.DetailScreen.route)
                                }
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                val gender =
                                    if (user.sexe == "Homme" || user.sexe == "Male") R.drawable.menavatar
                                    else R.drawable.femaleavatar

                                val color =
                                    if (user.sexe == "Homme" || user.sexe == "Male") Color.Cyan
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
                                        text = user.fullName ?: "",
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    )
                                    val exp = user.experience?.let {
                                        filterViewModel.extractExp(it)
                                    } ?: "new"

                                    Text(
                                        text = exp,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp
                                        ),
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(top = 5.dp)
                                    )

                                }

                            }
                        }

                    }
                }
                filteredUser.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = filteredUser.loadState.refresh as LoadState.Error
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
                            val error = filteredUser.loadState.append as LoadState.Error
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