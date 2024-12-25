package eu.codlab.lorcana.math

class Scenario internal constructor(
    val id: String,
    var name: String,
    private val parent: Deck,
) {
    private val onProbabilityUpdated: MutableList<(Double) -> Unit> = mutableListOf()
    private val mutableCards: MutableList<ExpectedCard> = mutableListOf()

    val cards: List<ExpectedCard>
        get() {
            return mutableCards
        }

    var others = ExpectedCard(
        "others",
        "others",
        originalAmount = parent.size,
        originalMin = 0,
        originalMax = parent.hand
    ) { _, _, _ ->
        // nothing for this one
    }
        private set

    fun addCard(expectedCard: ExpectedCard) {
        mutableCards.add(expectedCard)
    }

    fun addCallback(callback: (Double) -> Unit) {
        if (!onProbabilityUpdated.contains(callback)) {
            onProbabilityUpdated.add(callback)
        }
    }

    fun removeCallback(callback: (Double) -> Unit) {
        if (onProbabilityUpdated.contains(callback)) {
            onProbabilityUpdated.remove(callback)
        }
    }

    fun calculate(): Double {
        val validator = CardValidator()

        val invalid = cards.find {
            val result = validator.validate(parent, it)

            !result.amountValid || !result.minValid || !result.maxValid
        }

        if (null != invalid) return -1.0

        return calculate(
            parent.size,
            parent.hand,
            others.amount,
            cards
        )
    }

    fun add(id: String, amount: Long? = null, min: Long? = null, max: Long? = null): ExpectedCard {
        remove(id)

        return ExpectedCard(id, "", amount ?: 0, min ?: 0, max ?: 0) { _, _, _ ->
            calculate().let { result ->
                onProbabilityUpdated.forEach { it.invoke(result) }
            }
        }.also {
            mutableCards.add(it)

            updateRemainingCards()
        }
    }

    fun remove(id: String) {
        val index = mutableCards.indexOfFirst { it.id == id }
        if (index < 0) return

        mutableCards.removeAt(index)

        calculate().let { result ->
            onProbabilityUpdated.forEach { it.invoke(result) }
        }
    }

    fun update(id: String, name: String) {
        val holder = mutableCards.firstOrNull { it.id == id } ?: return
        holder.name = name
    }

    fun update(id: String, amount: Long, min: Long, max: Long): Boolean {
        val holder = mutableCards.firstOrNull { it.id == id } ?: return false

        val changed = amount != holder.amount || min != holder.min || max != holder.max
        holder.update(amount, min, max)

        if (changed) {
            updateRemainingCards()
        }

        return amount <= parent.size && min <= parent.hand && max <= parent.size
    }

    fun updateRemainingCards() {
        val cardsAddedAmount = mutableCards.map { it.amount }
            .reduceOrNull { acc, l -> acc + l } ?: 0
        val cardsMinAmount = mutableCards.map { it.min }
            .reduceOrNull { acc, l -> acc + l } ?: 0

        others.update(
            amount = parent.size - cardsAddedAmount,
            min = 0,
            max = parent.hand - cardsMinAmount
        )

        calculate().let { result ->
            onProbabilityUpdated.forEach { it.invoke(result) }
        }
    }
}
