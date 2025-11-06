package eu.codlab.lorcana.blipya.home.scaffold

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.LocalMenuState
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.isScreenExpanded
import eu.codlab.lorcana.blipya.widgets.TopAppBarExtended
import eu.codlab.navigation.LocalNavigator
import eu.codlab.navigation.LocalNavigatorCanGoBack
import kotlinx.coroutines.launch

@Composable
fun TopBarWrapper() {
    val scaffold = LocalMenuState.current
    val appModel = LocalApp.current
    val navigator = LocalNavigator.current

    val scope = rememberCoroutineScope()

    val state by appModel.states.collectAsState()
    val canGoBack = LocalNavigatorCanGoBack.current

    println("canGoBack $canGoBack")

    val isScreenExpanded = LocalWindow.current.isScreenExpanded()

    Surface(elevation = 8.dp) {
        TopAppBarExtended(
            title = state.appBarState.showTitle(),
            topSpacer = true,
            canGoBack = canGoBack,
            isScreenExpanded = isScreenExpanded,
            appModel = appModel
        ) {
            if (canGoBack) {
                navigator.navigateUp()
                return@TopAppBarExtended
            }

            scope.launch {
                scaffold.drawerState.let {
                    if (it.isOpen) {
                        it.close()
                    } else {
                        it.open()
                    }
                }
            }
        }
    }
}
