package eu.codlab.lorcana.blipya.deck.scenario.show

import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.StateViewModel
import korlibs.io.util.UUID
import korlibs.time.DateTime
import kotlin.math.pow
import kotlin.math.roundToInt

data class ShowScenarioModelState(
    val deck: DeckModel,
    val name: String,
    val scenario: Scenario,
    val expectedCards: List<ExpectedCard>,
    val probability: Double,
    val updatedAt: DateTime = DateTime.now()
)

@Suppress("TooManyFunctions")
class ShowScenarioModel(
    deck: DeckModel,
    scenario: Scenario
) :
    StateViewModel<ShowScenarioModelState>(
        ShowScenarioModelState(
            deck,
            name = scenario.name,
            scenario = scenario,
            expectedCards = scenario.cards,
            probability = scenario.probability.value
        )
    ) {
    companion object {
        fun fake(): ShowScenarioModel {
            val deck = Deck(UUID.randomUUID().toString(), "", 0, 0)
            val scenario = deck.appendNewScenario("", "")

            return ShowScenarioModel(DeckModel(deck), scenario)
        }
    }

    init {
        safeLaunch {
            states.value.scenario.probability.collect {
                updateState {
                    copy(probability = it)
                }
            }
        }
    }
}

fun Double.round(decimals: Long = 2): Double {
    val factor = 10.0.pow(decimals.toDouble())
    return (this * factor).roundToInt() / factor
}
