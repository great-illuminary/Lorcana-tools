package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.deck.DeckConfiguration
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import eu.codlab.visio.design.appbar.FloatingActionButtonState
import kotlinx.serialization.Serializable

@Serializable
data class RouteDeck(
    val uuid: String,
) : RouteParameterTo

object RouterDeck : Router<RouteDeck> {
    override val klass = RouteDeck::class

    fun navigateTo(uuid: String) = NavigateTo(
        route = RouteDeck(uuid = uuid),
        stack = NavigateToStack(
            popBackStack = false,
            options = NavigateWithNavOptions(
                launchSingleTop = false,
                popUpBackTo = RouteMain
            )
        )
    )

    override fun route(navBackStackEntry: NavBackStackEntryWrapper) =
        RouteDeckImpl(navBackStackEntry.toRoute(RouteDeck::class))

    override fun isMatching(route: String) =
        route.split("/").let { it.size == 3 && it[1] == "deck" }

    private fun extract(path: String) = path.split("/")[2]

    override fun navigateFrom(path: String) = RouteDeck(uuid = extract(path))
}

class RouteDeckImpl(params: RouteDeck) : Route<RouteDeck>(
    route = "/deck/{uuid}",
    params = params,
) {
    override fun toPath() = route.replace("{uuid}", params.uuid)

    @Composable
    override fun scene() {
        val appModel: AppModel = LocalApp.current
        val deck = appModel.states.value.decks.firstOrNull { it.id == params.uuid } ?: return


        appModel.setAppBarState(
            AppBarState.Regular(
                title = deck.name,
                emptyList()
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
        // return "/deck/${deck.id}"
    }
}
