package eu.codlab.lorcana.math

import eu.codlab.lorcana.math.tools.Mulligan
import eu.codlab.lorcana.math.tools.MulliganExpected
import kotlin.test.Test
import kotlin.test.assertEquals

class TestMulligan {
    @Test
    fun testMulliganDefault() {
        // using Frank Karsten example
        val mulligan = Mulligan(
            60,
            listOf(
                MulliganExpected(
                    "Object", 8
                ),
                MulliganExpected(
                    "Belle", 4
                )
            )
        )

        val result = mulligan.calculate()

        assertEquals(56.1, result.onPlay * 100, 0.1)
        assertEquals(60.3, result.onDraw * 100, 0.1)
    }
}