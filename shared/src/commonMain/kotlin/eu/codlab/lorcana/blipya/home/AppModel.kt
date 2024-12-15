package eu.codlab.lorcana.blipya.home

import androidx.compose.material.ScaffoldState
import eu.codlab.files.VirtualFile
import eu.codlab.lorcana.blipya.save.ConfigurationLoader
import eu.codlab.lorcana.blipya.utils.RootPath
import eu.codlab.lorcana.blipya.utils.toDeck
import eu.codlab.lorcana.blipya.utils.toDeckModel
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.FloatingActionButtonState
import eu.codlab.lorcana.math.Deck
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import korlibs.io.util.UUID
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

data class AppModelState(
    var currentRoute: String,
    var initialized: Boolean = false,
    var loading: Boolean = false,
    val appBarState: AppBarState = AppBarState(),
    val floatingActionButtonState: FloatingActionButtonState? = null,
    val decks: List<Deck> = listOf(),
    val showPromptNewDeck: Boolean = false
)

@Suppress("TooManyFunctions")
data class AppModel(
    val appId: String,
    val appSecret: String
) : StateViewModel<AppModelState>(AppModelState("/main")) {
    var onBackPressed: AppBackPressProvider = AppBackPressProvider()

    private val configurationLoader = ConfigurationLoader(VirtualFile(RootPath, "Blipya"))

    var navigator: Navigator? = null
    var scaffoldState: ScaffoldState? = null

    companion object {
        fun fake(): AppModel {
            return AppModel("", "")
        }
    }

    fun isInitialized() = states.value.initialized

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun initialize() = launch {
        configurationLoader.init()

        val decks = configurationLoader.configuration.decks.map { it.toDeck() }

        updateState {
            copy(
                initialized = true,
                decks = decks
            )
        }
    }

    fun setAppBarState(appBarState: AppBarState) = launch {
        updateState {
            copy(appBarState = appBarState)
        }
    }

    fun setFloatingActionButton(floatingActionButtonState: FloatingActionButtonState) = launch {
        updateState {
            copy(floatingActionButtonState = floatingActionButtonState)
        }
    }

    fun showAddDeck(showPromptNewDeck: Boolean) = launch {
        updateState { copy(showPromptNewDeck = showPromptNewDeck) }
    }

    fun addDeck(deckName: String, onDeck: (Deck) -> Unit) = launch {
        val newDeck = Deck(
            id = UUID.randomUUID().toString(),
            name = deckName,
            deckSize = 0,
            defaultHand = 0
        )
        val decks = states.value.decks + newDeck
        saveDecks(decks)

        updateState {
            copy(decks = decks)
        }

        onDeck(newDeck)
    }

    fun popBackStack() {
        navigator?.popBackStack()
    }

    fun show(deck: Deck) {
        val route = "/deck/${deck.id}"

        popBackStack()

        navigator?.navigate(
            route = route,
            options = NavOptions(
                launchSingleTop = false,
                popUpTo = PopUpTo.None
            )
        )

        launch {
            scaffoldState?.drawerState?.close()
        }

        updateState {
            copy(
                currentRoute = route,
                appBarState = AppBarState(),
                floatingActionButtonState = null
            )
        }
    }

    fun floatingActionButton(): FloatingActionButtonState? {
        return states.value.floatingActionButtonState
    }

    fun saveDecks() = launch {
        saveDecks(states.value.decks)
    }

    private suspend fun saveDecks(decks: List<Deck>) {
        configurationLoader.save(decks.map { it.toDeckModel() })
    }
}
