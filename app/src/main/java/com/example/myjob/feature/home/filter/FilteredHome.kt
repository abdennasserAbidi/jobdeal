package com.example.myjob.feature.home.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.rememberLifecycleEvent

@Composable
fun FilteredHome(
    navController: NavController,
    filterViewModel: FilteredHomeViewModel = hiltViewModel()
) {

    val filteredUser by filterViewModel.filteredUser.collectAsState()

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            filterViewModel.validateFilter(GlobalEntries.criteriaModel)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.lighter_gray))) {

        Column(modifier = Modifier.fillMaxSize()) {
            val green = colorResource(id = R.color.whatsapp)

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(bottomEnd = 70.dp))
                            .background(Color.Gray)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.filtercandidates),
                            contentDescription = null,
                            contentScale = ContentScale.Crop, // Or ContentScale.Fit, depending on your need
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.6f))
                        )

                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.TopEnd)
                                .padding(top = 10.dp, end = 10.dp),
                            contentDescription = ""
                        )
                    }
                }

                items(filteredUser) { profile ->
                    Card(
                        elevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 10.dp),
                        shape = RoundedCornerShape(30.dp)
                    ) {

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                        ) {

                            Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = profile.fullName ?: "",
                                    modifier = Modifier.padding(start = 10.dp),
                                    style = MaterialTheme.typography.h6
                                )

                                Text(
                                    text = profile.preferredActivitySector ?: "",
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }

                            Text(
                                text = profile.address ?: "",
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )

                            Text(
                                text = profile.email ?: "",
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )

                            Text(
                                text = profile.availability ?: "",
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )

                            Text(
                                text = profile.rangeSalary ?: "",
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )

                            Text(
                                text = profile.phone ?: "",
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )
                        }

                    }
                }
            }
            

            
            
            
            

        }

    }


}