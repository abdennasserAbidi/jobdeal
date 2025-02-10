package com.example.myjob.feature.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.feature.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompanyProfile(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        val color = colorResource(id = R.color.whatsapp)
        val shape = RoundedCornerShape(20.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .animateContentSize()
                .height(if (expanded) 400.dp else 200.dp)
                .background(color, shape)
        ){




        }

        Card(
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(top = 75.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    //navController.navigate(Screen.ProfileScreen.route)
                    expanded = !expanded
                },
            elevation = 5.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Validate",
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