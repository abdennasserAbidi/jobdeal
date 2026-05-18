package com.example.myjob.feature.invitation.company

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries.invitationModel
import com.example.myjob.common.GlobalEntries.matchInvitation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendInvitationScreen(
    navController: NavController
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Send Invitation",
            modifier = Modifier.padding(top = 20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            //val invitationParam by homeViewModel.invitationParam.collectAsState()
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
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = postName,
                onValueChange = {
                    postName = it
                    invitationModel.message = it
                    //homeViewModel.changePostName(it)
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
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = descriptions,
                onValueChange = {
                    descriptions = it
                    invitationModel.description = it
                    //homeViewModel.changeDescriptions(it)
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
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = type,
                onValueChange = {
                    type = it
                    invitationModel.typeContract = it

                    //homeViewModel.changeTypeContract(it)
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
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = disponibility,
                onValueChange = {
                    disponibility = it
                    invitationModel.disponibility = it

                    //homeViewModel.changeDisponibility(it)
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
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = salary,
                onValueChange = {
                    salary = it
                    /*invitationModel.sa = it

                    homeViewModel.changeSalary(it)*/
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
                        matchInvitation()
                        navController.popBackStack()
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
                        navController.popBackStack()
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