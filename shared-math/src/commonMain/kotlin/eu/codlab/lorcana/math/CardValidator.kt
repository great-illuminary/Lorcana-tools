package eu.codlab.lorcana.math

import eu.codlab.lorcana.math.LorcanaInfo.DefaultHandSize

class CardValidator {
    fun validate(deck: Deck, card: ExpectedCard) =
        validate(deck, card.amount, card.min, card.max)

    fun validate(deck: DeckState, card: ExpectedCard) =
        validate(deck, card.amount, card.min, card.max)

    fun validate(deck: Deck, amount: Long, min: Long, max: Long) =
        validate(deck.state.value, amount, min, max)

    fun validate(deck: DeckState, amount: Long, min: Long, max: Long) = CardValidation(
        amountValid = amount < deck.size,
        minValid = min in 1..<amount && min <= deck.hand,
        maxValid = max in min..amount && max <= deck.size,
    )

    fun validateMulligan(amount: Long) = amount <= DefaultHandSize
}

data class CardValidation(
    val minValid: Boolean,
    val maxValid: Boolean,
    val amountValid: Boolean
)
