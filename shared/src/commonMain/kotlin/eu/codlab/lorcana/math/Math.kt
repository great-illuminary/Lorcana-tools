package eu.codlab.lorcana.math

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

private const val MaxPercentage = 100.0

fun factorial(x: Long) = factorial(1, x)

fun factorial(startAt: Long, x: Long): List<Long> {
    if (startAt <= 0 || x <= 0) {
        return listOf(1)
    }

    return (startAt..x).map { it }
}

@Suppress("ReturnCount")
fun choose(k: Long, n: Long): Long {
    val kPos = if (k < 0) 0 else k
    val nPos = if (n < 0) 0 else n
    if (k == n) return 1

    // we normally do n!/(k! * (n - k)!)
    // meaning that it's 1*...*n / ( (1*...*k) * (1*...*(n-k))!
    // we can then already simplify n! with -> (k+1)*...*n / (n-k)!

    // instead of having the following ->
    // val numerator = factorial(nPos)
    // val denominator = listOf(
    //    factorial(nPos - kPos),
    //    factorial(kPos)
    // ).flatten()

    // we can have
    val numerator = factorial(kPos + 1, nPos)
    val denominator = factorial(nPos - kPos)

    val (reducedNumerator, reducedDenominator) = reduceNumeratorDenominator(numerator, denominator)

    // compute both terms
    val denominatorValue = reducedDenominator.reduceOrNull { acc, l -> acc * l } ?: BigInteger(1)
    if (denominatorValue == BigInteger(0)) return 1

    val numeratorValue = reducedNumerator.reduceOrNull { acc, l -> acc * l } ?: BigInteger(1)

    return (numeratorValue / denominatorValue).longValue()
}

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

    val chosen = choose(handSize, deckSize)

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
        return currentHand.map { choose(it.first, it.second) }.reduceMultiplyOr0()
            .let { result ->
                // and now calculate how much cards remaining we can have to complete the hand
                if (currentHandSize < handSize) {
                    val chosen = choose(handSize - currentHandSize, miscAmount)
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

private fun reduceNumeratorDenominator(
    numerator: List<Long>,
    denominator: List<Long>
): Pair<List<BigInteger>, List<BigInteger>> {
    val reducedNumerator = numerator.toMutableList()
    val reducedDenominator = denominator.toMutableList()

    var i = 0

    while (i < reducedDenominator.size) {
        val value = reducedDenominator[i]
        val j = reducedNumerator.indexOf(value)

        if (j >= 0) {
            reducedDenominator.removeAt(i)
            reducedNumerator.removeAt(j)
        } else {
            i++
        }
    }

    return reducedNumerator.map { it.toBigInteger() } to
            reducedDenominator.map { it.toBigInteger() }
}
