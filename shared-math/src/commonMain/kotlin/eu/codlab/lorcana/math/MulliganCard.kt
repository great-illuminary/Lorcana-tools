package eu.codlab.lorcana.math

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class MulliganCardState(
    val name: String,
    val amount: Long
)

class MulliganCard internal constructor(
    val id: String,
    name: String,
    amount: Long,
) {
    private val _state = MutableStateFlow(
        MulliganCardState(
            name,
            amount = amount
        )
    )
    val state: StateFlow<MulliganCardState> = _state

    var name: String
        get() = state.value.name
        set(value) {
            _state.update { it.copy(name = value) }
        }

    var amount: Long
        get() = state.value.amount
        private set(value) {
            _state.update { it.copy(amount = value) }
        }

    internal fun update(amount: Long) {
        _state.update { it.copy(amount = amount) }
    }

    override fun toString(): String {
        return "{amt: $amount}"
    }
}
