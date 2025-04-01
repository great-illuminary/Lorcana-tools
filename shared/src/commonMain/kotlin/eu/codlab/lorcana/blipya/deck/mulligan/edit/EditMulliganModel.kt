package eu.codlab.lorcana.blipya.deck.mulligan.edit

import eu.codlab.lorcana.blipya.deck.scenario.show.ShowScenarioModel
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.MulliganCard
import eu.codlab.lorcana.math.MulliganScenario
import eu.codlab.lorcana.math.tools.MulliganResult
import eu.codlab.viewmodel.StateViewModel
import korlibs.io.util.UUID
import korlibs.time.DateTime
import kotlinx.coroutines.Job

data class EditScenarioModelState(
    val deck: DeckModel,
    val name: String,
    val mulligan: MulliganScenario,
    val cards: List<MulliganCard>,
    val probability: MulliganResult,
    val updatedAt: DateTime = DateTime.now()
)

@Suppress("TooManyFunctions")
class EditMulliganModel(
    private val appModel: AppModel,
    deck: DeckModel,
    mulligan: MulliganScenario
) :
    StateViewModel<EditScenarioModelState>(
        EditScenarioModelState(
            deck,
            name = mulligan.name,
            mulligan = mulligan,
            cards = mulligan.cards,
            probability = mulligan.probability.value
        )
    ) {
    // future implementation will be to check the validity of this
    // private val validator = CardValidator()

    private var collectingProbability: Job? = null

    init {
        collectProbability(states.value.mulligan)
    }

    private fun collectProbability(mulligan: MulliganScenario) {
        collectingProbability?.cancel()
        collectingProbability = null

        collectingProbability = safeLaunch {
            mulligan.probability.collect {
                triggerProbability(it)
            }
        }
    }

    fun add(id: String) = safeLaunch {
        states.value.mulligan.add(id)

        updateCards()
    }

    fun removeLast() = safeLaunch {
        val mulligan = states.value.mulligan
        mulligan.cards.lastOrNull()?.let { mulligan.remove(it.id) }

        updateCards()
    }

    private fun updateCards() {
        val array = states.value.mulligan.cards.clone()

        updateState {
            copy(
                updatedAt = DateTime.now(),
                cards = array,
                probability = states.value.mulligan.probability.value
            )
        }

        saveDeck()
    }

    private fun saveDeck() = safeLaunch {
        appModel.saveDecks()
    }

    fun updateScenario(id: String, amount: Long) = safeLaunch {
        states.value.mulligan.update(id, amount)

        saveDeck()
        // triggerProbability()
    }

    fun updateScenario(name: String) {
        states.value.mulligan.name = name

        saveDeck()
    }

    fun updateScenario(id: String, name: String) {
        states.value.mulligan.update(id, name)

        saveDeck()
    }

    companion object {
        fun fake(): ShowScenarioModel {
            val deck = Deck(UUID.randomUUID().toString(), "", 0, 0)
            val scenario = deck.appendNewScenario("", "")

            return ShowScenarioModel(DeckModel(deck), scenario)
        }
    }

    private fun triggerProbability(newValue: MulliganResult) {
        updateState {
            copy(probability = newValue)
        }
    }
}

private fun <T> List<T>.clone() = map { it }
