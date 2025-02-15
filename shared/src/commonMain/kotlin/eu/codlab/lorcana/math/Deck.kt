package eu.codlab.lorcana.math

import eu.codlab.lorcana.contexts.DefaultDispatcher
import korlibs.io.async.async
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            }
        }
    }

    fun appendNewScenario(id: String, name: String) =
        Scenario(id, name, this.state.value)
            .also { this.addScenario(it) }

    fun addScenario(scenario: Scenario) {
        if (_scenarios.value.contains(scenario)) {
            println("addScenario :: already added, skipping")
            return
        }

        println("addScenario :: adding the scenario ${scenario.id}")

        _scenarios.update { it + scenario }
    }

    fun removeScenario(scenario: Scenario) {
        if (_scenarios.value.contains(scenario)) {
            _scenarios.update { it - scenario }
        }
    }
}
