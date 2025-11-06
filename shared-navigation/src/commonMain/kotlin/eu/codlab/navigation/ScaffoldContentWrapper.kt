package eu.codlab.visio.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eu.codlab.visio.design.theme.LocalApplicationColorTheme
import eu.codlab.visio.design.utils.LocalFrameProvider
import eu.codlab.visio.design.utils.LocalWindow
import eu.codlab.visio.design.utils.WindowType
import eu.codlab.visio.design.widgets.BottomSpacer

@Composable
fun ColumnScope.ScaffoldContentWrapper(
    model: NavigationListener,
    possibleRoutes: List<Route>,
    defaultRoute: Route,
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

    val navigateTo = LocalNavigatorNavigateTo.current

    Row(modifier = Modifier.fillMaxSize()) {
        // no drawer content just for now
        /*if (LocalWindow.current.isScreenExpanded()) {
            DrawerContent(
                modifier = Modifier
                    .padding()
                    .fillMaxHeight()
                    .width(menuSizeForPhablet),
                tiny = tinyMenuInPhablet,
                onMenuItemSelected = onMenuItemSelected
            )
        }*/

        LocalFrameProvider(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                NavHost(
                    navController = navigator,
                    startDestination = defaultRoute,
                    modifier = Modifier.fillMaxSize().imePadding().background(
                        LocalApplicationColorTheme.current.background.fragment
                    )
                ) {
                    /*
                    using the (route::class) { composable }
                    instead of composable<T>{ } for extendability
                     */
                    possibleRoutes.map { route ->
                        composable(route::class) {
                            route.scene(navigateTo) // TODO pass it
                        }
                    }
                }

                BottomSpacer()
            }
        }
    }
}
