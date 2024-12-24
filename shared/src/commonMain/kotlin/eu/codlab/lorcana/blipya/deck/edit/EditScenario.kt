package eu.codlab.lorcana.blipya.deck.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.invalid
import eu.codlab.blipya.res.result
import eu.codlab.blipya.res.scenario_name
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.card.ShowCard
import eu.codlab.lorcana.blipya.deck.scenario.round
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.MinusAdd
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.rememberViewModel
import korlibs.io.util.UUID

@Composable
fun EditScenario(
    modifier: Modifier,
    app: AppModel,
    deck: DeckModel,
    scenario: Scenario
) {
    val model = rememberViewModel { EditScenarioModel(app, deck, scenario) }
    val state by model.states.collectAsState()

    var name by remember { mutableStateOf(TextFieldValue(state.name)) }

    val color = defaultCardBackground()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        DefaultCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = color
        ) {
            Column(
                Modifier.padding(16.dp).background(color),
            ) {
                CustomOutlinedEditText(
                    modifier = Modifier.width(200.dp),
                    value = name,
                    onValueChanged = {
                        name = it
                        model.updateScenario(name.text)
                    },
                    label = {
                        TextNormal(Res.string.scenario_name.localized())
                    }
                )

                Spacer(Modifier.height(8.dp))

                MinusAdd(
                    onMinus = {
                        model.removeLast()
                    },
                    onPlus = {
                        model.add(UUID.randomUUID().toString())
                    }
                )

                DisplayStatisticalResult(probability = state.probability)

                Spacer(Modifier.height(16.dp))

                HorizontalDivider(thickness = 1.dp)

                state.expectedCards.forEach { holder ->
                    Spacer(Modifier.height(16.dp))
                    ShowCard(Modifier.fillMaxWidth(), model, state.deck, holder)
                }
            }
        }
    }
}
