package eu.codlab.lorcana.math

import eu.codlab.lorcana.contexts.DefaultDispatcher
import eu.codlab.lorcana.math.tools.MulliganResult
import eu.codlab.lorcana.math.tools.MulliganUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MulliganScenario internal constructor(
    val id: String,
    var name: String,
    defaultDeckState: DeckState
) {
    // todo : another context
    private val context = CoroutineScope(DefaultDispatcher)
    private val mutableCards: MutableList<MulliganCard> = mutableListOf()

    private var parentState: DeckState = defaultDeckState

    val cards: List<MulliganCard>
        get() {
            return mutableCards
        }

    private val _probability = MutableStateFlow(calculate())
    val probability: StateFlow<MulliganResult> = _probability

    fun addCard(card: MulliganCard) {
        mutableCards.add(card)
        updateRemainingCards()
    }

    private fun calculate(): MulliganResult {
        val mulligan = MulliganUtils(
            parentState.size,
            mutableCards.map { it.state.value }
        )
        return mulligan.calculate()
    }

    fun add(id: String, amount: Long? = null): MulliganCard {
        remove(id)

        return MulliganCard(id, "", amount ?: 0)
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

    fun update(id: String, amount: Long): Boolean {
        val holder = mutableCards.firstOrNull { it.id == id } ?: return false

        val changed = amount != holder.amount
        holder.update(amount)

        if (changed) {
            updateRemainingCards()
        }

        return amount <= parentState.size
    }

    fun updateRemainingCards() {
        _probability.update { calculate() }
    }

    internal fun setParentState(state: DeckState) {
        parentState = state
        updateRemainingCards()
    }

    private fun MulliganCard.collectWithContext() {
        context.async {
            state.collect {
                _probability.update { calculate() }
            }
        }
    }
}
