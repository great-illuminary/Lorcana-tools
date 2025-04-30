package eu.codlab.lorcana.math.tools

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.math.min

object BinomialCoefficient {
    fun factorial(x: Long) = factorial(1, x)

    fun factorial(startAt: Long, x: Long): List<Long> {
        if (startAt <= 0 || x <= 0) {
            return listOf(1)
        }

        return (startAt..x).map { it }
    }

    private val binomialCache: MutableMap<Long, MutableMap<Long, Long>> = mutableMapOf()

    fun binomial(k: Long, n: Long): Long {
        if (k == n) return 1

        val cache = binomialCache.getOrPut(k) { mutableMapOf() }

        cache[n]?.let { return it }

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
        val max = min(k, n - k)
        val denominator = BigInteger(max).factorial()

        // and finally calculating the numerator values (reduced via multiplication)
        var numerator = BigInteger(1)
        (1..max).forEach { i -> numerator *= ((n + 1 - i)) }

        return (numerator / denominator).longValue(true).also {
            cache[n] = it
        }
    }
}
