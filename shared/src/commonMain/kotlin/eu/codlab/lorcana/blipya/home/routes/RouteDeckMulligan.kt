package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.deck.mulligan.edit.EditMulligan
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.home.navigate.NavigateToStack
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeExecute
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.lorcana.math.MulliganScenario
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

class RouteDeckMulligan : Route(
    "/deck/{uuid}/mulligan/{mulligan}",
    navTransition = NavTransition(),
    swipeProperties = SwipeProperties()
) {
    @Composable
    override fun scene(
        backStackEntry: BackStackEntry
    ) {
        val appModel: AppModel = LocalApp.current
        val (deck, mulligan) = backStackEntry.toHolder(appModel.states.value.decks) ?: return

        Column(
            modifier = Modifier.fillMaxSize()
                .defaultBackground()
                .verticalScroll(state = rememberScrollState())
        ) {
            EditMulligan(
                modifier = Modifier.fillMaxSize(),
                appModel,
                deck = deck,
                mulligan = mulligan
            )
        }
    }

    override fun onInternalEntryIsActive(
        appModel: AppModel,
        defaultActions: List<MenuItem>,
        backStackEntry: BackStackEntry
    ): String {
        val (deck, mulligan) = backStackEntry.toHolder(appModel.states.value.decks) ?: return "/"

        appModel.setAppBarState(
            AppBarState.Regular(
                title = mulligan.name,
                defaultActions
            )
        )

        return "/deck/${deck.id}/mulligan/${mulligan.id}"
    }

    override fun isMatching(path: String): Boolean {
        val split = path.split("/")

        if (split.size < 4) return false

        return split[1] == "deck" && split[3] == "mulligan"
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
            deck to deck.mulligans.firstOrNull { it.id == path<String>("mulligan")!! }!!
        }
    }

    fun navigateTo(deck: DeckModel, mulligan: MulliganScenario) = NavigateTo(
        "/deck/${deck.id}/mulligan/${mulligan.id}",
        navigateToStack()
    )
}
