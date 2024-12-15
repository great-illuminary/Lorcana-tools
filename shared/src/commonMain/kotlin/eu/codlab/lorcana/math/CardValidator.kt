package eu.codlab.lorcana.math

class CardValidator {
    fun validate(deck: Deck, card: ExpectedCard) = validate(deck, card.amount, card.min, card.max)

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