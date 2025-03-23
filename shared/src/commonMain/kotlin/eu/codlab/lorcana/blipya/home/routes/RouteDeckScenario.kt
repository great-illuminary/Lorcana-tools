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
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeExecute
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import moe.tlaster.precompose.navigation.BackStackEntry
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

    override fun onEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    ) {
        val (_, scenario) = backStackEntry.toHolder(appModel.states.value.decks) ?: return

        appModel.setAppBarState(
            AppBarState.Regular(
                title = scenario.name,
                defaultActions
            )
        )
    }

    private fun BackStackEntry.toHolder(decks: List<DeckModel>) = safeExecute {
        decks.firstOrNull { it.id == path<String>("uuid")!! }?.let { deck ->
            deck to deck.scenarios.firstOrNull { it.id == path<String>("scenario")!! }!!
        }
    }
}
