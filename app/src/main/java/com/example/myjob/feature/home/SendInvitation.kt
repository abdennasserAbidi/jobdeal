package com.example.myjob.feature.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendInvitation(
    homeViewModel: HomeViewModel,
    validate: () -> Unit,
    openFormInvitation: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                        openFormInvitation()
                    },
                contentDescription = ""
            )

            Text(
                text = stringResource(id = R.string.send_invitation_text),
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val invitationParam by homeViewModel.invitationParam.collectAsState()
            val listTypeContract by homeViewModel.listTypeContract.collectAsState()
            var selected by remember { mutableStateOf(0) }

            val tabWidth = 140.dp

            val indicatorOffset: Dp by animateDpAsState(
                targetValue = tabWidth * selected,
                animationSpec = tween(easing = LinearEasing), label = "",
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.73f)
                    .padding(top = 10.dp)
                    .height(50.dp)
            ) {

                //indicator
                /*Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .padding(top = 10.dp)
                        .offset(x = indicatorOffset)
                        .clip(shape = RectangleShape)
                        .background(
                            color = colorResource(id = R.color.whatsapp),
                        )
                )*/

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    listTypeContract.mapIndexed { index, text ->
                        val isSelected = index == selected

                        val tabTextColor: Color by animateColorAsState(
                            targetValue = if (isSelected) colorResource(id = R.color.whatsapp)
                            else Color.Black,
                            animationSpec = tween(easing = LinearEasing), label = "",
                        )

                        Column(modifier = Modifier
                            .width(tabWidth)
                            .wrapContentHeight()) {
                            Text(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        selected = index
                                    }
                                    .width(tabWidth)
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 12.dp,
                                    ),
                                text = text,
                                color = tabTextColor,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .padding(top = 10.dp)
                                    .background(
                                        color = Color.Red
                                    )
                            )
                        }
                    }
                }
            }



            var postName by remember { mutableStateOf("Dveloppeur Android") }

            Text(
                text = stringResource(id = R.string.post_title_text),
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
                    homeViewModel.changePostName(it)
                },
                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
            )

            var descriptions by remember { mutableStateOf("Creer une application pour connecter les entreprises avec les candidats facilement.") }

            Text(
                text = stringResource(id = R.string.post_description_text),
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
                colors = TextFieldDefaults.colors(
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

            val avalability = stringResource(id = R.string.disponibility1_text)
            var disponibility by remember { mutableStateOf(avalability) }

            Text(
                text = stringResource(id = R.string.disponibility_text),
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
                    homeViewModel.changeDisponibility(it)
                },
                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
            )

            var salary by remember { mutableStateOf("1000") }
            var tgm by remember { mutableStateOf("1000") }

            val salaryText = stringResource(id = R.string.salary_text)

            val text = if (selected == 0) salaryText else "TGM"
            val payement = if (selected == 0) salary else tgm


            Text(
                text = text,
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
                value = payement,
                onValueChange = {

                    if (selected == 0) {
                        salary = it
                        homeViewModel.changeSalary(it)
                    } else {
                        tgm = it
                        homeViewModel.changeTgm(it)
                    }
                },
                textStyle = TextStyle(Color.Black, fontSize = 14.sp)
            )

            if (selected == 1) {

                var nbDays by remember { mutableStateOf("") }

                Text(
                    text = stringResource(id = R.string.nb_day_text),
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
                    value = nbDays,
                    onValueChange = {
                        nbDays = it
                        homeViewModel.changeNbDays(it)
                    },
                    textStyle = TextStyle(Color.Black, fontSize = 14.sp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(top = 20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                    validate()
                    openFormInvitation()

                }
                .background(
                    color = colorResource(id = R.color.whatsapp),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.send_text),
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