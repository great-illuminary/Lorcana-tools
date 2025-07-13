package eu.codlab.lorcana.blipya.home.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import eu.codlab.lorcana.blipya.home.BackgroundWrapper
import eu.codlab.lorcana.blipya.home.LocalNavigator
import eu.codlab.lorcana.blipya.home.drawer.DrawerContent
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.utils.LocalFrameProvider
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.isScreenExpanded
import eu.codlab.lorcana.blipya.widgets.BottomSpacer
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.Navigation
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun ColumnScope.ScaffoldContentWrapper(
    onMenuItemSelected: (title: String, navigateTo: NavigateTo) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val navigator = LocalNavigator.current

    var tinyMenuInPhablet = LocalWindow.current == WindowType.PHABLET

    val interactionSource = remember { MutableInteractionSource() }
    val menuSizeForPhablet = if (tinyMenuInPhablet) {
        42.dp
    } else {
        250.dp
    }

    val defaultImplementation = PossibleRoutes.fromPath(Navigation.originalPath()) ?: PossibleRoutes.Main

    println("defaultImplementation = $defaultImplementation")

    Row(modifier = Modifier.fillMaxSize()) {
        if (LocalWindow.current.isScreenExpanded()) {
            DrawerContent(
                modifier = Modifier
                    .padding()
                    .fillMaxHeight()
                    .width(menuSizeForPhablet),
                tiny = tinyMenuInPhablet,
                onMenuItemSelected = onMenuItemSelected
            )
        }

        LocalFrameProvider(
            modifier = Modifier.fillMaxSize()
        ) {
            BackgroundWrapper(
                modifier = Modifier.fillMaxSize().clickable(
                    interactionSource = interactionSource,
                    indication = null // this gets rid of the ripple effect
                ) {
                    if (currentPlatform == Platform.ANDROID ||
                        currentPlatform == Platform.IOS
                    ) {
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
                        initialRoute = defaultImplementation.impl.route,
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
