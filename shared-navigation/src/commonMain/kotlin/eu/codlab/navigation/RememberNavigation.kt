package eu.codlab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
expect fun rememberNavigation(): MutableState<String>
