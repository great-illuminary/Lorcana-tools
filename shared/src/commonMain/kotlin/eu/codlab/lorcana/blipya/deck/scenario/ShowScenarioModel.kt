package eu.codlab.lorcana.blipya.deck.scenario

import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.StateViewModel
import korlibs.io.util.UUID
import korlibs.time.DateTime
import kotlin.math.pow
import kotlin.math.roundToInt

data class ShowScenarioModelState(
    val deck: Deck,
    val name: String,
    val scenario: Scenario,
    val expectedCards: List<ExpectedCard>,
    val probability: Double,
    val updatedAt: DateTime = DateTime.now()
)

@Suppress("TooManyFunctions")
class ShowScenarioModel(
    private val appModel: AppModel,
    deck: Deck,
    scenario: Scenario
) :
    StateViewModel<ShowScenarioModelState>(
        ShowScenarioModelState(
            deck,
            name = scenario.name,
            scenario = scenario,
            expectedCards = scenario.cards,
            probability = scenario.calculate()
        )
    ) {
    companion object {
        fun fake(): ShowScenarioModel {
            val deck = Deck(UUID.randomUUID().toString(), "", 0, 0)
            val scenario = Scenario("", "", deck) { _, _, _ -> /** nothing*/ }
                .also { deck.addScenario(it) }

            return ShowScenarioModel(
                AppModel.fake(),
                deck,
                scenario
            )
        }
    }

    fun triggerProbability() {
        println("triggerProbability ${states.value.scenario.calculate()}")
        updateState {
            copy(probability = states.value.scenario.calculate())
        }
    }
}

private fun <T> List<T>.clone() = map { it }

fun Double.round(decimals: Long = 2): Double {
    val factor = 10.0.pow(decimals.toDouble())
    return (this * factor).roundToInt() / factor
}