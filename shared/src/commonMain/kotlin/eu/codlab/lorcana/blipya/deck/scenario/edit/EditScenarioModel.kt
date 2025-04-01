package eu.codlab.lorcana.blipya.deck.scenario.edit

import eu.codlab.lorcana.blipya.deck.scenario.show.ShowScenarioModel
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.StateViewModel
import korlibs.io.util.UUID
import korlibs.time.DateTime
import kotlinx.coroutines.Job

data class EditScenarioModelState(
    val deck: DeckModel,
    val name: String,
    val scenario: Scenario,
    val expectedCards: List<ExpectedCard>,
    val probability: Double,
    val updatedAt: DateTime = DateTime.now()
)

@Suppress("TooManyFunctions")
class EditScenarioModel(
    private val appModel: AppModel,
    deck: DeckModel,
    scenario: Scenario
) :
    StateViewModel<EditScenarioModelState>(
        EditScenarioModelState(
            deck,
            name = scenario.name,
            scenario = scenario,
            expectedCards = scenario.cards,
            probability = scenario.probability.value
        )
    ) {
    // future implementation will be to check the validity of this
    // private val validator = CardValidator()

    private var collectingProbability: Job? = null

    init {
        collectProbability(states.value.scenario)
    }

    private fun collectProbability(scenario: Scenario) {
        collectingProbability?.cancel()
        collectingProbability = null

        collectingProbability = safeLaunch {
            scenario.probability.collect {
                triggerProbability(it)
            }
        }
    }

    fun changeDeck(deck: DeckModel, scenario: Scenario) = safeLaunch {
        collectProbability(scenario)

        updateState {
            copy(
                deck = deck,
                scenario = scenario,
                probability = scenario.probability.value,
                expectedCards = scenario.cards.clone()
            )
        }
    }

    fun add(id: String) = safeLaunch {
        states.value.scenario.add(id)

        updateCards()
    }

    fun removeLast() = safeLaunch {
        val scenario = states.value.scenario
        scenario.cards.lastOrNull()?.let { scenario.remove(it.id) }

        updateCards()
    }

    private fun updateCards() {
        val array = states.value.scenario.cards.clone()

        updateState {
            copy(
                updatedAt = DateTime.now(),
                expectedCards = array,
                probability = states.value.scenario.probability.value
            )
        }

        saveDeck()
    }

    private fun saveDeck() = safeLaunch {
        appModel.saveDecks()
    }

    fun updateScenario(id: String, amount: Long, min: Long, max: Long) = safeLaunch {
        states.value.scenario.update(id, amount, min, max)

        saveDeck()
        // triggerProbability()
    }

    fun updateScenario(name: String) {
        states.value.scenario.name = name

        saveDeck()
    }

    fun updateScenario(id: String, name: String) {
        states.value.scenario.update(id, name)

        saveDeck()
    }

    companion object {
        fun fake(): ShowScenarioModel {
            val deck = Deck(UUID.randomUUID().toString(), "", 0, 0)
            val scenario = deck.appendNewScenario("", "")

            return ShowScenarioModel(DeckModel(deck), scenario)
        }
    }

    private fun triggerProbability(newValue: Double) {
        updateState {
            copy(probability = newValue)
        }
    }
}

private fun <T> List<T>.clone() = map { it }
