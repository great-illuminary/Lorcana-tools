package eu.codlab.visio.design.appbar

import androidx.compose.ui.graphics.vector.ImageVector

data class FloatingActionButtonState(
    val icon: ImageVector,
    val contentDescription: String,
    val action: () -> Unit
)
