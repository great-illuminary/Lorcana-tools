package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.DrawerDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import eu.codlab.compose.widgets.StatusBarAndNavigation
import eu.codlab.lorcana.blipya.deck.PromptForNewScenarioOrMulligan
import eu.codlab.lorcana.blipya.decks.PromptForNewDeck
import eu.codlab.lorcana.blipya.home.drawer.DrawerContent
import eu.codlab.lorcana.blipya.home.drawer.DrawerSizeShape
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.home.routes.RouterDeck
import eu.codlab.lorcana.blipya.home.scaffold.FloatingActionButtonWrapper
import eu.codlab.lorcana.blipya.home.scaffold.TopBarWrapper
import eu.codlab.lorcana.blipya.init.InitializeScreen
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.isScreenExpanded
import eu.codlab.lorcana.blipya.widgets.rememberSizeAwareScaffoldState
import eu.codlab.navigation.LocalNavigator
import eu.codlab.navigation.NavigateTo
import eu.codlab.navigation.Navigation
import eu.codlab.navigation.ScaffoldContentWrapper
import eu.codlab.safearea.views.SafeArea
import eu.codlab.safearea.views.SafeAreaBehavior
import korlibs.io.async.launch

val LocalMenuState: ProvidableCompositionLocal<ScaffoldState> =
    compositionLocalOf { throw IllegalStateException("no LocalMenuState") }

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun AppContent() {
    StatusBarAndNavigation()

    val scaffoldState = rememberSizeAwareScaffoldState()
    val navigator = LocalNavigator.current
    val navigatorState by navigator.states.collectAsState()

    LaunchedEffect(scaffoldState) {
        scaffoldState.drawerState.close()
    }

    val model = LocalApp.current
    val viewScope = rememberCoroutineScope()

    LaunchedEffect(model, navigator, scaffoldState) {
        model.navigator = navigator
        model.closeDrawer = {
            viewScope.launch { scaffoldState.drawerState.close() }
        }
    }

    LaunchedEffect(navigatorState.currentNavBackStackEntry) {
        val entry = navigatorState.currentNavBackStackEntry
        if (null == entry) return@LaunchedEffect

        PossibleRoutes.entries.firstOrNull {
            try {
                it.route(entry)
                true
            } catch (_: Throwable) {
                false
            }
        }?.let { router ->
            Navigation.setPath(router.route(entry).toPath())
        }
    }

    val currentState by model.states.collectAsState()

    val canGoBack = navigatorState.canGoBack

    if (!currentState.initialized) {
        InitializeScreen(
            LocalApp.current,
            Modifier.fillMaxSize()
        )
        return
    }

    /*val actions = listOf(
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
    )*/

    val isScreenExpanded = LocalWindow.current.isScreenExpanded()

    val onMenuItemSelected: (String, NavigateTo) -> Unit = { _, navigateTo -> navigator.navigateTo(navigateTo) }

    PromptForNewDeck(
        model,
        showPrompt = currentState.showPromptNewDeck,
        onDismiss = { model.showAddDeck(false) }
    ) { model.show(RouterDeck.navigateTo(it.id)) }

    PromptForNewScenarioOrMulligan(
        model,
        showPrompt = currentState.showPromptNewScenario
    ) { model.showAddScenario(false) }


    CompositionLocalProvider(
        LocalMenuState provides scaffoldState,
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
                    drawerScrimColor = if (!isScreenExpanded) {
                        DrawerDefaults.scrimColor
                    } else {
                        Color.Transparent
                    },
                    drawerContent = {
                        DrawerContent(
                            modifier = Modifier.fillMaxSize(),
                            onMenuItemSelected = onMenuItemSelected
                        )
                    },
                    drawerGesturesEnabled = !canGoBack && !isScreenExpanded, // Gestures are enabled only on smaller and medium screens
                    drawerShape = DrawerSizeShape(),
                    topBar = { TopBarWrapper() },
                    floatingActionButton = { FloatingActionButtonWrapper() },
                    content = {
                        ScaffoldContentWrapper(
                            model,
                            PossibleRoutes.entries,
                            currentState.originalAndDefaultRoute,
                            onMenuItemSelected = onMenuItemSelected,
                        ) { modifier, onMenuItemSelected, tiny ->
                            DrawerContent(
                                modifier,
                                tiny,
                                onMenuItemSelected,
                            )
                        }
                    }
                )
            }
        }
    }
}
