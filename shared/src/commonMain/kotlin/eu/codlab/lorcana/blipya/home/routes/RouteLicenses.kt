package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.licenses_title
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.licenses.LicensesScreen
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
object RouteLicenses : RouteParameterTo

object RouterLicenses : RouterNoParameters<RouteLicenses> {
    override val klass = RouteLicenses::class

    override fun navigateTo() = NavigateTo(
        route = RouteLicenses,
        stack = NavigateToStack(
            popBackStack = true,
            options = NavigateWithNavOptions(
                launchSingleTop = true
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteLicenses

    override fun route(navBackStackEntry: NavBackStackEntry) = RouteLicensesImpl()

    override fun isMatching(route: String) = route == "/licenses"

    override fun navigateFrom(path: String) = RouteLicenses
}

class RouteLicensesImpl : Route<RouteLicenses>(
    route = "/licenses",
    params = RouteLicenses,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current

        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.licenses_title,
                emptyList()
            )
        )

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
}
