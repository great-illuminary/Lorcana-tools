package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.licenses_title
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.licenses.LicensesScreen
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.transition.NavTransition

class RouteLicenses : Route(
    "/licenses",
    navTransition = NavTransition(),
    swipeProperties = SwipeProperties()
) {
    @Composable
    override fun scene(backStackEntry: BackStackEntry) {
        val appModel = LocalApp.current

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            LicensesScreen(
                Modifier.fillMaxSize(),
                appModel
            )
        }
    }

    override fun onEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    ) {
        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.licenses_title,
                defaultActions
            )
        )
    }
}
