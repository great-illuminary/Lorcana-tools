package eu.codlab.lorcana.blipya.home

import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import eu.codlab.blipya.config.SharedConfig
import eu.codlab.files.VirtualFile
import eu.codlab.lorcana.Lorcana
import eu.codlab.lorcana.LorcanaLoaded
import eu.codlab.lorcana.blipya.account.Account
import eu.codlab.lorcana.blipya.appbar.AppBarState
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.home.routes.RouteMain
import eu.codlab.lorcana.blipya.home.routes.RouterDeckMulligan
import eu.codlab.lorcana.blipya.home.routes.RouterDeckScenario
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.model.toDeck
import eu.codlab.lorcana.blipya.save.ConfigurationLoader
import eu.codlab.lorcana.blipya.save.RavensburgerPlayHubUser
import eu.codlab.lorcana.blipya.save.SavedAuthentication
import eu.codlab.lorcana.blipya.utils.*
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.raw.VariantClassification
import eu.codlab.lorcana.raw.VirtualCard
import eu.codlab.navigation.*
import eu.codlab.sentry.wrapper.Sentry
import eu.codlab.viewmodel.StateViewModel
import eu.codlab.viewmodel.launch
import eu.codlab.visio.design.appbar.AppBarStateProvider
import eu.codlab.visio.design.appbar.AppBarUiState
import eu.codlab.visio.design.appbar.FloatingActionButtonState
import io.ktor.websocket.*
import korlibs.io.util.UUID
import korlibs.time.DateTime
import kotlinx.coroutines.async
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val Millis = 1000.0

data class AppModelState(
    var initialized: Boolean = false,
    var loading: Boolean = false,
    override val appBarState: AppBarState = AppBarState.Regular(),
    val floatingActionButtonState: FloatingActionButtonState? = null,
    val decks: List<DeckModel> = listOf(),
    val showPromptNewDeck: Boolean = false,
    val showPromptNewScenario: Boolean = false,
    val lorcana: LorcanaLoaded? = null,
    val mapDreamborn: Map<String, Pair<VirtualCard, VariantClassification>> = mutableMapOf(),
    val authentication: SavedAuthentication? = null,
    val requestForGoogleAuthenticationState: String? = null,
    val ravensburgerPlayHubUser: RavensburgerPlayHubUser? = null,
    val originalAndDefaultRoute: RouteParameterTo = RouteMain,
) : AppBarUiState

