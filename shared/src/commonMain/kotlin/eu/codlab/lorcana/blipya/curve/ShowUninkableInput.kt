package eu.codlab.lorcana.blipya.curve

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.curve_info_deck_related_uninkables_known
import eu.codlab.blipya.res.curve_info_deck_related_uninkables_number
import eu.codlab.lorcana.blipya.utils.localized

@Composable
fun ShowUninkableInput(
    modifier: Modifier,
    model: CurveInformationModel
) {
    val state by model.states.collectAsState()

    ShowLongInput(
        modifier,
        Res.string.curve_info_deck_related_uninkables_number.localized(),
        initialValue = { it.uninkableInDeck },
        canBeDisabled = true,
        enablePlaceholder = Res.string.curve_info_deck_related_uninkables_known.localized(),
        isEnabled = state.knownUninkables,
        onEnabled = {
            model.setKnownUninkables(it)
        },
        model = model
    ) {
        model.setUninkables(it.text.toLongOrNull() ?: 0L)
    }
}
