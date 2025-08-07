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
import androidx.compose.ui.unit.dp
import eu.codlab.lorcana.blipya.home.BackgroundWrapper
import eu.codlab.lorcana.blipya.home.LocalNavigator
import eu.codlab.lorcana.blipya.home.drawer.DrawerContent
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.home.routes.Route
import eu.codlab.lorcana.blipya.utils.LocalFrameProvider
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.isMobile
import eu.codlab.lorcana.blipya.utils.isScreenExpanded
import eu.codlab.lorcana.blipya.widgets.BottomSpacer
import eu.codlab.lorcana.blipya.widgets.defaultBackground
import eu.codlab.navigation.Navigation
import eu.codlab.platform.currentPlatform
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun ColumnScope.ScaffoldContentWrapper(
    onMenuItemSelected: (title: String, navigateTo: Route) -> Unit
) {
    val navigator = LocalNavigator.current
    val tinyMenuInPhablet = LocalWindow.current == WindowType.PHABLET
    val interactionSource = remember { MutableInteractionSource() }
    val menuSizeForPhablet = if (tinyMenuInPhablet) {
        42.dp
    } else {
        250.dp
    }

    val defaultImplementation = PossibleRoutes.fromPath(Navigation.originalPath()) ?: PossibleRoutes.Main

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
                ) { if (!currentPlatform.isMobile) return@clickable }
            ) {
                Column(modifier = Modifier.weight(1f).defaultBackground()) {
                    NavHost(
                        modifier = Modifier.weight(1f).defaultBackground(),
                        // Assign the navigator to the NavHost
                        navigator = navigator,
                        // Navigation transition for the scenes in this NavHost, this is optional
                        navTransition = NavTransition(),
                        // The start destination
                        initialRoute = defaultImplementation.asDefaultRoute!!.route,
                    ) {
                        PossibleRoutes.entries.forEach {
                            scene(
                                route = it.route,
                                navTransition = it.navTransition,
                                swipeProperties = it.swipeProperties
                            ) { backStackEntry -> it.scene(backStackEntry) }
                        }
                    }

                    BottomSpacer()
                }
            }
        }
    }
}
