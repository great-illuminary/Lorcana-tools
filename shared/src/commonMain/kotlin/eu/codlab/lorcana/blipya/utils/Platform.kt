package eu.codlab.lorcana.blipya.utils

import eu.codlab.platform.Platform

val Platform.isMobile: Boolean
    get() = when (this) {
        Platform.ANDROID -> true
        Platform.IOS -> true
        else -> false
    }
