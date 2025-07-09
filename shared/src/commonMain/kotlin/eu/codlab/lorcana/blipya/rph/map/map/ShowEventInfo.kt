package eu.codlab.lorcana.blipya.rph.map.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.open
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.rph.models.Event
import eu.codlab.lorcana.blipya.rph.models.EventHolder
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Composable
fun ShowEventInfo(
    modifier: Modifier,
    eventHolder: EventHolder,
    onEventSelected: (EventHolder) -> Unit
) {
    val event = eventHolder.event
    val color = defaultCardBackground()

    DefaultCard(
        modifier = modifier,
        backgroundColor = color,
        columnModifier = Modifier.padding(8.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextNormal(
                text = event.name,
                fontSize = LocalFontSizes.current.map.callOutNormal,
                fontWeight = FontWeight.Bold
            )

            Column(Modifier.height(32.dp)) { }

            OutlinedButton(
                onClick = { onEventSelected(eventHolder) }
            ) {
                TextNormal(
                    text = Res.string.open.localized(),
                    color = AppColor.Black
                )
            }
        }
    }
}

@Composable
@HotPreview(widthDp = 250, heightDp = 50, darkMode = true)
@HotPreview(widthDp = 250, heightDp = 50, darkMode = false)
private fun ShowEventInfoPreview() {
    HotPreviewApp(Modifier.fillMaxSize(), isDarkTheme = isSystemInDarkTheme()) {
        ShowEventInfo(
            Modifier.fillMaxSize(),
            EventHolder(event = Event(0, ""))
        ) {
            // nothing
        }
    }
}
