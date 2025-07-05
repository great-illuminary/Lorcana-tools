package eu.codlab.lorcana.blipya.rph.map.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.compose.widgets.CustomOutlinedButton
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.rph.map.RphMapModel
import eu.codlab.lorcana.blipya.widgets.DatePickerDialog
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.lorcana.blipya.widgets.endOfDay
import korlibs.time.DateFormat

@Composable
fun MapDateSelection(
    modifier: Modifier,
    model: RphMapModel
) {
    var showDateSelection by remember { mutableStateOf(false) }

    if (showDateSelection) {
        DatePickerDialog(
            model.states.value.selectedDate,
            onDismiss = {
                showDateSelection = false
            }
        ) {
            println("selected date ${it.endOfDay.format(DateFormat.FORMAT1)}")
            model.setSelectedDate(it.endOfDay)
            showDateSelection = false
        }
    }

    val color = defaultCardBackground()
    val state by model.states.collectAsState()

    DefaultCard(
        modifier = modifier,
        backgroundColor = color,
        columnModifier = Modifier.padding(8.dp)
    ) {
        CustomOutlinedButton(
            onClick = { showDateSelection = true }
        ) {
            TextNormal(
                state.selectedDate.format(DateFormat.FORMAT_DATE)
            )
        }
    }
}

@Composable
@HotPreview(widthDp = 250, heightDp = 50, darkMode = true)
@HotPreview(widthDp = 250, heightDp = 50, darkMode = false)
private fun MapDateSelection() {
    HotPreviewApp(Modifier.fillMaxSize(), isDarkTheme = isSystemInDarkTheme()) {
        MapDateSelection(
            Modifier.fillMaxSize(),
            RphMapModel()
        )
    }
}
