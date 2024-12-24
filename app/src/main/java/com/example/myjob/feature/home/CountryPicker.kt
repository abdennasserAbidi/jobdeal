package com.example.myjob.feature.home

import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.phonekit.getFlagResource
import com.example.myjob.domain.entities.NewCountry

@Composable
fun CountryPicker(
    list: List<NewCountry>,
    onClick: (newCountry: NewCountry) -> Unit,
    onBack: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = 10.dp)
        ) {

            Text(
                text = "Country Code", modifier = Modifier
                    .weight(0.85f)
            )
            Icon(
                imageVector = Icons.Default.Close,
                modifier = Modifier
                    .weight(0.15f)
                    .size(22.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onBack()
                    },
                contentDescription = ""
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(4.dp),
            elevation = 5.dp
        ) {

            SearchView {

            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            itemsIndexed(
                items = list,
                key = { i, _ ->
                    View.generateViewId()
                }
            ) { index, item ->
                Log.i("hifeoahegheauhifae", "CountryPicker: ${item.code}")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onClick(item)
                        }
                ) {

                    Row(
                        modifier = Modifier.align(Alignment.CenterStart),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = context.getFlagResource(item.iso2)),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(30.dp)
                        )

                        Text(
                            text = item.name,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Text(text = "${item.code}", modifier = Modifier.align(Alignment.CenterEnd))
                }

            }
        }

    }
}