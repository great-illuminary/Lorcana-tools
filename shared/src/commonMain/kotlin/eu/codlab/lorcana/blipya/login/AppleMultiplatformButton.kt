package eu.codlab.lorcana.blipya.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun AppleMultiplatformButton(
    modifier: Modifier,
    onGoogleAuthentIdToken: (Result<String>) -> Unit
) {
    val model = rememberViewModel { GoogleAuthenticationModel() }

    AppleButtonUiContainer(
        modifier,
        linkAccount = false,
        onResult = {
            model.setResult(it, onGoogleAuthentIdToken)
        }
    ) {
        AppleSignInButton(modifier = modifier) { this.onClick() }
    }
}
