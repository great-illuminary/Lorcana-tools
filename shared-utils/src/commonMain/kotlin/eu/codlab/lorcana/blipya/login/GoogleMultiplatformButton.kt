package eu.codlab.lorcana.blipya.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun GoogleMultiplatformButton(
    modifier: Modifier,
    onGoogleAuthentIdToken: (Result<String>) -> Unit
)
