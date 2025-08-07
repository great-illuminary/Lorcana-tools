package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.deck.DeckConfiguration
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.home.navigate.NavigateToStack
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeExecute
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.FloatingActionButtonState
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

class RouteDeck : Route(
    "/deck/{uuid}",
    navTransition = NavTransition(),
    swipeProperties = SwipeProperties()
) {
    @Composable
    override fun scene(backStackEntry: BackStackEntry) {
        val appModel: AppModel = LocalApp.current
        val deck = backStackEntry.toHolder(appModel.states.value.decks) ?: return

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
        ) {
            DeckConfiguration(
                appModel,
                deck = deck,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    override fun onInternalEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    ): String {
        val deck = backStackEntry.toHolder(appModel.states.value.decks) ?: return "/"

        appModel.setAppBarState(
            AppBarState.Regular(
                title = deck.name,
                defaultActions
            )
        )

        appModel.setFloatingActionButton(
            FloatingActionButtonState(
                icon = Icons.Filled.Add,
                contentDescription = "Add a new scenario"
            ) {
                println("showAddScenario(true)")
                appModel.showAddScenario(true)
            }
        )

        return "/deck/${deck.id}"
    }

    override fun isMatching(path: String) = path.startsWith("/deck")

    override fun navigateToStack() = NavigateToStack(
        popBackStack = true,
        options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.None
        )
    )

    override val asDefaultRoute = null

    private fun BackStackEntry.toHolder(decks: List<DeckModel>) = safeExecute {
        decks.firstOrNull { it.id == path<String>("uuid")!! }
    }

    fun navigateTo(deck: DeckModel) = NavigateTo("/deck/${deck.id}", navigateToStack())
}
