package com.example.myjob.feature.profile

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.Subject
import com.example.myjob.feature.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllCareer(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }

    val salary by profileViewModel.salary.collectAsState()

    var id by remember { mutableStateOf(0) }

    var sliderPosition by remember { mutableIntStateOf(if (salary == 0) 1000 else salary) }

    val experience: LazyPagingItems<Experience> =
        profileViewModel.experience.collectAsLazyPagingItems()

    val withDetail = experience.itemCount != 0
    val removeExpState by profileViewModel.removeExpState.collectAsState()

    LaunchedEffect(removeExpState) {
        Log.i("eazzaazazazz", "1: $removeExpState")
        Log.i("eazzaazazazz", "2: ${experience.itemCount == 0}")
        if (removeExpState.isNotEmpty() && removeExpState == "removed successfully" && experience.itemCount == 0) {
            Log.i("eazzaazazazz", "AllCareer: ${experience.itemCount}")
            navController.navigate(Screen.ProfileScreen.route)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(White)) {

        Card(
            elevation = 5.dp,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(70.dp)
            )
            {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            val previousScreenName =
                                navController.previousBackStackEntry?.destination?.route
                            if (previousScreenName == Screen.ProfileScreen.route) navController.popBackStack()
                            else navController.navigate(Screen.ProfileScreen.route)
                        },
                        contentDescription = "arrow back"
                    )

                    Text(
                        text = stringResource(R.string.experience_text),
                        modifier = Modifier.padding(start = 20.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Icon(
                    imageVector = Icons.Default.Add,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterEnd)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            GlobalEntries.idExp = View.generateViewId()
                            GlobalEntries.experience = Experience()
                            profileViewModel.changeUpdateOrAdd()
                            profileViewModel.changeDestinationExpForm("list")
                            navController.navigate(Screen.CareerFormScreen.route)
                        },
                    contentDescription = "arrow back"
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {

            items(experience.itemCount) { index ->
                val item = experience[index] ?: Experience()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.lighter_gray),
                            RoundedCornerShape(20.dp)
                        )
                        .background(
                            colorResource(id = R.color.lighter_gray),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {

                    Card(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, top = 20.dp)
                            ) {

                                Text(
                                    text = item.title ?: "",
                                    modifier = Modifier.weight(0.7f),
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                /*Row(
                                    modifier = Modifier
                                        .weight(0.2f)
                                        .padding(start = 10.dp)
                                ) {

                                    Icon(
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                id = item.id
                                                GlobalEntries.idExp = item.id
                                                GlobalEntries.experience = item
                                                sliderPosition = item.salary ?: 1000
                                                profileViewModel.changeUpdateOrAdd(item)
                                                profileViewModel.changeDestinationExpForm("list")
                                                navController.navigate(Screen.CareerFormScreen.route)
                                            },
                                        imageVector = Icons.Filled.Create,
                                        tint = Color.Black,
                                        contentDescription = "update"
                                    )

                                    Icon(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                            ) {
                                                id = item.id
                                                profileViewModel.removeExperience(item.id)
                                                profileViewModel.changeDestinationExpForm("list")
                                            },
                                        imageVector = Icons.Filled.Delete,
                                        tint = Color.Black,
                                        contentDescription = "delete"
                                    )

                                }*/
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp)
                            )
                            {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp)
                                ) {

                                    val name = item.companyName ?: ""
                                    val newName =
                                        if (name.contains('(')) name.split('(')[1].dropLast(1)
                                        else name

                                    Text(
                                        text = "$newName ",
                                        color = Color.Black
                                    )

                                    Text(text = "- ${item.type ?: ""}", color = Color.Black)
                                }

                                Text(
                                    text = item.place ?: "",
                                    modifier = Modifier.padding(top = 5.dp),
                                    color = Gray
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp)
                                ) {
                                    val d1 = item.dateStart ?: ""
                                    val s1 = d1.split(",")[1]
                                    val s2 = d1.split(",")[2]
                                    Text(
                                        text = "$s1 $s2",
                                        color = Gray
                                    )

                                    Text(
                                        text = " to ",
                                        color = Gray
                                    )

                                    val d3 = item.dateEnd ?: ""

                                    if (d3.isNotEmpty()) {
                                        val d2 = if (d3 == "Present") "Present" else {
                                            val e1 = d3.split(",")[1]
                                            val e2 = d3.split(",")[2]
                                            "$e1 $e2"
                                        }

                                        Text(
                                            text = d2,
                                            color = Gray,
                                        )
                                    } else {
                                        Text(
                                            text = "Present",
                                            color = Gray,
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Update",
                            modifier = Modifier.clickable(
                              interactionSource = interactionSource,
                              indication = null
                            ) {
                                id = item.id
                                GlobalEntries.idExp = item.id
                                GlobalEntries.experience = item
                                sliderPosition = item.salary ?: 1000
                                profileViewModel.changeUpdateOrAdd(item)
                                profileViewModel.changeDestinationExpForm("list")
                                navController.navigate(Screen.CareerFormScreen.route)
                            },
                            color = colorResource(id = R.color.whatsapp))

                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .width(70.dp)
                                .padding(start = 20.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    id = item.id
                                    profileViewModel.removeExperience(item.id)
                                    profileViewModel.changeDestinationExpForm("list")
                                }
                                .background(Transparent, RoundedCornerShape(5.dp)),
                            contentAlignment = Alignment.Center
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .alpha(0.1f)
                                    .background(Red, RoundedCornerShape(5.dp))
                            )

                            Text(
                                text = "Delete",
                                color = colorResource(id = R.color.dark_red)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            experience.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val error = experience.loadState.refresh as LoadState.Error
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
                        val error = experience.loadState.append as LoadState.Error
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
}