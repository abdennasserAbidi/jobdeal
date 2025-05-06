package com.example.myjob.feature.home

import android.util.Log
import androidx.camera.video.VideoRecordEvent.Start
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.TopInwardCurveShape
import com.example.myjob.domain.entities.User

@Composable
fun ActionButtons(
    onSave: () -> Unit,
    onSkip: () -> Unit,
    onMatch: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //skip
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onSkip()
                    }
                    .background(
                        color = colorResource(id = R.color.whatsapp),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                    contentDescription = "",
                    tint = Color.White
                )
            }

            Text(
                text = "Skip",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //save
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onSave()
                    }
                    .background(
                        color = colorResource(id = R.color.whatsapp),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Filled.Favorite,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                    contentDescription = "",
                    tint = Color.White
                )
            }

            Text(
                text = "Save",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //connect
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onMatch()
                    }
                    .background(
                        color = colorResource(id = R.color.whatsapp),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Filled.Home,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                    contentDescription = "",
                    tint = Color.White
                )
            }

            Text(
                text = "Connect",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun AcceptRejectButtons(
    onSave: () -> Unit,
    onSkip: () -> Unit,
    onMatch: () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    androidx.compose.material3.Card(
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterStart)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onSkip()
                    },
                elevation = 3.dp
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Reject",
                        tint = Color.Red
                    )
                }

            }

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onSave()
                    },
                elevation = 3.dp
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Accept",
                        tint = Color.Green
                    )
                }

            }

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onMatch()
                    },
                elevation = 3.dp
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accept",
                        tint = Color.Green
                    )
                }

            }
        }
    }
}

