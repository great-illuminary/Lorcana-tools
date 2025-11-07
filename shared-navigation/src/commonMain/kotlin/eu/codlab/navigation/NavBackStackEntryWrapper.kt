package eu.codlab.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlin.reflect.KClass

class NavBackStackEntryWrapper(private val navBackStackEntry: NavBackStackEntry) {
    public fun <T> toRoute(route: KClass<*>): T = navBackStackEntry.toRoute(route)
}