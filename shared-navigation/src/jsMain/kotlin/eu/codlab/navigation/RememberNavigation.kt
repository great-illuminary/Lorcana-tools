package eu.codlab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.browser.window
import org.w3c.dom.events.Event

@Composable
actual fun rememberNavigation(): MutableState<String> {
    val currentPath = remember { mutableStateOf(window.location.pathname) }
    DisposableEffect(Unit) {
        val onPopState: (Event) -> Unit = {
            currentPath.value = window.location.pathname
        }
        window.addEventListener(POP_STATE, onPopState)
        onDispose {
            window.removeEventListener(POP_STATE, onPopState)
        }
    }

    return currentPath
}
