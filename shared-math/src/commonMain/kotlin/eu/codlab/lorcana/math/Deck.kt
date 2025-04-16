package eu.codlab.lorcana.math

import eu.codlab.dispatchers.DefaultDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class DeckState(
    val name: String,
    val size: Long,
    val hand: Long,
)

class Deck(
    val id: String,
    name: String,
    deckSize: Long,
    defaultHand: Long
) {
    private val context = CoroutineScope(DefaultDispatcher)
    private val _state = MutableStateFlow(
        DeckState(
            name,
            size = deckSize,
            hand = defaultHand
        )
    )
    val state: StateFlow<DeckState> = _state

    private val _scenarios = MutableStateFlow<List<Scenario>>(emptyList())
    val scenarios: StateFlow<List<Scenario>> = _scenarios

    private val _mulligans = MutableStateFlow<List<MulliganScenario>>(emptyList())
    val mulligans: StateFlow<List<MulliganScenario>> = _mulligans

    var name: String
        get() = state.value.name
        set(value) {
            _state.update { it.copy(name = value) }
        }

    var size: Long
        get() = state.value.size
        set(value) {
            _state.update { it.copy(size = value) }
        }

    var hand: Long
        get() = state.value.hand
        set(value) {
            _state.update { it.copy(hand = value) }
        }

    init {
        context.async {
            _state.collect {
                scenarios.value.forEach { scenar -> scenar.setParentState(it) }
                mulligans.value.forEach { scenar -> scenar.setParentState(it) }
            }
        }
    }

    fun appendNewMulligan(
        id: String,
        name: String,
        cards: List<Triple<String, String, Long>> = emptyList()
    ) = MulliganScenario(id, name, this.state.value, cards).also { this.addMulligan(it) }

    fun addMulligan(mulligan: MulliganScenario) {
        if (_mulligans.value.contains(mulligan)) {
            return
        }

        _mulligans.update { it + mulligan }
    }

    fun removeMulligan(mulligan: MulliganScenario) {
        if (_mulligans.value.contains(mulligan)) {
            _mulligans.update { it - mulligan }
        }
    }

    fun appendNewScenario(id: String, name: String) =
        Scenario(id, name, this.state.value)
            .also { this.addScenario(it) }

    fun addScenario(scenario: Scenario) {
        if (_scenarios.value.contains(scenario)) {
            return
        }

        _scenarios.update { it + scenario }
    }

    fun removeScenario(scenario: Scenario) {
        if (_scenarios.value.contains(scenario)) {
            _scenarios.update { it - scenario }
        }
    }
}
