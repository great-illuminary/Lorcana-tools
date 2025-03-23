package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.StatusBarAndNavigation
import eu.codlab.lorcana.blipya.deck.PromptForNewScenarioOrMulligan
import eu.codlab.lorcana.blipya.decks.PromptForNewDeck
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.init.InitializeScreen
import eu.codlab.lorcana.blipya.login.LoginScreen
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.LocalFrameProvider
import eu.codlab.lorcana.blipya.widgets.BottomSpacer
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.TopAppBarExtended
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.lorcana.blipya.widgets.rememberSizeAwareScaffoldState
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform
import eu.codlab.safearea.views.SafeArea
import eu.codlab.safearea.views.SafeAreaBehavior
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

val LocalMenuState: ProvidableCompositionLocal<ScaffoldState?> =
    compositionLocalOf { null }

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun AppContent() {
    StatusBarAndNavigation()

    val scaffoldState = rememberSizeAwareScaffoldState()
    val scope = rememberCoroutineScope()
    val navigator = rememberNavigator("AppContent")
    val canGoBack by navigator.canGoBack.collectAsState(initial = false)

    LaunchedEffect(scaffoldState) {
        scaffoldState.drawerState.close()
    }

    val model = LocalApp.current

    LaunchedEffect(model, navigator, scaffoldState) {
        model.navigator = navigator
        model.scaffoldState = scaffoldState
    }

    val currentState by model.states.collectAsState()

    /*val isScreenExpanded = when (LocalWindow.current) {
        WindowType.SMARTPHONE_TINY -> false
        WindowType.SMARTPHONE -> false
        WindowType.PHABLET -> true
        WindowType.TABLET -> true
    }*/

    val currentEntry by navigator.currentEntry.collectAsState(null)
    val backStackCount by navigator.backStackCount.collectAsState(0)
    println("currentEntry $backStackCount -> ${currentEntry?.route?.route}")

    if (!currentState.initialized) {
        InitializeScreen(
            LocalApp.current,
            Modifier.fillMaxSize()
        )
        return
    }

    val actions = listOf(
        MenuItem.MenuItemOverflowMenu(
            imageVector = Icons.Outlined.ManageAccounts,
            contentDescription = "Login"
        ) {
            LoginScreen(
                Modifier.widthIn(
                    AppSizes.overflowMenu.minWidth,
                    AppSizes.overflowMenu.maxWidth
                ),
                model
            )
        }
    )

    LaunchedEffect(currentEntry) {
        val entry = currentEntry ?: return@LaunchedEffect

        val route = PossibleRoutes.fromRoute(entry.route.route)
        route?.impl?.onEntryIsActive(model, actions, entry)
    }

    PromptForNewDeck(
        model,
        showPrompt = currentState.showPromptNewDeck,
        onDismiss = { model.showAddDeck(false) }
    ) { model.show(NavigateTo.Deck(it)) }

    println("AppContent redraw with ${currentState.showPromptNewScenario}")
    PromptForNewScenarioOrMulligan(
        model,
        showPrompt = currentState.showPromptNewScenario
    ) { model.showAddScenario(false) }

    CompositionLocalProvider(
        LocalMenuState provides scaffoldState
    ) {
        SafeArea(
            SafeAreaBehavior(
                extendToTop = true,
                extendToBottom = true,
                extendToStart = true,
                extendToEnd = true
            )
        ) {
            Column {
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        val scaffold = LocalMenuState.current
                        val appModel = LocalApp.current
                        val state by appModel.states.collectAsState()

                        Surface(elevation = 8.dp) {
                            TopAppBarExtended(
                                title = state.appBarState.showTitle(),
                                topSpacer = true,
                                canGoBack = canGoBack,
                                // isScreenExpanded = isScreenExpanded,
                                appModel = model
                            ) {
                                if (canGoBack) {
                                    navigator.goBack()
                                    return@TopAppBarExtended
                                }
                                scope.launch {
                                    scaffold?.drawerState?.let {
                                        if (it.isOpen) {
                                            it.close()
                                        } else {
                                            it.open()
                                        }
                                    }
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        Column {
                            val state by LocalApp.current.states.collectAsState()

                            state.floatingActionButtonState?.let {
                                FloatingActionButton(
                                    onClick = it.action
                                ) {
                                    Icon(
                                        imageVector = it.icon,
                                        contentDescription = it.contentDescription
                                    )
                                }

                                BottomSpacer()
                            }
                        }
                    }
                ) {
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val focusManager = LocalFocusManager.current
                    val interactionSource = remember { MutableInteractionSource() }
                    LocalFrameProvider(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        BackgroundWrapper(
                            modifier = Modifier.fillMaxSize().clickable(
                                interactionSource = interactionSource,
                                indication = null // this gets rid of the ripple effect
                            ) {
                                if (currentPlatform != Platform.JVM) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus(true)
                                }
                            }
                        ) {
                            Column(modifier = Modifier.weight(1f).defaultBackground()) {
                                NavHost(
                                    modifier = Modifier.weight(1f).defaultBackground(),
                                    // Assign the navigator to the NavHost
                                    navigator = navigator,
                                    // Navigation transition for the scenes in this NavHost, this is optional
                                    navTransition = NavTransition(),
                                    // The start destination
                                    initialRoute = "/main",
                                    /*swipeProperties = SwipeProperties(
                                        //spaceToSwipe = 50.dp
                                    )*/
                                ) {
                                    PossibleRoutes.entries.forEach {
                                        scene(
                                            route = it.impl.route,
                                            navTransition = it.impl.navTransition,
                                            swipeProperties = it.impl.swipeProperties
                                        ) { backStackEntry -> it.impl.scene(backStackEntry) }
                                    }
                                }

                                BottomSpacer()
                            }
                        }
                    }
                }
            }
        }
    }
}
