package eu.codlab.lorcana.blipya.widgets

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import eu.codlab.lorcana.blipya.utils.LocalWindow
import eu.codlab.lorcana.blipya.utils.WindowType

@Composable
fun rememberSizeAwareScaffoldState(): ScaffoldState {
    val commonSnackbarHostState = remember { SnackbarHostState() }
    val compactScaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(DrawerValue.Closed),
        snackbarHostState = commonSnackbarHostState
    )
    val expandedScaffoldState = rememberScaffoldState(
        drawerState = DrawerState(DrawerValue.Closed),
        snackbarHostState = commonSnackbarHostState
    )
    val isScreenExpanded = LocalWindow.current == WindowType.TABLET

    return if (isScreenExpanded) {
        expandedScaffoldState
    } else {
        compactScaffoldState
    }
}
