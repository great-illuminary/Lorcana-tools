package eu.codlab.lorcana.blipya.utils

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class MapManipulation(
    val tileSize: Int,
    val levelCount: Int
) {
    fun actualZoom(newZoom: Double) = max(min(newZoom, maxZoom), minZoom)

    val fullWidthOrHeight = tileSize * 2.0.pow(levelCount).toInt()

    val zoomInCoeff = 1.1

    val zoomOutCoeff = 0.9

    val maxZoom = 3.0

    val minZoom = 0.00003

    val defaultZoom = 0.0003
}
