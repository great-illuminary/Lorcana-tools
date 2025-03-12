package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.theme.ApplicationTheme
import eu.codlab.lorcana.blipya.theme.createFontSizes
import eu.codlab.lorcana.blipya.utils.LocalFrame
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.widgets.defaultBackground

@Composable
fun HotPreviewApp(
    modifier: Modifier = Modifier,
    windowType: WindowType = WindowType.TABLET,
    frameType: WindowType = WindowType.TABLET,
    isDarkTheme: Boolean,
    isPreview: Boolean = false,
    content: @Composable () -> Unit
) {
    val fontSizes = createFontSizes()

    CompositionLocalProvider(
        LocalWindow provides windowType,
        LocalFrame provides frameType,
        LocalIsPreview provides isPreview,
        LocalFontSizes provides fontSizes,
    ) {
        Column(modifier.fillMaxSize().defaultBackground()) {
            ApplicationTheme(isDarkTheme, content)
        }
    }
}
