package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.deck.scenario.edit.EditScenario
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.home.navigate.NavigateToStack
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeExecute
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.lorcana.math.Scenario
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

class RouteDeckScenario :
    Route(
        "/deck/{uuid}/scenario/{scenario}",
        navTransition = NavTransition(),
        swipeProperties = SwipeProperties()
    ) {
    @Composable
    override fun scene(backStackEntry: BackStackEntry) {
        val appModel: AppModel = LocalApp.current
        val (deck, scenario) = backStackEntry.toHolder(appModel.states.value.decks) ?: return

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
                .verticalScroll(state = rememberScrollState())
        ) {
            EditScenario(
                modifier = Modifier.fillMaxSize(),
                appModel,
                deck = deck,
                scenario = scenario
            )
        }
    }

    override fun onInternalEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    ): String {
        val (deck, scenario) = backStackEntry.toHolder(appModel.states.value.decks) ?: return "/"

        appModel.setAppBarState(
            AppBarState.Regular(
                title = scenario.name,
                defaultActions
            )
        )

        return "/deck/${deck.id}/scenario/${scenario.id}"
    }

    override fun isMatching(path: String): Boolean {
        val split = path.split("/")

        if (split.size < 4) return false

        return split[1] == "deck" && split[3] == "scenario"
    }

    override fun navigateToStack() = NavigateToStack(
        popBackStack = false,
        options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.None
        )
    )

    override val asDefaultRoute = null

    private fun BackStackEntry.toHolder(decks: List<DeckModel>) = safeExecute {
        decks.firstOrNull { it.id == path<String>("uuid")!! }?.let { deck ->
            deck to deck.scenarios.firstOrNull { it.id == path<String>("scenario")!! }!!
        }
    }

    fun navigateTo(deck: DeckModel, scenario: Scenario) = NavigateTo(
        "/deck/${deck.id}/scenario/${scenario.id}",
        navigateToStack()
    )
}
