package eu.codlab.lorcana.math

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class CardState(
    val name: String,
    val min: Long,
    val max: Long,
    val amount: Long
)

class ExpectedCard internal constructor(
    val id: String,
    name: String,
    amount: Long,
    min: Long,
    max: Long
) {
    private val _state = MutableStateFlow(
        CardState(
            name,
            min = min,
            max = max,
            amount = amount
        )
    )
    val state: StateFlow<CardState> = _state

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

    var min: Long
        get() = state.value.min
        private set(value) {
            _state.update { it.copy(min = value) }
        }

    var max: Long
        get() = state.value.max
        private set(value) {
            _state.update { it.copy(max = value) }
        }

    internal fun update(amount: Long, min: Long, max: Long) {
        _state.update {
            it.copy(
                max = max,
                amount = amount,
                min = min
            )
        }
    }

    override fun toString(): String {
        return "{amt: $amount, min: $min, max: $max}"
    }
}
