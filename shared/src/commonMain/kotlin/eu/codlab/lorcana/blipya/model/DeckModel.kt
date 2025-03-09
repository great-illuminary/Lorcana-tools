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
        get() = deck.state.value.name
        set(value) {
            deck.name = value
        }

    var size: Long
        get() = deck.state.value.size
        set(value) {
            deck.size = value
        }

    var hand: Long
        get() = deck.state.value.hand
        set(value) {
            deck.hand = value
        }

    val scenarios: List<Scenario>
        get() {
            return deck.scenarios.value
        }

    fun toDeckModel(): SavedDeckModel {
        val state = deck.state.value
        return SavedDeckModel(
            id = deck.id,
            name = state.name,
            size = state.size,
            hand = state.hand,
            scenarii = deck.scenarios.value.map { scenar ->
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
            dreambornDeck = dreamborn
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
            val scenario = newDeck.appendNewScenario(
                id = scenar.id,
                name = scenar.name,
            )

            scenar.cards.forEach { holder ->
                scenario.add(
                    holder.id,
                    amount = holder.amount,
                    min = holder.min,
                    max = holder.max
                )
                scenario.update(holder.id, holder.name)
            }
        }

        newDeck
    }

    return DeckModel(
        deck = deck,
        dreamborn = dreambornDeck
    )
}
