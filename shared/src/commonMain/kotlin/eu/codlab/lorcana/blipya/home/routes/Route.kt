package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.runtime.Composable
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.widgets.MenuItem
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.transition.NavTransition

sealed class Route(
    val route: String,
    val deepLinks: List<String> = emptyList(),
    val navTransition: NavTransition? = null,
    val swipeProperties: SwipeProperties? = null,
) {
    @Composable
    abstract fun scene(
        backStackEntry: BackStackEntry
    )

    abstract fun onEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    )
}
