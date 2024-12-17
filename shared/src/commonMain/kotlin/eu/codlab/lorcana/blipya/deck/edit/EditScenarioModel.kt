package eu.codlab.lorcana.blipya.deck.edit

import eu.codlab.lorcana.blipya.deck.scenario.ShowScenarioModel
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.math.CardValidator
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import korlibs.io.util.UUID
import korlibs.time.DateTime

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
            probability = scenario.calculate()
        )
    ) {
    private val validator = CardValidator()

    private val onProbability: (Double) -> Unit = {
        println("onProbability called on parent's object")
        triggerProbability()
    }

    init {
        states.value.scenario.addCallback(onProbability)
    }

    fun changeDeck(deck: DeckModel, scenario: Scenario) = launch {
        states.value.scenario.removeCallback(onProbability)
        scenario.addCallback(onProbability)

        updateState {
            copy(
                deck = deck,
                scenario = scenario,
                probability = scenario.calculate(),
                expectedCards = scenario.cards.clone()
            )
        }
    }

    fun add(id: String) = launch {
        states.value.scenario.add(id)

        updateCards()
    }

    fun removeLast() = launch {
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
                probability = states.value.scenario.calculate()
            )
        }

        saveDeck()
    }

    private fun saveDeck() = launch {
        appModel.saveDecks()
    }

    fun updateScenario(id: String, amount: Long, min: Long, max: Long) = launch {
        states.value.scenario.update(id, amount, min, max)

        saveDeck()
        triggerProbability()
    }

    fun updateScenario(name: String) {
        states.value.scenario.name = name

        println("scenario name to -> $name")

        saveDeck()

        println("scenario name to -> ${states.value.scenario.name}")
    }

    fun updateScenario(id: String, name: String) {
        states.value.scenario.update(id, name)

        saveDeck()
    }

    companion object {
        fun fake(): ShowScenarioModel {
            val deck = Deck(UUID.randomUUID().toString(), "", 0, 0)
            val scenario = Scenario("", "", deck) { _, _, _ -> /** nothing*/ }
                .also { deck.addScenario(it) }

            return ShowScenarioModel(
                AppModel.fake(),
                DeckModel(deck),
                scenario
            )
        }
    }

    private fun triggerProbability() {
        println("triggerProbability ${states.value.scenario.calculate()}")
        updateState {
            copy(probability = states.value.scenario.calculate())
        }
    }
}

private fun <T> List<T>.clone() = map { it }
