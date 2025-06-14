package eu.codlab.lorcana.blipya.curve

import androidx.compose.runtime.toMutableStateMap
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.lorcana.math.CurveInfo
import eu.codlab.lorcana.math.CurveScenario
import eu.codlab.viewmodel.StateViewModel

data class CurveInformationModelState(
    val curvesCost: MutableMap<Int, Long>,
    val cardsInDeck: Long = 60,
    val uninkableInDeck: Long = 0,
    val knownUninkables: Boolean = false,
    val calculatedDeckCurve: CurveInfo? = null
)

@Suppress("MagicNumber")
class CurveInformationModel : StateViewModel<CurveInformationModelState>(
    CurveInformationModelState(
        curvesCost = (0..12).map { it to 0L }.toMutableStateMap(),
    )
) {
    fun setCurveCost(index: Int, value: Long) {
        println("set curveCost")
        states.value.curvesCost.let {
            it[index] = value
            update(
                it,
                states.value.uninkableInDeck,
                states.value.knownUninkables
            )
        }
    }

    fun setKnownUninkables(knownUninkables: Boolean) = update(
        states.value.curvesCost,
        states.value.uninkableInDeck,
        knownUninkables
    )

    fun setUninkables(knownUninkables: Long) = update(
        states.value.curvesCost,
        knownUninkables,
        states.value.knownUninkables
    )

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun update(
        curvesCost: MutableMap<Int, Long>,
        uninkableInDeck: Long,
        knownUninkables: Boolean
    ) = safeLaunch {
        val calculateDeckCurve = try {
            calculateDeckCurve(curvesCost, uninkableInDeck, knownUninkables)
        } catch (_: Throwable) {
            null
        }

        println("calculated curve $calculateDeckCurve")
        updateState {
            copy(
                curvesCost = curvesCost,
                knownUninkables = knownUninkables,
                calculatedDeckCurve = calculateDeckCurve
            )
        }
    }

    companion object {
        fun fake() = CurveInformationModel()
    }

    @Suppress("MagicNumber")
    private fun calculateDeckCurve(
        cost: Map<Int, Long>,
        uninkableInDeck: Long,
        knownUninkables: Boolean
    ): CurveInfo {
        // we will compute the cards with no modifications,
        // then 2 cards of each "turn" will be at least kept
        // then 4 cards of each "turn"
        val compute = CurveScenario(
            "",
            "",
            cost.keys.sorted().map { cost[it] ?: 0L },
            expectedTresholdSuccess = 90.0,
            knownUninkablesInDeck = if (knownUninkables) uninkableInDeck else null
        )

        return compute.calculate()
    }
}
