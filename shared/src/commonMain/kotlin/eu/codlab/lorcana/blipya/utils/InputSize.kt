package eu.codlab.lorcana.blipya.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberInputSize(min: Dp = 90.dp) = when (LocalWindow.current) {
    WindowType.SMARTPHONE_TINY -> min
    WindowType.SMARTPHONE -> min
    WindowType.PHABLET -> 90.dp
    WindowType.TABLET -> 100.dp
}
