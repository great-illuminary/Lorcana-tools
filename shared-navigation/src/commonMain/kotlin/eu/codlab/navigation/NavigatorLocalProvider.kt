package eu.codlab.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

val LocalNavigator: ProvidableCompositionLocal<NavHostController> =
    compositionLocalOf { error("No LocalNavigator defined") }

val LocalNavigatorCanGoBack: ProvidableCompositionLocal<Boolean> =
    compositionLocalOf { error("No LocalNavigatorCanGoBack defined") }
val LocalNavigatorNavigateUp: ProvidableCompositionLocal<() -> Boolean> =
    compositionLocalOf { error("No LocalNavigatorNavigateUp defined") }

val LocalNavigatorNavigateTo: ProvidableCompositionLocal<(NavigateTo) -> Unit> =
    compositionLocalOf { error("No LocalNavigatorNavigateTo defined") }

@Composable
fun NavigatorLocalProvider(
    model: NavigationListener,
    content: @Composable () -> Unit
) {
    val navigator = rememberNavController()
    // Get current back stack entry
    val backStackEntry by navigator.currentBackStackEntryAsState()
    var canGoBack by remember { mutableStateOf(navigator.previousBackStackEntry != null) }

    val goBack = { navigator.navigateUp() }

    val navigateTo = { navigateTo: NavigateTo ->
        navigateTo(navigator, navigateTo)
        model.shown(navigateTo)
    }

    LaunchedEffect(backStackEntry) {
        canGoBack = navigator.previousBackStackEntry != null
    }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalNavigatorCanGoBack provides canGoBack,
        LocalNavigatorNavigateUp provides goBack,
        LocalNavigatorNavigateTo provides navigateTo,
    ) {
        content()
    }
}