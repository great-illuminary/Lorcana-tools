package eu.codlab.lorcana.blipya.model

import eu.codlab.lorcana.blipya.save.Dreamborn
import eu.codlab.lorcana.blipya.save.ExpectedCardModel
import eu.codlab.lorcana.blipya.save.SavedDeckModel
import eu.codlab.lorcana.blipya.save.ScenarioModel
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.Scenario

data class DeckModel(
    val deck: Deck,
    var dreamborn: Dreamborn? = null
) {
    val id: String
        get() {
            return deck.id
        }

    var name: String
        get() {
            return deck.name
        }
        set(value) {
            deck.name = value
        }

    var size: Long
        get() {
            return deck.size
        }
        set(value) {
            deck.size = value
        }

    var hand: Long
        get() {
            return deck.hand
        }
        set(value) {
            deck.hand = value
        }

    val scenarii: List<Scenario>
        get() {
            return deck.scenarii
        }

    fun addScenario(scenario: Scenario) = deck.addScenario(scenario)

    fun removeScenario(scenario: Scenario) = deck.removeScenario(scenario)

    fun toDeckModel(): SavedDeckModel {
        return SavedDeckModel(
            id = deck.id,
            name = deck.name,
            size = deck.size,
            hand = deck.hand,
            scenarii = deck.scenarii.map { scenar ->
                println("setting the scenario's name in the json to ${scenar.name}")
                ScenarioModel(
                    id = scenar.id,
                    name = scenar.name,
                    cards = scenar.cards.map { card ->
                        ExpectedCardModel(
                            id = card.id,
                            name = card.name,
                            amount = card.amount,
                            min = card.min,
                            max = card.max
                        )
                    }
                )
            },
            dreamborn = dreamborn
        )
    }
}

fun SavedDeckModel.toDeck(): DeckModel {
    val deck = Deck(
        id = this.id,
        name = this.name,
        deckSize = this.size,
        defaultHand = this.hand,
    ).let { newDeck ->
        scenarii.forEach { scenar ->
            val scenario = Scenario(
                id = scenar.id,
                name = scenar.name,
                parent = newDeck,
            ) { _, _, _ -> /** */ }

            scenar.cards.forEach { holder ->
                scenario.add(
                    holder.id,
                    amount = holder.amount,
                    min = holder.min,
                    max = holder.max
                )
                scenario.update(holder.id, holder.name)
            }

            newDeck.addScenario(scenario)
        }

        newDeck
    }

    return DeckModel(
        deck = deck,
        dreamborn = dreamborn
    )
}
