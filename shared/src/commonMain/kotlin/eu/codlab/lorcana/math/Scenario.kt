package eu.codlab.lorcana.math

import eu.codlab.lorcana.contexts.DefaultDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class Scenario internal constructor(
    val id: String,
    var name: String,
    defaultDeckState: DeckState
) {
    // todo : another context
    private val context = CoroutineScope(DefaultDispatcher)
    private val mutableCards: MutableList<ExpectedCard> = mutableListOf()

    private var parentState: DeckState = defaultDeckState

    val cards: List<ExpectedCard>
        get() {
            return mutableCards
        }

    val others = ExpectedCard(
        "others",
        "others",
        amount = parentState.size,
        min = 0,
        max = parentState.hand
    )

    private val _probability = MutableStateFlow(calculate())
    val probability: StateFlow<Double> = _probability

    fun addCard(expectedCard: ExpectedCard) {
        mutableCards.add(expectedCard)
        updateRemainingCards()
    }

    private fun calculate(): Double {
        val validator = CardValidator()

        val invalid = cards.find {
            val result = validator.validate(parentState, it)

            !result.amountValid || !result.minValid || !result.maxValid
        }

        if (null != invalid) return -1.0

        return eu.codlab.lorcana.math.tools.calculate(
            parentState.size,
            parentState.hand,
            others.amount,
            cards
        )
    }

    fun add(id: String, amount: Long? = null, min: Long? = null, max: Long? = null): ExpectedCard {
        remove(id)

        return ExpectedCard(id, "", amount ?: 0, min ?: 0, max ?: 0)
            .also {
                it.collectWithContext()

                mutableCards.add(it)

                updateRemainingCards()
            }
    }

    fun remove(id: String) {
        val index = mutableCards.indexOfFirst { it.id == id }
        if (index < 0) return

        //TODO -> remove the collected instance for this card will be better
        mutableCards.removeAt(index)

        _probability.update { calculate() }
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

        return amount <= parentState.size && min <= parentState.hand && max <= parentState.size
    }

    fun updateRemainingCards() {
        val cardsAddedAmount = mutableCards.map { it.amount }
            .reduceOrNull { acc, l -> acc + l } ?: 0
        val cardsMinAmount = mutableCards.map { it.min }
            .reduceOrNull { acc, l -> acc + l } ?: 0

        others.update(
            amount = parentState.size - cardsAddedAmount,
            min = 0,
            max = parentState.hand - cardsMinAmount
        )

        _probability.update { calculate() }
    }

    internal fun setParentState(state: DeckState) {
        parentState = state
        updateRemainingCards()
    }

    private fun ExpectedCard.collectWithContext() {
        context.async {
            state.collect {
                _probability.update { calculate() }
            }
        }
    }
}
