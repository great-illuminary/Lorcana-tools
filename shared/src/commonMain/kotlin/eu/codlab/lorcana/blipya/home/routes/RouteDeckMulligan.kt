package eu.codlab.lorcana.blipya.home.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.deck.mulligan.edit.EditMulligan
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.safeExecute
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.*
import kotlinx.serialization.Serializable

@Serializable
data class RouteDeckMulligan(
    val deck: String,
    val mulligan: String,
) : RouteParameterTo

object RouterDeckMulligan : Router<RouteDeckMulligan> {
    override val klass = RouteDeckMulligan::class

    fun navigateTo(deck: String, mulligan: String) = NavigateTo(
        route = RouteDeckMulligan(deck = deck, mulligan = mulligan),
        stack = NavigateToStack(
            popBackStack = false,
            options = NavigateWithNavOptions(
                launchSingleTop = false,
                popUpBackTo = RouteDeck(uuid = deck)
            )
        )
    )

    override fun navigateFrom(path: String) = extract(path).let { (deck, mulligan) ->
        RouteDeckMulligan(deck = deck, mulligan = mulligan)
    }

    private fun extract(path: String) = path.split("/").let {
        it[2] to it[4]
    }

    override fun route(navBackStackEntry: NavBackStackEntry) =
        RouteDeckMulliganImpl(navBackStackEntry.toRoute())

    override fun isMatching(route: String) =
        route.split("/").let {
            if (it.size < expectedSplitSize) return@let false
            it[indexDeck] == "deck" && it[indexMulligan] == "mulligan"
        }

    private val expectedSplitSize = 4
    private val indexDeck = 1
    private val indexMulligan = 3
}

class RouteDeckMulliganImpl(params: RouteDeckMulligan) : Route<RouteDeckMulligan>(
    route = "/deck/{uuid}/mulligan/{mulligan}",
    params = params,
) {
    override fun toPath() = route.replace("{uuid}", params.deck)
        .replace("{mulligan}", params.mulligan)

    @Composable
    override fun scene() {
        val appModel: AppModel = LocalApp.current
        val (deck, mulligan) = params.toHolder(appModel.states.value.decks) ?: return


        appModel.setAppBarState(
            AppBarState.Regular(
                title = mulligan.name,
                emptyList()
            )
        )

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

        // return "/deck/${deck.id}/mulligan/${mulligan.id}"
    }

    private fun RouteDeckMulligan.toHolder(decks: List<DeckModel>) = safeExecute {
        decks.firstOrNull { it.id == this.deck }?.let { deck ->
            deck to deck.mulligans.first { it.id == this.mulligan }
        }
    }
}
