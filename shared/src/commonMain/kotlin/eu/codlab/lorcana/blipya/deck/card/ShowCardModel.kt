package eu.codlab.lorcana.blipya.deck.card

import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.ExpectedCard
import eu.codlab.lorcana.math.LorcanaInfo.DefaultHandSize
import eu.codlab.lorcana.math.tools.calculate
import eu.codlab.viewmodel.StateHandler
import eu.codlab.viewmodel.StateViewModel
import korlibs.io.util.UUID
import korlibs.time.DateTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ShowCardModelState(
    val deck: Deck,
    val cardNumber: Long,
    val probability: Double,
    val updatedAt: DateTime = DateTime.now()
)

interface ShowCardModel : StateHandler<ShowCardModelState> {
    fun setCardNumber(cardNumber: Long)
}

@Suppress("TooManyFunctions")
class ShowCardModelImpl(
    deck: Deck,
    cardNumber: Long,
) :
    ShowCardModel, StateViewModel<ShowCardModelState>(
    ShowCardModelState(
        deck,
        cardNumber = cardNumber,
        probability = 0.0
    )
) {
    private var collectingProbability: Job? = null

    init {
        collectProbability(states.value.deck)
    }

    override fun setCardNumber(cardNumber: Long) {
        safeLaunch {
            updateState { copy(cardNumber = cardNumber) }
        }
    }

    private fun collectProbability(deck: Deck) {
        collectingProbability?.cancel()
        collectingProbability = null

        collectingProbability = safeLaunch {
            deck.state.collect {
                val cardNumber = states.value.cardNumber
                val others = deck.size - cardNumber

                if (cardNumber > DefaultHandSize || cardNumber > deck.size) {
                    triggerProbability(0.0)
                    return@collect
                }

                val calculation = calculate(
                    deck.size,
                    DefaultHandSize,
                    others,
                    listOf(ExpectedCard("", "", cardNumber, 1, cardNumber))
                )
                triggerProbability(calculation)
            }
        }
    }

    companion object {
        fun fake(): ShowCardModel {
            val deck = Deck(UUID.randomUUID().toString(), "", 0, 0)
            return object : ShowCardModel {
                override fun setCardNumber(cardNumber: Long) {
                    // nothing
                }

                override val states: StateFlow<ShowCardModelState>
                    get() = MutableStateFlow(
                        ShowCardModelState(
                            deck = deck,
                            cardNumber = 1,
                            probability = 0.0
                        )
                    )

                override fun setState(state: ShowCardModelState) {
                    // nothing
                }

                override fun updateState(block: ShowCardModelState.() -> ShowCardModelState) {
                    // nothing
                }
            }
        }
    }

    private fun triggerProbability(newValue: Double) {
        updateState {
            copy(probability = newValue)
        }
    }
}
