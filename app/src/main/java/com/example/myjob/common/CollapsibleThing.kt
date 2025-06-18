package com.example.myjob.common

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll

@Composable
fun CollapsibleThing(
    connection: CollapsingAppBarNestedScrollConnection,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    headerContent: @Composable () -> Unit,
    filterBar: @Composable (connection: CollapsingAppBarNestedScrollConnection) -> Unit
) {

    val state = rememberScrollableState { delta -> 0f }

    Surface(
        modifier = modifier.fillMaxSize(), color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(connection)
        ) {
            Column(
                modifier = Modifier.scrollable(orientation = Orientation.Vertical, state = state)
            ) {
                ExpandedHeader(
                    modifier = Modifier,
                    connection = connection,
                    headerContent = {
                        headerContent()
                    },
                    filterBar = {
                        filterBar(it)
                    }
                )
                content()
            }
        }
    }
}
