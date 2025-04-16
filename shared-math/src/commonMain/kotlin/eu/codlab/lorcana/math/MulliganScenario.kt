package eu.codlab.lorcana.math

import eu.codlab.dispatchers.DefaultDispatcher
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
    defaultDeckState: DeckState,
    savedCards: List<Triple<String, String, Long>> = emptyList()
) {
    // todo : another context
    private val context = CoroutineScope(DefaultDispatcher)
    private val mutableCards: MutableList<MulliganCard> = savedCards
        .map { (id, name, amount) -> MulliganCard(id, name, amount) }
        .toMutableList()

    private var parentState: DeckState = defaultDeckState

    val cards: List<MulliganCard>
        get() {
            return mutableCards
        }

    private val _probability = MutableStateFlow(MulliganResult(0.0, 0.0))
    val probability: StateFlow<MulliganResult> = _probability

    init {
        updateRemainingCards()
    }

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

        // TODO -> remove the collected instance for this card will be better
        mutableCards.removeAt(index)

        updateRemainingCards()
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

    internal fun setParentState(state: DeckState) {
        parentState = state
        updateRemainingCards()
    }

    private fun MulliganCard.collectWithContext() {
        context.async {
            state.collect {
                updateRemainingCards()
            }
        }
    }

    private var isInUpdatingRemainingCards = false
    private var requestedUpdatingRemainingCards = false

    private fun updateRemainingCards() {
        // if we attempt to make multiple calls, just make sure
        // we are doing 1 and calling a retrigger afterward
        if (isInUpdatingRemainingCards) {
            requestedUpdatingRemainingCards = true
            return
        }

        isInUpdatingRemainingCards = true

        context.async {
            _probability.update { calculate() }

            while (requestedUpdatingRemainingCards) {
                requestedUpdatingRemainingCards = false

                // since calculating can take some time, we make sure
                // that we manage cases where UI would retrigger update
                _probability.update { calculate() }
            }

            isInUpdatingRemainingCards = false
        }
    }
}
