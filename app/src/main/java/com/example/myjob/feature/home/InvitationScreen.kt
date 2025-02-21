package com.example.myjob.feature.home

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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.ErrorMessage
import com.example.myjob.common.LoadingNextPageItem
import com.example.myjob.common.PageLoader
import com.example.myjob.domain.entities.InvitationModel

@Composable
fun InvitationScreen(
    navController: NavController,
    invitationViewModel: InvitationViewModel = hiltViewModel()
) {

    val invitations = invitationViewModel.invitations.collectAsLazyPagingItems()
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 85.dp)
        ) {

            items(invitations.itemCount) { index ->
                val item = invitations[index] ?: InvitationModel()

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
                                    text = item.message,
                                    modifier = Modifier.weight(0.7f),
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp)
                            )
                            {

                                Text(
                                    modifier = Modifier.padding(top = 5.dp),
                                    text = item.description,
                                    color = Color.Black
                                )

                                val name = item.companyName
                                val newName =
                                    if (name.contains('(')) name.split('(')[1].dropLast(1)
                                    else name

                                Text(
                                    modifier = Modifier.padding(top = 5.dp),
                                    text = "$newName ",
                                    color = Color.Black
                                )

                                Text(
                                    text = item.typeContract,
                                    modifier = Modifier.padding(top = 5.dp),
                                    color = Color.Gray
                                )

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
                        Text(
                            text = stringResource(id = R.string.update_text),
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                            },
                            color = colorResource(id = R.color.whatsapp)
                        )

                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .width(90.dp)
                                .padding(start = 20.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {

                                }
                                .background(Color.Transparent, RoundedCornerShape(5.dp)),
                            contentAlignment = Alignment.Center
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .alpha(0.1f)
                                    .background(Color.Red, RoundedCornerShape(5.dp))
                            )

                            Text(
                                text = stringResource(id = R.string.delete_text),
                                color = colorResource(id = R.color.dark_red)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
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

        val shapeInit =  RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = colorResource(id = R.color.whatsapp)),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.popBackStack()
                        },
                    contentDescription = ""
                )

                Text(
                    text = "My Invitations",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }

}