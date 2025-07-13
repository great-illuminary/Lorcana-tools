package eu.codlab.lorcana.blipya.home.navigate

import moe.tlaster.precompose.navigation.NavOptions

data class NavigateTo(
    val route: String,
    val stack: NavigateToStack
)

data class NavigateToStack(
    val popBackStack: Boolean,
    val options: NavOptions
)
