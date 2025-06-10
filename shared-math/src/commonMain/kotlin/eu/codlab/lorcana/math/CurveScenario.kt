package eu.codlab.lorcana.math

import eu.codlab.lorcana.math.tools.CurveUtils

class CurveScenario(
    val id: String,
    val name: String,
    val numberOfCardsInCurve: List<Long>,
    val expectedTresholdSuccess: Double,
    val originalCardsSeen: Long = 7,
    val knownUninkablesInDeck: Long? = null,
    val numberOfInkableKeptInCurve: List<Long> = emptyList(),
) {
    private val overrideNumberOfUninkableKeptInCurve = numberOfInkableKeptInCurve.sum()

    private val totalCards = numberOfCardsInCurve.sum()
    private val maxInk = numberOfCardsInCurve.indexOfLast { it > 0 }
    private val utils = CurveUtils()

    private var averageCost =
        1.0 * numberOfCardsInCurve.mapIndexed { i, v -> i * v }.sum() / totalCards

    fun calculate(): CurveInfo {
        if (maxInk < 0) {
            throw IllegalStateException("maxInk is invalid")
        }

        val cardsSeen = originalCardsSeen + maxInk
        val uninkablesInDeck = knownUninkablesInDeck ?: calculateExpectedMaxUninkables(cardsSeen)
        val uninkables = uninkablesInDeck + overrideNumberOfUninkableKeptInCurve

        if (uninkables < 0 || uninkables > totalCards) {
            throw IllegalStateException("Uninkable count is invalid")
        }

        val inkablesInDeck = totalCards - uninkables

        val overallProbability = (maxInk..cardsSeen).map { seen ->
            utils.hyperGeometric(seen, totalCards, inkablesInDeck, cardsSeen)
        }.sum() * 100

        val turnsProbability = numberOfCardsInCurve.mapIndexed { turnTreshold, _ ->
            val cardsSeen = originalCardsSeen + turnTreshold
            val turn = turnTreshold + 1

            val probability = (turn..cardsSeen).map { draws ->
                utils.hyperGeometric(draws, totalCards, inkablesInDeck, cardsSeen)
            }.sum()

            CurveTurnInfo(
                turn,
                probability * 100.0
            )
        }

        return CurveInfo(
            averageCost = averageCost,
            maxInk = maxInk,
            cardsSeen = cardsSeen,
            inkablesInDeck = inkablesInDeck,
            uninkablesInDeck = uninkablesInDeck,
            probability = overallProbability,
            totalCards = totalCards,
            turnsInfo = turnsProbability
        )
    }

    private fun calculateExpectedMaxUninkables(cardsSeen: Long): Long {
        var uninkables = 0L
        (0..totalCards).forEach { seen ->
            val remainingCards = totalCards - seen
            val prob = (maxInk..cardsSeen).sumOf { fill ->
                utils.hyperGeometric(fill, totalCards, remainingCards, cardsSeen)
            }

            if (100 * prob >= expectedTresholdSuccess) {
                uninkables = seen
            }
        }

        return uninkables
    }
}

data class CurveInfo(
    val averageCost: Double,
    val maxInk: Int,
    val cardsSeen: Long,
    val inkablesInDeck: Long,
    val uninkablesInDeck: Long,
    val probability: Double,
    val totalCards: Long,
    val turnsInfo: List<CurveTurnInfo>
)

data class CurveTurnInfo(
    val turn: Int,
    val probability: Double
)