@Suppress("TooManyFunctions")
data class AppModel(
    val appId: String,
    val appSecret: String
) : StateViewModel<AppModelState>(AppModelState()),
    NavigationListener,
    // IRequestForUrlToOpen,
    AppBarStateProvider<AppModelState> {
    var onBackPressed: AppBackPressProvider = AppBackPressProvider()

    private val accountClient = Account()
    private val configurationLoader = ConfigurationLoader(VirtualFile(RootPath, "Blipya"))
    private var activeDeck: DeckConfigurationModel? = null
    private var json = Json
    private val backendSocket = accountClient.createSocket()

    var closeDrawer: () -> Unit = { /* nothing to do */ }

    companion object {
        fun fake() = AppModel("", "")
    }

    var navigator: NavigatorModel? = null

    fun isInitialized() = states.value.initialized

    fun initialize() {
        Sentry.init {
            it.dsn = SharedConfig.sentryDsn
        }

        safeLaunch {
            configurationLoader.init()

            GoogleAuthProvider.create(
                credentials = GoogleAuthCredentials(
                    serverId = SharedConfig.googleAuthServerId
                )
            )

            // AuthentInit.initialize()
            Firebase.initialize()

            Firebase.logEvent("app_initialized")

            val path = Navigation.originalPath()
            val originalAndDefaultRoute = PossibleRoutes.defaultFromUrlLaunched(path)

            val (decks, authentication) = configurationLoader.configuration.let { conf ->
                conf.decks.map { it.toDeck() } to conf.authentication
            }

            val lorcana = safeSuspend { Lorcana().loadFromResources() }

            val validAuthent = authentication?.let {
                accountClient.checkAccount(it.token)
            } == true

            val mapDreamborn = mutableMapOf<String, Pair<VirtualCard, VariantClassification>>()

            lorcana?.cards?.forEach { card ->
                card.variants.forEach {
                    mapDreamborn.put(it.dreamborn, card to it)
                }
            }

            updateState {
                copy(
                    initialized = true,
                    decks = decks,
                    lorcana = lorcana,
                    mapDreamborn = mapDreamborn,
                    originalAndDefaultRoute = originalAndDefaultRoute,
                    authentication = if (validAuthent) authentication else null,
                    ravensburgerPlayHubUser = configurationLoader.configuration.rphUser
                )
            }

            onReceiveMessage()
        }
    }

    @Suppress("SwallowedException", "TooGenericExceptionCaught")
    private fun onReceiveMessage() = launch {
        async {
            backendSocket.incoming.collect {
                try {
                    val googleToken = json.decodeFromString(
                        SocketMessage.serializer(
                            BackendGoogleCookie.serializer()
                        ),
                        it.readText()
                    ).message

                    saveTokenFromBackend(googleToken.token, googleToken.expiresIn)
                } catch (err: Throwable) {
                    // nothing for now
                }
            }
        }
    }

    override fun setAppBarState(appBarState: AppBarState) {
        safeLaunch {
            updateState {
                copy(appBarState = appBarState)
            }
        }
    }

    fun setFloatingActionButton(floatingActionButtonState: FloatingActionButtonState) = safeLaunch {
        updateState {
            copy(floatingActionButtonState = floatingActionButtonState)
        }
    }

    fun showAddDeck(showPromptNewDeck: Boolean) = safeLaunch {
        updateState { copy(showPromptNewDeck = showPromptNewDeck) }
    }

    fun showAddScenario(showPromptNewScenario: Boolean) = safeLaunch {
        updateState { copy(showPromptNewScenario = showPromptNewScenario) }
    }

    fun addDeck(deckName: String, onDeck: (DeckModel) -> Unit) = safeLaunch {
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

    fun show(navigateTo: NavigateTo) {
        this.navigator?.navigateTo(navigateTo)
    }

    override fun shown(navigateTo: NavigateTo) {
        closeDrawer()

        updateState {
            copy(
                appBarState = AppBarState.Regular(),
                floatingActionButtonState = null
            )
        }
    }

    val floatingActionButton: FloatingActionButtonState?
        get() {
            return states.value.floatingActionButtonState
        }

    fun saveDecks() = safeLaunch {
        saveDecks(states.value.decks)
    }

    private suspend fun saveDecks(decks: List<DeckModel>) {
        configurationLoader.save(decks.map { it.toDeckModel() })
    }

    fun addScenario() {
        val id = UUID.randomUUID().toString()
        activeDeck?.add(id) { deck, scenario ->
            show(RouterDeckScenario.navigateTo(deck.id, scenario.id))
        }
    }

    fun addMulligan() {
        val id = UUID.randomUUID().toString()
        activeDeck?.addMulligan(id) { deck, mulligan ->
            show(RouterDeckMulligan.navigateTo(deck.id, mulligan.id))
        }
    }

    fun setActiveDeck(model: DeckConfigurationModel) {
        activeDeck = model
    }

    @Suppress("TooGenericExceptionCaught")
    fun login(token: String) = safeLaunch {
        try {
            val account = accountClient.login(token)

            saveTokenFromBackend(account.token, account.expiresIn)
        } catch (err: Throwable) {
            // TODO manage network issue
            err.printStackTrace()
        }
    }

    private suspend fun saveTokenFromBackend(token: String, expiresIn: Long) {
        val now = DateTime.now().add(0, expiresIn * Millis)
        val authentication = configurationLoader.save(token, now.unixMillisLong)

        updateState { copy(authentication = authentication) }
    }

    fun saveRavensburgerPlayHubSelection(username: String, id: Long) = launch {
        val ravensburgerPlayHubUser = configurationLoader.saveRavensburgerPlayHub(username, id)

        updateState { copy(ravensburgerPlayHubUser = ravensburgerPlayHubUser) }
    }

    /*override suspend fun requestForUrlToOpen(provider: String): String? {
        val id = DateTime.now().unixMillisLong
        val state = UUID.randomUUID().toString()
        val obj = SocketMessage(id, RequestForUrlToOpen(provider, state))
        val serializerIn = SocketMessage.serializer(RequestForUrlToOpen.serializer())
        backendSocket.emit(obj, serializerIn)

        updateState {
            copy(
                requestForGoogleAuthenticationState = state
            )
        }

        return backendSocket.waitForSocket(id, Constants.backendTimeout, ResultForUrlToOpen.serializer())?.url
    }*/

    fun cardFromDreamborn(dreambornId: String): Pair<VirtualCard, VariantClassification>? {
        val pair = states.value.mapDreamborn[dreambornId]

        if (null != pair) return pair

        var foundVariant: VariantClassification? = null
        val card = states.value.lorcana?.cards?.find { card ->
            val variant = card.variants.firstOrNull { variant -> variant.dreamborn == dreambornId }

            if (null != variant) foundVariant = variant
            null != variant
        }

        return if (null != card && null != foundVariant) {
            card to foundVariant!!
        } else {
            null
        }
    }

    fun disconnect(onDone: () -> Unit) = safeLaunch {
        configurationLoader.disconnect()

        updateState { copy(authentication = null) }
        onDone()
    }

    fun delete(onDone: () -> Unit) = safeLaunch {
        configurationLoader.disconnect()

        updateState { copy(authentication = null) }
        onDone()
    }
}

@Serializable
data class RequestForUrlToOpen(
    val request: String,
    val state: String
)

@Serializable
data class BackendGoogleCookie(
    val provider: String,
    val token: String,
    @SerialName("expires_in")
    val expiresIn: Long
)

@Serializable
data class ResultForUrlToOpen(
    val url: String
)

@Serializable
data class SocketMessage<T>(
    val id: Long,
    val message: T
)
