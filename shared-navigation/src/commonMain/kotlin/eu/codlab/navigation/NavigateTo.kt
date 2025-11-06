package eu.codlab.navigation

import androidx.navigation.NavController

interface RouteParameterTo

data class NavigateTo(
    val route: RouteParameterTo,
    val stack: NavigateToStack
)

data class NavigateWithNavOptions(
    val launchSingleTop: Boolean,
    val popUpBackTo: RouteParameterTo? = null
)

data class NavigateToStack(
    val popBackStack: Boolean,
    val options: NavigateWithNavOptions
)

fun navigateTo(navController: NavController, navigateTo: NavigateTo) {
    if (navigateTo.stack.popBackStack) {
        navController.popBackStack()
    }

    navController.navigate(
        route = navigateTo.route
    ) {
        launchSingleTop = navigateTo.stack.options.launchSingleTop

        navigateTo.stack.options.popUpBackTo?.let { popUpTo(it) }
    }
}