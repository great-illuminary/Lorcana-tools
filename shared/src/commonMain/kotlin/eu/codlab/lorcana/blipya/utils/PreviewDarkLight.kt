package eu.codlab.lorcana.blipya.utils

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.home.PreviewApp

@Composable
fun PreviewDarkLightColumn(
    modifier: Modifier = Modifier,
    windowType: WindowType = WindowType.TABLET,
    frameType: WindowType = WindowType.TABLET,
    submodifier: ColumnScope.() -> Modifier = { Modifier },
    repeatableContent: @Composable ColumnScope.(modifier: Modifier, isDark: Boolean) -> Unit
) = eu.codlab.compose.widgets.preview.PreviewDarkLightColumn(
    modifier = modifier,
    submodifier = submodifier
) { internalModifier, isDark ->
    PreviewApp(
        modifier = internalModifier,
        windowType = windowType,
        frameType = frameType,
        isDarkTheme = isDark,
        content = {
            repeatableContent(submodifier(), isDark)
        }
    )
}

@Composable
fun PreviewDarkLightRow(
    modifier: Modifier = Modifier,
    windowType: WindowType = WindowType.TABLET,
    frameType: WindowType = WindowType.TABLET,
    submodifier: RowScope.() -> Modifier = { Modifier },
    repeatableContent: @Composable RowScope.(modifier: Modifier, isDark: Boolean) -> Unit
) = eu.codlab.compose.widgets.preview.PreviewDarkLightRow(
    modifier = modifier,
    submodifier = submodifier
) { internalModifier, isDark ->
    PreviewApp(
        modifier = internalModifier,
        windowType = windowType,
        frameType = frameType,
        isDarkTheme = isDark,
        content = {
            repeatableContent(submodifier(), isDark)
        }
    )
}
