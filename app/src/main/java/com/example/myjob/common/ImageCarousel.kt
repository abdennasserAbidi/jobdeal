package com.example.myjob.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.myjob.R
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.home.SwipeableCardState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(
    userState: List<User>,
    modifier: Modifier = Modifier
) {

    val pagerState = rememberPagerState(userState.size)

    Box(
        modifier
            .defaultMinSize(minHeight = 500.dp)
            .fillMaxWidth()
    ) {

        Card(
            elevation = 8.dp,
            modifier = modifier
                .width(250.dp)
                .fillMaxHeight(0.65f)
                .padding(top = 30.dp)
                .align(Alignment.Center)
                .background(Color.White)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {

            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            itemSpacing = 10.dp,
            contentPadding = PaddingValues(horizontal = 90.dp),
            count = userState.size
        ) { page ->

            Log.i("kvzlkvnrej", "index: $page")
            Log.i("kvzlkvnrej", "ImageCarousel: ${userState[page].fullName}")

            val sex = userState[page].sexe ?: ""

            val img = if (sex == "Femme" || sex == "female") R.drawable.femalecandidate
            else R.drawable.malecandidate

            val isCurrent = page == pagerState.currentPage

            val modifierImage = Modifier.size(200.dp)
            if (!isCurrent) modifierImage.blur(15.dp)

            val pageOffset = (
                    (pagerState.currentPage - page) + pagerState
                        .currentPageOffset
                    ).absoluteValue

            val scale = lerp(0.9f, 1f, 1 - pageOffset.coerceIn(0f, 1f))
            val alpha = lerp(0.5f, 1f, 1 - pageOffset.coerceIn(0f, 1f))

            Card(
                elevation = 3.dp,
                shape = RectangleShape,
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer {
                        this.scaleX = scale
                        this.scaleY = scale
                        this.alpha = alpha
                    }
                    .aspectRatio(1f)
                    .background(Color.Red)
            ) {
                Image(
                    painter = painterResource(id = img),
                    modifier = modifierImage,
                    contentDescription = ""
                )
            }


            /*Box(
                modifier = Modifier
                    .size(200.dp)
                    .then(
                        if (!isCurrent) Modifier.blur(15.dp)
                        else Modifier.background(
                            colorResource(id = R.color.whatsapp),
                            shape = RectangleShape
                        )
                    )
                    .background(
                        colorResource(id = R.color.whatsapp),
                        shape = RectangleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AA",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.rubikbold,
                                weight = FontWeight.Bold
                            )
                        )
                    )
                )
            }*/

        }

    }

}