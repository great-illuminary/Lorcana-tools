package eu.codlab.lorcana.blipya.deck.mulligan.card

import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.math.CardValidator
import eu.codlab.lorcana.math.MulliganCard
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch

data class ShowMulliganCardModelState(
    val deck: DeckModel,
    val card: MulliganCard,
    val amountValid: Boolean = true,
)

private val validator = CardValidator()

@Suppress("TooManyFunctions")
class ShowMulliganCardModel(
    deck: DeckModel,
    card: MulliganCard,
) : StateViewModel<ShowMulliganCardModelState>(
    ShowMulliganCardModelState(
        deck,
        card = card,
        amountValid = validator.validateMulligan(card.amount)
    )
) {
    fun update(amount: Long) = launch {
        updateState {
            copy(
                amountValid = validator.validateMulligan(amount)
            )
        }
    }
}
