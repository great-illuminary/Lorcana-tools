package eu.codlab.lorcana.blipya.utils

import androidx.compose.runtime.Composable
import kotlin.math.min

@Composable
fun rememberColumns(max: Int = 3): Int {
    val columns = when (LocalFrame.current) {
        WindowType.SMARTPHONE_TINY -> min(max, 1)
        WindowType.SMARTPHONE -> min(max, 1)
        WindowType.PHABLET -> min(max, 2)
        WindowType.TABLET -> max
    }

    return columns
}
