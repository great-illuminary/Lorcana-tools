package eu.codlab.lorcana.math

import eu.codlab.lorcana.math.tools.MulliganUtils
import kotlin.test.Test
import kotlin.test.assertEquals

class TestMulligan {
    @Test
    fun testMulliganDefault() {
        // using Frank Karsten example
        val mulliganUtils = MulliganUtils(
            60,
            listOf(
                MulliganCardState(
                    "Object",
                    8
                ),
                MulliganCardState(
                    "Belle",
                    4
                )
            )
        )

        val result = mulliganUtils.calculate()

        assertEquals(56.1, result.onPlay, 0.1)
        assertEquals(60.3, result.onDraw, 0.1)
    }
}
