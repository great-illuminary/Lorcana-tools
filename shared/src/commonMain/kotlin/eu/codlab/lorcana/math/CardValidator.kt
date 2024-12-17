package eu.codlab.lorcana.math

import eu.codlab.lorcana.blipya.model.DeckModel

class CardValidator {
    fun validate(deck: DeckModel, card: ExpectedCard) =
        validate(deck.deck, card.amount, card.min, card.max)

    fun validate(deck: Deck, card: ExpectedCard) =
        validate(deck, card.amount, card.min, card.max)

    fun validate(deck: DeckModel, amount: Long, min: Long, max: Long) =
        validate(deck.deck, amount, min, max)

    fun validate(deck: Deck, amount: Long, min: Long, max: Long) = CardValidation(
        amountValid = amount < deck.size,
        minValid = min in 1..<amount && min <= deck.hand,
        maxValid = max in min..amount && max <= deck.size,
    )
}

data class CardValidation(
    val minValid: Boolean,
    val maxValid: Boolean,
    val amountValid: Boolean
)