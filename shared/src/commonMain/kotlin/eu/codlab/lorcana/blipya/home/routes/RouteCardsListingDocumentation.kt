package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.title_cards_listing_documentation
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.cards.documentation.CardsListingDocumentation
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
object RouteCardsListingDocumentation : RouteParameterTo

object RouterCardsListingDocumentation : RouterNoParameters<RouteCardsListingDocumentation> {
    override val klass = RouteCardsListingDocumentation::class

    override fun navigateTo() = NavigateTo(
        route = RouteCardsListingDocumentation,
        stack = NavigateToStack(
            popBackStack = false,
            options = NavigateWithNavOptions(
                launchSingleTop = false
            )
        )
    )

    override fun isCurrentRoute(routeParameterTo: RouteParameterTo?) =
        null != routeParameterTo && routeParameterTo is RouteCardsListingDocumentation

    override fun route(navBackStackEntry: NavBackStackEntryWrapper) = RouteCardsListingDocumentationImpl()

    override fun isMatching(route: String) = route == "/cards/documentation"

    override fun navigateFrom(path: String) = RouteCardsListingDocumentation
}

class RouteCardsListingDocumentationImpl : Route<RouteCardsListingDocumentation>(
    route = "/cards/documentation",
    params = RouteCardsListingDocumentation,
) {
    @Composable
    override fun scene() {
        val appModel = LocalApp.current

        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.title_cards_listing_documentation,
                emptyList()
            )
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            CardsListingDocumentation(
                Modifier.fillMaxSize(),
                appModel
            )
        }
    }
}
