package eu.codlab.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import eu.codlab.viewmodel.StateViewModel

data class NavigatorModelState(
    val canGoBack: Boolean = false,
    val currentNavBackStackEntry: NavBackStackEntryWrapper? = null
)

class NavigatorModel : StateViewModel<NavigatorModelState>(NavigatorModelState()) {
    var navigationListener: NavigationListener? = null

    private val navigatorListener = NavController.OnDestinationChangedListener { controller, _, _ ->
        controller.currentBackStackEntry?.let { entry ->
            val canGoBack = navigator?.previousBackStackEntry != null

            updateState {
                copy(
                    canGoBack = canGoBack,
                    currentNavBackStackEntry = NavBackStackEntryWrapper(entry)
                )
            }
        }
    }

    internal var navigator: NavHostController? = null

    fun setNavigator(navigator: NavHostController?) {
        this.navigator?.removeOnDestinationChangedListener(navigatorListener)
        this.navigator = navigator
        navigator?.addOnDestinationChangedListener(navigatorListener)

        val canGoBack = navigator?.previousBackStackEntry != null

        updateState { copy(canGoBack = canGoBack) }
    }

    fun goBack() = navigator?.navigateUp() ?: false

    fun navigateTo(navigateTo: NavigateTo) {
        navigator?.let { navigateTo(it, navigateTo) }
        navigationListener?.shown(navigateTo)
    }

    fun navigateUp() = navigator?.navigateUp() ?: false
}