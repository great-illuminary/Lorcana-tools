package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.title_cards_listing_documentation
import eu.codlab.lorcana.blipya.cards.documentation.CardsListingDocumentation
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.home.navigate.NavigateToStack
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.transition.NavTransition

class RouteCardsListingDocumentation : Route(
    "/cards/documentation",
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
            CardsListingDocumentation(
                Modifier.fillMaxSize(),
                appModel
            )
        }
    }

    override fun onInternalEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    ): String {
        appModel.setAppBarState(
            AppBarState.Localized(
                title = Res.string.title_cards_listing_documentation,
                defaultActions
            )
        )

        return route
    }

    override fun navigateToStack() = NavigateToStack(
        popBackStack = false,
        options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.None
        )
    )

    override val asDefaultRoute = navigateTo()

    fun navigateTo() = NavigateTo(route, navigateToStack())
}
