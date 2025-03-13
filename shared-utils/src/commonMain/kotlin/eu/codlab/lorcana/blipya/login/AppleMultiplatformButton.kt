package eu.codlab.lorcana.blipya.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform

@Composable
fun AppleMultiplatformButton(
    modifier: Modifier,
    onGoogleAuthentIdToken: (Result<String>) -> Unit
) {
    if (currentPlatform == Platform.ANDROID || currentPlatform == Platform.IOS) {
        AppleMultiplatformButtonPlatformSupport(modifier, onGoogleAuthentIdToken)
    } else {
        AppleMultiplatformButtonOAuthOnly(modifier)
    }
}
