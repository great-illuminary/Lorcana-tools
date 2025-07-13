package eu.codlab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
actual fun rememberNavigation(): MutableState<String> {
    val currentPath = remember { mutableStateOf("") }
    // nothing in non js for now

    return currentPath
}
