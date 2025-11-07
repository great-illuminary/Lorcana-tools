package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.rph_map_stores_title
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.rph.map.events.RphMapStores
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
object RouteRphMapStores : RouteParameterTo

object RouterRphMapStores : RouterNoParameters<RouteRphMapStores> {
    override val klass = RouteRphMapStores::class

    override fun navigateTo() = NavigateTo(
        route = RouteRphMapStores,
        stack = NavigateToStack(
            popBackStack = true,
            options = NavigateWithNavOptions(
                launchSingleTop = true
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteRphMapStores

    override fun route(navBackStackEntry: NavBackStackEntryWrapper) = RouteRphMapStoresImpl()

    override fun isMatching(route: String) = route == "/rph/map/stores"

    override fun navigateFrom(path: String) = RouteRphMapStores
}

class RouteRphMapStoresImpl : Route<RouteRphMapStores>(
    "/rph/map/stores",
    params = RouteRphMapStores,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current

        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.rph_map_stores_title,
                emptyList()
            )
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            RphMapStores(Modifier.fillMaxSize())
        }
    }
}
