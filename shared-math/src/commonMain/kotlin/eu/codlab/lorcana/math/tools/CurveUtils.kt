package eu.codlab.lorcana.math.tools

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import eu.codlab.lorcana.math.tools.BinomialCoefficient.binomial

class CurveUtils {
    private val mode = DecimalMode(30, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO, 30)

    fun hyperGeometric(
        successesDraws: Long,
        totalPopulationSize: Long,
        totalSuccessesInThePopulation: Long,
        numberOfDraws: Long
    ): Double {
        val remainingInTotal = totalPopulationSize - totalSuccessesInThePopulation
        val others = numberOfDraws - successesDraws

        if (0L == totalSuccessesInThePopulation) return 0.0

        return BigDecimal.fromLong(binomial(successesDraws, totalSuccessesInThePopulation), mode)
            .multiply(BigDecimal.fromLong(binomial(others, remainingInTotal), mode))
            .div(BigDecimal.fromLong(binomial(numberOfDraws, totalPopulationSize), mode))
            .doubleValue(false)
    }
}
