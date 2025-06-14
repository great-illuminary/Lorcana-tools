package eu.codlab.lorcana.blipya.curve

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.curve_info_cost_input
import eu.codlab.lorcana.blipya.utils.localized

@Composable
fun ShowCurveInput(
    modifier: Modifier,
    key: Int,
    model: CurveInformationModel
) {
    ShowLongInput(
        modifier,
        Res.string.curve_info_cost_input.localized().replace("{cost}", "$key"),
        initialValue = { it.curvesCost[key] ?: 0L },
        model
    ) {
        model.setCurveCost(key, it.text.toLongOrNull() ?: 0L)
    }
}
