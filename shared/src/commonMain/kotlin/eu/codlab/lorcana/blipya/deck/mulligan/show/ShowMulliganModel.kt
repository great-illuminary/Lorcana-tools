package eu.codlab.lorcana.blipya.deck.mulligan.show

import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.MulliganCard
import eu.codlab.lorcana.math.MulliganScenario
import eu.codlab.lorcana.math.tools.MulliganResult
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import korlibs.io.util.UUID
import korlibs.time.DateTime

data class ShowScenarioModelState(
    val deck: DeckModel,
    val name: String,
    val mulligan: MulliganScenario,
    val cards: List<MulliganCard>,
    val probability: MulliganResult,
    val updatedAt: DateTime = DateTime.now(),
    val showPrompt: Boolean = false
)

@Suppress("TooManyFunctions")
class ShowMulliganModel(
    deck: DeckModel,
    mulligan: MulliganScenario
) :
    StateViewModel<ShowScenarioModelState>(
        ShowScenarioModelState(
            deck,
            name = mulligan.name,
            mulligan = mulligan,
            cards = mulligan.cards,
            probability = mulligan.probability.value
        )
    ) {
    companion object {
        fun fake(): ShowMulliganModel {
            val deck = Deck(UUID.randomUUID().toString(), "", 0, 0)
            val mulligan = deck.appendNewMulligan("", "")

            return ShowMulliganModel(DeckModel(deck), mulligan)
        }
    }

    init {
        safeLaunch {
            states.value.mulligan.probability.collect {
                updateState {
                    copy(probability = it)
                }
            }
        }
    }

    fun showPrompt(show: Boolean) {
        launch {
            updateState { copy(showPrompt = show) }
        }
    }
}
