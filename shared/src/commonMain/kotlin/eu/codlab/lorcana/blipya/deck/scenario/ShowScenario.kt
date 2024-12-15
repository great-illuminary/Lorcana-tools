package eu.codlab.lorcana.blipya.deck.scenario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.deck.card.ShowCard
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.widgets.MinusAdd
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.rememberViewModel
import korlibs.io.util.UUID

@Composable
fun ShowScenario(
    modifier: Modifier,
    app: AppModel,
    parentModel: DeckConfigurationModel,
    deck: Deck,
    scenario: Scenario
) {
    val model = rememberViewModel { ShowScenarioModel(app, deck, scenario) }
    val state by model.states.collectAsState()

    var name by remember { mutableStateOf(TextFieldValue(state.name)) }

    val color = defaultCardBackground()
    val maxWidth = rememberInputSize()

    Column {
        CustomOutlinedEditText(
            modifier = Modifier.width(200.dp),
            value = name,
            onValueChanged = {
                name = it
                model.updateScenario(name.text)
            },
            label = {
                TextNormal("Scenario Name")
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

        Row {
            TextNormal("Result : ${state.probability}")
        }

        Spacer(Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp)

        state.expectedCards.forEach { holder ->
            Spacer(Modifier.height(16.dp))
            ShowCard(Modifier.fillMaxWidth(), model, state.deck, holder)
        }
    }
}

@Composable
fun rememberInputSize(min: Dp = 90.dp) = when (LocalWindow.current) {
    WindowType.SMARTPHONE_TINY -> min
    WindowType.SMARTPHONE -> min
    WindowType.PHABLET -> 90.dp
    WindowType.TABLET -> 100.dp
}
