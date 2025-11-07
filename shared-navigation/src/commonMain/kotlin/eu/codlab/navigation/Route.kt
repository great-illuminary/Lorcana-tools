package eu.codlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import kotlin.reflect.KClass

interface Router<T : RouteParameterTo> {
    val klass: KClass<T>

    fun route(navBackStackEntry: NavBackStackEntryWrapper): Route<T>

    fun isMatching(route: String): Boolean

    fun navigateFrom(path: String): T
}

interface RouterNoParameters<T : RouteParameterTo> : Router<T> {
    fun navigateTo(): NavigateTo
    fun isCurrentRoute(routeParameterTo: RouteParameterTo?): Boolean
}

abstract class Route<T : RouteParameterTo>(
    val route: String,
    val params: T,
    val deepLinks: List<String> = emptyList(),
    //val navTransition: NavTransition? = null,
    //val swipeProperties: SwipeProperties? = null,
) {
    open fun toPath(): String = route

    @Composable
    abstract fun scene(
        //navigateTo: (NavigateTo) -> Unit,
        // don't pass backStackEntry: NavBackStackEntry
        // will need specific parameters
    )
}