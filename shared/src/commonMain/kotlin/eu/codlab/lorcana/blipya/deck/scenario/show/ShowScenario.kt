package eu.codlab.lorcana.blipya.deck.scenario.show

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.LocalNavigator
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.delete
import eu.codlab.blipya.res.edit
import eu.codlab.blipya.res.show_scenario_delete_text
import eu.codlab.blipya.res.show_scenario_delete_title
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.deck.edit.DisplayStatisticalResult
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.utils.tintColor
import eu.codlab.lorcana.blipya.widgets.MenuItemOverflowMenu
import eu.codlab.lorcana.blipya.widgets.PopupConfirm
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun ShowScenario(
    modifier: Modifier,
    app: AppModel,
    parentModel: DeckConfigurationModel,
    deck: DeckModel,
    scenario: Scenario
) {
    val model = rememberViewModel { ShowScenarioModel(deck, scenario) }
    val state by model.states.collectAsState()
    val navigator = LocalNavigator.current

    if (state.promptDelete) {
        PopupConfirm(
            title = Res.string.show_scenario_delete_title.localized(),
            text = Res.string.show_scenario_delete_text.localized(),
            onDismiss = { model.showPrompt(false) },
            onConfirm = {
                model.showPrompt(false)
                parentModel.delete(scenario)
                navigator?.pop()
            }
        )
    }

    Column(modifier) {
        Row {
            TextNormal(
                modifier = Modifier.weight(1.0f),
                text = state.name
            )

            MenuItemOverflowMenu(
                tint = Color.White,
                imageVector = Icons.Outlined.MoreVert,
                id = scenario.id,
                contentDescription = "Manage"
            ) {
                // show the actions to edit & delete the current scenario
                listOf(
                    Triple(
                        Res.string.edit.localized(),
                        Icons.Default.Edit
                    ) {
                        app.show(
                            PossibleRoutes.DeckScenario.navigateTo(
                                state.deck,
                                state.scenario
                            )
                        )
                    },
                    Triple(
                        "${Res.string.delete.localized()} ${state.name}",
                        Icons.Default.Delete
                    ) { model.showPrompt(true) },
                ).forEach { (contentDescription, imageVector, onClick) ->
                    IconButton(onClick) {
                        Icon(
                            imageVector,
                            contentDescription,
                            tint = tintColor()
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(AppSizes.paddings.reduced))

        DisplayStatisticalResult(probability = state.probability)
    }
}
