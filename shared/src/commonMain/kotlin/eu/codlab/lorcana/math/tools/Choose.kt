package eu.codlab.lorcana.math.tools

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

object Choose {
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

        val (reducedNumerator, reducedDenominator) = reduceNumeratorDenominator(
            numerator,
            denominator
        )

        // compute both terms
        val denominatorValue =
            reducedDenominator.reduceOrNull { acc, l -> acc * l } ?: BigInteger(1)
        if (denominatorValue == BigInteger(0)) return 1

        val numeratorValue = reducedNumerator.reduceOrNull { acc, l -> acc * l } ?: BigInteger(1)

        return (numeratorValue / denominatorValue).longValue()
    }

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
}