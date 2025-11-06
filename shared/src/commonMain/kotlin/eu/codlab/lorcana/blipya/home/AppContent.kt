package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
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
import eu.codlab.lorcana.blipya.home.routes.RouteMain
import eu.codlab.lorcana.blipya.home.routes.RouterDeck
import eu.codlab.lorcana.blipya.home.scaffold.FloatingActionButtonWrapper
import eu.codlab.lorcana.blipya.home.scaffold.TopBarWrapper
import eu.codlab.lorcana.blipya.icons.ManageAccounts
import eu.codlab.lorcana.blipya.init.InitializeScreen
import eu.codlab.lorcana.blipya.login.LoginScreen
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.isScreenExpanded
import eu.codlab.lorcana.blipya.widgets.MenuItem
import eu.codlab.lorcana.blipya.widgets.rememberSizeAwareScaffoldState
import eu.codlab.navigation.LocalNavigator
import eu.codlab.navigation.LocalNavigatorCanGoBack
import eu.codlab.navigation.LocalNavigatorNavigateTo
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
    val navigateTo = LocalNavigatorNavigateTo.current
    val navigator = LocalNavigator.current

    LaunchedEffect(scaffoldState) {
        scaffoldState.drawerState.close()
    }

    val model = LocalApp.current
    val viewScope = rememberCoroutineScope()

    DisposableEffect(navigator, model) {
        model.setNavigator(navigator)

        onDispose { model.setNavigator(null) }
    }

    LaunchedEffect(model, navigateTo, scaffoldState) {
        model.navigateTo = navigateTo
        model.closeDrawer = {
            viewScope.launch { scaffoldState.drawerState.close() }
        }
    }

    val currentState by model.states.collectAsState()

    val canGoBack = LocalNavigatorCanGoBack.current

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

    val onMenuItemSelected: (String, NavigateTo) -> Unit = { newTitle, navigateTo ->
        navigateTo(navigateTo)
        // model.shown(path.)
        // path.asDefaultRoute?.let { model.shown(it) }
    }

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
