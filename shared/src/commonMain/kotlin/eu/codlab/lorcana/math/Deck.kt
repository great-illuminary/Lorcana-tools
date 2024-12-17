package eu.codlab.lorcana.math

class Deck(
    val id: String,
    var name: String,
    deckSize: Long,
    defaultHand: Long
) {
    var size: Long = deckSize
        set(value) {
            val changed = field != value
            field = value
            if (changed) {
                scenarii.forEach { it.updateRemainingCards() }
            }
        }

    var hand: Long = defaultHand
        set(value) {
            val changed = field != value
            field = value
            if (changed) {
                scenarii.forEach { it.updateRemainingCards() }
            }
        }

    private val mutableScenarii = mutableListOf<Scenario>()
    val scenarii: List<Scenario>
        get() = mutableScenarii

    fun addScenario(scenario: Scenario) {
        if (!mutableScenarii.contains(scenario)) {
            mutableScenarii.add(scenario)
        }
    }

    fun removeScenario(scenario: Scenario) {
        if (mutableScenarii.contains(scenario)) {
            mutableScenarii.remove(scenario)
        }
    }
}
