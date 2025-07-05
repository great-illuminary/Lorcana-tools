package eu.codlab.maps

data class XY(
    val x: Double,
    val y: Double
) {
    fun toPair() = x to y
}
