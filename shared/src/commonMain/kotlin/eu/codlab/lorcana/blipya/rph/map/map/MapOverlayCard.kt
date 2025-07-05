package eu.codlab.lorcana.blipya.rph.map.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Composable
fun MapOverlayCard(
    modifier: Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val color = defaultCardBackground()

    DefaultCard(
        modifier = modifier,
        backgroundColor = color,
        columnModifier = Modifier.fillMaxSize().let {
            if (null != onClick) {
                it.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick
                )
            } else {
                it
            }
        }
    ) {
        content()
    }
}

@Composable
@HotPreview(widthDp = 150, heightDp = 50, darkMode = true)
@HotPreview(widthDp = 150, heightDp = 50, darkMode = false)
private fun Preview() {
    HotPreviewApp(Modifier.fillMaxSize(), isDarkTheme = isSystemInDarkTheme()) {
        MapOverlayCard(
            Modifier.fillMaxSize()
        ) {
            // nothing
        }
    }
}
