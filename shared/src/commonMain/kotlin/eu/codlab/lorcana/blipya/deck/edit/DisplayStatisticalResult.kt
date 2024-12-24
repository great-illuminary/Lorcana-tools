package eu.codlab.lorcana.blipya.deck.edit

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.invalid
import eu.codlab.blipya.res.result
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.scenario.round
import eu.codlab.lorcana.blipya.utils.localized

@Composable
fun DisplayStatisticalResult(
    modifier: Modifier = Modifier,
    probability: Double
) {
    val result = if (probability < 0) {
        Res.string.invalid.localized()
    } else {
        "${probability.round(2)}%"
    }

    Row(modifier) {
        TextNormal("${Res.string.result.localized()} : $result")
    }
}