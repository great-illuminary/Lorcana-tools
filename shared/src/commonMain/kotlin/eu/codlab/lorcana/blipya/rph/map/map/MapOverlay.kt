package eu.codlab.lorcana.blipya.rph.map.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.rph.map.events.RphMapModel
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceAction
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceCalendar
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceZoomable
import eu.codlab.lorcana.blipya.theme.AppSizes

@Composable
fun MapOverlay(
    modifier: Modifier,
    model: MapInterfaceAction
) {
    Box(modifier.padding(AppSizes.map.cardVerticalPadding)) {
        if (model is MapInterfaceZoomable) {
            MapButtons(
                Modifier.align(Alignment.TopStart),
                model
            )
        }

        if (model is MapInterfaceCalendar) {
            Column(
                Modifier.align(Alignment.TopEnd),
                verticalArrangement = Arrangement.spacedBy(
                    AppSizes.map.cardVerticalPadding
                ),
                horizontalAlignment = Alignment.End
            ) {
                MapDateSelection(
                    Modifier,
                    model
                )
            }
        }
    }
}

@Composable
@HotPreview(widthDp = 600, heightDp = 400, darkMode = true)
@HotPreview(widthDp = 600, heightDp = 400, darkMode = false)
private fun MapOverlayPreview() {
    HotPreviewApp(Modifier.fillMaxSize(), isDarkTheme = isSystemInDarkTheme()) {
        MapOverlay(
            Modifier.fillMaxSize(),
            RphMapModel(LocalUriHandler.current)
        )
    }
}