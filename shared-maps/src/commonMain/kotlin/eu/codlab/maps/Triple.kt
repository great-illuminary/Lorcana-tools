package eu.codlab.maps

internal data class Triple(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun toKey() = "$x/$y/$z"
}
