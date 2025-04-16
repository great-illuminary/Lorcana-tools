package eu.codlab.lorcana.math

import eu.codlab.lorcana.math.tools.BinomialCoefficient.binomial
import eu.codlab.lorcana.math.tools.BinomialCoefficient.factorial
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TestFactorial {
    private fun actualFactorial(n: Long) =
        factorial(n).reduce { acc, i -> acc * i }

    @Test
    fun testFactorialResults() {
        assertEquals(120, actualFactorial(5))
        assertEquals(3628800, actualFactorial(10))
        assertEquals(2432902008176640000, actualFactorial(20))
        assertFalse(86530198635 == actualFactorial(18))
    }

    @Test
    fun testChooseResults() {
        assertEquals(210, binomial(4, 10))
        assertEquals(4426165368, binomial(8, 64))
        assertEquals(55098996177225, binomial(8, 200))
        assertEquals(342700125300, binomial(11, 60))
    }
}
