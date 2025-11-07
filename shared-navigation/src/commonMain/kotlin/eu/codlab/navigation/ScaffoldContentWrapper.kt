package eu.codlab.navigation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.codlab.lorcana.blipya.utils.LocalFrameProvider
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.isScreenExpanded
import eu.codlab.lorcana.blipya.widgets.BottomSpacer

@Composable
fun ColumnScope.ScaffoldContentWrapper(
    model: NavigationListener,
    possibleRoutes: List<Router<*>>,
    defaultRoute: RouteParameterTo,
    onMenuItemSelected: (title: String, navigateTo: NavigateTo) -> Unit,
    drawerContent: @Composable (
        modifier: Modifier,
        onMenuItemSelected: (title: String, navigateTo: NavigateTo) -> Unit,
        tiny: Boolean,
    ) -> Unit,
) {
    val navigator = LocalNavigator.current
    val tinyMenuInPhablet = LocalWindow.current == WindowType.PHABLET
    val interactionSource = remember { MutableInteractionSource() }
    val menuSizeForPhablet = if (tinyMenuInPhablet) {
        42.dp
    } else {
        250.dp
    }

    val actualNavigator = navigator.navigator ?: return

    Row(modifier = Modifier.fillMaxSize()) {
        // no drawer content just for now
        if (LocalWindow.current.isScreenExpanded()) {
            drawerContent(
                Modifier
                    .padding()
                    .fillMaxHeight()
                    .width(menuSizeForPhablet),
                onMenuItemSelected,
                tinyMenuInPhablet,
            )
        }

        LocalFrameProvider(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                NavHost(
                    navController = actualNavigator,
                    startDestination = defaultRoute,
                    modifier = Modifier.fillMaxSize().imePadding()
                ) {
                    possibleRoutes.map { router ->
                        composable(router.klass) { entry ->
                            router.route(NavBackStackEntryWrapper(entry)).let { route ->
                                Navigation.setPath(route.toPath())
                                route.scene()
                            }
                        }
                    }
                }

                BottomSpacer()
            }
        }
    }
}
