package eu.codlab.lorcana.blipya.utils

fun WindowType.isScreenExpanded() = when (this) {
    WindowType.SMARTPHONE_TINY -> false
    WindowType.SMARTPHONE -> false
    WindowType.PHABLET -> true
    WindowType.TABLET -> true
}
