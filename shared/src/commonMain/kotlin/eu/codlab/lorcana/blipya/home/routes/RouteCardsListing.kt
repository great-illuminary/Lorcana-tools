package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.title_cards_listing
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.cards.listing.CardsListing
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
object RouteCardsListing : RouteParameterTo

object RouterCardsListing : RouterNoParameters<RouteCardsListing> {
    override val klass = RouteCardsListing::class

    override fun navigateTo() = NavigateTo(
        route = RouteCardsListing,
        stack = NavigateToStack(
            popBackStack = true,
            options = NavigateWithNavOptions(
                launchSingleTop = true
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteCardsListing

    override fun route(navBackStackEntry: NavBackStackEntry) = RouteCardsListingImpl()

    override fun isMatching(route: String) = route == "/cards"

    override fun navigateFrom(path: String) = RouteCardsListing
}

class RouteCardsListingImpl : Route<RouteCardsListing>(
    route = "/cards",
    params = RouteCardsListing,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current

        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.title_cards_listing,
                emptyList()
            )
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            CardsListing(appModel, Modifier.fillMaxSize())
        }
    }
}
