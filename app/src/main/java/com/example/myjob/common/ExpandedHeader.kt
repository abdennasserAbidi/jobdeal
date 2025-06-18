package com.example.myjob.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

@Composable
fun ExpandedHeader(
    modifier: Modifier = Modifier,
    connection: CollapsingAppBarNestedScrollConnection,
    headerContent: @Composable () -> Unit,
    filterBar: @Composable (connection: CollapsingAppBarNestedScrollConnection) -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val headerPlaceable = subcompose("header") {
            headerContent()
        }.first().measure(constraints)

        val navBarPlaceable = subcompose("navBar") {
            filterBar(connection)
        }.first().measure(constraints)

        connection.maxHeight = headerPlaceable.height.toFloat()
        connection.minHeight = navBarPlaceable.height.toFloat()

        val space = IntSize(
            constraints.maxWidth,
            headerPlaceable.height + connection.headerOffset.roundToInt()
        )
        layout(space.width, space.height) {
            headerPlaceable.place(0, connection.headerOffset.roundToInt())
            navBarPlaceable.place(
                Alignment.TopCenter.align(
                    IntSize(navBarPlaceable.width, navBarPlaceable.height),
                    space,
                    layoutDirection
                )
            )
        }
    }
}