@Composable
fun JobSwipeCard(
    profile: User,
    lang: String,
    openProfile: (profile: User) -> Unit,
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onSkip: () -> Unit,
    onMatch: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
    ) {

        Card(
            backgroundColor = Color.White,
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
            elevation = 3.dp
        ) {

            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        /*val gender =
                            if (profile.sexe == "Homme" || profile.sexe == "Male") R.drawable.malecandidate
                            else R.drawable.femalecandidate*/


                        val gender =
                            if (profile.sexe == "Homme" || profile.sexe == "Male") R.drawable.menavatar
                            else R.drawable.femaleavatar

                        val color =
                            if (profile.sexe == "Homme" || profile.sexe == "Male") Cyan
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

                        /*Image(
                            painter = painterResource(id = gender),
                            modifier = Modifier.weight(0.1f),
                            contentDescription = ""
                        )*/

                        Text(
                            text = profile.fullName ?: "",
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(start = 5.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        var isFavorite by remember { mutableStateOf(false) }
                        val icn =
                            if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder

                        Icon(
                            imageVector = icn,
                            contentDescription = "",
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                isFavorite = !isFavorite
                                onSave()
                            }
                        )

                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorResource(id = R.color.lighter_gray),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 10.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Disponibility : ",
                            modifier = Modifier.weight(0.7f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = profile.availability ?: "Now",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorResource(id = R.color.lighter_gray),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 15.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Skills : ",
                            modifier = Modifier.weight(0.7f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = profile.preferredActivitySector ?: "Now",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorResource(id = R.color.lighter_gray),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 15.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                            .padding(top = 15.dp)
                    ) {
                        Text(
                            text = "4 years experiences : ",
                            modifier = Modifier.weight(0.7f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "View more",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    Column {
                        val list = profile.experience ?: mutableListOf()
                        if (list.isNotEmpty() && list.size > 1) {

                            val exp1 = list[list.lastIndex]
                            val exp2 = list[list.lastIndex - 1]

                            Text(
                                text =  "${exp1.title} at ${exp1.companyName}",
                                modifier = Modifier.padding(top = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            val dateEnd = exp1.dateEnd ?: ""
                            val dateTo = if (dateEnd.isNotEmpty()) {
                                dateEnd.split(", ")[2]
                            } else "Present"

                            val dateFrom = exp1.dateStart ?: ""
                            val dateStart = if (dateFrom.isNotEmpty()) dateFrom.split(", ")[2]
                            else ""

                            Text(
                                text =  "$dateStart - $dateTo",
                                modifier = Modifier.padding(top = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )

                            Text(
                                text =  "${exp2.title} at ${exp2.companyName}",
                                modifier = Modifier.padding(top = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            val dateEnd2 = exp2.dateEnd ?: ""
                            val dateTo2 = if (dateEnd2.isNotEmpty()) {
                                dateEnd2.split(", ")[2]
                            } else "Present"

                            val dateFrom1 = exp2.dateStart ?: ""
                            val dateStart1 = if (dateFrom1.isNotEmpty()) dateFrom1.split(", ")[2]
                            else ""

                            Text(
                                text =  "$dateStart1 - $dateTo2",
                                modifier = Modifier.padding(top = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        } else if (list.size == 1) {
                            val exp1 = list[list.lastIndex]

                            Text(
                                text =  "${exp1.title} at ${exp1.companyName}",
                                modifier = Modifier.padding(top = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            val dateEnd = exp1.dateEnd ?: ""
                            val dateTo = if (dateEnd.isNotEmpty()) {
                                dateEnd.split(", ")[2]
                            } else "Present"

                            val dateFrom = exp1.dateStart ?: ""
                            val dateStart = if (dateFrom.isNotEmpty()) dateFrom.split(", ")[2]
                            else ""

                            Text(
                                text =  "$dateStart - $dateTo",
                                modifier = Modifier.padding(top = 8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorResource(id = R.color.lighter_gray),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 15.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Range salary : ",
                            modifier = Modifier.weight(0.7f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = profile.rangeSalary ?: "",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }


                /*Image(
                    painter = painterResource(id = R.drawable.malecandidate),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.2f)
                )*/
                /*Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val pageCount = 3
                    val pagerState = rememberPagerState(pageCount = { pageCount })

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.85f)
                    ) { pageIndex ->

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {

                            when (pageIndex) {
                                0 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        Text(
                                            text = "Resume",
                                            style = MaterialTheme.typography.h6
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 10.dp, top = 20.dp),
                                            text = profile.resumeUser(),
                                            style = MaterialTheme.typography.body2
                                        )
                                    }
                                }

                                1 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        Text(
                                            text = stringResource(id = R.string.experience_text),
                                            style = MaterialTheme.typography.h6
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Column(
                                            Modifier
                                                .padding(top = 10.dp)
                                                .padding(horizontal = 10.dp)
                                        ) {
                                            val list = profile.experience?.take(2)
                                            list?.mapIndexed { index, experience ->

                                                experience.title?.let { title ->
                                                    if (title.isNotEmpty()) {
                                                        Text(
                                                            text = title,
                                                            modifier = Modifier.padding(top = 10.dp),
                                                            style = MaterialTheme.typography.h6
                                                        )
                                                    }
                                                }

                                                Row(modifier = Modifier.padding(top = 5.dp)) {

                                                    experience.companyName?.let { name ->
                                                        if (name.isNotEmpty()) {
                                                            Text(text = name)
                                                        }
                                                    }

                                                    experience.typeContract?.let { type ->
                                                        if (type.isNotEmpty()) {
                                                            Text(text = " - $type")
                                                        }
                                                    }
                                                }

                                                Row(modifier = Modifier.padding(top = 5.dp)) {
                                                    Text(
                                                        text = experience.dateStart ?: "",
                                                    )

                                                    Text(
                                                        text = experience.dateEnd ?: "",
                                                    )
                                                }

                                                Text(
                                                    text = experience.place ?: "",
                                                    modifier = Modifier.padding(top = 5.dp)
                                                )

                                                Spacer(modifier = Modifier.height(10.dp))

                                                HorizontalDivider(
                                                    thickness = 1.dp,
                                                    modifier = Modifier.fillMaxWidth()
                                                )

                                                if (index == list.lastIndex) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(top = 15.dp)
                                                            .clickable(
                                                                interactionSource = interactionSource,
                                                                indication = null
                                                            ) {

                                                            }
                                                    ) {
                                                        Text(
                                                            text = "Show more",
                                                            fontSize = 16.sp,
                                                            modifier = Modifier
                                                                .align(Alignment.CenterStart)

                                                        )

                                                        Icon(
                                                            imageVector = Icons.Default.ArrowForwardIos,
                                                            contentDescription = "",
                                                            modifier = Modifier
                                                                .align(Alignment.CenterEnd)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                2 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        Text(
                                            text = stringResource(id = R.string.education_text),
                                            style = MaterialTheme.typography.h6
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Column(
                                            Modifier
                                                .padding(top = 10.dp)
                                                .padding(horizontal = 10.dp)
                                        ) {
                                            val list = profile.education?.take(2)
                                            list?.mapIndexed { index, educations ->

                                                educations.schoolName?.let { title ->
                                                    if (title.isNotEmpty()) {
                                                        Text(
                                                            text = title,
                                                            modifier = Modifier.padding(top = 10.dp),
                                                            style = MaterialTheme.typography.h6
                                                        )
                                                    }
                                                }

                                                Row(modifier = Modifier.padding(top = 5.dp)) {

                                                    educations.fieldStudy?.let { name ->
                                                        if (name.isNotEmpty()) {
                                                            Text(text = name)
                                                        }
                                                    }

                                                    educations.degree?.let { type ->
                                                        if (type.isNotEmpty()) {
                                                            Text(text = " - $type")
                                                        }
                                                    }
                                                }

                                                Row(modifier = Modifier.padding(top = 5.dp)) {
                                                    Text(
                                                        text = educations.dateStart ?: "",
                                                    )

                                                    Text(
                                                        text = educations.dateEnd ?: "",
                                                    )
                                                }

                                                Text(
                                                    text = educations.place ?: "",
                                                    modifier = Modifier.padding(top = 5.dp)
                                                )

                                                Spacer(modifier = Modifier.height(10.dp))

                                                HorizontalDivider(
                                                    thickness = 1.dp,
                                                    modifier = Modifier.fillMaxWidth()
                                                )

                                                if (index == list.lastIndex) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(top = 15.dp)
                                                            .clickable(
                                                                interactionSource = interactionSource,
                                                                indication = null
                                                            ) {

                                                            }
                                                    ) {
                                                        Text(
                                                            text = "Show more",
                                                            fontSize = 16.sp,
                                                            modifier = Modifier
                                                                .align(Alignment.CenterStart)

                                                        )

                                                        Icon(
                                                            imageVector = Icons.Default.ArrowForwardIos,
                                                            contentDescription = "",
                                                            modifier = Modifier
                                                                .align(Alignment.CenterEnd)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                    Row(
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .weight(0.15f)
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration)
                                    colorResource(id = R.color.whatsapp) else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(color, CircleShape)
                                    .size(8.dp)
                            )
                        }
                    }




                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = profile.fullName ?: "",
                            modifier = Modifier.padding(start = 10.dp),
                            style = MaterialTheme.typography.h6
                        )

                        Text(
                            text = "${profile.activitySector ?: ""} - ${profile.address ?: ""}",
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Gray
                        )
                    }
                }*/
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorResource(id = R.color.lighter_gray),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.lighter_gray),
                                RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                            ),
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(4.5f)
                                .padding(start = 10.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    onSkip()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = "Skipp",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "",
                                tint = Color.Red,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }

                        VerticalDivider(
                            thickness = 1.dp,
                            color = colorResource(id = R.color.lighter_gray),
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(4.5f)
                                .padding(end = 10.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    onMatch()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = "Send invitation",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.addcandidate),
                                contentDescription = "",
                                tint = colorResource(id = R.color.whatsapp),
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 10.dp)
                            )
                        }

                    }
                }


            }

        }

        /*Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(70.dp),
                elevation = 15.dp
            ) {

                Box(modifier = Modifier.matchParentSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = profile.fullName ?: "",
                            modifier = Modifier.padding(start = 10.dp),
                            style = MaterialTheme.typography.h6
                        )

                        Text(
                            text = "${profile.activitySector ?: ""} - ${profile.address ?: ""}",
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }


            }
        }*/

    }
}