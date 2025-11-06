package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.curve_title
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.curve.CurveInformation
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
object RouteCurve : RouteParameterTo

object RouterCurve : RouterNoParameters<RouteCurve> {
    override val klass = RouteCurve::class

    override fun navigateTo() = NavigateTo(
        route = RouteCurve,
        stack = NavigateToStack(
            popBackStack = true,
            options = NavigateWithNavOptions(
                launchSingleTop = true
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteCurve

    override fun route(navBackStackEntry: NavBackStackEntry) = RouteCurveImpl()

    override fun isMatching(route: String) = route == "/curve"

    override fun navigateFrom(path: String) = RouteCurve
}

class RouteCurveImpl : Route<RouteCurve>(
    route = "/curve",
    params = RouteCurve,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current
        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.curve_title,
                emptyList()
            )
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            CurveInformation(
                Modifier.fillMaxSize()
            )
        }
    }
}
