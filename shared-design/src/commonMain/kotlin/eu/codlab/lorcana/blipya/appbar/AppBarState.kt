package eu.codlab.lorcana.blipya.appbar

import androidx.compose.runtime.Composable
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.MenuItem
import org.jetbrains.compose.resources.StringResource

sealed class AppBarState(
    val actions: List<MenuItem>? = null
) {
    @Composable
    abstract fun showTitle(): String

    data object Hidden : AppBarState(null) {
        @Composable
        override fun showTitle() = "hidden"
    }

    class Regular(
        val title: String = "",
        actions: List<MenuItem>? = null
    ) : AppBarState(actions) {
        @Composable
        override fun showTitle() = title
    }

    class Localized(
        val title: StringResource,
        actions: List<MenuItem>? = null
    ) : AppBarState(actions) {
        @Composable
        override fun showTitle() = title.localized()
    }
}
