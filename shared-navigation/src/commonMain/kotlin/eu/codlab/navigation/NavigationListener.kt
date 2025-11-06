package eu.codlab.navigation

import eu.codlab.visio.design.appbar.AppBarStateSetter

interface NavigationListener: AppBarStateSetter {
    fun shown(navigateTo: NavigateTo)
}