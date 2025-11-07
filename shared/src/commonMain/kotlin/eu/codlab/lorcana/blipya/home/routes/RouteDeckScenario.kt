package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.deck.scenario.edit.EditScenario
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeExecute
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
data class RouteDeckScenario(
    val deck: String,
    val scenario: String,
) : RouteParameterTo

object RouterDeckScenario : Router<RouteDeckScenario> {
    override val klass = RouteDeckScenario::class

    fun navigateTo(deck: String, scenario: String) = NavigateTo(
        route = RouteDeckScenario(deck = deck, scenario = scenario),
        stack = NavigateToStack(
            popBackStack = false,
            options = NavigateWithNavOptions(
                launchSingleTop = false,
                popUpBackTo = RouteDeck(uuid = deck)
            )
        )
    )

    override fun route(navBackStackEntry: NavBackStackEntryWrapper) =
        RouteDeckScenarioImpl(navBackStackEntry.toRoute(RouteDeckScenario::class))

    override fun navigateFrom(path: String) = extract(path).let { (deck, scenario) ->
        RouteDeckScenario(deck = deck, scenario = scenario)
    }

    private fun extract(path: String) = path.split("/").let {
        it[2] to it[4]
    }

    override fun isMatching(route: String) =
        route.split("/").let {
            if (it.size < expectedSplitSize) return@let false
            it[indexDeck] == "deck" && it[indexScenario] == "scenario"
        }

    private val expectedSplitSize = 4
    private val indexDeck = 1
    private val indexScenario = 3
}

class RouteDeckScenarioImpl(params: RouteDeckScenario) :
    Route<RouteDeckScenario>(
        route = "/deck/{uuid}/scenario/{scenario}",
        params = params,
    ) {
    override fun toPath() = route.replace("{uuid}", params.deck)
        .replace("{secnario}", params.scenario)

    @Composable
    override fun scene() {
        val appModel: AppModel = LocalApp.current
        val (deck, scenario) = params.toHolder(appModel.states.value.decks) ?: return

        appModel.setAppBarState(
            AppBarState.Regular(
                title = scenario.name,
                emptyList()
            )
        )

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

        // return "/deck/${deck.id}/scenario/${scenario.id}"
    }

    private fun RouteDeckScenario.toHolder(decks: List<DeckModel>) = safeExecute {
        decks.firstOrNull { it.id == this.deck }?.let { deck ->
            deck to deck.scenarios.firstOrNull { it.id == this.scenario }!!
        }
    }
}
