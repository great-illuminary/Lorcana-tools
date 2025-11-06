package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.decks_title
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.decks.DecksScreen
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import eu.codlab.visio.design.appbar.FloatingActionButtonState
import kotlinx.serialization.Serializable

@Serializable
object RouteMain : RouteParameterTo

object RouterMain : RouterNoParameters<RouteMain> {
    override val klass = RouteMain::class

    override fun navigateTo() = NavigateTo(
        route = RouteMain,
        stack = NavigateToStack(
            popBackStack = true,
            options = NavigateWithNavOptions(
                launchSingleTop = true
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteMain

    override fun route(navBackStackEntry: NavBackStackEntry) = RouteMainImpl()

    override fun isMatching(route: String) = route == "/"

    override fun navigateFrom(path: String) = RouteMain
}

class RouteMainImpl : Route<RouteMain>(
    route = "/",
    params = RouteMain,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current

        onInternalEntryIsActive(appModel, emptyList())

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            DecksScreen(
                Modifier.fillMaxSize(),
                appModel
            ) {
                appModel.show(
                    RouterDeck.navigateTo(it.id)
                )
            }
        }
    }

    private fun onInternalEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>
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
}
