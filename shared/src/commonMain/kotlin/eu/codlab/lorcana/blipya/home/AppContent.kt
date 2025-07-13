package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DrawerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import eu.codlab.compose.widgets.StatusBarAndNavigation
import eu.codlab.lorcana.blipya.deck.PromptForNewScenarioOrMulligan
import eu.codlab.lorcana.blipya.decks.PromptForNewDeck
import eu.codlab.lorcana.blipya.home.drawer.DrawerContent
import eu.codlab.lorcana.blipya.home.drawer.DrawerSizeShape
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.home.routes.Route
import eu.codlab.lorcana.blipya.home.scaffold.FloatingActionButtonWrapper
import eu.codlab.lorcana.blipya.home.scaffold.ScaffoldContentWrapper
import eu.codlab.lorcana.blipya.home.scaffold.TopBarWrapper
import eu.codlab.lorcana.blipya.icons.ManageAccounts
import eu.codlab.lorcana.blipya.init.InitializeScreen
import eu.codlab.lorcana.blipya.login.LoginScreen
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.isScreenExpanded
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.rememberSizeAwareScaffoldState
import eu.codlab.safearea.views.SafeArea
import eu.codlab.safearea.views.SafeAreaBehavior
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

val LocalMenuState: ProvidableCompositionLocal<ScaffoldState> =
    compositionLocalOf { error("No LocalMenuState defined") }
val LocalNavigator: ProvidableCompositionLocal<Navigator> =
    compositionLocalOf { error("No LocalNavigator defined") }

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun AppContent() {
    StatusBarAndNavigation()

    val scaffoldState = rememberSizeAwareScaffoldState()
    val navigator = rememberNavigator("AppContent")

    LaunchedEffect(scaffoldState) {
        scaffoldState.drawerState.close()
    }

    val model = LocalApp.current

    LaunchedEffect(model, navigator, scaffoldState) {
        model.navigator = navigator
        model.scaffoldState = scaffoldState
    }

    val currentState by model.states.collectAsState()

    val canGoBack by navigator.canGoBack.collectAsState(initial = false)
    val currentEntry by navigator.currentEntry.collectAsState(null)
    val backStackCount by navigator.backStackCount.collectAsState(0)

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

    val isScreenExpanded = LocalWindow.current.isScreenExpanded()

    val onMenuItemSelected: (String, Route) -> Unit = { newTitle, path ->
        path.asDefaultRoute?.let { model.show(it) }
    }

    LaunchedEffect(currentEntry) {
        val entry = currentEntry ?: return@LaunchedEffect

        val route = PossibleRoutes.fromRoute(entry.route.route)
        route?.onEntryIsActive(model, actions, entry)
    }

    PromptForNewDeck(
        model,
        showPrompt = currentState.showPromptNewDeck,
        onDismiss = { model.showAddDeck(false) }
    ) { model.show(PossibleRoutes.Deck.navigateTo(it)) }

    println("AppContent redraw with ${currentState.showPromptNewScenario}")
    PromptForNewScenarioOrMulligan(
        model,
        showPrompt = currentState.showPromptNewScenario
    ) { model.showAddScenario(false) }

    CompositionLocalProvider(
        LocalMenuState provides scaffoldState,
        LocalNavigator provides navigator
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
                            onMenuItemSelected = onMenuItemSelected
                        )
                    }
                )
            }
        }
    }
}
