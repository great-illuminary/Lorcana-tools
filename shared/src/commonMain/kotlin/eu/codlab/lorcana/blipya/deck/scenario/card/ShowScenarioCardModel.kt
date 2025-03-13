package eu.codlab.lorcana.blipya.deck.scenario.card

import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.math.CardValidator
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch

data class ShowScenarioCardModelState(
    val deckModel: DeckModel,
    val card: ExpectedCard,
    val amountValid: Boolean = true,
    val minValid: Boolean = true,
    val maxValid: Boolean = true,
)

private val validator = CardValidator()

@Suppress("TooManyFunctions")
class ShowScenarioCardModel(
    deckModel: DeckModel,
    card: ExpectedCard,
) : StateViewModel<ShowScenarioCardModelState>(
    ShowScenarioCardModelState(
        deckModel = deckModel,
        card = card,
        amountValid = validator.validate(deckModel.deck, card).amountValid,
        minValid = validator.validate(deckModel.deck, card).minValid,
        maxValid = validator.validate(deckModel.deck, card).maxValid,
    )
) {
    fun update(amount: Long, min: Long, max: Long) = launch {
        updateState {
            val validation = validator.validate(deckModel.deck, amount, min, max)

            copy(
                amountValid = validation.amountValid,
                minValid = validation.minValid,
                maxValid = validation.maxValid,
            )
        }
    }
}
