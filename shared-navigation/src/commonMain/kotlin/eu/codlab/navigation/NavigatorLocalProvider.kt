package eu.codlab.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import eu.codlab.viewmodel.rememberViewModel

val LocalNavigator: ProvidableCompositionLocal<NavigatorModel> =
    compositionLocalOf { error("No LocalNavigator defined") }

@Composable
fun NavigatorLocalProvider(
    model: NavigationListener,
    content: @Composable () -> Unit
) {
    val navigator = rememberNavController()
    val navigatorModel = rememberViewModel { NavigatorModel() }

    DisposableEffect(navigator, navigatorModel) {
        navigatorModel.setNavigator(navigator)
        navigatorModel.navigationListener = model

        onDispose { navigatorModel.setNavigator(null) }
    }

    CompositionLocalProvider(
        LocalNavigator provides navigatorModel,
        content = content
    )
}