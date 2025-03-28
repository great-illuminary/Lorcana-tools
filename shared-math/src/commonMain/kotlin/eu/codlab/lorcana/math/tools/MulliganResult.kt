package eu.codlab.lorcana.math.tools

data class MulliganResult(
    val onPlay: Double,
    val onDraw: Double
)

data class WritableMulliganResult(
    var onPlay: Double,
    var onDraw: Double
) {
    fun toMulliganResult() = MulliganResult(
        onPlay = onPlay * PercentMultiplier,
        onDraw = onDraw * PercentMultiplier
    )
}

private const val PercentMultiplier = 100
