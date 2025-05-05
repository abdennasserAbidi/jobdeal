package com.example.myjob.feature.home.filter

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.feature.home.HomeViewModel
import com.example.myjob.feature.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    homeViewModel: SearchViewModel = hiltViewModel()
) {

    val interactionSource = remember { MutableInteractionSource() }

    val query by homeViewModel.query.collectAsState()
    val user by homeViewModel.words.collectAsState()

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
                    imageVector = Icons.Default.Close,
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
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.93f)
                    .fillMaxHeight(0.85f)
                    .padding(top = 20.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                shape = RectangleShape,
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
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    tint = colorResource(id = R.color.whatsapp),
                                    contentDescription = ""
                                )
                            },
                            onValueChange = { homeViewModel.updateQuery(it) }, // Met à jour la requête
                            label = { Text("Rechercher un mot") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        items(user) { user ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .padding(horizontal = 10.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        GlobalEntries.userForCompany = user
                                        navController.navigate(Screen.DetailScreen.route)
                                    },
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(id = R.color.lighter_gray)
                                ),
                                elevation = CardDefaults.cardElevation(5.dp)
                            ) {
                                val experiences = user.experience

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                ) {

                                    val gender =
                                        if (user.sexe == "Homme" || user.sexe == "Male") R.drawable.malecandidate
                                        else R.drawable.femalecandidate

                                    Image(
                                        painter = painterResource(id = gender),
                                        modifier = Modifier
                                            .size(30.dp)
                                            .align(Alignment.CenterStart),
                                        contentDescription = ""
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                            .padding(horizontal = 10.dp)
                                            .align(Alignment.CenterStart),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {



                                        Text(
                                            text = user.fullName ?: "",
                                            modifier = Modifier
                                                .padding(start = 15.dp)
                                        )

                                    }

                                }

                                /*Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                ) {


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
                                }*/

                            }

                            Spacer(modifier = Modifier.height(14.dp))

                        }

                    }


                }


            }

        }

    }


}