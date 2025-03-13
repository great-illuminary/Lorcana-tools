package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DefaultCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    backgroundColor: Color,
    elevation: Dp = 4.dp,
    border: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) = Card(
    modifier = modifier.let {
        if (null != onClick) {
            it.combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
        } else {
            it
        }
    },
    shape = shape,
    colors = CardDefaults.cardColors(
        containerColor = backgroundColor
    ),
    elevation = defaultCardElevation(elevation),
    border = border,
) {
    Column(columnModifier.background(backgroundColor)) {
        content()
    }
}

@Composable
fun defaultCardElevation(elevation: Dp) = CardDefaults.cardElevation(
    defaultElevation = elevation,
    pressedElevation = elevation,
    focusedElevation = elevation,
    hoveredElevation = elevation,
    draggedElevation = elevation,
    disabledElevation = elevation
)
