package eu.codlab.lorcana.blipya.deck

import androidx.compose.ui.text.input.TextFieldValue
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.utils.asLongOrNull
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import korlibs.io.async.launch
import korlibs.io.util.UUID
import korlibs.time.DateTime

data class DeckConfigurationModelState(
    val deck: Deck,
    val name: String,
    var deckSize: TextFieldValue,
    var handSize: TextFieldValue,
    val scenarii: List<Scenario>,
    val updatedAt: DateTime = DateTime.now()
)

@Suppress("TooManyFunctions")
class DeckConfigurationModel(private val appModel: AppModel, deck: Deck) :
    StateViewModel<DeckConfigurationModelState>(
        DeckConfigurationModelState(
            deck,
            name = deck.name,
            deckSize = TextFieldValue("${deck.size}"),
            handSize = TextFieldValue("${deck.hand}"),
            scenarii = deck.scenarii
        )
    ) {
    fun changeDeck(deck: Deck) = launch {
        // TODO switch listeners

        updateState {
            copy(
                deck = deck,
                deckSize = TextFieldValue("${deck.size}"),
                handSize = TextFieldValue("${deck.hand}"),
                scenarii = deck.scenarii.clone()
            )
        }
    }

    fun add(id: String, added: (Scenario) -> Unit) = launch {
        val scenario = Scenario(id, "", states.value.deck) { _, _, _ ->
            launch {
                updateState { copy(updatedAt = DateTime.now()) }
            }
        }

        states.value.deck.addScenario(scenario)

        updateCards()

        added(scenario)
    }

    fun delete(scenario: Scenario) {
        states.value.deck.removeScenario(scenario)

        updateCards()
    }

    fun removeLast() = launch {
        val deck = states.value.deck
        deck.scenarii.lastOrNull()?.let { deck.removeScenario(it) }

        updateCards()
    }

    private fun updateCards() {
        val array = states.value.deck.scenarii.clone()

        updateState {
            copy(
                updatedAt = DateTime.now(),
                scenarii = array,
            )
        }

        saveDeck()
    }

    private fun saveDeck() = launch {
        appModel.saveDecks()
    }

    fun updateDeckSize(size: TextFieldValue) = launch {
        updateState {
            copy(deckSize = size)
        }

        size.asLongOrNull()?.let {
            states.value.deck.size = it
        }

        saveDeck()
        //TODO update scenarii
    }

    fun updateHandSize(size: TextFieldValue) = launch {
        updateState {
            copy(handSize = size)
        }

        size.asLongOrNull()?.let {
            states.value.deck.hand = it
        }

        saveDeck()
        //TODO update scenarii
    }

    fun updateDeck(name: String) {
        states.value.deck.name = name

        saveDeck()
    }

    companion object {
        fun fake() = DeckConfigurationModel(
            AppModel.fake(),
            Deck(UUID.randomUUID().toString(), "", 0, 0)
        )
    }
}

private fun <T> List<T>.clone() = map { it }
