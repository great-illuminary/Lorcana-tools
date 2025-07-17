package eu.codlab.lorcana.blipya.rph.map.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.rph.map.map.MapOverlay
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceAction
import eu.codlab.viewmodel.rememberViewModel
import ovh.plrapps.mapcompose.ui.MapUI

@Composable
fun <T, S : MapModelState, H> RphMap(
    modifier: Modifier,
    appModel: AppModel,
    createModel: (UriHandler) -> T
) where T : MapInterfaceAction,
        T : MapModel<S, H> {
    val uriHandler = LocalUriHandler.current
    val model = rememberViewModel { createModel(uriHandler) }

    val state by model.states.collectAsState()

    Box(modifier) {
        if (!state.initialized) {
            println("not initialized")
            return@Box
        }
        MapUI(
            modifier = modifier,
            state = model.mapState
        )

        MapOverlay(
            modifier = Modifier.fillMaxSize(),
            model
        )
    }
}