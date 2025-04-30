package eu.codlab.lorcana.math.tools

import eu.codlab.lorcana.math.LorcanaInfo.DefaultHandSize
import eu.codlab.lorcana.math.MulliganCardState
import kotlin.math.min

/**
 * I stumbled recently upon an article made by Frank Karsten which highlighted some work around
 * the meta around the 10th of March 2025, he linked is implementation of a mulligan oriented
 * draw manager so I told myself that it was indeed a real interesting possible implementation
 * which is fundamentally different from the calculation which is managing scenarios + drawn card
 *
 * In this case what happens is that we suppose a loop of selected cards and check how much
 * of those cards we can statistically draw on an opening hand and mulligan specifically so that
 * we have (for now) at least 1 of each mandatory cards (note : I mostly ported the algorithm
 * from python to kotlin, there will be evolutions to make it so that multiple of the same card is
 * manageable)
 *
 * Credits to the original code to https://github.com/frankkarsten
 */
class MulliganUtils(
    private val deckSize: Long,
    private val cards: List<MulliganCardState>
) {
    // transform to a map of name -> number of cards
    private val listOfCardsInTheDeck = cards.associate { it.name to it.amount }

    // and create the number of other cards in the deck
    private val othersInTheDeck = deckSize - cards.sumOf { it.amount }

    fun calculate(): MulliganResult {
        val writableResult = WritableMulliganResult(0.0, 0.0)

        val keys = cards.map { it.name }

        loopResult(writableResult, keys)

        return writableResult.toMulliganResult()
    }

    private fun loopResult(writableResult: WritableMulliganResult, keys: List<String>) {
        val wholeListOfCombination = mutableListOf<Map<String, Long>>()

        if (keys.isEmpty()) return

        prepareMap(listOfCardsInTheDeck, wholeListOfCombination, mutableMapOf(), keys.first(), keys)

        // we now have our list of possible scenarios which are the expected starting hands
        // we can be valid with

        wholeListOfCombination.forEach { hand ->
            manageSpecificHand(writableResult, hand)
        }
    }

    private fun manageSpecificHand(result: WritableMulliganResult, hand: Map<String, Long>) {
        val inHand = hand.values.sum()
        val containsAtLeast1OfEach = null == hand.values.find { it < 1L }

        val othersInHand = DefaultHandSize - inHand
        val initialOpeningHandProb = multivariateHyperGeometric(
            listInDeck = listOfCardsInTheDeck,
            othersInDeck = othersInTheDeck,
            listInHand = hand,
            othersInHand = othersInHand
        )

        if (containsAtLeast1OfEach) {
            result.onPlay += initialOpeningHandProb
            result.onDraw += initialOpeningHandProb
            return
        }

        // we didn't have both of them so now we need to actually toss everything we didn't want...
        val toKeepInHand = hand.keys.associate { it to min(hand[it]!!, 1) }
        val cardsKeptInHand = toKeepInHand.values.sum()
        val cardsToAdjust = DefaultHandSize - cardsKeptInHand

        // we compute the actual number of relevant cards which are in the deck now
        val remainingInTheDeck = listOfCardsInTheDeck.keys.associate {
            // we now have the ones in hand less in the deck before we tossed everything
            it to listOfCardsInTheDeck[it]!! - hand[it]!!
        }

        val wholeListOfCombination = mutableListOf<Map<String, Long>>()
        prepareMap(
            remainingInTheDeck,
            wholeListOfCombination,
            mutableMapOf(),
            remainingInTheDeck.keys.first(),
            remainingInTheDeck.keys.toList(),
            handSize = cardsKeptInHand,
            maximumCardsAdjusted = cardsToAdjust
        )

        wholeListOfCombination.forEach { list ->
            manageSpecificAdjustedHand(
                result,
                toKeepInHand,
                remainingInTheDeck,
                othersInTheDeck - othersInHand,
                adjustedCards = list,
                cardsToAdjust = cardsToAdjust,
                initialOpeningHandProb = initialOpeningHandProb
            )
        }
    }

    private fun manageSpecificAdjustedHand(
        result: WritableMulliganResult,
        hand: Map<String, Long>,
        remainingInTheDeck: Map<String, Long>,
        othersInTheDeck: Long,
        adjustedCards: Map<String, Long>,
        cardsToAdjust: Long,
        initialOpeningHandProb: Double
    ) {
        val combinedHand = hand.keys.associate { it to hand[it]!! + adjustedCards[it]!! }
        val inHand = cardsToAdjust - adjustedCards.values.sum()
        val containsAtLeast1OfEach = null == combinedHand.values.find { it < 1L }

        val adjustmentHandProb = multivariateHyperGeometric(
            listInDeck = remainingInTheDeck,
            othersInDeck = othersInTheDeck,
            listInHand = adjustedCards,
            othersInHand = inHand
        )

        val adjustedProb = initialOpeningHandProb * adjustmentHandProb

        if (containsAtLeast1OfEach) {
            result.onPlay += adjustedProb
            result.onDraw += adjustedProb
            return
        }

        // we still could have a chance on the draw
        // but in this case, we need to check for which key we could recover if possible
        val zeroed = combinedHand.values.filter { it == 0L }
        val unrecoverable = zeroed.size > 1
        // we are getting the key which can be recovered and used with the lists of card in the deck
        val recoverableKey = combinedHand.keys.find { combinedHand[it]!! == 0L }

        if (unrecoverable || zeroed.isEmpty() || null == recoverableKey) {
            // we have more than 1 keys where the count is 0, we can't recover to have all >= 1
            return
        }

        val remaining = deckSize - DefaultHandSize - cardsToAdjust

        // we returned early if we had more than "recoverableKey" to recover potentially
        result.onDraw += adjustedProb * listOfCardsInTheDeck[recoverableKey]!! / remaining
    }

    private fun prepareMap(
        remainingCardsInTheDeck: Map<String, Long>,
        wholeListOfCombination: MutableList<Map<String, Long>>,
        map: MutableMap<String, Long>,
        key: String,
        keys: List<String>,
        handSize: Long = 0,
        maximumCardsAdjusted: Long = DefaultHandSize
    ) {
        // second case, we are going down more...
        val checkUpperHand = min(remainingCardsInTheDeck[key]!!, maximumCardsAdjusted) + 1

        var nextKeys = keys.filterIndexed { index, _ -> index > 0 }

        (0..<checkUpperHand).forEach { taken ->
            val currentHandSize = handSize + taken
            map[key] = taken

            // if we can go down, we do
            if (nextKeys.isNotEmpty()) {
                prepareMap(
                    remainingCardsInTheDeck,
                    wholeListOfCombination,
                    map,
                    nextKeys.first(),
                    nextKeys,
                    currentHandSize
                )
            } else if (currentHandSize <= maximumCardsAdjusted) {
                // we check that we can end up in a scenario
                // where we would be able to have those cards + remaining ones
                val result = map.keys.associate { it to map[it]!! }

                wholeListOfCombination.add(result)
            }
        }
    }

    private fun multivariateHyperGeometric(
        listInDeck: Map<String, Long>,
        othersInDeck: Long,
        listInHand: Map<String, Long>,
        othersInHand: Long
    ): Double {
        var result = 1.0
        var sumDeck = 0L
        var sumNeeded = 0L

        fun inEach(inHand: Long, inDeck: Long) {
            result *= BinomialCoefficient.binomial(inHand, inDeck)
            sumDeck += inDeck
            sumNeeded += inHand
        }

        listInDeck.keys.forEach { inEach(listInHand[it]!!, listInDeck[it]!!) }
        inEach(othersInHand, othersInDeck)

        return result / BinomialCoefficient.binomial(sumNeeded, sumDeck)
    }
}
