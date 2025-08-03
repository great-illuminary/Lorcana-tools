package eu.codlab.lorcana.blipya.cards.listing

import eu.codlab.lexer.Parser
import eu.codlab.lorcana.LorcanaLoaded
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VirtualCard
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

data class CardsListingModelState(
    var loaded: Boolean = false,
    var search: String = "",
    var cards: List<Pair<VariantClassification, VirtualCard>> = emptyList()
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

            querySearch.debounce(2.seconds).collect {
                println("new value for the query is $it")
                updateCards(it)
            }
        }
    }

    fun search(query: String) = launch {
        updateState { copy(search = query) }
        querySearch.value = query
    }

    private var currentId = 0
    private fun updateCards(search: String) = safeLaunch {
        try {
            currentId++
            val id = currentId
            if (search.trim().isEmpty()) return@safeLaunch

            val cards = parser.parse(search).let {
                cards.filter { (variant, card) -> it.match(card, variant) }
            }

            if (id != currentId) return@safeLaunch

            updateState { copy(cards = cards) }
        } catch (err: Throwable) {
            err.printStackTrace()
        }
    }
}
