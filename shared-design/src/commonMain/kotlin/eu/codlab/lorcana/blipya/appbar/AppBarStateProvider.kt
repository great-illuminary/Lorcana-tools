package eu.codlab.visio.design.appbar

import eu.codlab.lorcana.blipya.appbar.AppBarState
import kotlinx.coroutines.flow.StateFlow

interface AppBarUiState {
    val appBarState: AppBarState
}

interface AppBarStateProvider<T : AppBarUiState> {
    val states: StateFlow<T>

    fun setAppBarState(appBarState: AppBarState)
}
