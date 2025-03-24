package eu.codlab.lorcana.blipya.home.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import eu.codlab.lorcana.blipya.home.BackgroundWrapper
import eu.codlab.lorcana.blipya.home.LocalNavigator
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.utils.LocalFrameProvider
import eu.codlab.lorcana.blipya.widgets.BottomSpacer
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun ColumnScope.ScaffoldContentWrapper() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val navigator = LocalNavigator.current

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
