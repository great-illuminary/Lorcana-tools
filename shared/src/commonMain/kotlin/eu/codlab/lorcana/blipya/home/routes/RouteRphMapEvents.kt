package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.rph_map_title
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.rph.map.events.RphMapEvents
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
object RouteRphMapEvents : RouteParameterTo

object RouterRphMapEvents : RouterNoParameters<RouteRphMapEvents> {
    override val klass = RouteRphMapEvents::class

    override fun navigateTo() = NavigateTo(
        route = RouteRphMapEvents,
        stack = NavigateToStack(
            popBackStack = true,
            options = NavigateWithNavOptions(
                launchSingleTop = true
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteRphMapEvents

    override fun route(navBackStackEntry: NavBackStackEntry) = RouteRphMapEventsImpl()

    override fun isMatching(route: String) = route == "/licenses"

    override fun navigateFrom(path: String) = RouteRphMapEvents
}

class RouteRphMapEventsImpl : Route<RouteRphMapEvents>(
    route = "/rph/map/events",
    params = RouteRphMapEvents,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current

        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.rph_map_title,
                emptyList()
            )
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            RphMapEvents(Modifier.fillMaxSize())
        }
    }
}
