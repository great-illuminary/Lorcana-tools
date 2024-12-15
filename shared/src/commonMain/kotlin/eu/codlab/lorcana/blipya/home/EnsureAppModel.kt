package eu.codlab.lorcana.blipya.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import eu.codlab.lorcana.blipya.init.InitializeScreen

@Composable
fun EnsureAppModel(
    content: @Composable (appModel: AppModel) -> Unit
) {
    val appModel = LocalApp.current
    val state by appModel.states.collectAsState()

    LaunchedEffect(state) {
        if (!state.initialized) {
            appModel.initialize()
        }
    }

    if (!appModel.isInitialized()) {
        InitializeScreen(globalApp = appModel)
    } else {
        content(appModel)
    }
}
