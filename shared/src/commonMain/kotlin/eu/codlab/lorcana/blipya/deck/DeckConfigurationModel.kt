package eu.codlab.lorcana.blipya.deck

import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.input.TextFieldValue
import eu.codlab.http.Configuration
import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.deck.card.DeckMap
import eu.codlab.lorcana.blipya.deck.card.VirtualCardNumber
import eu.codlab.lorcana.blipya.dreamborn.CardNumber
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.save.Dreamborn
import eu.codlab.lorcana.blipya.utils.asLongOrNull
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.cards.CardType
import eu.codlab.lorcana.cards.Classification
import eu.codlab.lorcana.math.CurveInfo
import eu.codlab.lorcana.math.CurveScenario
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.MulliganScenario
import eu.codlab.lorcana.math.Scenario
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import korlibs.datastructure.keep
import korlibs.io.util.UUID
import korlibs.time.DateTime

data class DeckConfigurationModelState(
    val deck: DeckModel,
    val name: String,
    var deckSize: TextFieldValue,
    var handSize: TextFieldValue,
    val scenarii: List<Scenario>,
    val mulligans: List<MulliganScenario>,
    val updatedAt: DateTime = DateTime.now(),
    val loadingDreamborn: Boolean = false,
    val deckContent: DeckMap? = null,
    val calculateDeckCurve: CalculatedDeckCurve? = null
)

data class CalculatedDeckCurve(
    val original: CurveInfo,
    val withKeeping4OfEach: CurveInfo,
    val withKeeping2OfEach: CurveInfo,
)

