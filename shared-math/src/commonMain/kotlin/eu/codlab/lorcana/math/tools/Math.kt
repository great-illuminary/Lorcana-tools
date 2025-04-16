package eu.codlab.lorcana.math.tools

import eu.codlab.lorcana.math.ExpectedCard

private const val MaxPercentage = 100.0

fun calculate(
    deckSize: Long,
    handSize: Long,
    miscAmount: Long,
    objects: List<ExpectedCard>
): Double {
    if (miscAmount == 0L && deckSize == handSize) {
        return MaxPercentage
    }

    val recursive = recursiveCombination(handSize, miscAmount, emptyList(), 0, objects, 0)

    val chosen = BinomialCoefficient.binomial(handSize, deckSize)

    return if (chosen == 0L) 0.0 else (recursive * 1.0 / chosen) * MaxPercentage
}

@Suppress("ReturnCount")
private fun recursiveCombination(
    handSize: Long,
    miscAmount: Long,
    currentHand: List<Pair<Long, Long>>,
    currentHandSize: Long,
    objects: List<ExpectedCard>,
    depth: Int
): Long {
    // if we are in an invalid state
    if (currentHandSize > handSize) {
        return 0
    }

    if (currentHandSize == handSize) {
        // check if we can find at least one item which is not with an empty minimum
        objects.firstOrNull { it.min != 0L } ?: return 0
    }

    // we don't have any objects anymore, we can check the resulting result
    // by combining all choose(k,n) & remaining hand size if any

    if (objects.isEmpty()) {
        // calculate the probability to have at least "k" cards in amount of said cards
        return currentHand.map { BinomialCoefficient.binomial(it.first, it.second) }.reduceMultiplyOr0()
            .let { result ->
                // and now calculate how much cards remaining we can have to complete the hand
                if (currentHandSize < handSize) {
                    val chosen = BinomialCoefficient.binomial(handSize - currentHandSize, miscAmount)
                    result * chosen
                } else {
                    result
                }
            }
    }

    // we extract can calculate the combinations
    return objects.last().let { last ->
        (last.min..last.max).sumOf { i ->
            recursiveCombination(
                handSize,
                miscAmount,
                currentHand + (i to last.amount),
                currentHandSize + i,
                objects.dropLast(1),
                depth + 1
            )
        }
    }
}

private fun Iterable<Long>.reduceMultiplyOr0(): Long =
    reduceOrNull { acc, l -> acc * l } ?: 0
