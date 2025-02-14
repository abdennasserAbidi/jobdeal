package com.example.myjob.feature.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.feature.navigation.Screen

@Composable
fun FilterScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val interactionSource = remember { MutableInteractionSource() }

    Filter(navController, interactionSource, homeViewModel)

}

@Composable
fun FilterPreview(

) {

}

@Composable
fun Filter(
    navController: NavController,
    interactionSource: MutableInteractionSource,
    homeViewModel: HomeViewModel
) {
    Scaffold(
        topBar = {
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
                ) {

                    Text(
                        text = "JobDeal",
                        modifier = Modifier.align(Alignment.CenterStart),
                        style = MaterialTheme.typography.h6,
                        color = colorResource(id = R.color.whatsapp)
                    )

                    Icon(
                        imageVector = Icons.Default.Settings,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Screen.SettingScreen.route)
                            },
                        contentDescription = "settings"
                    )
                }
            }
        }
    ) {
        Log.i("", "Filter: $it")
    }
}


@Composable
@Preview
fun PreviewFilter() {
    FilterPreview()
}