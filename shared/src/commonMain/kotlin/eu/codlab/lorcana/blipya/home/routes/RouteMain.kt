package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.decks_title
import eu.codlab.lorcana.blipya.decks.DecksScreen
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.FloatingActionButtonState
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.transition.NavTransition

class RouteMain : Route(
    "/main",
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
            DecksScreen(
                Modifier.fillMaxSize(),
                appModel
            ) { appModel.show(NavigateTo.Deck(it)) }
        }
    }

    override fun onInternalEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    ): String {
        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.decks_title,
                defaultActions
            )
        )

        appModel.setFloatingActionButton(
            FloatingActionButtonState(
                icon = Icons.Filled.Add,
                contentDescription = "Add a new deck"
            ) { appModel.showAddDeck(true) }
        )

        return "/"
    }

    override fun isMatching(path: String) = path == "/"
}
