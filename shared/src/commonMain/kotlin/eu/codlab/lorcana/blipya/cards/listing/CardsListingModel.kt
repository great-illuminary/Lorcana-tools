package eu.codlab.lorcana.blipya.cards.listing

import androidx.compose.ui.platform.UriHandler
import eu.codlab.lexer.Parser
import eu.codlab.lorcana.LorcanaLoaded
import eu.codlab.lorcana.blipya.utils.Constants
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VirtualCard
import eu.codlab.sentry.wrapper.Sentry
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

data class CardsListingModelState(
    var loaded: Boolean = false,
    var search: String = "",
    var cards: List<Pair<VariantClassification, VirtualCard>> = emptyList(),
    var searchError: Throwable? = null,
)

@OptIn(FlowPreview::class)
class CardsListingModel(
    lorcanaLoaded: LorcanaLoaded
) : StateViewModel<CardsListingModelState>(CardsListingModelState()) {
    private val cards = lorcanaLoaded.cards.map { card -> card.variants.map { it to card } }.flatten()
    private val parser = Parser()
    private val querySearch = MutableStateFlow("")

    init {

        safeLaunch(
            onError = {
                it.printStackTrace()
            }
        ) {
            updateState { copy(cards = cards) }

            querySearch.debounce(Constants.searchDebounce).collect {
                updateCards(it)
            }
        }
    }

    fun search(query: String) = launch {
        updateState { copy(search = query) }
        querySearch.value = query
    }

    fun openProxy(uriHandler: UriHandler) {
        val list = states.value.cards.map {
            "${it.first.set.name.lowercase()}-${it.first.id}x4"
        }.joinToString(",") { it }

        uriHandler.openUri("https://blipya.com/proxy.html?cards=$list")
    }

    @Suppress("TooGenericExceptionCaught")
    private fun updateCards(search: String) = safeLaunch {
        try {
            if (search.trim().isEmpty()) return@safeLaunch

            val cards = parser.parse(search).let {
                cards.filter { (variant, card) -> it.match(card, variant) }
            }.sortedWith(compareBy({ it.first.set }, { it.first.id }))

            updateState { copy(cards = cards, searchError = null) }
        } catch (err: Throwable) {
            err.printStackTrace()
            updateState { copy(searchError = err) }
            Sentry.captureException(err)
        }
    }
}
