package eu.codlab.lorcana.blipya.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import eu.codlab.lorcana.blipya.theme.AppColor

val LocalWindow = compositionLocalOf<WindowType> { error("No LocalWindow set") }
val LocalFrame = compositionLocalOf { WindowType.TABLET }

@Composable
fun LocalFrameProvider(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val localDensity = LocalDensity.current
    var frame by remember { mutableStateOf(WindowType.TABLET) }
    CompositionLocalProvider(
        LocalFrame provides frame,
    ) {
        Box(
            modifier = modifier.onGloballyPositioned { coordinates ->
                val width = with(localDensity) { coordinates.size.width.toDp() }

                val newWindowSize = WindowType.basedOnWidth(width)
                if (newWindowSize != frame) {
                    frame = newWindowSize
                }

                // throw IllegalStateException("frame -> $frame / $width")
            }.background(AppColor.BackgroundBlue)
        ) {
            content()
        }
    }
}

@Composable
fun LocalWindowProvider(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val localDensity = LocalDensity.current
    var window by remember { mutableStateOf(WindowType.TABLET) }
    CompositionLocalProvider(
        LocalWindow provides window,
    ) {
        Box(
            modifier = modifier.onGloballyPositioned { coordinates ->
                val width = with(localDensity) { coordinates.size.width.toDp() }

                val newWindowSize = WindowType.basedOnWidth(width)
                if (newWindowSize != window) {
                    window = newWindowSize
                }
            }.background(AppColor.BackgroundBlue)
        ) {
            content()
        }
    }
}
