package eu.codlab.lorcana.blipya.utils

import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Constants {
    const val maxUsersInSearch = 10
    const val halfAlpha = 0.5f
    val backendTimeout = 5.seconds
    val searchDebounce = 200.milliseconds
}
