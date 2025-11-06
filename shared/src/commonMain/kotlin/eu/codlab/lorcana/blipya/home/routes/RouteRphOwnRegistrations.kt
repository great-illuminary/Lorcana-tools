package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.rph_own_registrations
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.rph.own.RphOwnRegistrations
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
object RouteRphOwnRegistrations : RouteParameterTo

object RouterRphOwnRegistrations : RouterNoParameters<RouteRphOwnRegistrations> {
    override val klass = RouteRphOwnRegistrations::class

    override fun navigateTo() = NavigateTo(
        route = RouteRphOwnRegistrations,
        stack = NavigateToStack(
            popBackStack = true,
            options = NavigateWithNavOptions(
                launchSingleTop = true
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteRphOwnRegistrations

    override fun route(navBackStackEntry: NavBackStackEntry) = RouteRphOwnRegistrationsImpl()

    override fun isMatching(route: String) = route == "/rph/own_registrations"

    override fun navigateFrom(path: String) = RouteRphOwnRegistrations
}

class RouteRphOwnRegistrationsImpl : Route<RouteRphOwnRegistrations>(
    route = "/rph/own_registrations",
    params = RouteRphOwnRegistrations,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current

        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.rph_own_registrations,
                emptyList()
            )
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            RphOwnRegistrations(Modifier.fillMaxSize(), appModel)
        }
    }
}