@Suppress("TooManyFunctions")
class DeckConfigurationModel(private val appModel: AppModel, deck: DeckModel) :
    StateViewModel<DeckConfigurationModelState>(
        DeckConfigurationModelState(
            deck,
            name = deck.name,
            deckSize = TextFieldValue("${deck.size}"),
            handSize = TextFieldValue("${deck.hand}"),
            scenarii = deck.scenarios,
            mulligans = deck.mulligans,
            deckContent = null
        )
    ) {
    private val client = createClient(
        Configuration(
            enableLogs = true,
            socketTimeoutMillis = 30000,
            requestTimeoutMillis = 30000
        )
    )

    private fun generateDeck(cardNumbers: List<CardNumber>): DeckMap {
        val characters = mutableListOf<VirtualCardNumber>()
        val actions = mutableListOf<VirtualCardNumber>()
        val songs = mutableListOf<VirtualCardNumber>()
        val objects = mutableListOf<VirtualCardNumber>()
        val locations = mutableListOf<VirtualCardNumber>()

        cardNumbers.forEach {
            val (card, variant) = appModel.cardFromDreamborn(it.card) ?: return@forEach

            val hasSong = null != card.classifications.find { c -> c.slug == Classification.Song }

            when (card.type) {
                CardType.Glimmer -> characters
                CardType.Item -> objects
                CardType.Location -> locations
                CardType.Action -> if (hasSong) {
                    songs
                } else {
                    actions
                }
            }.add(VirtualCardNumber(card, variant, it.number))
        }

        return DeckMap(
            characters = characters.sortedBy { it.card.cost },
            actions = actions.sortedBy { it.card.cost },
            songs = songs.sortedBy { it.card.cost },
            objects = objects.sortedBy { it.card.cost },
            locations = locations.sortedBy { it.card.cost }
        )
    }

    init {
        launch {
            val list = deck.dreamborn?.data?.list ?: return@launch

            val deckContent = generateDeck(list)
            val calculateDeckCurve = try {
                calculateDeckCurve(deckContent)
            } catch (err: Throwable) {
                null
            }

            updateState {
                copy(
                    deckContent = deckContent,
                    calculateDeckCurve = calculateDeckCurve
                )
            }
        }
    }

    private suspend fun dreamborn(id: String): eu.codlab.lorcana.blipya.dreamborn.Deck {
        val dreamborn = id.split("?").first().let { intermediate ->
            intermediate.split("/").lastOrNull() ?: intermediate
        }.trim()

        val url = "https://api-lorcana.com/deck/fetch/light/$dreamborn"
        val request = client.get(url)

        if (request.status.isSuccess()) return request.body()
        throw IllegalStateException("Error while loading $url")
    }

    @Suppress("TooGenericExceptionCaught")
    fun loadDreamborn(url: String) = safeLaunch {
        if (states.value.loadingDreamborn) return@safeLaunch

        updateState { copy(loadingDreamborn = true) }

        try {
            val dreamborn = dreamborn(url)

            // TODO set to model as well ?
            val dreambornDeck = states.value.deck.dreamborn?.copy(
                url = url,
                data = dreamborn
            ) ?: Dreamborn(
                url = url,
                data = dreamborn
            )

            states.value.deck.dreamborn = dreambornDeck
            val list = dreamborn.list

            val deckContent = generateDeck(list)
            val calculateDeckCurve = try {
                calculateDeckCurve(deckContent)
            } catch (err: Throwable) {
                null
            }

            updateState {
                copy(
                    loadingDreamborn = false,
                    deckContent = deckContent,
                    calculateDeckCurve = calculateDeckCurve
                )
            }

            saveDeck()
        } catch (err: Throwable) {
            // TODO show error ?
            err.printStackTrace()
            updateState { copy(loadingDreamborn = false) }
        }
    }

    fun changeDeck(deck: DeckModel) = safeLaunch {
        // TODO switch listeners

        updateState {
            copy(
                deck = deck,
                deckSize = TextFieldValue("${deck.size}"),
                handSize = TextFieldValue("${deck.hand}"),
                scenarii = deck.scenarios.clone()
            )
        }
    }

    fun add(id: String, added: (DeckModel, Scenario) -> Unit) = safeLaunch {
        val deck = states.value.deck
        val scenario = deck.deck.appendNewScenario(id, "")

        updateCards()

        added(deck, scenario)
    }

    fun delete(scenario: Scenario) {
        states.value.deck.deck.removeScenario(scenario)

        updateCards()
    }

    fun addMulligan(id: String, added: (DeckModel, MulliganScenario) -> Unit) = safeLaunch {
        val deck = states.value.deck
        val mulligan = deck.deck.appendNewMulligan(id, "")

        updateCards()

        added(deck, mulligan)
    }

    fun delete(mulligan: MulliganScenario) {
        states.value.deck.deck.removeMulligan(mulligan)

        updateCards()
    }

    fun removeLast() = safeLaunch {
        val deck = states.value.deck
        deck.scenarios.lastOrNull()?.let { deck.deck.removeScenario(it) }

        updateCards()
    }

    private fun updateCards() {
        val array = states.value.deck.scenarios.clone()
        val mulligans = states.value.deck.mulligans.clone()

        updateState {
            copy(
                updatedAt = DateTime.now(),
                scenarii = array,
                mulligans = mulligans
            )
        }

        saveDeck()
    }

    private fun saveDeck() = safeLaunch {
        appModel.saveDecks()
    }

    fun updateDeckSize(size: TextFieldValue) = safeLaunch {
        updateState {
            copy(deckSize = size)
        }

        size.asLongOrNull()?.let {
            states.value.deck.size = it
        }

        saveDeck()
        // TODO update scenarii
    }

    fun updateHandSize(size: TextFieldValue) = safeLaunch {
        updateState {
            copy(handSize = size)
        }

        size.asLongOrNull()?.let {
            states.value.deck.hand = it
        }

        saveDeck()
        // TODO update scenarii
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
                    val found = card.variants.find { variant -> variant.dreamborn == c.key }
                    if (null != found) variant = found
                    null != found
                }

                variant?.let { finalVariant ->
                    val str = "${finalVariant.set.name.lowercase()}-${finalVariant.id}"
                    (0..<c.value).map { str }
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

    @Throws(IllegalStateException::class)
    private fun calculateDeckCurve(deck: DeckMap): CalculatedDeckCurve {
        val computes = listOf(0L, 2L, 4L).map { removeAtLeast ->
            CurveScenario(
                "",
                "",
                deck.curved(),
                expectedTresholdSuccess = 95.0,
                knownUninkablesInDeck = deck.uninkables().toLong(),
                numberOfInkableKeptInCurve = deck.curvedInkables().map { value ->
                    if (value > removeAtLeast) {
                        removeAtLeast
                    } else {
                        value
                    }
                },
            )
        }
        return CalculatedDeckCurve(
            original = computes[0].calculate(),
            withKeeping2OfEach = computes[1].calculate(),
            withKeeping4OfEach = computes[2].calculate()
        ).also {
            println(it.original)
            println(it.withKeeping2OfEach)
            println(it.withKeeping4OfEach)
        }
    }
}

private fun <T> List<T>.clone() = map { it }
