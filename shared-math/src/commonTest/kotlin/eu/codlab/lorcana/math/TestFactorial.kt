package eu.codlab.lorcana.math

import eu.codlab.lorcana.math.tools.Choose.choose
import eu.codlab.lorcana.math.tools.Choose.factorial
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
        assertEquals(210, choose(4, 10))
        assertEquals(4426165368, choose(8, 64))
        assertEquals(55098996177225, choose(8, 200))
        assertEquals(342700125300, choose(11, 60))
    }
}
