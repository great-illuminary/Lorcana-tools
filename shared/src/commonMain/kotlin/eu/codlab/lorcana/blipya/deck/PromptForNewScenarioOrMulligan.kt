package eu.codlab.lorcana.blipya.deck

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.cancel
import eu.codlab.blipya.res.new_scenario_option_draws
import eu.codlab.blipya.res.new_scenario_option_mulligan
import eu.codlab.blipya.res.new_scenario_text
import eu.codlab.blipya.res.new_scenario_title
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.PopupConfirm
import eu.codlab.lorcana.blipya.widgets.PopupConfirmAction

@Composable
fun PromptForNewScenarioOrMulligan(
    appModel: AppModel,
    showPrompt: Boolean,
    onDismiss: () -> Unit
) {
    PopupConfirm(
        show = showPrompt,
        title = Res.string.new_scenario_title.localized(),
        text = Res.string.new_scenario_text.localized(),
        onDismiss = {
            onDismiss()
        },
        actions = listOf(
            PopupConfirmAction(
                text = Res.string.cancel.localized(),
                isOutlined = false
            ) {
                onDismiss()
            },
            PopupConfirmAction(
                text = Res.string.new_scenario_option_draws.localized(),
                isOutlined = true
            ) {
                appModel.addScenario()
                appModel.showAddScenario(false)
            },
            PopupConfirmAction(
                text = Res.string.new_scenario_option_mulligan.localized(),
                isOutlined = true
            ) {
                appModel.addMulligan()
                appModel.showAddScenario(false)
            },
        )
    )
}

@Preview
@Composable
private fun PromptForNewScenarioOrMulliganPreview() {
    PreviewDarkLightColumn { _, _ ->
        PromptForNewScenarioOrMulligan(AppModel.fake(), true) {
            /* nothing */
        }
    }
}
