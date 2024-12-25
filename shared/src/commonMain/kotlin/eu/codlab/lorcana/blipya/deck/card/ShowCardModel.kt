package eu.codlab.lorcana.blipya.deck.card

import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.math.CardValidator
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch

data class ShowCardModelState(
    val deck: DeckModel,
    val card: ExpectedCard,
    val amountValid: Boolean = true,
    val minValid: Boolean = true,
    val maxValid: Boolean = true,
)

private val validator = CardValidator()

@Suppress("TooManyFunctions")
class ShowCardModel(
    deck: DeckModel,
    card: ExpectedCard,
) : StateViewModel<ShowCardModelState>(
    ShowCardModelState(
        deck,
        card = card,
        amountValid = validator.validate(deck, card).amountValid,
        minValid = validator.validate(deck, card).minValid,
        maxValid = validator.validate(deck, card).maxValid,
    )
) {
    fun update(amount: Long, min: Long, max: Long) = launch {
        updateState {
            val validation = validator.validate(deck, amount, min, max)

            copy(
                amountValid = validation.amountValid,
                minValid = validation.minValid,
                maxValid = validation.maxValid,
            )
        }
    }
}
