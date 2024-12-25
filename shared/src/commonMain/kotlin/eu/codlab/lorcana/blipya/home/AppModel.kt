package eu.codlab.lorcana.blipya.home

import androidx.compose.material.ScaffoldState
import eu.codlab.files.VirtualFile
import eu.codlab.lorcana.Lorcana
import eu.codlab.lorcana.LorcanaLoaded
import eu.codlab.lorcana.blipya.account.Account
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.model.toDeck
import eu.codlab.lorcana.blipya.save.ConfigurationLoader
import eu.codlab.lorcana.blipya.save.SavedAuthentication
import eu.codlab.lorcana.blipya.utils.AuthentInit
import eu.codlab.lorcana.blipya.utils.RootPath
import eu.codlab.lorcana.blipya.widgets.AppBarState
import eu.codlab.lorcana.blipya.widgets.FloatingActionButtonState
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.math.Scenario
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import korlibs.io.util.UUID
import korlibs.time.DateTime
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

private const val Millis = 1000.0

data class AppModelState(
    var currentRoute: String,
    var initialized: Boolean = false,
    var loading: Boolean = false,
    val appBarState: AppBarState = AppBarState(),
    val floatingActionButtonState: FloatingActionButtonState? = null,
    val decks: List<DeckModel> = listOf(),
    val showPromptNewDeck: Boolean = false,
    val lorcana: LorcanaLoaded? = null,
    val authentication: SavedAuthentication? = null
)

@Suppress("TooManyFunctions")
data class AppModel(
    val appId: String,
    val appSecret: String
) : StateViewModel<AppModelState>(AppModelState("/main")) {
    var onBackPressed: AppBackPressProvider = AppBackPressProvider()

    private val accountClient = Account()
    private val configurationLoader = ConfigurationLoader(VirtualFile(RootPath, "Blipya"))
    private var activeDeck: DeckConfigurationModel? = null

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

        AuthentInit.initialize()

        val (decks, authentication) = configurationLoader.configuration.let { conf ->
            conf.decks.map { it.toDeck() } to conf.authentication
        }

        val lorcana = try {
            Lorcana().loadFromResources()
        } catch (err: Throwable) {
            err.printStackTrace()
            null
        }

        val validAuthent = authentication?.let {
            accountClient.checkAccount(it.token)
        } ?: false

        updateState {
            copy(
                initialized = true,
                decks = decks,
                lorcana = lorcana,
                authentication = if (validAuthent) authentication else null
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

    fun addDeck(deckName: String, onDeck: (DeckModel) -> Unit) = launch {
        val newDeck = DeckModel(
            Deck(
                id = UUID.randomUUID().toString(),
                name = deckName,
                deckSize = 0,
                defaultHand = 0
            )
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

    fun show(deck: DeckModel) {
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

    fun show(deck: DeckModel, scenario: Scenario) {
        val route = "/deck/${deck.id}/scenario/${scenario.id}"

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

    private suspend fun saveDecks(decks: List<DeckModel>) {
        configurationLoader.save(decks.map { it.toDeckModel() })
    }

    fun addScenario(deck: DeckModel) {
        val id = UUID.randomUUID().toString()
        activeDeck?.add(id) { show(deck, it) }
    }

    fun setActiveDeck(model: DeckConfigurationModel) {
        activeDeck = model
    }

    @Suppress("TooGenericExceptionCaught")
    fun login(token: String) = launch {
        try {
            val account = accountClient.login(token)

            val now = DateTime.now().add(0, account.expiresIn * Millis)
            val authentication = configurationLoader.save(account.token, now.unixMillisLong)

            updateState { copy(authentication = authentication) }
        } catch (err: Throwable) {
            // TODO manage network issue
            err.printStackTrace()
        }
    }
}
