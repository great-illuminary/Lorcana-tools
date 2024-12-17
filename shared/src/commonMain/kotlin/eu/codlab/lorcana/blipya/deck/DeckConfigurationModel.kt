package eu.codlab.lorcana.blipya.deck

import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.input.TextFieldValue
import eu.codlab.http.Configuration
import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.save.Dreamborn
import eu.codlab.lorcana.blipya.utils.asLongOrNull
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.Scenario
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import korlibs.io.util.UUID
import korlibs.time.DateTime

data class DeckConfigurationModelState(
    val deck: DeckModel,
    val name: String,
    var deckSize: TextFieldValue,
    var handSize: TextFieldValue,
    val scenarii: List<Scenario>,
    val updatedAt: DateTime = DateTime.now(),
    val loadingDreamborn: Boolean = false,
)

@Suppress("TooManyFunctions")
class DeckConfigurationModel(private val appModel: AppModel, deck: DeckModel) :
    StateViewModel<DeckConfigurationModelState>(
        DeckConfigurationModelState(
            deck,
            name = deck.name,
            deckSize = TextFieldValue("${deck.size}"),
            handSize = TextFieldValue("${deck.hand}"),
            scenarii = deck.scenarii
        )
    ) {

    private val client = createClient(
        Configuration(
            enableLogs = true,
            socketTimeoutMillis = 30000,
            requestTimeoutMillis = 30000
        )
    ) { /** nothing*/ }

    private suspend fun dreamborn(id: String): eu.codlab.lorcana.blipya.dreamborn.Deck {
        val dreamborn = id.split("?").first().let { intermediate ->
            intermediate.split("/").lastOrNull() ?: intermediate
        }.trim()

        val url = "https://api-lorcana.com/deck/fetch/$dreamborn"
        val request = client.get(url)

        if (request.status.isSuccess()) return request.body()
        throw IllegalStateException("Error while loading $url")
    }


    fun loadDreamborn(url: String) = launch {
        if (states.value.loadingDreamborn) return@launch

        updateState { copy(loadingDreamborn = true) }

        try {
            //TODO set to model as well ?
            //states.value.deck.dreamborn = states.value.deck.dreamborn?.copy(
            //    url = url, data = null
            //) ?: Dreamborn(url = url)

            val dreamborn = dreamborn(url)

            //TODO set to model as well ?
            states.value.deck.dreamborn = states.value.deck.dreamborn?.copy(
                url = url, data = dreamborn
            ) ?: Dreamborn(url = url, data = dreamborn)

            updateState { copy(loadingDreamborn = false) }

            saveDeck()
        } catch (err: Throwable) {
            // TODO show error ?
            err.printStackTrace()
            updateState { copy(loadingDreamborn = false) }
        }
    }

    fun changeDeck(deck: DeckModel) = launch {
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
        val scenario = Scenario(id, "", states.value.deck.deck) { _, _, _ ->
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

    fun openDreamborn(uriHandler: UriHandler) {
        val lorcana = appModel.states.value.lorcana ?: return


        states.value.deck.dreamborn?.data?.let { data ->
            val mapped = data.cards.mapNotNull { c ->
                var variant: VariantClassification? = null
                lorcana.cards.find { card ->
                    val found = card.variants.find { variant -> variant.dreamborn == c.dreamborn }
                    if (null != found) variant = found
                    null != found
                }

                variant?.let { finalVariant ->
                    val str = "${finalVariant.set.name.lowercase()}-${finalVariant.id}"
                    (0..<c.count).map { str }
                }
            }.flatten()

            val cards = mapped.joinToString(",") { it }

            uriHandler.openUri("https://blipya.com/proxy.html?cards=$cards")
        }
    }

    companion object {
        fun fake() = DeckConfigurationModel(
            AppModel.fake(),
            DeckModel(Deck(UUID.randomUUID().toString(), "", 0, 0))
        )
    }
}

private fun <T> List<T>.clone() = map { it }
